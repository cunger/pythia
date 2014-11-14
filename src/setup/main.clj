(ns setup.main
  (:require [setup.lexx.run :as lexx]
  	        [setup.lexx.render :as render]
  	        [setup.lexx.signature :as signature]
            [setup.ggen.run :as ggen]
            [settings]
            [clojure.java.io :as io]))


;; Main

(defn run [& files]

  (let [target (if-not (nil? settings/target) settings/target "Application")]

    ; empty the domain definitions file
    (spit (str settings/semantic-definitions "Domain.clj") "(ns core.definitions.Domain)")

    ; generate domain grammar
    (println "\nGenerating domain grammar.\n")
    (lexx/run files)
    ; compose application grammar
    (println "\nComposing application grammar.\n")
    (ggen/run target)

    ; store what needs to be accessible in online mode
    ; 1. copy compiled .pgf to gf-server-grammars folder
    (io/copy (io/file (str ggen/temp target ".pgf")) (io/file (str settings/gf-server-grammars target ".pgf")))
    ; 2. store definitions in semantic-definitions folder
    (spit (str settings/semantic-definitions "Domain.clj") (render/render "definitions" @signature/abstract))
    ; 3. store tokens file in resources
    (io/copy (io/file (str ggen/temp "tokens")) (io/file "resources/grammars/tokens"))

    (println "\nDone.")
    (java.lang.System/exit 0)))
