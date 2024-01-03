(ns data.contact
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]))

;; (s/def :contact/id int?)
(s/def :contact/first-name (s/and string? str/blank?))
(s/def :contact/last-name string?)
(s/def :contact/email string?)
(s/def :contact/phone-number string?)

(s/def :unq/contact (s/keys :req-un [:contact/first-name :contact/last-name :contact/email :contact/phone-number]))

(defn valid-contact
  [queryparams]
  s/conform :unq/contact queryparams)

(s/conform :unq/contact {:first-name "" :last-name "kettunen" :email "rk@gmail.com" :phone-number "0123456789"})
