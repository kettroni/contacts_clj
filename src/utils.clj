(ns utils
  (:require
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]
   [clojure.string :as str]))

(defn capitalize-words
  "Capitalize every word in a string"
  [s]
  (->> (str/split (str s) #"\b")
       (map str/capitalize)
       str/join))

(defn- keys-to-case
  [keys-to-case-fun m]
  (cske/transform-keys keys-to-case-fun m))

(defn keys-to-kebab
  [m]
  (keys-to-case csk/->kebab-case-keyword m))

(defn keys-to-snake
  [m]
  (keys-to-case csk/->snake_case_keyword m))

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
