(ns leiningen.set-dep-ver
  (:require [rewrite-clj.zip :as z]))

(defn find-dep
  "Given a zipper which is already on a dependencies vector, [[foo \"1.2.3\"][bar \"3.0\"]], etc, return the zipper positioned at [bar \"3.0\"], or nil"
  [deps name]
  (assert (z/vector? deps))
  (let [first-dep (z/down deps)]
    (assert (z/vector? first-dep))
    (loop [d first-dep]
      (when (z/sexpr d)
        (if (-> d z/sexpr first (= name))
          d
          (do
            (println "recurring")
            (recur (z/right d))))))))

(defn find-dependencies
  "Takes a zipper of the project.clj source. Returns the zipper positioned at the beginning of the dependencies vector"
  [project]
  (-> project
      (z/find-value z/next 'defproject)
      (z/find-value z/next :dependencies)
      z/right))

(defn update-dependency [project dep new-v]
  (some-> project
          find-dependencies
          (find-dep dep)
          z/down
          z/right
          (z/replace new-v)))

(defn set-dep-ver
  "Rewrites the project.clj, setting the version of the dependency 'dep-name to 'new-ver"
  [project dep-name new-ver]
  (let [dep-name (symbol dep-name)]
    (if-let [updated (update-dependency (z/of-file "project.clj") dep-name new-ver)]
      (do
        (spit "project.clj" (with-out-str (z/print-root updated)))
        (println "updated")
        (System/exit 0))
      (do
        (println "failed")
        (System/exit 1)))))
