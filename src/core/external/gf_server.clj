(ns core.external.gf_server
  (:require [settings]
            [setup.utils :refer [contains-string?]]
            [core.external.http :as http]
            [clojure.data.json  :as json]
            [clojure.java.shell :as shell]))


;; Aux

(defn build-request 
  ([grammar string] (str settings/localhost grammar string))
  ([string] (str settings/localhost string)))


;; Start GF server 

(defn start-server []
  (println "Starting GF server...") 
  ; TODO check whether running. if yes, restart, else start.
)


;; Check for grammars

(defn loaded-grammars [] 
  (let [request (build-request "grammars.cgi")
        gs (http/get-response request http/get-body)]
    (if gs gs [])))

(defn check-for-grammar [g]
  (let [gs (loaded-grammars)]
    (if (empty? gs)
        (do
        (println (str "No grammars loaded.\n(Probably " settings/gf-server-grammars " is empty.)"))
        (java.lang.System/exit 1))
        (do
        (println "Available grammars:" gs)
        (newline)
        (if (some #{g} gs) 
            (println (str "Loaded " g "."))
            (do 
              (println (str "[ERROR] " g " is not available."))
              (java.lang.System/exit 1)))))))
  ;; TODO if grammar(s) not available, recommend generic fallback grammar


;; Parsing 

(defn no-uninstantiated-metavariables? [tree]
  (empty? (re-find #"\s\?\d+\s" tree)))

; grammar string -> seq { :<lang> [parse-tree] } 
(defn request-parse [grammar string]

  (let [request (build-request grammar (str "?command=parse&input=" (http/urlize string)))
        result  (http/get-response request :body)]

    (if-not (nil? result)
      (for [r (json/read-str result)] 
        (if (contains? r "trees")
            (let [trees (filter no-uninstantiated-metavariables? (get r "trees"))]
                 { :language (get r "from") :trees trees })))
      [])))


;; Linearization 

(defn request-linearization [grammar tree]
  
  (let [request  (build-request grammar (str "?command=linearize&tree=" (http/urlize tree)))
        response (http/get-response request :body)]
     (get (first (json/read-str response)) "text")))

(defn useful? [lin]
  (not (or (clojure.string/blank? lin)        ; lin should not be blank
           (contains-string? lin "\\[")       ; lin should not contain unlinearized elements
           (contains-string? lin "XXX")       ; lin should not contain dummy elements
           (contains-string? lin "Entity")))) ; lin should not contain named entity placeholders


;; Random generation

(defn generate-random [grammar]

  (loop  [grammar  grammar]
    (let [request  (build-request grammar "?command=random")
          response (http/get-response request http/get-body)
          random   (get (first response) "tree")
          lin      (request-linearization grammar random)]
      (if (useful? lin) 
          lin
          (recur grammar)))))
