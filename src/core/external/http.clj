(ns core.external.http
  (:require [org.httpkit.client :as client]
            [clojure.data.json  :as json]))


(defn urlize [string] (clojure.string/replace string " " "+"))

(defn get-response [method request options postprocessing] 
  (let [response (case method 
                       :get  (if (empty? options)
                                 (client/get request)
                                 (client/get request options))
                       :post (client/post request options))]
    (postprocessing @response)))

(def get-body (comp json/read-str :body))
