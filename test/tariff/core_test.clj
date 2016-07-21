(ns tariff.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [tariff.core :refer :all]))

(deftest parse-alias-test
  (testing "happypath"
    (is (= {:ns 'clojure.spec
            :alias 's}
           (parse-alias '[clojure.spec :as s])))

    (is (= {:ns 'clojure.spec
            :alias nil}
           (parse-alias '[clojure.spec])))))

(deftest get-require-block-test
  (testing "happypath"
    (let [test-data '(ns myns.blah
                       (:require [clojure.spec :as s]
                                 [clojure.spec :as spec]
                                 [clojure.blah]))
          result (get-require-block test-data)]
      (is (= {'myns.blah '({:ns clojure.spec
                            :alias s}
                            {:ns clojure.spec
                             :alias spec}
                            {:ns clojure.blah
                             :alias nil})}
             result)))))

(deftest test-validate-namespaces
  (testing "if we use an alias in two different namespaces do we die?"
    (is (thrown? Exception (validate-namespaces "tariff.bad"))))
  (testing "if we alias a namespace in two different ways do we die?"
    (is (thrown? Exception (validate-namespaces "tariff.terrible")))))


