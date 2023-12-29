(ns db.contact
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]))

(def dbname "contact.db")

(def db-spec
  {:dbtype "sqlite"
   :dbname (str "db/" dbname)})

(def create-table-string (-> (h/create-table :contact :if-not-exists)
                             (h/with-columns [[:id :integer :primary-key :autoincrement]
                                              [:firstname [:varchar 32] :not-null]
                                              [:lastname [:varchar 32] :not-null]
                                              [:email [:varchar 32] :not-null :unique]
                                              [:phonenumber [:varchar 32] :not-null :unique]])
                             sql/format
                             first
                             str))

(defn- create-contact
  [fname lname email phonenumber]
  (j/execute! db-spec (-> (h/insert-into :contact)
                          (h/columns :firstname :lastname :email :phonenumber)
                          (h/values [[fname lname email phonenumber]])
                          (sql/format {:pretty true}))))

(defn create-table [] (j/execute! db-spec create-table-string))
(defn drop-table [] (j/execute! db-spec "DROP TABLE IF EXISTS contact"))
(defn create-example-contact [] (create-contact "roni" "kettunen" "rk@gmail.com" "0123456789"))

(defn get-contacts
  []
  (into [] (j/query db-spec (-> (h/select :*)
                                (h/from :contact)
                                sql/format
                                first
                                str))))

(defn get-contact
  [id]
  (first (into [] (j/query db-spec (-> (h/select :*)
                                (h/from :contact)
                                sql/format
                                first
                                (str " WHERE id = " id))))))

(comment
  ;; Table commands
  (create-table)
  (drop-table)

  ;; Test creation and SELECT
  (create-example-contact)
  (get-contacts)
  (get-contact 1)
)
