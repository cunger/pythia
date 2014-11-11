(ns applications.qa.main
  (:require [settings]
            [core.main :as core]
            [core.external.gf_server :as gf]
            [core.data.LambdaRDF :refer [show-as-code show-as-sparql]]
            [core.nlu.robustness.preprocessing :refer [normalize]]
            [applications.qa.endpoint :as endpoint]
            [clojure.java.io :as io]))


(declare get-input)
(declare show-output)
(declare sparqlize)


;; Main

(defn interaction-loop [grammar]
  (loop [input (get-input)]
    (let [normalized-input (normalize input)]
      (case normalized-input
            ; exit condition
            "exit" (java.lang.System/exit 0)
            "quit" (java.lang.System/exit 0)
            "q"    (java.lang.System/exit 0)
            ; specific speech acts
            "say something" (core/dispatch :generate-random grammar input)
            ; anything else 
            (let [result (core/dispatch :parse-and-interpret grammar input)]

                 (if result 
                    ; show result
                    (do (show-output result) 
                    ; sparqlize and send query to endpoint
                        (let [query  (sparqlize (first (:exprs result)))
                              answer (endpoint/execute-query query)]

                        (println "\nAnswer(s) from " settings/sparql-endpoint ":\n")
                        (endpoint/print-answer answer)))
                    ; otherwise 
                    (println "\nSorry, I didn't understand you.")) 
                    ; TODO lin SorryDidntUnderstand
      ))
      ; repeat
      (recur (get-input)))))

(defn run []

  (println "\nPythia v2\n")

  (let [grammar (if-not (nil? settings/grammar) settings/grammar "Application.pgf")]
    ; check grammar
    (gf/check-for-grammar grammar)
    ; start interaction
    (interaction-loop grammar)))


;; Get and show

(defn get-input []
  (println "\n=================================================\n")
  (print "> ")
  (flush)
  (read-line))

(defn show-output [interpretation]
  (let [ asts (:asts interpretation)
         sem  (:sem  interpretation) 
         expr (first (:exprs interpretation))
         lang (:lang interpretation) ]
    (do
      (println "\n-------------------------------------------------")
      (println "\n* Smallest parse tree ( out of" (count asts) "from" lang "):")
      (if-not (empty? asts)
        (println "\n " (apply min-key count asts))
        (println "\n None.")) 
      (println "\n---- Step 1 ----")
      (println "\n* Unresolved expression:")
      (println "\n " (show-as-code sem))
      (println "\n* RDF/SPARQL:")
      (if sem 
        (println "\n " (sparqlize sem))
        (println "\n None."))
      (println "\n---- Step 2 ----")
      (println "\n* Resolved expression:")
      (println "\n " (show-as-code expr))
      (println "\n* RDF/SPARQL:")
      (if expr 
        (println "\n " (sparqlize expr))
        (println "\n None."))
      ))) 


;; Aux 

(defn sparqlize [sem]
  (try (show-as-sparql sem)
  (catch Exception e (str "[ERROR] SPARQL serialization failed:\n" (.getMessage e)))))
