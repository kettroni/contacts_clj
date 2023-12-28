(ns server.core
  (:require
   [data.contact           :refer [valid-contact?]]
   [db.contact             :refer [get-contact get-contacts]]
   [reitit.ring            :refer [ring-handler router]]
   [ring.adapter.jetty     :refer [run-jetty]]
   [ring.middleware.flash  :refer [flash-response wrap-flash]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response     :refer [redirect]]
   [templates.contact      :refer [contact-detail-view contact-edit-view
                                   contacts-view new-contact-view]]
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
      ;; TODO: implement saving and validation.
      ;; (create-contact c)
      (let [resp (assoc (redirect "/contacts" 303) :flash "Created new contact!")]
        (flash-response resp request))
      {:status 400
       :headers {"Content-Type" "text/plaintext; charset=UTF-8"}
       :body (str "Invalid contact information: " fps)})))

(defn- id-from-request-path [request]
  (let [path-params (:path-params request)]
    (parse-number (:id path-params))))

(defn- contact-detail [request]
  (let [c-id (id-from-request-path request)
        contact (get-contact c-id)]
    {:status 200
     :headers {"Content-Type" "text/html; charset=UTF-8"}
     :body    (contact-detail-view contact)}))

(defn- contact-edit [request]
  (let [fps (:form-params request)]
    (if fps
      ;; form-params available, handle post request.
      (if (valid-contact? fps)
        (let [resp (assoc (redirect "/contacts" 303) :flash "Edited contact succesfully!")]
            (flash-response resp request))
        ;; TODO: implement saving and validation.
        {:status 400
         :headers {"Content-Type" "text/plaintext; charset=UTF-8"}
         :body (str "Invalid contact information: " fps)})

      ;; No form-params, handle get request.
      (let [c-id (id-from-request-path request)
            contact (get-contact c-id)]
        {:status 200
         :headers {"Content-Type" "text/html; charset=UTF-8"}
         :body    (contact-edit-view (:id contact))}))))

(defn- contact-delete [request]
  (let [c-id (id-from-request-path request)
        resp (assoc (redirect "/contacts" 303) :flash "Deleted contact!")]
    (flash-response resp request)))

(def my-router
  (router [["/"                    {:get root}]
           ["/contacts"            {:get contacts}]
           ["/contacts/new"        {:get new-contact
                                    :post (-> create-new-contact
                                              wrap-params
                                              wrap-flash)
                                    :conflicting true}]
           ["/contacts/:id"        {:get contact-detail
                                    :conflicting true}]
           ["/contacts/:id/edit"   {:get contact-edit
                                    :post (-> contact-edit
                                              wrap-params
                                              wrap-flash)}]
           ["/contacts/:id/delete" {:post (-> contact-delete
                                              wrap-flash)}]]))

(def app (ring-handler my-router))

(defn -main
  [& _] (run-jetty app {:port 6969}))
