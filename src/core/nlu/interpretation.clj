(ns core.nlu.interpretation
    (:require [core.definitions.Effects :refer :all]
              [core.definitions.Core_ :refer :all]
              [core.definitions.Core_Anaphora :refer :all]
              [core.definitions.Clauses_ :refer :all]
              [core.definitions.Domain :refer :all]
              [core.definitions.QuestionAnswering :refer :all]
              [core.data.LambdaRDF :refer :all])
    (:import  [core.data.LambdaRDF Term Filter Lambda Select Ask Triple Not And Or Quant Sel Condition Sel Path]))



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

; { :sem Expr :asts [AST] :lang <grammar_name> } -> { :sem Expr :exprs [{ :expr Expr :score float }] :asts [AST] :lang <grammar-name> }
(defn handle [interpretation]
  (let [ evaluated    (evaluate (show (:sem interpretation)))
         instantiated (instantiate evaluated) ]
  { :exprs instantiated :sem (:sem interpretation) :asts (:asts interpretation) :lang (:lang interpretation) }))

; Expr -> [{ :expr Expr :score float }]
(defn instantiate [e]
  ; Based on candidate instantiations in STM, instantiate and score
  [{ :expr e :score 1 }]) ; TODO
