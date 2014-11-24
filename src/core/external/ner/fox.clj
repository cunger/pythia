(ns core.external.ner.fox
  (:require [settings]
            [core.external.http :as http]
            [clojure.data.json  :as json]
            [clojure.java.shell :as shell]))



(declare type-list)
(declare http-request)
(declare curl)


;; Main 
;; must implement: get-entities, filter-entities

(defn get-entities [input]
  (let [response (http-request input)
        status   (:status response)]

    (if (= status 200)

        (let [body (java.net.URLDecoder/decode (:body response))
              json-readable (clojure.string/replace (clojure.string/replace body (re-pattern "\"\\{") "{") (re-pattern "\\}\"") "}")
              json-result   (json/read-str json-readable)
              output        (get json-result "output")
              resources     (if (contains? output "@graph")
                                (get output "@graph")
                                [output])]

             (for [resource resources]
                  { :uri    (get resource "means" ) 
                    :form   (get resource "ann:body")
                    :offset (get resource "beginIndex")
                    :types  (type-list (get resource "@type"))}))
        
        [])
    ))


;; Request 

(defn http-request [input]
  (http/get-response :post "http://139.18.2.164:4444/api" 
  { :headers { "content-type" "application/json" 
               "accept" "application/json" }
    :body    (json/json-str { :input  input
                              :type   "text"
                              :task   "ner" 
                              :output "JSON-LD" })} 
  identity))

;; Aux

(defn type-list [types]
  (remove #(= % "ann:Annotation") (map #(clojure.string/replace % "scmsann:" "") types)))

(defn filter-entities [entities]
  entities)

(defn most-general-type [entity] (clojure.string/replace (last (:types entity)) "http://dbpedia.org/ontology/" ""))