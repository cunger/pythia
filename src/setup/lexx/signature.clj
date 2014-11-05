(ns setup.lexx.signature)


(def iso "src/setup/lexx/iso639")
(def log "src/setup/lexx/target/report.log")


;; DEF

(def namespaces   (atom {}))
(def namespaces-i (atom 0))

(def sense-index  (atom {}))

(def abstract (atom {}))
(def concrete (atom {}))


;; LANGUAGE 

(def language-map (atom {}))

(defn init-language-map! []
  (with-open [rdr   (clojure.java.io/reader iso)]
      (doseq [line  (line-seq rdr)]
        (let [codes (clojure.string/split line #":")]
         (if (not   (clojure.string/blank? (nth codes 4)))
             (doseq [code (filter (comp not clojure.string/blank?) codes)]
              (swap! language-map assoc (clojure.string/trim code) (clojure.string/trim (nth codes 4)))))))))

;; NAMESPACES

(defn add-to-namespaces! [k v]
  (swap! namespaces assoc k v)
  (swap! namespaces-i + @namespaces-i 1))

;; ABSTRACT

(defn init-abstract! []
  (swap! abstract assoc :entity    []
                        :predicate []
                        :relation  []
                        :statement []))

(defn add-to-abstract! [k uri sense] 
  (if (contains? @sense-index uri)
      (if-not (= (get @sense-index uri) sense) 
              (println "[WARNING] Trying to overwrite a sense in abstract:\n* Old:" (get @sense-index uri) "\n* New:" sense))
      (do 
        (swap! abstract assoc k (cons sense (k @abstract))) 
        (swap! sense-index assoc uri sense)
        sense)))

;; CONCRETE

; INIT 

(defn init-concrete! []
  (reset! concrete {}))

(defn add-language-to-concrete! [language]
  (swap! concrete assoc language { :lin nil :oper nil }))

; UPDATE

(declare update-concrete!)

(defn add-lin-to-concrete! [language element]
  (let [elements (:lin (language @concrete))
        id       (:identifier element)]
    (if (contains? elements id) 
        (update-concrete! language :lin (assoc elements id { :lin (cons (:lin element) (:lin (get elements id))) :args (:args element) })) ; TODO what if args clash
        (update-concrete! language :lin (assoc elements id { :lin (cons (:lin element) '()) :args (:args element) })))))

(defn add-oper-to-concrete! [language element]
  (let [elements (:oper (language @concrete))]
    (if-not (some #{element} elements)
            (update-concrete! language :oper (cons element elements)))))

(defn update-concrete! [language lin-or-oper new-elements]
  (swap! concrete assoc language (assoc (language @concrete) lin-or-oper new-elements)))

; SERIALIZE

(defn serialize-lins! []
  (doseq [language (keys @concrete)]
    (update-concrete! language :lin 
      (for [[k v] (:lin (language @concrete))] 
                  (str k " " (clojure.string/join " " (:args v)) " = " (clojure.string/join " | " (distinct (:lin v))) ";")))))
 

;; REPORTING

(defn report [] 
  (with-open [w (clojure.java.io/writer log)]
    (println "Writing log to" log "...")
    (.write w (str "lemon2gf log (" (java.util.Date.) ")"))
    (.write w "\n------ @abstract ------")
    (.write w "\nPredicates:")
    (doseq [p (:predicate @abstract)]
      (.write w (str "\n* " (:identifier p) " >> " (:definition p))))
    (.write w "\nRelations:")
    (doseq [r (:relation @abstract)]
      (.write w (str "\n* " (:identifier r) " >> " (:definition r))))
    (.write w "\nEntities:")
    (doseq [e (:entity @abstract)]
      (.write w (str "\n* " (:identifier e) " >> " (:definition e))))
    (.write w "\nStatements:")
    (doseq [s (:statement @abstract)]
      (.write w (str "\n* " (:identifier s) " >> " (:definition s))))
  ; (println "------ @sense-index ---")
  ; (doseq [ [k v] @signature/sense-index ]
  ;   (println (str k " >> " v)))
  ; (println "-----------------------")
    (.write w "\n------ @namespaces ---")
    (doseq [ [k v] @namespaces ]
      (.write w (str "\n" k " >> " v)))
    (.write w "\n------ @concrete -------")
    (.write w "\nLin:")
    (doseq [[k v] (:lin @concrete)]
      (.write w (str "\n* " k " " (:args v) " = " (:lin v))))
    (.write w "\nOper:")
    (doseq [[k v] (:oper @concrete)]
     (.write w (str "\n* " k " = " v)))
))