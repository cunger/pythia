(ns core.definitions.QuestionAnswering
  (:require [core.nlu.context.short_term_memory :as stm]
  	        [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Not And Filter Quant Sel Condition Ask Select]))


(defn gimme [pred]
  (let [v (make-var "v" (stm/get-fresh!))]
    (Select. v (pred v))))

(defn Get [pred] (gimme pred))