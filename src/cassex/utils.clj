(ns cassex.utils
  (:refer-clojure :exclude [update])
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]
            [taoensso.timbre :as timbre :refer [log debug info]]))

(defn execute-query [session query]
  (alia/execute session query))

(defn execute-query-dsl [session query]
  (alia/execute session (->raw query)))

(defn get-table [name & rest]
  (info (str "name: " name ", rest: " rest))
  (create-table name (column-definitions (partition 2 rest))))

(defn get-index [table column indexName]
  (create-index table column (index-name indexName)))

(defn generate-index [session table column indexName]
  (execute-query-dsl session (get-index table column indexName))) 

(defn generate-table [session name & rest]
  (let [t (create-table name (column-definitions (partition 2 rest)))]
    (info (->raw t))
    ; (execute-query-dsl session (drop-table name))
    (execute-query-dsl session t)))

(defn create-db [db-name]
  (create-keyspace db-name (with {:replication {:class "SimpleStrategy" :replication_factor 1}})))  