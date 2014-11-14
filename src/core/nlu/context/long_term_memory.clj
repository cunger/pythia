(ns core.nlu.context.long_term_memory)


;; The long-term memory comprises all (stateful) information that are needed during the whole interaction. 

(defn tokens [] ; :: [Str]
  (clojure.string/split (slurp "resources/grammars/tokens") #"\s"))


;; Print

(defn show-ltm []
  (println "LTM: ..."))