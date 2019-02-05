(defproject tictactoe "0.1.0-SNAPSHOT"
  :description "tic tac toe game"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.namespace "0.2.11"]]
  :main ^:skip-aot tictactoe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev"]}}
  :plugins [[lein-kibit "0.1.6"]])
