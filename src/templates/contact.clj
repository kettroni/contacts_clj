(ns templates.contact
  (:require
   [clojure.string :refer [capitalize]]
   [hiccup.form :refer [form-to]]
   [hiccup2.core :refer [html]]))

(defn- contact-tr
  [c]
  [:tr [:td (:fname c)]
       [:td (:lname c)]
       [:td (:pnumber c)]
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

(defn- new-contact-button [] [:p [:a {:href "contacts/new"} "Add Contact"]])

(defn contacts-view
  [contacts]
  (str (html (contacts-table contacts)
             (new-contact-button))))

(defn- form-field [fieldname]
  (let [capFn (capitalize fieldname)]
   [:p [:label {:for fieldname} capFn]
       [:input {:name fieldname :id fieldname :type fieldname :placeholder capFn}]
       [:span]]))

(defn- new-contact-form
  []
  (form-to [:post "/contacts/new"]
           [:fieldset [:legend "Contact Values"]
                     (form-field "email")
                     (form-field "first_name")
                     (form-field "last_name")
                     (form-field "phone")]
           [:button "Save"]))

(defn new-contact-view
  []
  (str (html (new-contact-form))))
