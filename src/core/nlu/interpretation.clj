(ns core.nlu.interpretation
    (:require [core.data.LambdaRDF :refer :all]
              [core.definitions.Core_ :refer :all]
              [core.definitions.Core_Anaphora :refer :all]
              [core.definitions.Clauses_ :refer :all]
              [core.definitions.Domain :refer :all]
              [core.definitions.QuestionAnswering :refer :all]))


;; Interpretation

(defn evaluate [e]
  (set-fresh! 0)
  (in-ns 'core.nlu.interpretation)
  (eval (read-string e))) ; TODO need to switch back namespaces


;; { :language <Str> :trees (<Str>) } -> LazySeq { :sem <meaning_represenation> :asts [<parse-trees>] :lang <grammar_name> }
(defn interpret [parse-result]
  (let [language (:language parse-result)
        trees    (:trees parse-result)
        ; Interpret parse trees and group them by semantics
        t_i      (for [t trees] [t (evaluate (str "(" (constants-to-functions t) ")"))]) 
        is       (distinct (for [[t i] t_i] i))                 
        i_ts     (for [i is] {:sem i :asts (filter (fn [t] (some #(= [t i] %) t_i)) trees) :lang language})] 
        ; Return 
        (println ">>>>>" i_ts) ; DEBUG
        i_ts))
