(ns core.definitions.Core_
  (:require [core.external.ner.ner :as ner]
            [core.nlu.reasoning.oracle :as oracle]
            [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Triple Not And Filter Quant Sel Condition Ask Select]))


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
  (let [v (get-fresh-var!)]
    (fn [x] (Quant. :some v [] [(rel v x)]))))

(defn modify [pred1 pred2]
  (fn [x] (And. (pred1 x) (pred2 x))))

; Determiners 

(defn The_empty [e] e)

(defn The  [pred] (sel pred []))
(defn This [pred] (sel pred []))
(defn That [pred] (sel pred []))

(defn gq1 [quant]
  (fn [pred1 pred2]
      (let [v (get-fresh-var!)]
        (Quant. quant v [(pred1 v)] [(pred2 v)]))))

(defn gq2 [quant]
  (fn [pred rel]
      (let [v (get-fresh-var!)]
       (fn [x] (Quant. quant v [(pred v)] [(rel x v)])))))

(def Some1 (gq1 :some))
(def Some2 (gq2 :some))
(def All1  (gq1 :all ))
(def All2  (gq2 :all ))
(def No1   (gq1 :no  ))
(def No2   (gq2 :no  ))

; TODO Only

; For NER 

(def Entity1 (ner/resolve-entity :Entity1))
(def Entity2 (ner/resolve-entity :Entity2))
(def Entity3 (ner/resolve-entity :Entity3))
(def Entity4 (ner/resolve-entity :Entity4))
(def Entity5 (ner/resolve-entity :Entity5))
(def Entity6 (ner/resolve-entity :Entity6))
(def Entity7 (ner/resolve-entity :Entity7))
(def Entity8 (ner/resolve-entity :Entity8))
(def Entity9 (ner/resolve-entity :Entity9))

; For robust parsing

;(def  UnknownClass (fn [x] (Triple. x rdf-type (sel nil [(Condition. :type :class)]))))
;(defn UnknownEntity [cn] (sel cn [(Condition. :type :individual)]))
;(defn UnknownPredicate [cn] (fn [x] (Triple. x rdf-type (sel (fn [_] (cn x)) [(Condition. :type :class)]))))  
;(defn UnknownRelation [cn1,cn2] (fn [x,y] (Triple. x (sel nil [(Condition. :type :property) (Condition. :domain cn1) (Condition. :range cn2)]) y)))

;; Semantically light expressions

(defn light_Rel [conditions] (fn [x,y] (oracle/bridge x y conditions)))

(def have_Rel    (light_Rel []))
(def with_Rel    (light_Rel []))
(def possess_Rel (light_Rel []))
(def in_Rel      (light_Rel [(Condition. :kind :location)]))
(def in_Rel      (light_Rel [(Condition. :kind :origin)]))
