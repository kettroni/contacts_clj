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

(defn keys-to-kebab
  "Returns a map with kebab-case keys."
  [map]
  (cske/transform-keys csk/->kebab-case-keyword map))

(defn keys-to-snake
  "Returns a map with snake_case keys."
  [map]
  (cske/transform-keys csk/->snake_case_keyword map))

(defn kebab-to-title
  "'kebab-case' -> 'Kebab Case'"
  [s]
  (-> (str/replace s #"-" " ")
      capitalize-words))

(defn to-snake
  "Returns string as snake_case"
  [s]
  (csk/->snake_case s))

(defn parse-number
  "Reads a number from a string. Returns nil if not a number."
  [s]
  (when (re-find #"^-?\d+\.?\d*$" s)
    (read-string s)))
