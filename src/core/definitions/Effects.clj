(ns core.definitions.Effects
  (:require [core.nlu.context.short_term_memory :as stm]
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
  (let [x (make-var "x" (stm/get-fresh!))]
  ; oracle/... context/...
  ; add candidates for x to stm/candidates 
  ; return x
  x)) 

(defn bridge [e1 e2 & conditions]
  (let [e (make-var "e" (stm/get-fresh!))]
  ; oracle/...
  ; add candidates for e to stm/candidates 
  ; return bridge, i.e. (triple e1 p e2) or (and (triple e1 p1 v) (triple v p2 e2)) or ...
  (Path. e1 e2 []))) 
