(ns core.nlu.reasoning.oracle
  (:require [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Triple Term]))


(defn bridge [resource1 resource2 conditions] 
  (Triple. resource1 (Term. :var "p") resource2)
  ;; TODO 
)