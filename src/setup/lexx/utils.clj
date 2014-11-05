(ns setup.lexx.utils
  (:require [setup.lexx.signature :as signature]
            [setup.utils :refer :all]
            [clojure.set :as set]))


;; RDF

(def rdf-type "(Term. :uri \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\")")

;; STRING 

(defn wrap [operator strings]
  (let [len (count strings)]
    (clojure.string/join (concat (for [i (range 0 len)] 
                                      (if (= i (- len 1)) 
                                          (nth strings i)
                                          (str "(" operator " " (nth strings i) " "))) 
                                 (for [i (range 1 len)] ")")))))

;; URI 

(defn uri? [string] (or (contains-string? string "/") (contains-string? string "#")))

;; URI TO IDENTIFIER

(defn to-ascii [string]
  (clojure.string/replace string #"[^\x00-\x7F]" ""))

(defn to-identifier [string]
  ((comp 
    #(clojure.string/replace % #"\s+" "_")
    #(clojure.string/replace % #"-"   "_")
    #(clojure.string/replace % #"\+"  "_")
    #(clojure.string/replace % #"/"   "_")
    #(to-ascii %))
   string))

(defn frag [uri]
  ; if uri contains neither "#" nor "/"
  (if (and (not (contains-string? uri "#")) (not (contains-string? uri "/")))
      ; probably: if it contains ":", it is a blank node, else a literal
      (str (if (contains-string? uri ":") "node_" "literal_") (clojure.string/replace uri #"[^A-Za-z0-9]" ""))
      ; else proceed
      (let [matching-prefixes (filter #(.startsWith uri %) (keys @signature/namespaces))]
        (if-not (empty? matching-prefixes)
          ; if prefix of uri is in signature/namespaces, replace it accordingly
          (let [prefix (first matching-prefixes)
                ident  (clojure.string/replace uri prefix "")]
            (str (get @signature/namespaces prefix) "_" (to-identifier ident)))
          ; otherwise replace prefix with new abbreviation and store it in signature/namespaces 
          (let [new-n       (str "n" @signature/namespaces-i)
                hash-split  (clojure.string/split uri #"#")
                slash-split (clojure.string/split uri #"/")
                split       (if (> (count hash-split) 1) hash-split slash-split)
                separator   (if (> (count hash-split) 1) "#" "/")
                prefix      (str (clojure.string/join separator (init split)) separator)]
            (do 
               (signature/add-to-namespaces! prefix new-n) 
               (str new-n "_" (to-identifier (last split)))))))))

(defn frag-without-prefix [uri]
  (cond (contains-string? uri "#") (last (clojure.string/split uri #"#"))
        (contains-string? uri "/") (last (clojure.string/split uri #"/"))
        :else                           (str "id_" (to-identifier uri))))

; ATTENTION unfrag doesn't work because to-identifier replaces a bunch of characters by "_" (which is not reversible)
; 
; (defn unfrag [string]
;   (let [split  (clojure.string/split string #"_")
;         prefix (first split)
;         namesp (get @signature/namespaces prefix (str prefix "_"))]
;    (str namesp (clojure.string/join "_" (rest split)))))

;; ARRAY

(defn clean-map [m]
  ; remove nil and [] values, and [v] --> v
  (fmap-vals (fn [v] (if (and (sequential? v) (= (count v) 1)) (first v) v)) 
             (apply dissoc (remove-nil-values m) (filter (fn [k] (and (sequential? (get m k)) (empty? (get m k)))) (keys m)))))
