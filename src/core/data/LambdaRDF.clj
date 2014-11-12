(ns core.data.LambdaRDF
  (:require [core.nlu.context.short_term_memory :as stm]))


;;;; Expressions ;;;;

(defprotocol Expr 
  ;; Operations 
  (replace-all    [e i x] "Replaces all occurences of (Term :index i) in e for x")  ; :: Expr -> Expr
  (get-rdf-type   [e v]   "Extracts the type(s) of v (if mentioned)") ; :: Expr -> [{ :op (:id | :domain | :range) :uri Str }]
  ;; Printing :: Expr -> Str
  (show-as-code   [e]     "Print expression as code")
  (show-as-sparql [e]     "Print expression in show-as-sparql"))


;; Terms
;; kind ::= :uri | :var | :literal | :index

(defrecord Term [kind ident]
           Expr
           ; Operations 
           (replace-all  [this i x]
             (case kind
                   :index (if (= ident i) x (Term. kind ident))
                   (Term. kind ident)))           
           (get-rdf-type [this v] [])
           ; Printing
           (show-as-code [this] 
             (case kind 
                   :uri     (str "(Term. :uri \"" ident "\")")
                   :var     (str "(Term. :var \"" ident "\")")
                   :index   (str "(Term. :index \"" ident "\")")
                   :literal (str "(Term. :literal \"" ident "\")")))
           (show-as-sparql [this]
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
           (replace-all    [this i x] (Count. (replace-all v i x)))
           (get-rdf-type   [this v] [])
           (show-as-code   [this] (str "(Count. " (show-as-code v) ")"))
           (show-as-sparql [this] (str "COUNT(DISTINCT " (show-as-sparql v) ")")))


;; Filters
;; op ::= :equals | :less | :greater | : lessequals | :greaterequals | ...

(defn op-string [op] (case op :equals "=" :less "<" :greater ">" :lessequals "<=" :greaterequals ">="))

(defrecord Filter [op t1 t2]
           Expr
           (replace-all    [this i x] (Filter. op (replace-all t1 i x) (replace-all t2 i x)))
           (get-rdf-type   [this v] [])
           (show-as-code   [this] (str "(Filter. " op " " (show-as-code t1) " " (show-as-code t2) ")"))
           (show-as-sparql [this] (str "FILTER (" (show-as-sparql t1) (op-string op) (show-as-sparql t2) ")")))


;; Triples

(defrecord Triple [subj prop obj]
           Expr
           (replace-all    [this i x] (Triple. (replace-all subj i x) (replace-all prop i x) (replace-all obj i x)))
           (get-rdf-type   [this v]   (cond (and (= prop rdf-type) (= subj v))
                                            [{ :op :id :uri (:ident obj) }]
                                            (and (= (:kind prop) :uri) (= subj v))
                                            [{ :op :domain :uri (:ident prop) }]
                                            (and (= (:kind prop) :uri) (= obj v))
                                            [{ :op :range :uri (:ident prop) }]
                                            :else []))
           (show-as-code   [this] (str "(Triple. " (show-as-code subj) " " (show-as-code prop) " " (show-as-code obj) ")"))
           (show-as-sparql [this] (str (show-as-sparql subj) " " (show-as-sparql prop) " " (show-as-sparql obj) " . ")))


;; Functions 

(defrecord Lambda [v e]
           Expr
           (replace-all    [this i x] (Lambda. (replace-all v i x) (replace-all e i x)))
           (get-rdf-type   [this v]   (get-rdf-type e v))
           (show-as-code   [this] (str "(fn [" (show-as-code v) "] " (show-as-code e) ")"))
           (show-as-sparql [this] (str "")))

(defrecord Ask [e]
           Expr
           (replace-all    [this i x] (Ask. (replace-all e i x)))
           (get-rdf-type   [this v]   (get-rdf-type e v))
           (show-as-code   [this] (str "(check " (show-as-code e) ")"))
           (show-as-sparql [this] (str "ASK { " (show-as-sparql e) " }")))

(defrecord Select [v e]
           Expr
           (replace-all    [this i x] (Select. (replace-all v i x) (replace-all e i x)))
           (get-rdf-type   [this v]   (get-rdf-type e v))
           (show-as-code   [this] (str "(bind " (show-as-code v) " " (show-as-code e) ")"))
           (show-as-sparql [this] (str "SELECT DISTINCT " (show-as-sparql v) " WHERE { " (show-as-sparql e) " }")))


;; Logical operators

(defrecord Not [e]
           Expr
           (replace-all    [this i x] (Not. (replace-all e i x)))
           (get-rdf-type   [this v]   (get-rdf-type e v))
           (show-as-code   [this] (str "(lnot " (show-as-code e) ")"))
           (show-as-sparql [this] (str "FILTER NOT EXISTS { " (show-as-sparql e) " }")))

(defrecord And [e1 e2]
           Expr
           (replace-all    [this i x] (And. (replace-all e1 i x) (replace-all e2 i x)))
           (get-rdf-type   [this v]   (concat (get-rdf-type e1 v) (get-rdf-type e2 v)))
           (show-as-code   [this] (str "(land " (show-as-code e1) " " (show-as-code e2) ")"))
           (show-as-sparql [this] (str (show-as-sparql e1) " " (show-as-sparql e2))))

(defrecord Or [e1 e2]
           Expr
           (replace-all    [this i x] (Or. (replace-all e1 i x) (replace-all e2 i x)))
           (get-rdf-type   [this v]   (concat (get-rdf-type e1 v) (get-rdf-type e2 v)))
           (show-as-code   [this] (str "(lor " (show-as-code e1) " " (show-as-code e2) ")"))
           (show-as-sparql [this] (str "{ " (show-as-sparql e1) " } UNION { " (show-as-sparql e2) " }")))


;; Generalized quantifiers
;; quant ::= :some | :all | :no | ...

(defn join-string [f s xs] (if (empty? xs) "" (apply str (concat (map (fn [x] (str (f x) s)) (drop-last xs)) (f (last xs))))))

(defrecord Quant [q v es1 es2]
           Expr
           (replace-all    [this i x] (Quant. q (replace-all v i x) (map #(replace-all % i x) es1) (map #(replace-all % i x) es2)))
           (get-rdf-type   [this v]   (concat (apply concat (map #(get-rdf-type % v) es1)) (apply concat (map #(get-rdf-type % v) es2))))
           (show-as-code   [this] (str "(quant " q " " (show-as-code v) " [" (join-string show-as-code " " es1) "] [" (join-string show-as-code " " es2) "])"))
           (show-as-sparql [this] (case q 
                                :some (str (join-string show-as-sparql " " es1) " " (join-string show-as-sparql " " es2))
                                :no   (str "FILTER NOT EXISTS { " (join-string show-as-sparql " " es1) " " (join-string show-as-sparql " " es2) " }")
                                :all  (str (join-string show-as-sparql " " es2) " SELECT " (show-as-sparql v) " { " (join-string show-as-sparql " " es1)  " } GROUP BY " (show-as-code v)))))


;; Effects

(defrecord Condition [k v]
           Expr
           (replace-all    [this i x] (Condition. k (replace-all v i x)))
           (get-rdf-type   [this v]   [])
           (show-as-code   [this] (str "{" k " " v "}"))
           (show-as-sparql [this] (str "")))


(defrecord Sel [cs]
           Expr
           (replace-all    [this i x] (Sel. (map #(replace-all % i x) cs)))
           (get-rdf-type   [this v]   [])
           (show-as-code   [this] (str "(sel " (join-string show-as-code " " cs) ")"))
           (show-as-sparql [this] (show-as-sparql (make-var "x" (stm/get-fresh!)))))

(defrecord Path [ent1 ent2 cs]
           Expr
           (replace-all    [this i x] (Path. (replace-all ent1 i x) (replace-all ent2 i x) (map #(replace-all % i x) cs)))
           (get-rdf-type   [this v]   [])
           (show-as-code   [this] (str "(bridge " (show-as-code ent1) " " (show-as-code ent2) " " (join-string show-as-code " " cs) ")"))
           (show-as-sparql [this] (let [p (make-var "p" (stm/get-fresh!))] 
                                  (str "{ " (show-as-sparql ent1) " " (show-as-sparql p) " " (show-as-sparql ent2) " . } UNION { " (show-as-sparql ent2) " " (show-as-sparql p) " " (show-as-sparql ent1) " . }"))))


;; Extending Expr to nil
(extend-type nil Expr (replace-all [_ _ _] nil) (get-rdf-type [_ _] []) (show-as-code [_] "nil") (show-as-sparql [_] "nil"))
