(ns core.nlu.robustness.chunking
  (:require [core.nlu.context.long_term_memory :as ltm]
            [clojure.math.combinatorics :as combo]))



(declare words)

(defn remove-unknown-tokens [input]
  (let [new-input (clojure.string/join " " (filter (fn [w] (some #{w} (ltm/tokens))) (words input)))]
    (println "Removing unknown tokens...")
    (println "Final input:" new-input)
    new-input))

;; aux 

(defn words [string] (clojure.string/split string #"\s")) 

(defn splits [n] (map (partial map (partial reduce +)) (combo/partitions (repeat n 1))))
(defn n-gram-splits-up-to [n] (reduce concat (for [x (range 1 (+ n 1))] (splits x)))) 

; Replacing n-grams

; String -> (String)
(defn replace-n-grams [n string repl]
  (let [ws (words string)]
    (for [x (range (+ 1 (- (count ws) n)))] 
      (let [substring (clojure.string/join " " (take n (drop x ws)))
            regex (re-pattern (str "(^|\\s)" substring "(\\s|$)"))]
        (if-not (some #{repl} (words substring)) 
                (clojure.string/replace string regex (str "$1" repl "$2")))))))

; Recursing over a partitioning

(defn one-step [n strings repl]
  (let [spaces (comp #(clojure.string/replace % #"\s\s+" " ") #(clojure.string/replace % #"(^\s)|(\s$)" ""))]
    (map spaces (filter (complement nil?) (flatten (map #(replace-n-grams 1 % repl) strings))))))

(defn recurse-over-partitioning [partitioning string]
  (loop [p partitioning strings [string]]
    (if (empty? p) 
        strings
        (recur (rest p) (distinct (concat (one-step (first p) strings "XXX")
                                          (one-step (first p) strings "")))))))


;; Generating all partial strings

; generates 1775 partials for "i like new york cheese cake"

(defn generate-all-partials [string]
  (flatten 
    (for [split (n-gram-splits-up-to (- (count (words string)) 1))] ; - 1 to leave at least one token unreplaced
      (recurse-over-partitioning split string))))


;; Extracting all substrings

; String -> (String)
(defn get-substrings [string]
  "Gets all continuous substrings of string, ordered by length (longest first)."
  (let [ws (words string) len (count ws)]
    (for [substring-length (rseq (vec (range 1 (- len 1))))
          start (range 0 (- len substring-length))]
      (clojure.string/join " " (subvec ws start (+ start substring-length))))))
