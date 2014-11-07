(ns core.definitions.Core_
  (:require [core.external.ner.ner :as ner]
            [core.nlu.context.short_term_memory :as stm]
            [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Path Not And Filter Quant Sel Condition Ask Select]))


;; Core

(defn owl_Thing [x] (Triple. x rdf-type owl-Thing))

(defn class2predicate  [c] c)
(defn entity2predicate [e] (fn [x] (Filter. :equals x e)))

; Coordination

; TODO AndEntity
; TODO OrEntity 

; Application and modification

(defn apply1 [pred arg]
  (pred arg))

(defn apply2 [rel subj obj]
  (rel subj obj))

(defn partial_apply1 [rel subj]
  (fn [x] (rel subj x)))

(defn partial_apply2 [rel obj]
  (fn [x] (rel x obj)))

(defn existential_closure [rel]
  (let  [v (make-var "v" (stm/get-fresh!))]
    (fn [x] (Quant. :some v [] [(rel v x)]))))

(defn modify [pred1 pred2]
  (fn [x] (And. (pred1 x) (pred2 x))))

; Determiners 

(defn The_empty [e] e)

(defn The  [pred] (Sel. [(Condition. :satisfy pred)]))
(defn This [pred] (Sel. [(Condition. :satisfy pred)]))
(defn That [pred] (Sel. [(Condition. :satisfy pred)]))

(defn gq1 [quant]
  (fn [pred1 pred2]
      (let [v (make-var "v" (stm/get-fresh!))]
        (Quant. quant v [(pred1 v)] [(pred2 v)]))))

(defn gq2 [quant]
  (fn [pred rel]
      (let [v (make-var "v" (stm/get-fresh!))]
       (fn [x] (Quant. quant v [(pred v)] [(rel x v)])))))

(def Some1 (gq1 :some))
(def Some2 (gq2 :some))
(def All1  (gq1 :all ))
(def All2  (gq2 :all ))
(def No1   (gq1 :no  ))
(def No2   (gq2 :no  ))

; TODO Only

; For NER 

(defn Entity1 [] (Term. :uri (ner/resolve-entity :Entity1)))
(defn Entity2 [] (Term. :uri (ner/resolve-entity :Entity2)))
(defn Entity3 [] (Term. :uri (ner/resolve-entity :Entity3)))
(defn Entity4 [] (Term. :uri (ner/resolve-entity :Entity4)))
(defn Entity5 [] (Term. :uri (ner/resolve-entity :Entity5)))
(defn Entity6 [] (Term. :uri (ner/resolve-entity :Entity6)))
(defn Entity7 [] (Term. :uri (ner/resolve-entity :Entity7)))
(defn Entity8 [] (Term. :uri (ner/resolve-entity :Entity8)))
(defn Entity9 [] (Term. :uri (ner/resolve-entity :Entity9)))

(defn constants-to-functions [string]
  (let [replacements [ [#"Entity1" "(Entity1)"]
                       [#"Entity2" "(Entity2)"]
                       [#"Entity3" "(Entity3)"]
                       [#"Entity4" "(Entity4)"]
                       [#"Entity5" "(Entity5)"]
                       [#"Entity6" "(Entity6)"]
                       [#"Entity7" "(Entity7)"]
                       [#"Entity8" "(Entity8)"]
                       [#"Entity9" "(Entity9)"] ]]
    (reduce #(apply clojure.string/replace %1 %2) string replacements)))
; TODO you can do this way more elegantly!

; For robust parsing

;(def  UnknownClass (fn [x] (Triple. x rdf-type (sel nil [(Condition. :type :class)]))))
;(defn UnknownEntity [cn] (sel cn [(Condition. :type :individual)]))
;(defn UnknownPredicate [cn] (fn [x] (Triple. x rdf-type (sel (fn [_] (cn x)) [(Condition. :type :class)]))))  
;(defn UnknownRelation [cn1,cn2] (fn [x,y] (Triple. x (sel nil [(Condition. :type :property) (Condition. :domain cn1) (Condition. :range cn2)]) y)))

;; Semantically light expressions

(def have_Rel    (fn [x y] (Path. x y [])))
(def with_Rel    (fn [x y] (Path. x y [])))
(def possess_Rel (fn [x y] (Path. x y [(Condition. :kind :possess)])))
(def in_Rel      (fn [x y] (Path. x y [(Condition. :kind [:location :time])])))
