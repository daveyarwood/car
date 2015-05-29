(ns car.current-mileage
  (:require [car.util   :refer (parse-number-or-fail)]
            [car.config :refer :all]))

(defn set-current-mileage!
  [mileage]
  (set-config! [:current] mileage))

(defn ask-for-current-mileage!
  []
  (print "Enter current odometer reading: ") (flush)
  (let [new-mileage (parse-number-or-fail (read-line)
                                          :on-fail ask-for-current-mileage!)]
    (set-current-mileage! new-mileage)
    new-mileage))

(defn get-current-mileage!
  []
  (if-let [existing (from-config [:current])]
    existing
    (ask-for-current-mileage!)))
