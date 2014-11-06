(ns core.data.LambdaRDF)


;;;; Expressions ;;;;

(defprotocol Expr 
  (show   [e] "Print expression in LambdaRDF")
  (sparql [e] "Print expression in SPARQL"))


;; aux

(defn join-string [f s xs] (apply str (concat (map (fn [x] (str (f x) s)) (drop-last xs)) (f (last xs)))))

(defn op-string [op] (case op :equals "=" :less "<" :greater ">" :lessequals "<=" :greaterequals ">="))

;; Terms
;; kind ::= :uri | :var | :index | :literal

(defrecord Term [kind ident]
           Expr
           (show [this] 
             (case kind 
                   :uri    (apply str ["<" ident ">"])
                   :var    (apply str ["?" ident])
                   :index  (apply str ["[" ident "]"])
                   :literal ident))
           (sparql [this]
             (case kind 
                   :uri    (apply str ["<" ident ">"])
                   :var    (apply str ["?" ident])
                   :index  (apply str ["?" ident])
                   :literal ident)))

;; Term -> Term

(defrecord Count [v]
           Expr
           (show   [this] (str "(count " (show v) ")"))
           (sparql [this] (str "COUNT(DISTINCT " (sparql v) ")")))

;; Filters
;; op ::= :equals | :less | :greater | : lessequals | :greaterequals | ...

(defrecord Filter [op t1 t2]
           Expr
           (show   [this] (str "(" (op-string op) " " (show t1) " " (show t2) ")"))
           (sparql [this] (str "FILTER (" (sparql t1) (op-string op) (sparql t2) ")")))

;; Triples

(defrecord Triple [subj prop obj]
           Expr
           (show   [this] (str "(triple " (show subj) " " (show prop) " " (show obj) ")"))
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
           (show   [this] (str "(return " (show v) " " (show e) ")"))
           (sparql [this] (str "SELECT DISTINCT " (sparql v) " WHERE { " (sparql e) " }")))

;; Logical operators

(defrecord Not [e]
           Expr
           (show   [this] (str "(not " (show e) ")"))
           (sparql [this] (str "FILTER NOT EXISTS { " (sparql e) " }")))

(defrecord And [e1 e2]
           Expr
           (show   [this] (str "(and " (show e1) " " (show e2) ")"))
           (sparql [this] (str (sparql e1) " " (sparql e2))))

(defrecord Or [e1 e2]
           Expr
           (show   [this] (str "(or " (show e1) " " (show e2) ")"))
           (sparql [this] (str "{ " (sparql e1) " } UNION { " (sparql e2) " }")))

;; Generalized quantifiers
;; quant ::= :some | :all | :no | ...

(defrecord Quant [q v es1 es2]
           Expr
           (show   [this] (str "(quant " (name q) " " (show v) " [" (join-string show " " es1) "] [" (join-string show " " es2) "])"))
           (sparql [this] (case q 
                                :some (str (join-string sparql " " es1) " " (join-string sparql " " es2))
                                :no   (str "FILTER NOT EXISTS { " (join-string sparql " " es1) " " (join-string sparql " " es2) " }")
                                :all  (str (join-string sparql " " es2) " SELECT " (sparql v) " { " (join-string sparql " " es1)  " } GROUP BY " (show v)))))

;; Effects

(defrecord Sel [v e cs]
           Expr
           (show   [this] (str "(sel " (show v) (if-not e " {" (str " {:satisfy " (show e) " ")) (join-string show ", " cs) "})"))
           (sparql [this] (sparql v)))

(defrecord Condition [k v]
           Expr
           (show   [this] (str ":" (name k) " :" (name v)))
           (sparql [this] (str "")))


;; Extending Expr to nil
(extend-type nil Expr (show [_] "nil") (sparql [_] "nil"))


;;;; Fresh variable supply ;;;;

(def fresh (atom 0))

(defn set-fresh! [n] (reset! fresh n))
(defn inc-fresh! [] (swap! fresh inc))

(defn make-var [n] (Term. :var (apply str ["v" n])))

(defn get-fresh-var! [] (do (inc-fresh!) (make-var @fresh)))

;;;; Auxiliary functions ;;;;

(def rdf-type  (Term. :uri "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
(def owl-Thing (Term. :uri "http://www.w3.org/2002/07/owl#Thing"))

(defn sel [pred conditions]
  (let [v (get-fresh-var!)]
       (Sel. v (if pred (pred v) nil) conditions)))
    
