(ns setup.lexx.ontology
  (:require [setup.lexx.signature :as signature]
            [setup.lexx.utils     :as utils]
            [setup.lexx.query     :as query]))


(declare collect)
(declare construct-predicate)
(declare construct-relation)
(declare construct-entity)

;; MAIN

(defn process [graph]

    (signature/init-abstract!)
    
    (collect graph "ontology/classes.sparql"     construct-predicate)
    (collect graph "ontology/properties.sparql"  construct-relation)
    (collect graph "ontology/individuals.sparql" construct-entity)
)


;; CONSTRUCT SENSES

(defn collect [graph query construct]
  (doseq [concept (query/get-data query graph)]
         (construct graph concept)))

(defn either-already-in-abstract-or [concept kind sense]
  (let [uri (:uri concept)] 
    (if (contains? @signature/sense-index uri)
        (get       @signature/sense-index uri)
        (signature/add-to-abstract! kind uri sense))))

; INIVIDUALS

(defn construct-entity [graph concept]
  (either-already-in-abstract-or concept :entity 
    (let [ident (utils/frag (:uri concept))
          dfntn (str "(def  " ident " (Term. :uri \"" (:uri concept) "\"))")]
       { :cat :entity
         :identifier ident
         :definition dfntn })))

; PROPERTIES

(declare build-chain)

