(ns core.external.http
  (:require [org.httpkit.client :as client]
            [clojure.data.json  :as json]))


(defn urlize [string] (clojure.string/replace string " " "+"))

(defn get-response [request postprocessing] 
  (let [response (client/get request {:headers {"Accept" "application/json"}})]
    (postprocessing @response)))

(def get-body (comp json/read-str :body))
