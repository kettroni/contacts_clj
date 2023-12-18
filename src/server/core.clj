(ns server.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [hiccup2.core :as h]
            [ring.util.response :refer [redirect]]
            [reitit.ring :as ring])
  (:gen-class))

(defn hello-body
  []
  (str (h/html [:h1 "Hello World!"])))

(defn hello-handler [request]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (hello-body)})

(defn root-handler [request]
  (redirect "/contacts"))

(def router
  (ring/router
     [["/"         {:get root-handler}]
      ["/contacts" {:get hello-handler}]]))

(def app (ring/ring-handler router))

(defn -main [& args]
  (run-jetty app {:port 3000}))