(defn construct-relation [graph concept] 
  (either-already-in-abstract-or concept :relation
    (let [inverse-property (query/get-data-for-uri (:uri concept) "ontology/properties_inverse.sparql" graph)
          property-chain   (query/get-data-for-uri (:uri concept) "ontology/properties_chain.sparql" graph)]
      (cond
        ; if uri is an inverse property 
        (not (empty? inverse-property))
        (let [fst   (first inverse-property)
              inv   (:identifier (construct-relation graph fst))
              ident (str "INVERSE_" inv)] 
           { :cat :relation
             :identifier ident
             :definition (str "(defn " ident " [x y] (" inv " y x))") })
        ; if uri is a property chain
        (not (empty? property-chain))
        (let [ps    (map #(construct-relation graph %) property-chain)
              is    (map :identifier ps)
              ident (clojure.string/join "_o_" is)]
           { :cat :relation
             :identifier ident,
             :definition (let [chain (build-chain is)
                               vars  (map #(str "(Term. :var \"v" % "\")") (range 1 (inc (:i chain)))) 
                               start (map #(str "(Quant. :some " % " [] [") vars)
                               end   (repeat (count vars) "])")]
                              (str "(defn " ident " [x y] " (apply str (concat start (:string chain) end)) ")")) })
        ; if uri is none of those constructs
        :else 
        (let [ident (utils/frag (:uri concept))]
           { :cat :relation
             :identifier ident,
             :definition (str "(defn " ident " [x y] (Triple. x (Term. :uri \"" (:uri concept) "\") y))") } )))))

(defn build-chain [properties]
  (loop [chain (str "(And. (" (first properties) " x (Term. :var \"v1\"))") i 1 ps (rest properties)]
    (if (= (count ps) 1)
        { :string (str chain " (" (first ps) " (Term. :var \"v"i "\") y))") :i i }
        (recur (str "(And. " chain " (" (first ps) " (Term. :var \"v"i "\") (Term. :var \"v"(+ i 1) "\")))") (+ i 1) (rest ps)))))

; CLASSES

(defn construct-predicate [graph concept] 
  (either-already-in-abstract-or concept :predicate
    (let [complement-class   (query/get-data-for-uri (:uri concept) "ontology/classes_complement.sparql" graph)
          intersection-class (query/get-data-for-uri (:uri concept) "ontology/classes_intersection.sparql" graph)
          union-class        (query/get-data-for-uri (:uri concept) "ontology/classes_union.sparql" graph)
          restriction-class  (query/get-data-for-uri (:uri concept) "ontology/classes_restriction.sparql" graph)]
      (cond 
        ; if uri is a complement class
        (not (empty? complement-class))
        (let [fst   (first complement-class)
              ident (str "NOT_" (:identifier (construct-predicate graph fst)))]
            { :cat :predicate
              :identifier ident, 
              :definition (str "(defn " ident " [x] (Not. (Triple. x " utils/rdf-type " (Term. :uri \"" (:uri fst) "\"))))") })
        ; if uri is an intersection or union
        (or (not (empty? intersection-class)) (not (empty? union-class)))
        (let [classes  (if (not (empty? intersection-class)) intersection-class union-class)
              operator (if (not (empty? intersection-class)) "And." "Or.")
              cs       (map #(construct-predicate graph %) classes)
              is       (map :identifier cs)
              ident    (str (if (not (empty? intersection-class)) "AND_" "OR_") (clojure.string/join "_" is))] 
           { :cat :predicate
             :identifier ident,
             :definition (str "(defn " ident " [x] " (utils/wrap operator (map #(str "(" % " x)") is)) ")") })
        ; if uri is a restriction class
        (not (empty? restriction-class))
        (let [fst     (first restriction-class)
              p_uri   (:onProperty fst)
              c       (construct-relation graph {:uri p_uri})
              p_ident (:identifier c)]
          (cond 
            ; hasValue 
            (:hasValue fst)
            (let [ident (str "ENTITIES_WITH_" p_ident "_" (utils/frag (:hasValue fst)))
                  value (str "(Term. " (if (utils/uri? (:hasValue fst)) ":uri" ":literal") " \"" (:hasValue fst) "\")")]
               { :cat :predicate
                 :identifier ident
                 :definition (str "(defn " ident " [x] (" p_ident " x " value "))") })
            ; allValuesFrom
            (:allValuesFrom fst)
            (let [value-class (:identifier (construct-predicate graph { :uri (:allValuesFrom fst) }))
                  ident (str "ENTITIES_WITH_" p_ident "_ALL_" value-class)]
               { :cat :predicate
                 :identifier ident
                 :definition (str "(defn " ident " [x] (And. (" p_ident " x (Term. :var \"v\"))"
                                                            "(Triple. (Term. :var \"v\") " utils/rdf-type " (Term. :uri \"" (:allValuesFrom fst) "\"))))") })                
            ; someValuesFrom
            (:someValuesFrom fst)
            (let [value-class (:identifier (construct-predicate graph { :uri (:someValuesFrom fst) }))
                  ident (str "ENTITIES_WITH_" p_ident "_SOME_" value-class)]
               { :cat :predicate
                 :identifier ident
                 :definition (str "(defn " ident " [x] (And. (" p_ident " x (Term. :var \"v\"))"
                                                            "(Triple. (Term. :var \"v\") " utils/rdf-type " (Term. :uri \"" (:someValuesFrom fst) "\"))))") })
                                                            ; TODO Should have a different semantics!
            ; cardinality 
            (or (:cardinality fst) (:minCardinality fst) (:maxCardinality fst))
            (let [k     (cond (:cardinality fst) :cardinality (:minCardinality fst) :minCardinality (:maxCardinality fst) :maxCardinality)
                  k_id  (case k :cardinality "EXACTLY" :minCardinality "MIN" :maxCardinality "MAX")
                  card  (k fst)
                  ident (str "ENTITIES_WITH_" k_id "_" card "_" p_ident)]
               { :cat :predicate 
                 :identifier ident
                 :definition (str "(defn " ident " [x] (And. (" p_ident " x (Term. :var \"v\"))"
                                                            "(Triple. (Term. :var \"v\") " utils/rdf-type " (Term. :uri \"" (:someValuesFrom fst) "\"))))") })))
                                                            ; TODO Should have a different semantics!
        ; TODO enumeration class
        ; if uri is none of those constructs
        :else 
        (let [ident (utils/frag (:uri concept))] 
           { :cat :predicate
             :identifier ident
             :definition (str "(defn " ident " [x] (Triple. x " utils/rdf-type " (Term. :uri \"" (:uri concept) "\")))") } )))))
