(ns db.contact
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske]))

(def dbname "contact.db")

(def db-spec
  {:dbtype "sqlite"
   :dbname (str "db/" dbname)})

(def create-table-string (-> (h/create-table :contact :if-not-exists)
                             (h/with-columns [[:id :integer :primary-key :autoincrement]
                                              [:first-name [:varchar 32] :not-null]
                                              [:last-name [:varchar 32] :not-null]
                                              [:email [:varchar 32] :not-null :unique]
                                              [:phone-number [:varchar 32] :not-null :unique]])
                             sql/format
                             first
                             str))

(defn create-contact
  [c]
  (let [q (-> (h/insert-into :contact)
              (h/values [c])
              (sql/format {:pretty true}))]
    (println c)
    (println q)
    (j/execute! db-spec q)))

(defn- create-table [] (j/execute! db-spec create-table-string))
(defn- drop-table [] (j/execute! db-spec "DROP TABLE IF EXISTS contact"))
(defn- create-example-contact [] (create-contact {:first-name "roni" :last-name "kettunen" :email "rk@gmail.com" :phone-number "0123456789"}))
(defn- create-example-contact2 [] (create-contact {:first-name "ron" :last-name "fox" :email "ron.fox@gmail.com" :phone-number "9876543210"}))

(defn- keys-to-kebab
  [m]
  (cske/transform-keys csk/->kebab-case-keyword m))

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

(comment
  ;; Table commands
  (create-table)
  (drop-table)

  ;; Test creation and SELECT
  (create-example-contact)
  (create-example-contact2)
  (get-contacts)
  (get-contact 1)
)
