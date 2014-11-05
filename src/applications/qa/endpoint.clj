(ns applications.qa.endpoint
  (:require [settings]
            [seabass.core :as seabass]))


(declare type-of)


(defn execute-query [query] 
  (let [result (case (type-of query) 
                     :select (:data (seabass/bounce query settings/sparql-endpoint))
                     :ask    (seabass/ask query settings/sparql-endpoint)
                     (println "[ERROR] Unknown type of query (seems to be neither SELECT nor ASK):" query))]
    result))

(defn print-answer [result]
  (doseq [r result [k v] r]
    (println "*" k v)))


; AUX 

(defn type-of [query]
  (let [words (clojure.string/split (clojure.string/lower-case query) #"\s")]
  (cond 
       (some #{"select"} words) :select 
       (some #{"ask"}    words) :ask 
       :else                    nil)))

