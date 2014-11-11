(ns eval.qald
  (:require [settings]
            [core.main :as core]
            [core.data.LambdaRDF :refer [show-as-sparql]]
            [applications.qa.main :as qa]
            [applications.qa.endpoint :as endpoint]
            [clojure.xml :as xml]))


(def question-set-train "resources/eval/qald-4_multilingual_train.xml")
(def question-set-test  "resources/eval/qald-4_multilingual_test.xml")


(defn run []

  (let [xml-doc (xml/parse question-set-train)]

    (loop [questions (:content xml-doc) 
           result    {:mode :train}]
     
     (if (empty? questions) 
      
         ; compute global precision, recall and f-measure, and log result 
         (let [total         200 ; (count questions)
               global-result { :local-result result
                               :recall    (/ (reduce + (for [[k v] (dissoc result :mode)] (:recall    v))) total) 
                               :precision (/ (reduce + (for [[k v] (dissoc result :mode)] (:precision v))) total)
                               :f-measure (/ (reduce + (for [[k v] (dissoc result :mode)] (:f-measure v))) total) }] 
           (println global-result))

         (let [question        (first questions)
               question-id     (:id (:attrs question))
               question-node   (filter #(and (= (:tag %) :string) (= (:lang (:attrs %)) (name settings/language))) (:content question))
               query-node      (filter #(and (= (:tag %) :query)) (:content question))
               question-string (first (:content (first question-node)))
               goldst-query    (first (:content (first query-node)))
               goldst-results  (endpoint/execute-query goldst-query)
               goldst-answers  (if-not (sequential? goldst-results) [goldst-results] (apply concat (map vals goldst-results)))
               interpretation  (core/dispatch :parse-and-interpret settings/grammar question-string)
               pythia-query    (if interpretation (show-as-sparql (:sem interpretation)) nil)
               pythia-results  (if pythia-query (endpoint/execute-query pythia-query) [])
               pythia-answers  (if-not (sequential? pythia-results) [pythia-results] (apply concat (map vals pythia-results)))
               overlap         (clojure.set/intersection (set pythia-answers) (set goldst-answers))
               recall          (if (empty? pythia-answers) 0 (/ (count overlap) (count goldst-answers)))
               precision       (if (empty? pythia-answers) 1 (/ (count overlap) (count pythia-answers)))
               f-measure       (/ (* 2 precision recall) (+ precision recall))
               updated-result  (assoc result question-id {:recall recall :precision precision :f-measure f-measure}) 
              ]
              (recur (rest questions) updated-result))

      ))))

