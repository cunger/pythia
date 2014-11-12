(defproject pythia "0.1"
  :description "Pythia is a plug'n'play engine for ontology-driven, deep natural language understanding and generation over structured data."
  :url "http://ontosem.net/pythia/"
  :license {:name "GNU General Public License"
            :url "http://www.gnu.org/licenses/gpl.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.combinatorics "0.0.8"]
                 [org.clojure/data.json "0.2.5"]
                 [http-kit "2.1.16"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [seabass "2.1.1"]]
  :main start
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
