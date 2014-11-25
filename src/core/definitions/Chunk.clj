(ns core.definitions.Chunk
  (:require [setup.lexx.utils :as utils]
            [core.nlu.context.short_term_memory :as stm]
            [core.nlu.reasoning.oracle :as oracle]
            [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Path Not And Or Quant Ask Select]))



(defn OneChunk  [c]    [c])
(defn PlusChunk [c cs] (cons c cs))
(defn ChunkPhr  [cs]   (utils/wrap And. cs))


(defn Class_Chunk [c] 
  (let [x (make-var "e" (stm/get-fresh!))] 
       (Triple. x rdf-type c)))

(defn Predicate_Chunk [p] 
  (let [x (make-var "e" (stm/get-fresh!))] 
       (p x)))

(defn Relation_Chunk [r] 
  (let [x (make-var "e" (stm/get-fresh!))
        y (make-var "e" (stm/get-fresh!))] 
       (r x y)))

(defn Entity_Chunk [e] 
  (let [x (make-var "e" (stm/get-fresh!))
        p (make-var "p" (stm/get-fresh!))] 
       (Or. (Triple. x p e) (Triple. e p x))))

(defn Statement_Chunk [s] s)
