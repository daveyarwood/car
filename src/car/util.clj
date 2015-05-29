(ns car.util
  (:require [clojure.string :as str]))

(defn parse-number-or-fail
  [n & {:keys [on-fail]}]
  (try
    (Integer/parseInt n)
    (catch NumberFormatException e
      (println n "isn't a number...")
      (if on-fail (on-fail) (System/exit 1)))))

