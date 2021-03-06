(ns cassex.datamodel
  (:refer-clojure :exclude [update])
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]
            [cassex.utils :refer :all]))

(def cluster (alia/cluster {:contact-points ["localhost"]}))
(def session (alia/connect cluster))

(defn insert-data[]
  (execute-query-dsl session (insert :offices (values [[:id 1] [:sid "1"] [:city "Georgia"] [:country "USA"]])))
  (execute-query-dsl session (insert :offices (values [[:id 2] [:sid "2"] [:city "Bangalore"] [:country "India"]])))
  (execute-query-dsl session (insert :offices (values [[:id 3] [:sid "3"] [:city "Pune"] [:country "India"]]))))

(defn create-schema [in-ks]
  (alia/execute session (create-db in-ks))
  (alia/execute session (use-keyspace (keyword in-ks)))
  (generate-table session :offices :id :int :sid :varchar :city :varchar :country :varchar :primary-key :id)
  (generate-index session :offices :city "cityIndex")
  (generate-index session :offices :country "countryIndex")
  (insert-data))

; (use '[clojure.tools.namespace.repl :only (refresh)])
; (use 'cassex.datamodel :reload)
; (require 'cassex.datamodel :reload-all)
