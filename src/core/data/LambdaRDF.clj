(ns core.data.LambdaRDF
  (:require [core.nlu.context.short_term_memory :as stm]))


;;;; Expressions ;;;;

(defprotocol Expr 
  (show   [e] "Print expression as code")
  (sparql [e] "Print expression in SPARQL"))


;; Terms
;; kind ::= :uri | :var | :index | :literal

(defrecord Term [kind ident]
           Expr
           (show [this] 
             (case kind 
                   :uri     (str "(Term. :uri \"" ident "\")")
                   :var     (str "(Term. :var \"" ident "\")")
                   :index   (str "(Term. :index \"" ident "\")")
                   :literal (str "(Term. :literal \"" ident "\")")))
           (sparql [this]
             (case kind 
                   :uri    (str "<" ident ">")
                   :var    (str "?" ident)
                   :index  (str "?" ident)
                   :literal ident)))


(defn make-var [c n] (Term. :var (str c n)))

;; Predefined terms

(def rdf-type  (Term. :uri "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
(def owl-Thing (Term. :uri "http://www.w3.org/2002/07/owl#Thing"))

;; Term -> Term

(defrecord Count [v]
           Expr
           (show   [this] (str "(Count. " (show v) ")"))
           (sparql [this] (str "COUNT(DISTINCT " (sparql v) ")")))


;; Filters
;; op ::= :equals | :less | :greater | : lessequals | :greaterequals | ...

(defn op-string [op] (case op :equals "=" :less "<" :greater ">" :lessequals "<=" :greaterequals ">="))

(defrecord Filter [op t1 t2]
           Expr
           (show   [this] (str "(Filter. " op " " (show t1) " " (show t2) ")"))
           (sparql [this] (str "FILTER (" (sparql t1) (op-string op) (sparql t2) ")")))


;; Triples

(defrecord Triple [subj prop obj]
           Expr
           (show   [this] (str "(Triple. " (show subj) " " (show prop) " " (show obj) ")"))
           (sparql [this] (str (sparql subj) " " (sparql prop) " " (sparql obj) " . ")))


;; Functions 

(defrecord Lambda [v e]
           Expr
           (show   [this] (str "(fn [" (show v) "] " (show e) ")"))
           (sparql [this] (str "")))

(defrecord Ask [e]
           Expr
           (show   [this] (str "(check " (show e) ")"))
           (sparql [this] (str "ASK { " (sparql e) " }")))

(defrecord Select [v e]
           Expr
           (show   [this] (str "(bind " (show v) " " (show e) ")"))
           (sparql [this] (str "SELECT DISTINCT " (sparql v) " WHERE { " (sparql e) " }")))


;; Logical operators

(defrecord Not [e]
           Expr
           (show   [this] (str "(lnot " (show e) ")"))
           (sparql [this] (str "FILTER NOT EXISTS { " (sparql e) " }")))

(defrecord And [e1 e2]
           Expr
           (show   [this] (str "(land " (show e1) " " (show e2) ")"))
           (sparql [this] (str (sparql e1) " " (sparql e2))))

(defrecord Or [e1 e2]
           Expr
           (show   [this] (str "(lor " (show e1) " " (show e2) ")"))
           (sparql [this] (str "{ " (sparql e1) " } UNION { " (sparql e2) " }")))


;; Generalized quantifiers
;; quant ::= :some | :all | :no | ...

(defn join-string [f s xs] (if (empty? xs) "" (apply str (concat (map (fn [x] (str (f x) s)) (drop-last xs)) (f (last xs))))))

(defrecord Quant [q v es1 es2]
           Expr
           (show   [this] (str "(quant " (name q) " " (show v) " [" (join-string show " " es1) "] [" (join-string show " " es2) "])"))
           (sparql [this] (case q 
                                :some (str (join-string sparql " " es1) " " (join-string sparql " " es2))
                                :no   (str "FILTER NOT EXISTS { " (join-string sparql " " es1) " " (join-string sparql " " es2) " }")
                                :all  (str (join-string sparql " " es2) " SELECT " (sparql v) " { " (join-string sparql " " es1)  " } GROUP BY " (show v)))))


;; Effects

(defrecord Condition [k v]
           Expr
           (show   [this] (str "{" k " " v "}"))
           (sparql [this] (str "")))


(defrecord Sel [cs]
           Expr
           (show   [this] (str "(sel " (join-string show " " cs) ")"))
           (sparql [this] (sparql (make-var "x" (stm/get-fresh!)))))

(defrecord Path [ent1 ent2 cs]
           Expr
           (show   [this] (str "(bridge " (show ent1) " " (show ent2) " " (join-string show " " cs) ")"))
           (sparql [this] (let [p (make-var "p" (stm/get-fresh!))] 
                          (str "{ " (sparql ent1) " " (sparql p) " " (sparql ent2) " . } UNION { " (sparql ent2) " " (sparql p) " " (sparql ent1) " . }"))))


;; Extending Expr to nil
(extend-type nil Expr (show [_] "nil") (sparql [_] "nil"))
