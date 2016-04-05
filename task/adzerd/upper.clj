(ns adzerd.upper
  (:require
   [boot.core :refer :all]
   [clojure.java.io :as io]))

(defn compile-lc
  [lc-file uc-file]
  (->> lc-file
       slurp
       .toUpperCase
       (spit uc-file))
  )

(defn lc->uc-path
  [path]
  (.replaceAll (last  (clojure.string/split path #"/")) "\\.lc$" ".uc")
                                        ; (.replaceAll  "\\.lc$" ".uc")
  )

(deftask upper
  []
  (let [tmp (temp-dir!)
        state (atom nil)
        ]
    (with-pre-wrap fileset
      (empty-dir! tmp)
      (doseq [lc-tmp
              (->> fileset
                   (fileset-diff @state)
                   input-files
                   (by-ext [".lc"])
                   )
              :let [lc-file (tmpfile lc-tmp)
                    lc-path (tmppath lc-tmp)
                    uc-file (io/file tmp (lc->uc-path  lc-path))]]

        (if @state
          (do
            (println "Fetching ..."  lc-path)
            (compile-lc lc-file uc-file))
          )
        )
      (reset! state fileset)
      (->  fileset (add-resource tmp) commit!)
      )))

(defn wolrd
  [a]
  a)

(defn upcasef
  [lc-file dir uc-file]
  (->> lc-file
       slurp
       .toUpperCase
       (spit dir uc-file)))
(defn files-by
  [fileset extension]
  (->> fileset
       input-files
       (by-ext
        [(or extension ".lc")])))

(defn upcase-files
  [dir files]
  (doseq [f files
          :let [in-file (tmpfile f)
                rel-path (tmppath f)]]
    (println "Upcasing file %s...\n" in-file)
    (println "Upcasing %s...\n" rel-path)
    (upcasef in-file dir rel-path)))

(deftask dupper
  "convert file text to upper case."
  [x extension EXT str "The file extension."]
  (let [dir (temp-dir!)]
    (with-pre-wrap [fileset]
      (empty-dir! dir)
      (upcase-files dir (files-by fileset extension))
      (commit! (add-resource fileset dir)))))(boot.core/load-data-readers!)
