(ns cassex.schema
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]))

(def cluster (alia/cluster {:contact-points ["localhost"]}))
(def session (alia/connect cluster))

(defn create-db [db-name]
  (create-keyspace db-name (with {:replication {:class "SimpleStrategy" :replication_factor 1}})))

(defn get-blog-table []
  (create-table :blog_md (column-definitions [[:name :varchar]
                                              [:email :varchar]
                                              [:password :varchar]
                                              [:primary-key :email]])))
                                    
(defn get-posts-table[]
  (create-table :posts (column-definitions [[:title :varchar]
                                            [:text :varchar]
                                            [:blog_name :varchar]
                                            [:author_name :varchar]
                                            [:timestamp :timestamp]
                                            [:primary-key :title]])))

(defn get-comments-table []
  (create-table :comments (column-definitions [[:comment :varchar]
                                               [:author :varchar]
                                               [:timestamp :timestamp]
                                               [:primary-key :comment]])))

(defn execute-query [query]
  (alia/execute session query))

(defn execute-query-dsl [query]
  (alia/execute session (->raw query)))

(defn create-schema [in-ks]
  (let [blogmd (get-blog-table)
        posts (get-posts-table)
        comments (get-comments-table)]
    (alia/execute session (use-keyspace (keyword in-ks)))
    (execute-query-dsl blogmd)
    (execute-query-dsl posts)
    (execute-query-dsl comments)))
    

; (alia/execute session "USE crud;")

; (client/connect ["127.0.0.1" "localhost"])
; (use '[clojure.tools.namespace.repl :only (refresh)])
; (require 'cassex.schema :reload-all)