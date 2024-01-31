(ns validation
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :refer [blank?]]
   [clojure.walk :refer [keywordize-keys]]))

(s/check-asserts true)

(defn non-empty-string? [s] (and (string? s) ((complement blank?) s)))

(s/def ::first-name non-empty-string?)
(s/def ::last-name non-empty-string?)
(s/def ::email non-empty-string?)
(s/def ::phone-number non-empty-string?)

(s/def ::contact
  (s/keys :req-un [::first-name ::last-name ::email ::phone-number]))

(defn problem->error [p]
  (let [err-key (first (:path p))
        err-val (:val p)]
   {:error-key err-key
    :error-value err-val
    :error-message (str "Invalid value '" err-val "' for '" err-key "'")}))

(defn- explain->errors [explanation]
  (let [problems (:clojure.spec.alpha/problems explanation)]
    (map problem->error problems)))

(defn validate-contact
  [queryparams]
  (let [transformed-params (keywordize-keys queryparams)
        explanation (s/explain-data ::contact transformed-params)]
    (if explanation
      (let [errors (explain->errors explanation)
            failed-keys (into [] (map #(:err-key %) errors))
            input (:clojure.spec.alpha/value explanation)
            valid-inputs (apply dissoc input failed-keys)]
        {:errors errors :valid-inputs valid-inputs})
      {:errors [] :valid-inputs (s/conform ::contact transformed-params)})))

(defn- t []
  (explain->errors
     (s/explain-data ::contact {:first-name " "
                                :last-name 3
                                :email "rk@gmail.com"
                                :phone-number "0123456789"})))

(comment
  (validate-contact {:first-name "b", :last-name "asd", :email "asd@asd", :phone-number "asd"})
  (validate-contact {"first-name" "asd", "last-name" "asd", "email" "asd@asd", "phone-number" "asd"})
  (validate-contact {:first-name " " :last-name "kettunen" :email "rk@gmail.com" :phone-number "0123456789"})

  (s/explain-data ::contact {:first-name "b", :last-name "asd", :email "asd@asd", :phone-number "asd"})
  (s/assert ::contact {:first-name " " :last-name "kettunen" :email "rk@gmail.com" :phone-number "0123456789"}))

(t)
