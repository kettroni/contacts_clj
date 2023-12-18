(ns server.core
  (:require
   [data.contact           :refer [valid-contact?]]
   [db.contact             :refer [get-contacts]]
   [templates.contact      :refer [contacts-view
                                   new-contact-view]]
   [reitit.ring            :refer [ring-handler
                                   router]]
   [ring.adapter.jetty     :refer [run-jetty]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response     :refer [redirect]]))

(defn root [_] (redirect "/contacts"))

(defn contacts [_]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (contacts-view get-contacts)})

(defn new-contact [_]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (new-contact-view)})

(defn create-new-contact [request]
  (let [fps (:form-params request)]
    (if (valid-contact? fps)
      {:status  201
       :headers {"Content-Type" "text/html; charset=UTF-8"}
       :body    (str fps)}
      {:status 400
       :headers {"Content-Type" "text/plaintext; charset=UTF-8"}
       :body (str "Invalid contact information: " fps)})))

(def my-router
  (router
    [["/"             {:get root}]
     ["/contacts"     {:get contacts}]
     ["/contacts/new" {:get new-contact
                       :post (wrap-params create-new-contact)}]]))

(def app (ring-handler my-router))

(defn -main
  [& _] (run-jetty app {:port 3000}))
