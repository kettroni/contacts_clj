(ns db.contact)

(def get-contacts
  [{:fname "X1" :lname "Y1nen" :pnumber "0441234567" :email "x1@y1nen.com" :id 1}
   {:fname "X2" :lname "Y2nen" :pnumber "0441234568" :email "x2@y2nen.com" :id 2}
   {:fname "X3" :lname "Y3nen" :pnumber "0441234569" :email "x3@y3nen.com" :id 3}])

(defn get-contact
  [id]
  (first (filter (fn [c] (== (:id c) id)) get-contacts)))
