(ns validation
  (:require
   [clojure.spec.alpha :as s]
   [clojure.walk :refer [keywordize-keys]]))

;; (s/def ::id int?)
(s/def ::first-name string?)
(s/def ::last-name string?)
(s/def ::email string?)
(s/def ::phone-number string?)

(s/def :unq/contact
  (s/keys :req-un [::first-name ::last-name ::email ::phone-number]))

(defn valid-contact
  [queryparams]
  (let [transformed-params (keywordize-keys queryparams)]
    (when (s/valid? :unq/contact transformed-params)
      (s/conform :unq/contact transformed-params))))

(comment
  (valid-contact {:first-name "asd", :last-name "asd", :email "asd@asd", :phone-number "asd"})
  (valid-contact {"first-name" "asd", "last-name" "asd", "email" "asd@asd", "phone-number" "asd"})
  (s/conform :unq/contact {:first-name "asd" :last-name "kettunen" :email "rk@gmail.com" :phone-number "0123456789"}))
