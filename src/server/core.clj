(ns server.core
  (:require
   [data.contact           :refer [valid-contact?]]
   [db.contact             :refer [get-contact get-contacts]]
   [reitit.ring            :refer [ring-handler router]]
   [ring.adapter.jetty     :refer [run-jetty]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response     :refer [redirect]]
   [templates.contact      :refer [contact-detail-view contacts-view
                                   new-contact-view]]
   [utils :refer [parse-number]]))

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

(defn- contact-detail [request]
  (let [path-params (:path-params request)
        contact (get-contact (parse-number (:id path-params)))]
    {:status 200
     :headers {"Content-Type" "text/html; charset=UTF-8"}
     :body    (contact-detail-view contact)}))

(def my-router
  (router [["/" {:get root}]
           ["/contacts" {:get contacts}]
           ["/contacts/new" {:get new-contact
                             :post (wrap-params create-new-contact)
                             :conflicting true}]
           ["/contacts/:id" {:get contact-detail
                             :conflicting true}]
           ["/contacts/:id/edit" {:get contact-detail}]]))

(def app (ring-handler my-router))

(defn -main
  [& _] (run-jetty app {:port 6969}))
