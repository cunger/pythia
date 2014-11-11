(ns core.definitions.Effects
  (:require [core.nlu.context.short_term_memory :as stm]
            [core.nlu.reasoning.oracle :as oracle]
            [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Path Not And Or Quant Ask Select]))



;; Functions without effects

(defn bind  [v e]   (Select. v e))
(defn check [e]     (Ask. e))

(defn lnot   [e]     (Not. e))
(defn land   [e1 e2] (And. e1 e2))
(defn lor    [e1 e2] (Or.  e1 e2))

(defn quant  [q v es1 es2] (Quant. q v es1 es2))


;; Functions with effects 

(defn sel [& conditions]
  (let [i (stm/get-fresh!)]
  ; oracle/... context/...
  ; add candidates for x to stm/candidates 
  ; return x
  i)) 

(defn bridge [e1 e2 & conditions]
  (let [candidates (oracle/rank (oracle/find-paths e1 e2 conditions))
        i (stm/get-fresh!)]
    (if (empty? candidates)
        (Path. e1 e2 conditions)
        (do (swap! stm/candidates assoc i candidates)
            (Term. :index i)))))
