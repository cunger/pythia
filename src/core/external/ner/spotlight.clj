(ns core.external.ner.spotlight
  (:require [settings]
            [core.external.http :as http]
            [clojure.data.json  :as json]))



(declare type-list)


(defn http-request [input]
  (settings/language (settings/domain 
  { :dbpedia { :de (str "http://de.dbpedia.org/spotlight/rest/annotate?text=" input "&spotter=Default")
               :en (str "http://spotlight.dbpedia.org/rest/annotate?text=" input "&spotter=Default&confidence=0.5&support=20") 
               :es (str "http://spotlight.sztaki.hu:2231/rest/annotate?text=" input "&spotter=Default") }})))


;; Main 
;; must implement: get-entities, filter-entities

(defn get-entities [input]
  (let [request  (http-request (http/urlize input))
        response (http/get-response :get request {:headers {"accept" "application/json"}} identity)
        status   (:status response)]

    (if (= status 200) 

        (let [body (json/read-str (:body response))]

             (if (contains? body "Resources")
        
                 (for [resource (get body "Resources")]
                      { :uri    (get resource "@URI" ) 
                        :form   (get resource "@surfaceForm")
                        :offset (get resource "@offset")
                        :types  (type-list (get resource "@types"))})))
        
        [])))


;; Aux

(defn type-list [string]
  (map #(clojure.string/replace % "DBpedia:" "http://dbpedia.org/ontology/") 
        (filter #(.startsWith % "DBpedia:") 
                 (clojure.string/split string #"\,"))))

(defn filter-entities [entities]
  (remove #(or (empty? (:types %))
               (some #{"http://dbpedia.org/ontology/TopicalConcept"} (:types %)))
          entities))

(defn most-general-type [entity] (clojure.string/replace (last (:types entity)) "http://dbpedia.org/ontology/" ""))