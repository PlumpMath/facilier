(ns facilier.boot
  {:boot/export-tasks true}
  (:require [boot.core :as b]
            [reloaded.repl :refer [go]]
            [facilier.main :refer :all]
            [clojure.tools.namespace.repl :refer [disable-reload!]]))

(disable-reload!)

(b/deftask start-app
  [p port   PORT int  "Port"]
  (let [x (atom nil)]
    (b/with-pre-wrap fileset
      (swap! x (fn [x]
                  (if x
                    x
                    (do (setup-app! {:port port})
                        (go)))))
      fileset)))
