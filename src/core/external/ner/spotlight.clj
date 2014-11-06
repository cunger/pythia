(ns core.external.ner.spotlight
  (:require [settings]
            [core.external.http :as http]
            [clojure.data.json  :as json]))



(defn type-list [string]
  (map #(clojure.string/replace % "DBpedia:" "http://dbpedia.org/ontology/") 
        (filter #(.startsWith % "DBpedia:") 
                 (clojure.string/split string #"\,"))))

;; Main 

(defn get-entities [input]
  (let [request  (settings/ner-request (http/urlize input))
        response (http/get-response request identity)
        status   (:status response)
        body     (if-not (clojure.string/blank? (:body response)) (json/read-str (:body response)))]

    (if (and (= status 200) (contains? body "Resources"))
        
        (for [resource (get body "Resources")]
             { :uri    (get resource "@URI" ) 
               :form   (get resource "@surfaceForm")
               :offset (get resource "@offset")
               :types  (type-list (get resource "@types"))})
        
        [])))

(defn filter-entities [entities]
  (remove #(or (empty? (:types %))
               (some #{"http://dbpedia.org/ontology/TopicalConcept"} (:types %)))
          entities))

;; Aux

(defn most-general-type [entity] (clojure.string/replace (last (:types entity)) "http://dbpedia.org/ontology/" ""))