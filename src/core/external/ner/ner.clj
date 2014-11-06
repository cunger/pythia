(ns core.external.ner.ner
  (:require [core.external.ner.spotlight :as spotlight]
            [core.nlu.context.short_term_memory :as stm]))


(declare replace-entities)


;; Main 

(defn recognize [input]
  (let [entities  (spotlight/filter-entities (spotlight/get-entities input))
        new-input (replace-entities input entities 1 0)]
    (if (empty? entities)
        input
      (do 
        ; print entities and new-input
        (println "\nEntities:\n")
        (doseq [e entities] (println (str e)))
        (println (str "\nAdapted input: " new-input))
        ; return new-input
        new-input))))


;; Aux

(defn replace-entities [input entities i d] 
  ; d is the offset deviation
  (if (empty? entities)
      input
      (let [entity     (first entities)
            form       (:form entity)
            identifier (str "Entity" i)
            offset     (- (read-string (:offset entity)) d)
            new-input  (str (subs input 0 offset) (clojure.string/replace-first (subs input offset) form identifier))
            new-d      (+ d (- (count form) 1))]
        (do
          ; add entity to short term memory
          (swap! stm/entities assoc (keyword identifier) entity)
          ; recur
          (replace-entities new-input (rest entities) (+ i 1) new-d)))))


;; Semantic definition

(defn resolve-entity [identifier] 
  (:uri (get @stm/entities identifier)))
