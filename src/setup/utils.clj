(ns setup.utils
  (:require [clojure.java.io :as io]
            [clojure.set :as set]))


;; IO 

(defn files-in [dir-name]
  (rest (file-seq (io/file dir-name))))

(defn file-name-matches? [re file]
  (not (nil? (re-find re (.getName file)))))

(defn strip-src [file-name]
  (clojure.string/replace file-name "src/" ""))

(defn copy-in [dir file]
  (io/file (str dir (.getName file))))

(defn copy-to [file folder]
  (io/copy file (copy-in folder file)))

(defn clean [folder]
  (doseq [f (files-in folder)] 
    (io/delete-file f)))


;; LIST 

(defn init [ls] (take (- (count ls) 1) ls))

(defn minus    [ls1 ls2] (vec (set/difference   (set ls1) (set ls2))))
(defn overlap  [ls1 ls2] (vec (set/intersection (set ls1) (set ls2))))
(defn overlap? [ls1 ls2] (not (empty? (overlap ls1 ls2))))

(defn elem [e ls] (some #{e} ls))

;; ARRAY 

(defn fmap-vals [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn fmap-keys [f m]
  (into {} (for [[k v] m] [(f k) v])))

(defn remove-nil-values [m] 
  (apply dissoc m (filter (fn [k] (nil? (k m))) (keys m))))

(defn conjoin-values [x y] 
  (let [conjoined (distinct 
                  (let [x-a-coll (coll? x)
                        y-a-coll (coll? y)]
                  (cond (and x-a-coll (not y-a-coll)) (cons y x)
                        (and y-a-coll (not x-a-coll)) (cons x y)
                        (and x-a-coll y-a-coll) (concat x y)
                        (and (not x-a-coll) (not y-a-coll)) [x y])))]
        (if (= (count conjoined) 1) (first conjoined) conjoined)))

(defn apply-to-conjoined [f x]
  (if (sequential? x) (map f x) (f x))) 

(defn find-equivalence-classes [xs bool-func]
  ; returns a list of list of x's (where each list is an equivalence class, i.e. (func x1 x2) for any two elements is true)
  (reduce (fn [equivalence-classes x] 
            (loop [done []
                   todo equivalence-classes]
              (if (empty? todo)
                  (cons [x] done) ; case where no equivalence class for x was found
                  (let  [c (first todo)]
                        (if (bool-func x (first c))
                            (concat (cons (cons x c) done) (rest todo)) ; case where c is an equivalence for x
                            (recur (cons c done) (rest todo)))))))      ; case where c is not an equivalence class for x
          []
          xs))

(defn differ-only-in [array1 array2 ks]
  (every? (fn [k] (or (= (get array1 k) (get array2 k)) 
                      (elem k ks)))
          (concat (keys array1) (keys array2))))

(defn collapse [arrays]
  (apply (partial merge-with conjoin-values) arrays))

(defn collapse-bindings [arrays keys-to-collapse]
  ; collapses all arrays that differ at most in values for keys-to-collapse
  (map collapse (find-equivalence-classes arrays (fn [a1 a2] (differ-only-in a1 a2 keys-to-collapse)))))


;; STRING 

(defn words [string] (clojure.string/split string #"\s"))

(defn contains-string? [string substring]
  (not (nil? (re-matches (re-pattern (str ".*" substring ".*")) string))))
