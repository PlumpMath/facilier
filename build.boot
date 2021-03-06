(set-env!
 :source-paths    #{"src/cljs" "src/js" "src/clj"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "1.7.170-3"  :scope "test"]
                 [adzerk/boot-cljs-repl     "0.2.0"      :scope "test"]
                 [adzerk/boot-reload        "0.4.1"      :scope "test"]
                 [pandeiro/boot-http        "0.6.3"      :scope "test"]
                 [crisptrutski/boot-cljs-test "0.2.1-SNAPSHOT" :scope "test"]
                 [org.clojure/clojurescript "1.7.170"]
                 ;; Facilier Panel
                 [org.omcljs/om "1.0.0-alpha30" :exclusions [cljsjs/react]]
                 [cljs-react-test "0.1.3-SNAPSHOT" :scope "test"]
                 [prismatic/dommy "1.0.0" :score "test"]
                 [cljsjs/react-with-addons "0.14.3-0" :scope "test"]
                 [sablono "0.6.0"]
                 ;; Facilier Server
                 [org.clojure/core.async "0.2.374"]
                 [reloaded.repl "0.2.0"]
                 [com.stuartsierra/component "0.2.3"]
                 [ring "1.3.2"]
                 [ring-cors "0.1.7"]
                 [fogus/ring-edn "0.3.0"]
                 [compojure "1.4.0"]
                 [clj-http "1.1.0"]
                 ;; Facilier Testing
                 [org.clojure/test.check "0.9.0"]
                 ;; Facilier Client
                 [cljs-ajax "0.3.14"]
                 [maxwell "0.1.0-SNAPSHOT"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[reloaded.repl         :refer [go reset start stop system]]
 '[facilier.boot         :refer [start-app]])

(deftask build []
  (comp (speak)

        (cljs)
        ))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)
        (start-app :port 3005)))

(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs"))
  identity)

;; remove warning
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
        (test-cljs :js-env :phantom
                   :exit? true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs :js-env :phantom
                   :namespaces '[test.facilier.panel])))

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none :source-map true}
                 reload {:on-jsload 'facilier.panel/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
