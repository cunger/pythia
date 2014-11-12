(ns start
  (:gen-class :main true)
  (:require [setup.main :as setup]
            [applications.qa.main :as qa]
            [eval.qald :as qald]))


(defn -main [mode & files]

  (case mode
       "setup" (apply setup/run files)
       "qa"    (qa/run)
       "eval"  (qald/run) ; TODO dispatch depending on which evaluation should be run
       ))
