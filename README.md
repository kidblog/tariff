[![Build Status](https://travis-ci.org/brian-dawn/tariff.svg?branch=master)](https://travis-ci.org/brian-dawn/tariff)

# tariff

Tariff is a Clojure library that lets you validate how you alias your imports across your codebase.
In large Clojure codebases aliases can get unwieldy and it is easy to lose track of what common aliases you use.

Tariff takes an opinionated approach and will complain if you use the same alias for two different imports, or import
a namespace with two different aliases.

Tariff presently is not a leiningen plugin or anything of the sort. If you call validate-namespaces and your codebase
fails to hold true to these two rules it will throw an exception. I did it this way for simplicity and so I can hook
into existing test infrastructure easily.

## Installation

Clojars coming soon!

## Usage

Tariff can be integrated with your application by doing something along the lines of the following:
```clojure
(ns myapp.validate-namespaces
    (:require [tariff.core :as tariff]
              [clojure.test :refer :all]))

(deftest validate-namespaces
    (testing "Do we have reasonable aliases?"
        (tariff/validate-namespaces "myapp")))
```

## License

Copyright © 2016 Kidblog

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
