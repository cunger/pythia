(ns core.main
   (:require [core.external.gf_server :as gf]
             [core.external.ner.ner   :as ner]
             [core.nlu.interpretation :refer [interpret]]))


(declare parse-and-interpret)


;; Main

(defn dispatch [pipeline grammar input]

  (case pipeline
       :generate-random     (println "\n" (gf/generate-random grammar))
       :parse-and-interpret (parse-and-interpret grammar input)
       (java.lang.System/exit 1))
)


;; Parsing and interpretation pipeline

(defn parse-and-interpret [grammar normalized-input]

  (let [ ;; named entity recognition
         new-input (ner/recognize normalized-input)
         ;; parsing 
         parses    (gf/request-parse grammar new-input)
         ;; interpretation
         interpretations (apply concat (map interpret parses))
       ]

    (if-not (empty? interpretations)
      ; TODO rank interpretations
      (first interpretations)
      ; else: Partial parsing
      ; TODO (robustness/...)
    )
  ))