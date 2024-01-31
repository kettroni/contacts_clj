(ns templates
  (:require
   [hiccup.form :refer [form-to]]
   [hiccup2.core :refer [html]]
   [utils :refer [kebab-to-title
                  to-snake]]))

(defn- contact-tr
  [c]
  [:tr [:td (:first-name c)]
       [:td (:last-name c)]
       [:td (:phone-number c)]
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
  ([field-name & [initial-value]]
   (let [titled-field-name (kebab-to-title (name field-name))
         place-holder (str "insert '" titled-field-name "' here.")]
     [:p [:label {:for field-name} titled-field-name]
         [:input {:name field-name :id field-name :type field-name :placeholder (or initial-value place-holder) :value (str initial-value)}]
         [:span]])))

(defn- contact-form-fields
  [contact]
  ;; (map (fn [p] (form-field (first p) (second p))) contact))
  (map #(form-field % ((keyword %) contact)) [:first-name :last-name :email :phone-number]))

(defn- contact-form
  [post-path & [contact]]
  (form-to [:post post-path]
           [:fieldset [:legend "Contact Values"]
                      (contact-form-fields contact)
                      [:button "Save"]]))

(contact-form-fields nil)
(contact-form-fields {:first-name "ron" :last-name "fox" :email "ron.fox@gmail.com" :phone-number "9876543210"})

(defn- back-anchor []
  [:a {:href "/contacts"} "Back"])

(defn new-contact-view
  ;; ([]
  ;;  (str (html (contact-form "/contacts/new")
  ;;             (back-anchor))))
  ([& invalid-contact]
   (str (html (contact-form "/contacts/new" (:valid-inputs invalid-contact))
              (back-anchor)))))

(defn- delete-contact-form
  [id]
  [:form {:action (str "/contacts/" id "/delete") :method "post"}
   [:button "Delete Contact"]])

(defn contact-detail-view
  [c]
  (str (html [:h1 (str (:first-name c) " " (:last-name c))]
             [:div [:div (:phone-number c)]
                   [:div (:email c)]]
             [:p [:a {:href (str "/contacts/" (:id c) "/edit")} "Edit"]
                 (back-anchor)])))

(defn contact-edit-view
  [contact]
  (let [id (:id contact)]
    (str (html (contact-form (str "/contacts/" id "/edit") contact)
               (delete-contact-form id)
               (back-anchor)))))

(comment
  (contact-detail-view {:first-name "X1" :last-name "Y1nen" :phone-number "0441234567" :email "x1@y1nen.com" :id 1}))
