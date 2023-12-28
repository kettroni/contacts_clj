(ns db.contact
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]))

(def get-contacts
  [{:fname "X1" :lname "Y1nen" :pnumber "0441234567" :email "x1@y1nen.com" :id 1}
   {:fname "X2" :lname "Y2nen" :pnumber "0441234568" :email "x2@y2nen.com" :id 2}
   {:fname "X3" :lname "Y3nen" :pnumber "0441234569" :email "x3@y3nen.com" :id 3}])

(defn get-contact
  [id]
  (first (filter (fn [c] (== (:id c) id)) get-contacts)))

(def dbname "contact.db")

(def db-spec
  {:dbtype "sqlite"
   :dbname (str "db/" dbname)})

(comment
  (def create-table-string (-> (h/create-table :contact :if-not-exists)
                               (h/with-columns [[:id :integer :primary-key :autoincrement]
                                                [:firstname [:varchar 32] :not-null]
                                                [:lastname [:varchar 32] :not-null]
                                                [:email [:varchar 32] :not-null :unique]
                                                [[:unique nil :email :id]]])
                               sql/format
                               first
                               str))

  (defn- create-contact
    [fname lname email]
    (j/execute! db-spec (-> (h/insert-into :contact)
                            (h/columns :firstname :lastname :email)
                            (h/values [[fname lname email]])
                            (sql/format {:pretty true}))))

  (defn create-table [] (j/execute! db-spec create-table-string))
  (defn drop-table [] (j/execute! db-spec "DROP TABLE IF EXISTS contact"))
  (defn create-example-contact [] (create-contact "asd" "asd" "asd"))

  (defn get-contacts [] (j/query db-spec (-> (h/select :*)
                                             (h/from :contact)
                                             sql/format
                                             first
                                             str)))

  ;; Table commands
  (create-table)
  (drop-table)

  ;; Test creation and SELECT
  (create-example-contact)
  (get-contacts)
  )
