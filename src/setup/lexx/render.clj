(ns setup.lexx.render
  (:require [clostache.parser :as clostache]))


(defn template-path [file-name] (str "setup/lexx/templates/" file-name ".mustache"))
(defn template-file [file-name] (clojure.java.io/file (str "src/" (template-path file-name))))
(defn partials-file [part-name] (template-file (str "forms/" (name part-name))))

(defn get-partials [template-name]
; Finds references to partials in template 
; (path should be given relative to the templates folder, e.g. {{> forms/noun}})
  (let [template (slurp (template-file template-name))
  	    partials (map #(clojure.string/replace % #"> " "") (re-seq #"> \w+" template))]
    (reduce (fn [result p] (assoc result p (slurp (partials-file p)))) {} (map keyword partials))))


;; MAIN

(defn render [template-name context]
  (let [template (template-path template-name)]
    (if (.exists (template-file template-name))
        (clostache/render-resource template context (get-partials template-name))
        (println (str "[ERROR] Template not found: " template-name)))))
