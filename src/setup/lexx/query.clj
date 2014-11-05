(ns setup.lexx.query
  (:require [setup.lexx.utils :as utils]
            [seabass.core :as seabass]))


(defn sparql-path [file-name] (str "src/setup/lexx/sparql/" file-name))

(defn get-data [query graph]
  (:data (seabass/bounce (slurp (sparql-path query)) graph)))

(defn get-data-for-uri [uri query graph]
  (if (utils/uri? uri)
      (:data (seabass/bounce (clojure.string/replace (slurp (sparql-path query)) "<URI>" (str "<" uri ">")) graph))
      []))

(defn get-data-for-all-pos-queries [uri pos graph]
  (let [pre "lexicon/lexinfo/"
        dir (clojure.java.io/file (sparql-path (str pre pos)))]
    (if (.exists dir)
      (for [file (filter #(not (.endsWith % "~")) (.list dir))]
           (get-data-for-uri uri (str pre pos "/" file) graph)))))
