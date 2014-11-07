(ns core.definitions.Core_Anaphora 
  (:require [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Condition Sel And Triple]))


;; Anaphora

(def I     (Sel. [(Condition. :referent :speaker) (Condition. :number :singular)]))
(def We    (Sel. [(Condition. :referent :speaker) (Condition. :number :plural)]))
(def YouSg (Sel. [(Condition. :referent :hearer)  (Condition. :number :singular)]))
(def YouPl (Sel. [(Condition. :referent :hearer)  (Condition. :number :plural)]))
(def He    (Sel. [(Condition. :gender :masculine) (Condition. :number :singular)]))
(def She   (Sel. [(Condition. :gender :feminine)  (Condition. :number :singular)]))
(def It    (Sel. [(Condition. :gender :neuter)    (Condition. :number :singular)]))
(def They  (Sel. [(Condition. :number :plural)]))

(defn anaphor [pron] pron)
;(defn poss    [cn pron] (sel (fn [x] (And. (cn x) (Triple. x (sel nil [(Condition. :meaning :poss)]) pron) []))))

;(def this (sel ...)
;(def that (sel ...)
