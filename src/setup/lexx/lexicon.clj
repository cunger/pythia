(ns setup.lexx.lexicon
  (:require [setup.lexx.signature :as signature]
            [setup.lexx.query     :as query]
            [setup.lexx.render    :as render]
            [setup.lexx.utils     :as utils]
            [setup.utils          :refer :all]
            [clojure.data.json    :as json]
            [clojure.math.combinatorics :as combo]))


(declare construct-sense)
(declare argument-reversion?)
(declare build-arg-map)
(declare nest-values)
(declare add-markers)
(declare add-names)
(declare add-to-concrete)
(declare adjunct-permutation)
(declare collapse-keys)

(def keys-to-collapse { :prepositionalObject  :prepositionalArg 
                        :prepositionalAdjunct :prepositionalArg })


;; MAIN 

(defn process [graph] 

  (signature/init-language-map!)
  (signature/init-concrete!)

  ;; FOR EACH LEXICON
  (doseq [lexicons (query/get-data "lexicon/lexicon.sparql" graph)]
  	; get lexicon language
    (let [lexicon    (:uri lexicons)
          langresult (:l (first (query/get-data-for-uri lexicon "lexicon/language.sparql" graph)))
          iso-code   (get @signature/language-map langresult)
          language   (if iso-code (keyword iso-code) nil)]
      (if language
        (do 
          (signature/add-language-to-concrete! language)

      	  ;; FOR EACH ENTRY
  	      (doseq [entries (query/get-data-for-uri lexicon "lexicon/entries.sparql" graph)]
  	        (let [entry   (:uri entries)
                  ; get canonical form -> { :canonicalForm blah :canonicalFormIdentifier blubb }
                  canresult   (first (query/get-data-for-uri entry "lexicon/canonicalForm.sparql" graph)) 
                  canonical   (assoc canresult :canonicalFormIdentifier (utils/to-identifier (:canonicalForm canresult)))
                  ; get part of speech
                  posresult   (query/get-data-for-uri entry "lexicon/lexinfo/pos.sparql" graph)
                  pos         (if (empty? posresult) nil (utils/frag-without-prefix (:uri (first posresult))))
                  ; get part-of-speech-specific information (forms and features) -> { :gender neuter, :plural blubb } or nil
                  formsfeats  (if pos (collapse (apply concat (query/get-data-for-all-pos-queries entry pos graph))) {})
                  ; get frames
                  frameresult (query/get-data-for-uri entry "lexicon/lexinfo/frames.sparql" graph)
                  frames      (collapse-bindings (map remove-nil-values frameresult) (keys keys-to-collapse))] 

              ;; FOR EACH SENSE
              (doseq [senses  (query/get-data-for-uri entry "lexicon/sense.sparql" graph)]
                (let [sense   (:uri senses)
                      meaning (construct-sense graph sense)
                    ; context so far 
                      context (assoc (merge canonical (nest-values formsfeats :value))
                                     :mkCat (case (:cat meaning)
                                                   :entity    "mkEntity"
                                                   :predicate "mkPredicate"
                                                   :relation  "mkRelation"
                                                   "")
                                     :language (name language)
                                     language  true)
                    ; statement flag
                      is-statement (= (:cat meaning) :statement)
                    ; semantic arguments
                      sem-args (:sem-args meaning)
                      sem-vals (vals sem-args)
                      sem-map  (clojure.set/map-invert sem-args)
                    ; meaning 
                      add-to-meaning   (if (= (:cat meaning) :statement) { :args (keys sem-args) } {})
                      add-to-context   (if (= (:cat meaning) :statement) { :statement true } {})
                      final-definition (let [d (:definition meaning)] (if (fn? d) (d sem-map) d))
                      final-meaning    (merge add-to-meaning (assoc meaning :definition final-definition))]

                    ; if compound sense, then add sense to signature/abstract
                     (if is-statement (signature/add-to-abstract! (:cat meaning) sense final-meaning))

                     (if-not (empty? frames)

                      ;; FOR EACH FRAME
                          (doseq [frame frames] 
                            (let [frame-name (utils/frag-without-prefix (:frame frame)) 
                                  syn-args   (dissoc frame :frame)
                                  syn-vals   (flatten (vals syn-args))]

                              ; if the frame fits the sense
                              (if (empty? (minus sem-vals syn-vals)) ; i.e. no unbound semantic arguments, rather than (if-not (empty? bound)

                                ; check for unbound syn-args
                                (if-not (empty? (minus syn-vals sem-vals)) 
                                  ; if there are, skip the frame
                                  (println "[WARNING] There are unbound syntactic arguments:\n* Frame:" (remove-nil-values frame) "\n* Sense:" meaning)
                                  ; otherwise build context for rendering and render frame template
                                  (let [reverted { :reverse (if (argument-reversion? syn-args sem-args) "True" "False") }
                                        ; get markers of syn-args and decide on their name based on sem-arg keys
                                        extended-syn-args (add-markers (add-names (nest-values syn-args :uri) sem-args) graph)]
                                        ; TODO more renaming necessary?

                                    ; for all final syn behaviors, render template and add result to signature/concrete
                                    (doseq [final-syn-args (map collapse-keys (adjunct-permutation extended-syn-args))]
                                      (let [final-context  (merge context final-syn-args reverted add-to-context)
                                           ;render template
                                            result   (render/render (str "lexinfo/frames/" frame-name) final-context)
                                            lin-oper (if-not (nil? result) (json/read-str result))]

                                            ; add rendering result to signature/concrete
                                            (add-to-concrete lin-oper language (:identifier final-meaning) (if (contains? final-meaning :args) (:args final-meaning) [])))))))))
                          
                      ;; OTHERWISE FOR POS
                          (let [; render template
                                result   (render/render (str "lexinfo/pos/" pos) context)
                                lin-oper (if-not (nil? result) (json/read-str result))
                                args     []] ; TODO
                            (do
                              ; add rendering result to signature/concrete
                              (add-to-concrete lin-oper language (:identifier meaning) args)) 
                              ; add sense to signature/abstract        
                              (signature/add-to-abstract! (:cat meaning) sense meaning))     
                      )           
            )))
          )) ; fi doseq and do
          (if (clojure.string/blank? langresult) 
              (println (str "[ERROR] No language specified for the lexicon " (:uri lexicon))) 
              (println (str "[ERROR] Language \"" langresult "\" is not supported."))))

  ))

  (signature/serialize-lins!))



;; CONSTRUCTING SIMPLE AND COMPOUND SENSES

(defn construct-sense [graph sense]

  (let [; reference
        refresult (query/get-data-for-uri sense "lexicon/sense_reference.sparql" graph)
        reference (if (empty? refresult) nil (:uri (first refresult)))
        ; arguments
        argresult (query/get-data-for-uri sense "lexicon/sense_arguments.sparql" graph)
        args      (remove-nil-values (first argresult))
        ; resulting category
        cat       (cond (contains? args :isA)        :predicate
                        (contains? args :subjOfProp) :relation
                        (contains? args :objOfProp)  :relation 
                        :else                        :entity)
        ; lookup reference in signature
        sig-lookup (get @signature/sense-index reference)]

    (if sig-lookup 

       ; if in signature already, add args and return
       (assoc sig-lookup :sem-args args)

       ; else build a sense 
       (let [subsenses (query/get-data-for-uri sense "lexicon/sense_subsenses.sparql" graph)]

          (if (empty? subsenses)
      
              ;; SIMPLE SENSE
              (let [ident (if reference 
                             (utils/frag reference) 
                             (do (println "[WARNING] Sense without reference or subsenses: " sense) ; should not happen
                                 (utils/frag sense)))

                    dfnt  (case cat 
                               :predicate (str "(defn " ident " [x]   (Triple. x " utils/rdf-type " (Term. :uri \"" reference "\")))")
                               :relation  (str "(defn " ident " [x y] (Triple. x (Term. :uri \"" reference "\") y))")
                               :entity    (str "(def  " ident " (Term. :uri \"" reference "\"))"))
 
                    meaning { :identifier ident :definition dfnt :cat cat :sem-args args }]
                (do 
                   (signature/add-to-abstract! cat sense meaning)
                   meaning))
              
              ;; COMPOUND SENSE
              (let [subss (map (fn [s] (construct-sense graph (:uri s))) subsenses)
                    ident (clojure.string/join "_" (map :identifier subss))
                    dfnt  (fn [arg-map] 
                              (let [build-def  (fn [s] (case (:cat s)
                                                              :predicate (let [x (get arg-map (:isA (:sem-args s)))] 
                                                                              (str "(" (:identifier s) " " x ")"))
                                                              :relation  (let [x (get arg-map (:subjOfProp (:sem-args s)))
                                                                               y (get arg-map (:objOfProp  (:sem-args s)))]
                                                                              (str "(" (:identifier s) " " x " " y ")"))
                                                              :entity    (:identifier s)))
                                    body (utils/wrap "And." (map build-def subss))]
                                   (str "(defn " ident " [" (clojure.string/join " " (vals arg-map)) "] " body ")")))]

                ; NOTE: is not yet added to abstract, as dfnt is a function and is supplied with arguments in fn:process above
                { :identifier ident :definition dfnt :cat :statement 
                  :sem-args (build-arg-map (distinct (apply concat (map (comp vals :sem-args) subss)))) } ))))))



;; AUX

; Build syntactic argument arrays

(defn nest-values [m k]
  (let [nest (fn [v] { k v })]
  (fmap-vals (fn [v] (apply-to-conjoined nest v)) m)))

(defn add-names [syn-args sem-args]
  (let [sem-map    (clojure.set/map-invert sem-args)
        name-it    (fn [x] (if (contains? sem-map x) 
                               (name (get sem-map x))
                               (utils/frag-without-prefix x)))
        assoc-name (fn [x] (assoc x :name (name-it (:uri x))))] 
  (fmap-vals (fn [syn] (apply-to-conjoined assoc-name syn)) syn-args)))

(defn add-markers [syn-args graph]
  (let [get-markers   (fn [x] (let [markers (query/get-data-for-uri x "lexicon/marker.sparql" graph)]
                                 (if-not (empty? markers) (:marker (first markers)) [])))
        assoc-markers (fn [x] (let [markers (get-markers (:uri x))] (if-not (empty? markers) (assoc x :marker markers) x)))]
  (fmap-vals (fn [syn] (apply-to-conjoined assoc-markers syn)) syn-args)))

(defn collapse-keys [syn]
  (fmap-keys (fn [k] (get keys-to-collapse k k)) syn))

; Create all adjunct permutations

(defn is-Adjunct? [x] (contains-string? (name x) "Adjunct"))

(defn permutate-values [v] 
  (if (sequential? v) (apply concat (map combo/permutations (combo/subsets v))) [[] [v]]))

(defn adjunct-permutation [syn]
  (let [adjunct-keys (filter is-Adjunct? (keys syn))]
    (if (empty? adjunct-keys) 
        [syn]
        (let [syn-without-adjuncts (apply dissoc syn adjunct-keys)
              adjuncts             (into {} (for [k adjunct-keys] [k (get syn k)]))
              permutated-adjuncts  (fmap-vals permutate-values adjuncts)
              index-combinations   (apply combo/cartesian-product (map #(range 0 (count %)) (vals permutated-adjuncts)))
              adjunct-combinations (for [is index-combinations]
                                      (loop [m {} n 0] 
                                        (if (= n (count adjunct-keys))
                                            m
                                            (let [k (nth adjunct-keys n)]
                                            (recur (assoc m k (nth (get permutated-adjuncts k) (nth is n))) (inc n))))))]
            (for [combo adjunct-combinations]
                 (utils/clean-map (merge syn-without-adjuncts combo)))))))

; Build semantic argument arrays 

(defn build-arg-map [args]
  ; e.g. ["subject" "object"] --> { :arg1 "subject" :arg2 "object" }
  (into {} (map vector (map #(str "arg" %) (range 0 (count args))) args)))

; Check syn-sem arg mapping 

(defn argument-reversion? [syn-args sem-args]
  ; if the syntactic subject (:subject or :copulativeArg) = semantic object, then true, else false
  (let [subject (cond (contains? syn-args :subject)       (:subject syn-args)
                      (contains? syn-args :copulativeArg) (:copulativeArg syn-args)) ]
    (and (contains? sem-args :objOfProp) 
         (= subject (:objOfProp sem-args)))))

; Add lins and opers to concrete

(defn add-to-concrete [lin-oper language identifier args]
  (doseq [lin  (get lin-oper "lin")]
    (signature/add-lin-to-concrete!  language { :identifier identifier :lin lin :args args })) 
  (doseq [oper (get lin-oper "oper")]
    (signature/add-oper-to-concrete! language (str (clojure.string/replace oper "\n" "") ";"))))

