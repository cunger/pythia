(ns core.nlu.reasoning.oracle
  (:require [seabass.core :as seabass]
            [settings]
            [core.nlu.context.short_term_memory :as stm]
            [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple]))



(defn sparql-path [file-name] (str "src/core/nlu/reasoning/sparql/" file-name))

(defn query-for-uri [file-name uri]
  (let [query (clojure.string/replace (slurp (sparql-path file-name)) "<URI>" (str "<" uri ">"))]
       (:data (seabass/bounce query settings/sparql-endpoint))))


(declare get-most-specific-type)
(declare frequency)


(defn find-paths [e1 e2 conditions]
  ; TODO Also consider conditions.
  (let [p (make-var "p" (stm/get-fresh!))
        part1a (show-as-sparql (Triple. e1 p e2))
        part1b (show-as-sparql (Triple. e2 p e1))
        part2  (case (:kind e1) 
                      :var (show-as-sparql (Triple. e1 rdf-type (Term. :uri (get-most-specific-type e1))))
                      "")
        part3  (case (:kind e2)
                      :var (show-as-sparql (Triple. e2 rdf-type (Term. :uri (get-most-specific-type e2))))
                      "")
        query_a   (str "SELECT DISTINCT " (show-as-sparql p) " WHERE { " part1a " " part2 " " part3 " }")
        query_b   (str "SELECT DISTINCT " (show-as-sparql p) " WHERE { " part1b " " part2 " " part3 " }")
        results_a (:data (seabass/bounce query_a settings/sparql-endpoint))
        results_b (:data (seabass/bounce query_b settings/sparql-endpoint))
        paths_a   (for [m results_a] (Triple. e1 (Term. :uri (first (vals m))) e2))
        paths_b   (for [m results_b] (Triple. e2 (Term. :uri (first (vals m))) e1))]

    (concat paths_a paths_b)))

(defn rank [xs]
  (reverse (sort-by frequency xs)))


; Aux 

(defn frequency [triple]
  (let [query (str "SELECT (COUNT(*) AS ?c) WHERE { " (show-as-sparql triple) " }")]
       (:c (first (:data (seabass/bounce query settings/sparql-endpoint))))))

(defn get-most-specific-type [v]
  (let [types (for [type (get-rdf-type @stm/expression v)]
                   (case (:op type) 
                          :id (:uri type)
                          :domain (:uri (query-for-uri "domain.sparql" (:uri type)))
                          :range  (:uri (query-for-uri "range.sparql"  (:uri type)))
                          owl-Thing))]
    (if-not (empty? types)
      (first types) ; TODO determine most specific
      owl-Thing)))


