(ns cassex.datamodel
  (:refer-clojure :exclude [update])
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]
            [cassex.utils :refer :all]))

(def cluster (alia/cluster {:contact-points ["localhost"]}))
(def session (alia/connect cluster))

(defn create-db [db-name]
  (create-keyspace db-name (with {:replication {:class "SimpleStrategy" :replication_factor 1}})))

; generates an offices table with (id, city) as composite PK
; (defn get-offices-table []
;   (create-table :offices (column-definitions [[:id :int]
;                                               [:sid :varchar]
;                                               [:city :varchar]
;                                               [:country :varchar]
;                                               [:primary-key [:id]]])))

(defn get-offices-table []
  (get-table :offices :id :int :sid :varchar :city :varchar :country :varchar :primary-key [:id]))

(defn insert-data[]
  (execute-query-dsl session (insert :offices (values [[:id 1] [:sid "1"] [:city "Georgia"] [:country "USA"]])))
  (execute-query-dsl session (insert :offices (values [[:id 2] [:sid "2"] [:city "Bangalore"] [:country "India"]])))
  (execute-query-dsl session (insert :offices (values [[:id 3] [:sid "3"] [:city "Pune"] [:country "India"]]))))

(defn create-schema [in-ks]
  (let [offices (get-offices-table)]
    (alia/execute session (use-keyspace (keyword in-ks)))
    (execute-query-dsl session (drop-table :offices))
    (execute-query-dsl session offices)
    (execute-query-dsl session (create-index :offices :city (index-name "cityIndex")))
    (execute-query-dsl session (create-index :offices :country (index-name "countryIndex")))
    (insert-data)))

; (use '[clojure.tools.namespace.repl :only (refresh)])
; (use 'cassex.datamodel :reload)
; (require 'cassex.datamodel :reload-all)
