(ns cassex.utils
  (:refer-clojure :exclude [update])
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]))

(defn execute-query [session query]
  (alia/execute session query))

(defn execute-query-dsl [session query]
  (alia/execute session (->raw query)))

(defn get-table [name & rest]
  (create-table name (column-definitions (partition 2 rest))))