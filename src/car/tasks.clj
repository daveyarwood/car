(ns car.tasks
  (:require [car.config          :refer :all]
            [defun               :refer (defun)]
            [clojure.string      :as str]
            [car.current-mileage :refer :all]))

(defn- normalize-task-name
  [task-name]
  (str/lower-case (str/replace task-name #"[^\w]" "")))

(defn find-task-by-query
  "Returns the first task found in config-map that has the same normalized name
   as task-query. e.g. the query 'oil-change' will find the task 'Oil change'."
  [task-query]
  (first (keep-indexed (fn [i {task-name :name :as task-map}]
                         (when (= (normalize-task-name task-name)
                                  (normalize-task-name task-query))
                           (assoc task-map :id i)))
                       (from-config [:tasks]))))

(defn regular-task?
  [task-query]
  (contains? (find-task-by-query task-query) :interval))

(defn recorded-task?
  [task-query]
  (contains? (find-task-by-query task-query) :last-done))

(defn upcoming-task?
  [task-query]
  (contains? (find-task-by-query task-query) :due-at))

(defun overdue?
  ([(task-query :guard regular-task?)]
    {:pre [(recorded-task? task-query)]}
    (let [{:keys [interval last-done]} (find-task-by-query task-query)
          current-mileage (get-current-mileage!)
          time-since-last (- current-mileage last-done)]
      (>= time-since-last interval)))
  ([(task-query :guard upcoming-task?)]
    (let [{:keys [due-at]} (find-task-by-query task-query)
          current-mileage (get-current-mileage!)]
      (>= current-mileage due-at))))

(defn maintenance-schedule
  "Returns an infinite lazy sequence of all the mile markers when a regular
   task is due, starting from the last time it was done."
  [task-query]
  {:pre [(regular-task? task-query)
         (recorded-task? task-query)]}
  (let [{:keys [interval last-done]} (find-task-by-query task-query)]
    (iterate (partial + interval) last-done)))

(defn next-due
  "Returns the next mile marker when a regular task is due."
  [task-query current-mileage]
  {:pre [(regular-task? task-query)
         (recorded-task? task-query)]}
  (if (overdue? task-query)
    (let [{:keys [last-done interval]} (find-task-by-query task-query)]
      (+ last-done interval))
    (first (drop-while #(<= % current-mileage) (maintenance-schedule task-query)))))

(defn update-last-done!
  [task-query mileage]
  {:pre [(or
           (recorded-task? task-query)
           (upcoming-task? task-query))]}
  (let [{:keys [id]} (find-task-by-query task-query)]
    (set-config! [:tasks id :last-done] mileage)))

(defn convert-to-recorded-task!
  "For an upcoming task that gets done, converts it to a recorded task by removing the
   :due-at key from its config. (:last-done needs to be set first, making it a recorded
   task.)"
  [task-query]
  {:pre [(upcoming-task? task-query)
         (recorded-task? task-query)]}
  (let [{:keys [id]} (find-task-by-query task-query)]
    (update-config! [:tasks id] dissoc :due-at)))

(defn add-task!
  "Adds a new task to the config, along with any values supplied in the format
   foo=bar or foo:bar.

   e.g.
   (add-task! 'Replace cabin air filter' last-done=90000 interval=15000)"
  [task-name & configs]
  (let [task (apply merge {:name task-name}
                          (map (fn [cfg]
                                 (let [[k v] (str/split cfg #"[=:]")]
                                   {(keyword k) (try
                                                  (Integer/parseInt v)
                                                  (catch NumberFormatException e v))}))
                               configs))]
    (update-config! [:tasks] conj task)))

(defn delete-task!
  [task-query]
  (let [{:keys [id]} (find-task-by-query task-query)]
    (update-config! [:tasks] #(vec (concat (take id %) (drop (inc id) %))))))

(defn rename-task!
  [old-name new-name]
  (let [{:keys [id]} (find-task-by-query old-name)]
    (set-config! [:tasks id :name] new-name)))

(defn set-task-interval!
  "Sets interval for task. If interval is 0, removes the :interval key, making
   it no longer a regular task."
  [task-query interval]
  (let [{:keys [id]} (find-task-by-query task-query)]
    (if (zero? interval)
      (set-config! [:tasks id] (dissoc (from-config [:tasks id]) :interval))
      (set-config! [:tasks id :interval] interval))))

(defn set-task-due-at!
  "Sets mile marker when task is due. If due-at is 0, removes the :due-at key."
  [task-query due-at]
  (let [{:keys [id]} (find-task-by-query task-query)]
    (if (zero? due-at)
      (set-config! [:tasks id] (dissoc (from-config [:tasks id]) :due-at))
      (set-config! [:tasks id :due-at] due-at))))
