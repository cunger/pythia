(ns core.main
   (:require [core.nlu.context.short_term_memory :as stm]
             [core.external.gf_server :as gf]
             [core.external.ner.ner   :as ner]
             [core.nlu.robustness.preprocessing :refer [normalize]]
             [core.nlu.robustness.chunking      :refer [remove-unknown-tokens]]
             [core.nlu.interpretation :refer [interpret handle-effects]]))


(declare parse-and-interpret)
(declare pick-best)


;; Main

(defn dispatch [pipeline grammar input]

  (case pipeline
       :generate-random     (println "\n" (gf/generate-random grammar))
       :parse-and-interpret (parse-and-interpret grammar input)
       (java.lang.System/exit 1))
)


;; Parsing and interpretation pipeline

(defn parse-and-interpret [grammar input]

  (stm/init!)

  (let [;; tweak input
        normalized (normalize input)
        ner-ed     (ner/recognize normalized)
        cleaned    (remove-unknown-tokens ner-ed false) ; remove all tokens not in application grammar 
        stripped   (remove-unknown-tokens ner-ed true)  ; remove all tokens neither in application nor fallback grammar
        ; TODO this also removes, e.g., numbers (which it shouldn't)
        ;; parsing
        inputs (distinct [input normalized ner-ed cleaned stripped])
        parses (apply concat (map #(gf/request-parse grammar %) inputs)) 
        ;; interpretation
        interpretations       (apply concat (map interpret parses))
        final-interpretations (map handle-effects interpretations)]

    (pick-best final-interpretations)))


(defn pick-best [interpretations]
  (if-not (empty? interpretations)
          (first  interpretations)))
