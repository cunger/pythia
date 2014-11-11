(ns core.nlu.interpretation
    (:require [core.nlu.context.short_term_memory :as stm]
              [clojure.math.combinatorics :as combo]
              [core.definitions.Effects :refer :all]
              [core.definitions.Core_ :refer :all]
              [core.definitions.Core_Anaphora :refer :all]
              [core.definitions.Clauses_ :refer :all]
              [core.definitions.Domain :refer :all]
              [core.definitions.QuestionAnswering :refer :all]
              [core.data.LambdaRDF :refer :all])
    (:import  [core.data.LambdaRDF Term Filter Lambda Select Ask Triple Not And Or Quant Sel Condition Sel Path]))


;; Eval

(defn evaluate [e]
  (in-ns 'core.nlu.interpretation)
  (eval (read-string e))) ; TODO need to switch back namespaces?


;; Interpret AST

; { :language Str :trees (Str) } -> LazySeq { :sem Expr :asts [AST] :lang <grammar_name> }
(defn interpret [parse-result]
  (let [language (:language parse-result)
        trees    (:trees parse-result)
        ; Interpret parse trees and group them by semantics
        t_i      (for [t trees] [t (evaluate (str "(" (constants-to-functions t) ")"))]) 
        is       (distinct (for [[t i] t_i] i))                 
        i_ts     (for [i is] {:sem i :asts (filter (fn [t] (some #(= [t i] %) t_i)) trees) :lang language})] 
        ; Return 
        i_ts))


;; Resolve semantically light expressions, anaphors, etc.
(declare instantiate)

; { :sem Expr :asts [AST] :lang <grammar_name> } -> { :exprs [Expr] :sem Expr :asts [AST] :lang <grammar-name> }
(defn handle-effects [interpretation]
  (reset! stm/expression (:sem interpretation))
  (let [ handled (evaluate (show-as-code (:sem interpretation)))
         instantiated (instantiate handled) ]
  { :exprs instantiated :sem (:sem interpretation) :asts (:asts interpretation) :lang (:lang interpretation) }))

; Expr -> [Expr]
(defn instantiate [e]
  (let [possibilities  (apply combo/cartesian-product (for [[k vs] @stm/candidates] (combo/cartesian-product [k] vs)))
        instantiations (for [iter possibilities] (reduce (fn [x [k v]] (replace-all x k v)) e iter))]
        instantiations))
; TODO No consistency checks and ranking yet!
