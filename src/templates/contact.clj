(ns templates.contact
  (:require [hiccup2.core :refer [html]]))

(defn contact-tr
  [c]
  [:tr [:td (:fname c)]
       [:td (:lname c)]
       [:td (:pnumber c)]
       [:td (:email c)]
       [:td [:a {:href (str "/contacts/" (:id c) "/edit")} "Edit"]
            [:a {:href (str "/contacts/" (:id c))}  "View"]]])

(defn contacts-table
  [contacts]
  (str (html [:table [:thead [:th "First name"]
                             [:th "Last name"]
                             [:th "Phone number"]
                             [:th "Email address"]
                             [:th "Links"]]
                     [:tbody (map contact-tr contacts)]]
             [:p [:a {:href "contacts/new"} "Add Contact"]])))
