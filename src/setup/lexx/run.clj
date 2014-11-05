(ns setup.lexx.run
  (:require [setup.utils :as utils]
            [setup.lexx.signature :as signature]
            [setup.lexx.ontology  :as ontology]
            [setup.lexx.lexicon   :as lexicon]
            [setup.lexx.render :refer [render]]
            [seabass.core :as seabass]))


; TODO set proper classpath?
(defn target-path [file-name] (str "src/setup/lexx/target/" file-name)) 

;; MAIN 

(defn run [& files]

  ; clean target folder
  (utils/clean (target-path ""))
  
  ; why is files of the form ((arg1 arg2 ...)) instead of (arg1 arg2 ...)?
  (let [graph (apply seabass/build (first files))]

    (do
      ; process ontological and lexical information
      (println "Processing the ontology...")
  	  (ontology/process graph)
      (println "Processing the lexicon...")
  	  (lexicon/process  graph)
      ; write abstract syntax and definitions file
      (println "Writing abstract syntax...")
      (spit (target-path "Domain.gf")         (render "abstract"    @signature/abstract))
      (spit (target-path "defininitions.clj") (render "definitions" @signature/abstract))
      ; write a concrete syntax for each language for which a lexicon was provided
      (println "Writing concrete syntax...")
  	  (doseq [language (keys @signature/concrete)]
        (let [context (assoc (language @signature/concrete) :language (name language))]
          (spit (target-path (str "Domain" (name language) ".gf")) (render "concrete" context))))
    ))

  (signature/report)
)
