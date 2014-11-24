(ns core.definitions.Chunk
  (:require [setup.lexx.utils :as utils]
            [core.nlu.context.short_term_memory :as stm]
            [core.nlu.reasoning.oracle :as oracle]
            [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Path Not And Or Quant Ask Select]))



(defn OneChunk  [c]    [c])
(defn PlusChunk [c cs] (cons c cs))
(defn ChunkPhr  [cs]   (utils/wrap And. cs))

(defn Class_Chunk     [c] (fn [x] (Triple. x rdf-type c)))
(defn Entity_Chunk    [e] e)
(defn Predicate_Chunk [p] (fn [x] (p x)))
(defn Relation_Chunk  [r] (fn [x y] (r x y)))
(defn Statement_Chunk [s] s)
