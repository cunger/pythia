(ns core.external.ner.fox
  (:require [settings]
            [core.external.http :as http]
            [clojure.data.json  :as json]))



(declare type-list)


(defn http-request [input]
  (settings/language (settings/domain 
  { :dbpedia { :en (str "http://139.18.2.164:4444/call/ner/entities?input=" input "&type=text&task=ner") }})))


;; Main 
;; must implement: get-entities, filter-entities

(defn get-entities [input]
  (let [request  (http-request (http/urlize input))
        response (http/get-response request identity)
        status   (:status response)
        body     (if-not (clojure.string/blank? (:body response)) (json/read-str (:body response)))]

    ; (do
    ; (println ">>>>>" response) 

    (if (= status 200)
      
        (for [resource body]
             { :uri    (get resource "means" ) 
               :form   (get resource "ann:body")
               :offset (get resource "beginIndex")
               :types  (type-list (get resource "@type"))})
        
        []))) ; )


;; Aux

(defn type-list [types]
  (map #(clojure.string/replace % "scmsann:" "") types))

(defn filter-entities [entities]
  (remove #(or (empty? (:types %))
               (some #{"ann:Annotation"} (:types %)))
          entities))

(defn most-general-type [entity] (clojure.string/replace (last (:types entity)) "http://dbpedia.org/ontology/" ""))