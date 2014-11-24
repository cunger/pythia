(ns setup.ggen.run
  (:require [settings]
            [setup.utils        :as utils]
            [clostache.parser   :as clostache]
            [clojure.java.io    :as io]
            [clojure.java.shell :as shell]))


;; DEF

(def lexx-target     "src/setup/lexx/target/")
(def grammars-folder "src/setup/ggen/grammars/")
(def ggen-folder     "src/setup/ggen/")
(def temp            "src/setup/ggen/temp/")

(defn folder [pre]
  (case pre
        "Core_"    (str grammars-folder "core/core/")
        "Clauses_" (str grammars-folder "core/clauses/")))

(defn extensions [pre] 
  (case pre
        "Core_"    ["Anaphora","Datatypes"]
        "Clauses_" []))


;; AUX

(def domain-grammar-files   (filter #(utils/file-name-matches? #"Domain.*\.gf" %) (utils/files-in lexx-target)))

(defn find-languages []
  (for [f domain-grammar-files]
    (let [match (re-find (re-matcher #"Domain(.+)\.gf" (.getName f)))]
      (if-not (nil? match)
              (nth  match 1)))))

(defn relevant? [file prefix languages]
  (let [regex (re-pattern (str "(" prefix ".gf)|(" prefix "I.gf)|(" prefix (clojure.string/join "|" (map #(str "(" % ")") languages)) ".gf)"))]
       (re-matches regex (.getName file))))


;; MAIN

(defn run [target]

  ;; detect languages
  (let [languages (filter #(not (nil? %)) (find-languages))]

  ;; empty temp
  (utils/clean temp)

  ;; copy all required grammar modules to temp
   ; Domain (setup/lexx/target/Domain*.gf)
    (doseq [f domain-grammar-files]
      (utils/copy-to f temp))
   ; Basic
    (doseq [f (filter #(relevant? % "Basic" languages) (utils/files-in (str (folder "Core_") "Basic/")))]
      (utils/copy-to f temp))
   ; Core and Clauses
    (doseq [pre ["Core_" "Clauses_"]]
      ; copy
      (doseq [ext (map #(str pre %) (cons "" (extensions pre)))
              f (filter #(relevant? % ext languages) (utils/files-in (str (folder pre) ext)))]
        (utils/copy-to f temp))
      ; render
      (let [abstract (utils/strip-src (str (folder pre) pre "abstract.mustache"))
            concrete (utils/strip-src (str (folder pre) pre "concrete.mustache"))]
        (do
          (spit (str temp (clojure.string/replace pre "_" "") ".gf") 
                (clostache/render-resource abstract { :extensions (extensions pre) }))
          (doseq [language languages]
          (spit (str temp (clojure.string/replace pre "_" "") language ".gf")
                (clostache/render-resource concrete { :extensions (extensions pre) :language language })))))
    )
   ; Chunk
    (doseq [f (filter #(relevant? % "Chunk" languages) (utils/files-in (str grammars-folder "chunk/")))]
      (utils/copy-to f temp))  
    ; Application 
    (let [c       { :target target :startcat settings/startcat } 
          c-tc    (if settings/task-content  (assoc c    :task-content  settings/task-content)  c) 
          c-tc-tm (if settings/task-modality (assoc c-tc :task-modality settings/task-modality) c-tc)]
      (do
        ; copy 
        (if settings/task-content
            (doseq [f (filter #(relevant? % settings/task-content languages) (utils/files-in (str grammars-folder "task/content/"  settings/task-content)))]  
              (utils/copy-to f temp)))
        (if settings/task-modality
            (doseq [f (filter #(relevant? % settings/task-content languages) (utils/files-in (str grammars-folder "task/modality/" settings/task-modality)))] 
              (utils/copy-to f temp)))
        ; render 
        (spit (str temp target ".gf")
              (clostache/render-resource (utils/strip-src (str grammars-folder "Application_abstract.mustache")) c-tc-tm))
        (doseq [language languages]
        (spit (str temp target language ".gf")
              (clostache/render-resource (utils/strip-src (str grammars-folder "Application_concrete.mustache")) (assoc c-tc-tm :language language))))))

    ; compile app grammar in temp
    (let [grammars (clojure.string/join " " (map #(str target % ".gf") languages))
          compiled (shell/sh "gf" "-make" grammars :dir temp)] ; +RTS -K66M -RTS
      (if (= (:exit compiled) 0) 
          (println (str "Application grammar (" target ".pgf) successfully compiled."))
          (do 
            (println "Compilation failed with the following error message:\n" (:err compiled))
            (java.lang.System/exit 1))))

    ; write file with all tokens covered by the grammar to resources folder
    (spit (io/file (str temp "tokens.sh")) 
          (clostache/render-resource (str ggen-folder "tokens.mustache") { :target (str target ".pgf") }))
    (spit (io/file (str temp "tokens_fallback.sh")) 
          (clostache/render-resource (str ggen-folder "tokens_fallback.mustache") { :languages languages }))    
    (spit (io/file (str temp "tokens"))
          (:out (shell/sh "sh" "tokens.sh" :dir temp)))
    (spit (io/file (str temp "tokens_fallback"))
          (:out (shell/sh "sh" "tokens_fallback.sh" :dir temp)))
))
