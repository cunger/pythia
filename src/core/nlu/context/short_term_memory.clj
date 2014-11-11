(ns core.nlu.context.short_term_memory)


;; The short-term memory comprises all (stateful) information that are needed during processing one input utterance. 


(def named-entities (atom {}))
(def candidates     (atom {}))

(def expression     (atom nil))

;; Fresh variable supply

(def fresh (atom 0))

(defn set-fresh! [n] (reset! fresh n))
(defn inc-fresh! [] (swap! fresh inc))
(defn get-fresh! [] (inc-fresh!) @fresh)


;; Reset 

(defn init! []
  (reset! named-entities {})
  (reset! candidates {})
  (reset! expression nil)
  (reset! fresh 0))


;; Print

(defn show-stm []
  (println "STM: ..."))