(ns core.definitions.Core_Anaphora 
  (:require [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Condition Sel And Triple]))


;; Anaphora

(def I     (sel nil [(Condition. :referent :speaker) (Condition. :number :singular)]))
(def We    (sel nil [(Condition. :referent :speaker) (Condition. :number :plural)]))
(def YouSg (sel nil [(Condition. :referent :hearer)  (Condition. :number :singular)]))
(def YouPl (sel nil [(Condition. :referent :hearer)  (Condition. :number :plural)]))
(def He    (sel nil [(Condition. :gender :masculine) (Condition. :number :singular)]))
(def She   (sel nil [(Condition. :gender :feminine)  (Condition. :number :singular)]))
(def It    (sel nil [(Condition. :gender :neuter)    (Condition. :number :singular)]))
(def They  (sel nil [(Condition. :number :plural)]))

(defn anaphor [pron] pron)
;(defn poss    [cn pron] (sel (fn [x] (And. (cn x) (Triple. x (sel nil [(Condition. :meaning :poss)]) pron) []))))

;(def this (sel ...)
;(def that (sel ...)
