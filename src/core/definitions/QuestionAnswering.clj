(ns core.definitions.QuestionAnswering
  (:require [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Not And Filter Quant Sel Condition Ask Select]))


(defn gimme [pred]
  (let [v (get-fresh-var!)]
    (Select. v (pred v))))

(defn GiveMe    [pred] (gimme pred))
(defn GiveMeAll [pred] (gimme pred))