(ns core.nlu.robustness.preprocessing)


;; aux

; #{Char}
(def punctuation #{\?,\!,\.})
; #{Char} -> String -> String
(defn remove-chars [charset string]
  (apply str (filter (complement #(charset %)) string)))

; [[String String]]
(def replacements [[#"(^|\s)i\s" "$1I "] ["n't" " not"] ["ä" "ae"] ["ö" "oe"] ["ü" "ue"] ["ß" "ss"]])
; [[String String]] -> String -> String
(defn replace-stuff [repls string]
  (reduce (fn [s [o n]] (clojure.string/replace s o n)) string repls))

;; String normalization

; String -> String
(defn normalize [string]
  "Removes trailing whitespaces and punctuation."
  ((comp 
    (partial replace-stuff replacements)
    (partial remove-chars punctuation)
    clojure.string/lower-case
    clojure.string/trim)
  string))
