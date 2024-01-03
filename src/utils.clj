(ns utils
  (:require [clojure.string :as str]))

(defn capitalize-words
  "Capitalize every word in a string"
  [s]
  (->> (str/split (str s) #"\b")
       (map str/capitalize)
       str/join))

(defn kebab-to-title
  "'kebab-case' -> 'Kebab Case'"
  [s]
  (-> (str/replace s #"-" " ")
      capitalize-words))

(defn parse-number
  "Reads a number from a string. Returns nil if not a number."
  [s]
  (when (re-find #"^-?\d+\.?\d*$" s)
    (read-string s)))
