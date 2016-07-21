(defproject tariff "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                 [medley "0.8.2"]
                 [org.clojure/java.classpath "0.2.3"]
                 [org.clojure/tools.namespace "0.2.11"]]
  :main ^:skip-aot tariff.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
