(ns core.definitions.Clauses_
  (:require [core.data.LambdaRDF :refer :all])
  (:import  [core.data.LambdaRDF Term Count Triple Not And Or Filter Quant Sel Ask Select]))



;; aux 

(defn access [k m] (if (k m) (k m) (k (m))))


;; Clauses 

; Utterances 

(def say    identity) 
(def ask    identity)
(def prompt identity)

; Declarative sentences

(def present_pos identity)
(def past_pos    identity)
(def future_pos  identity)

(defn present_neg [s] (Not. s))
(defn past_neg    [s] (Not. s))
(defn future_neg  [s] (Not. s))

; Coordination

(defn AndSentence [s1,s2] (And. s1 s2))
(defn OrSentence  [s1,s2] (Or.  s1 s2))

; Questions

(defn YesNo [s] (Ask. s))

;(defn queryadv [whadv s] (sel s [(Condition. :type whadv)]))
; (def Where :place)
; (def When  :datetime)
; (def How   :manner)
; (def Why   :reason)

(defn query [wh body]
  (let [p (access :project wh) v (access :var wh) c (access :cond wh)]
       (Select. p (if-not c (body v) (And. c (body v))))))

(defn query_subj [rel wh e] (query wh (fn [x] (rel x e))))
(defn query_obj  [rel e wh] (query wh (fn [x] (rel e x))))

(defn query_pred [wh vp] (query wh (fn [x] (vp x))))

(defn query_copula_whPron [wh e] (query wh (fn [x] (Filter. :equals x e))))
; (defn query_copula_whAdv [_ whadv e] ...)
; TODO case whadv ...

(defn Who  [] (inc-fresh!) (let [v (make-var @fresh)] {:project v :var v :cond nil}))
(defn What [] (inc-fresh!) (let [v (make-var @fresh)] {:project v :var v :cond nil}))

(defn WhichSg [cn] (inc-fresh!) (let [v (make-var @fresh)] {:project v :var v :cond (cn v)}))
(defn WhichPl [cn] (inc-fresh!) (let [v (make-var @fresh)] {:project v :var v :cond (cn v)}))
(defn HowMany [cn] (inc-fresh!) (let [v (make-var @fresh)] {:project (Count. v) :var v :cond (cn v)}))

; Imperatives

; (defn imperative [pred] ...)

; Modalities

;    MCan, MKnow, MMust, MWant : Modality;
;    mod_clause : Modality -> Predicate -> Entity -> Statement;
