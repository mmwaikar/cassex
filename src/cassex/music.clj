(ns cassex.music
  (:refer-clojure :exclude [update])
  (:require [qbits.alia :as alia]
            [qbits.hayt :refer :all]
            [cassex.utils :refer :all]))

(def cluster (alia/cluster {:contact-points ["localhost"]}))
(def session (alia/connect cluster))

(defn insert-data[])

(defn create-schema [in-ks]
  (alia/execute session (create-db in-ks))
  (alia/execute session (use-keyspace (keyword in-ks)))
  (generate-table session :songs :id :uuid :title :text :album :text :artist :text :data :blob :primary-key :id)
  (generate-table session :playlists :id :uuid :song_order :int :song_id :uuid :title :text :album :text :artist :text :primary-key [:id :song_order])
  (generate-index session :playlists :artist "artistIndex")
  ; (generate-index session :offices :country "countryIndex")
  (insert-data))