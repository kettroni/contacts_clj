(ns server.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [hiccup2.core :refer [html]]
            [ring.util.response :refer [redirect]]
            [reitit.ring :refer [router ring-handler]]
            [db.contact :refer [get-contacts]]
            [templates.contact :refer [contacts-table]]))

(defn root [request]
  (redirect "/contacts"))

(defn contacts [request]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (contacts-table get-contacts)})

(defn new-contact [request]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (str (html [:h1 "Hello World!"]))})

(def my-router
  (router
     [["/"             {:get root}]
      ["/contacts"     {:get contacts}]
      ["/contacts/new" {:get new-contact}]]))

(def app (ring-handler my-router))

(defn -main [& args]
  (run-jetty app {:port 3000}))
