(ns car.config
  (:require [nomad           :refer (defconfig)]
            [clojure.java.io :as io]))

(def CONFIG-PATH (str (System/getenv "HOME") "/.config/car/config.edn"))

(defconfig config (io/file CONFIG-PATH))

(defn set-config!
  "Sets a value in config file. `path` is a 'get-in' style argument 
   vector used to specify a key in config."
  [path value]
  (spit CONFIG-PATH (assoc-in (config) path value)))

(defn update-config!
  "Updates a value in config file. `path` is a 'get-in' style argument 
   vector to specify a key in config."
  [path change-fn & args]
  (spit CONFIG-PATH (apply update-in (config) path change-fn args))) 

(defn from-config
  "Get a value from config. `path` is a 'get-in' style argument vector to 
   specify a key in config."
  [path]
  (get-in (config) path))
