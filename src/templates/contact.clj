(ns templates.contact
  (:require
   [clojure.string :refer [capitalize]]
   [hiccup.form :refer [form-to]]
   [hiccup2.core :refer [html]]))

(defn- contact-tr
  [c]
  [:tr [:td (:firstname c)]
       [:td (:lastname c)]
       [:td (:phonenumber c)]
       [:td (:email c)]
       [:td [:a {:href (str "/contacts/" (:id c) "/edit")} "Edit"]
            [:a {:href (str "/contacts/" (:id c))}  "View"]]])

(defn- contacts-table
  [contacts]
  [:table [:thead [:th "First name"]
                  [:th "Last name"]
                  [:th "Phone number"]
                  [:th "Email address"]
                  [:th "Links"]]
          [:tbody (map contact-tr contacts)]])

(defn- new-contact-button []
  [:p [:a {:href "contacts/new"} "Add Contact"]])

(defn contacts-view
  [contacts]
  (str (html (contacts-table contacts)
             (new-contact-button))))

(defn- form-field
  [fieldname]
  (let [capFn (capitalize fieldname)]
   [:p [:label {:for fieldname} capFn]
       [:input {:name fieldname :id fieldname :type fieldname :placeholder capFn}]
       [:span]]))

(defn- contact-form [post-path]
  (form-to [:post post-path]
           [:fieldset [:legend "Contact Values"]
                     (form-field "email")
                     (form-field "first_name")
                     (form-field "last_name")
                     (form-field "phone")]
           [:button "Save"]))

(defn- new-contact-form []
  (contact-form "/contacts/new"))

(defn- back-anchor []
  [:a {:href "/contacts"} "Back"])

(defn new-contact-view []
  (str (html (new-contact-form)
             (back-anchor))))

(defn- delete-contact-form
  [id]
  [:form {:action (str "/contacts/" id "/delete") :method "post"}
   [:button "Delete Contact"]])

(defn contact-detail-view
  [c]
  (str (html [:h1 (str (:firstname c) " " (:lastname c))]
             [:div [:div (:phonenumber c)]
                   [:div (:email c)]]
             [:p [:a {:href (str "/contacts/" (:id c) "/edit")} "Edit"]
                 (back-anchor)])))

(defn contact-edit-view
  [id]
  (str (html (contact-form (str "/contacts/" id "/edit"))
             (delete-contact-form id)
             (back-anchor))))

(comment
  (contact-detail-view {:firstname "X1" :lastname "Y1nen" :phonenumber "0441234567" :email "x1@y1nen.com" :id 1}))
