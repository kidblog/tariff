(ns tariff.core
  (:require [clojure.tools.namespace.parse :as p]
            [clojure.tools.namespace.find :as f]
            [clojure.java.classpath :as cp]
            [medley.core :refer [map-vals]]
            [clojure.string :as string])
  (:gen-class))

(defn parse-alias
  "Given a require block like [clojure.spec :as s] return a map with the ns and the alias."
  [require-chunk]
  (let [result
        (if (coll? require-chunk)
          {:ns (first require-chunk)
           :alias (second (drop-while #(not (= % :as)) require-chunk))}
          {:ns require-chunk
           :alias nil})]
    result))

(defn get-require-block
  [ns-form]
  {(second ns-form) ;; the ns name
   (let [req-block (filter #(and
                             (coll? %)
                             (= (first %)) :require)
                           ns-form)]
     (->> req-block
          (map rest)
          (mapcat identity) ;; Flatten one level
          (map parse-alias)))})

(defn get-ns-forms
  "Get all the ns forms from the java classpath of namespaces that start with the ns-prefix."
  [ns-prefix]
  (filter
    #(string/starts-with? (second %) ns-prefix)
    (f/find-ns-decls (cp/classpath))))

(defn build-require-map-from-ns
  [ns-prefix]
  (reduce merge (map get-require-block (get-ns-forms ns-prefix))))

;; The final data transformation.

(defn -build-comparison-map
  "Build up a comparison map from an ns map. Valid values for grp-by and extract
   are :ns and :alias"
  [grp-by extract ns-map]
  (map-vals
    #(map extract %)
    (group-by grp-by (apply concat
                            (vals ns-map)))))

(def find-different-aliases-by-ns (partial -build-comparison-map :alias :ns))
(def find-different-ns-by-alias (partial -build-comparison-map :ns :alias))



(defn validate-namespaces
  "Perform namespace validation. Will throw an exception if a namespace is aliased in two different ways
   or if two different namespaces share an alias. All ns-forms are checked that match the ns-prefix, loading
   is done via the Java classpath."
  [ns-prefix]
  (let [req-map   (build-require-map-from-ns ns-prefix)
        ns->alias (find-different-ns-by-alias req-map)
        alias->ns (find-different-aliases-by-ns req-map)]
    (do
      (doseq [[alias ns-list] alias->ns]
        (when (and (not (nil? alias))
                   (< 1 (count (set ns-list))))
          (throw (Exception. (str alias " is an alias for " (string/join ", " (set ns-list)))))))

      (doseq [[ns alias-list] ns->alias]
        (let [alias-set (set (remove nil? alias-list))]
          (when (< 1 (count alias-set))
            (throw (Exception. (str ns " is aliased to " (string/join ", " alias-set))))))))))
