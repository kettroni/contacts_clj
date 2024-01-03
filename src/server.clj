(ns server
  (:require
   [data                   :refer [valid-contact]]
   [db                     :refer [create-contact
                                   delete-contact
                                   get-contact
                                   get-contacts]]
   [utils                  :refer [parse-number]]
   [reitit.ring            :refer [ring-handler
                                   router]]
   [ring.adapter.jetty     :refer [run-jetty]]
   [ring.middleware.flash  :refer [flash-response
                                   wrap-flash]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response     :refer [redirect]]
   [templates              :refer [contact-detail-view
                                   contact-edit-view
                                   contacts-view
                                   new-contact-view]]))

(defn root [_] (redirect "/contacts"))

(defn contacts [_]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (contacts-view (get-contacts))})

(defn new-contact [_]
  {:status  200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body    (new-contact-view)})

(defn create-new-contact [request]
  (let [fps (:form-params request)
        c (valid-contact fps)]
    (if c
      ;; TODO: implement saving.
      (let [resp (assoc (redirect "/contacts" 303) :flash "Created new contact!")]
        (create-contact c)
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
  (let [fps (:form-params request)
        c (valid-contact fps)]
    (if fps
      ;; TODO: implement saving and validation.
      ;; form-params available, handle post request.
      (if c
        (let [resp (assoc (redirect "/contacts" 303) :flash "Edited contact succesfully!")]
            (flash-response resp request))
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
    (delete-contact c-id)
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