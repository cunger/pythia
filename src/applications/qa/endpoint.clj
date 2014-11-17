(ns applications.qa.endpoint
  (:require [settings]
            [seabass.core :as seabass]))


(declare type-of)


(defn execute-query [query] 

  (if-not (nil? query) 
    (case (type-of query) 
          :select (try (:data (seabass/bounce query settings/sparql-endpoint))
                       (catch Exception e (str "[ERROR] Querying failed for:\n" query "\nError:\n" (.getMessage e))))
          :ask    (try (seabass/ask query settings/sparql-endpoint)
                       (catch Exception e (str "[ERROR] Querying failed for:\n" query "\nError:\n" (.getMessage e))))
          (println "[ERROR] Unknown type of query (seems to be neither SELECT nor ASK):" query))
    []))

(defn print-answer [result]
  (if (coll? result)
      (doseq [r result [k v] r]
             (println "*" k v))
      (println "*" result)))


; AUX 

(defn type-of [query]
  (let [words (clojure.string/split (clojure.string/lower-case query) #"\s")]
  (cond 
       (some #{"select"} words) :select 
       (some #{"ask"}    words) :ask 
       :else                    nil)))

