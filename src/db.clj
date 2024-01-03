(ns db
  (:require
   [clojure.java.jdbc :as j]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [utils :refer [keys-to-kebab keys-to-snake]]))

(def ^:private dbname "contact.db")

(def ^:private db-spec
  {:dbtype "sqlite"
   :dbname (str "db/" dbname)})

(def ^:private create-table-string (-> (h/create-table :contact :if-not-exists)
                             (h/with-columns [[:id :integer :primary-key :autoincrement]
                                              [:first-name [:varchar 32] :not-null]
                                              [:last-name [:varchar 32] :not-null]
                                              [:email [:varchar 32] :not-null :unique]
                                              [:phone-number [:varchar 32] :not-null :unique]])
                             sql/format
                             first))

(defn create-contact
  [c]
  (let [c' (keys-to-snake c)
        q (-> (h/insert-into :contact)
              (h/values [c'])
              (sql/format {:pretty true}))]
    (j/execute! db-spec q)))

(defn- create-table [] (j/execute! db-spec create-table-string))
(defn- drop-table [] (j/execute! db-spec "DROP TABLE IF EXISTS contact"))
(defn- create-example-contact [] (create-contact {:first-name "roni" :last-name "kettunen" :email "rk@gmail.com" :phone-number "0123456789"}))
(defn- create-example-contact2 [] (create-contact {:first-name "ron" :last-name "fox" :email "ron.fox@gmail.com" :phone-number "9876543210"}))

(defn get-contacts
  []
  (let [q (str "select * from contact")]
    (->> (j/query db-spec q)
         (into [])
         keys-to-kebab)))

(defn get-contact
  [id]
  (let [q (str "select * from contact where id = " id)]
    (->> (j/query db-spec q)
         (into [])
         first
         keys-to-kebab)))

(defn delete-contact
  [id]
  (j/delete! db-spec :contact ["id = ?" id]))

(comment
  ;; Table commands
  (create-table)
  (drop-table)

  ;; Test creation and SELECT
  (create-example-contact)
  (create-example-contact2)
  (get-contacts)
  (get-contact 1)

  (delete-contact 1)
)
