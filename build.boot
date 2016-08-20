(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/bootlaces   "0.1.13" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.2"  :scope "test"]
                  [cljsjs/react "15.3.0-0"]])

(require '[adzerk.bootlaces :refer :all]
         '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "0.2.2")
(def +version+ (str +lib-version+ "-0"))

(task-options!
  pom  {:project     'ua.modnakasta/smoothscroll
        :version     +version+
        :description "A teeny tiny, standard compliant, smooth scroll script with ease-in-out effect and no dependency."
        :url         "https://github.com/alicelieutier/smoothScroll"
        :scm         {:url "https://github.com/modnakasta/smoothscroll"}
        :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(require '[boot.core :as c]
         '[boot.tmpdir :as tmpd]
         '[clojure.java.io :as io]
         '[clojure.string :as string])

(deftask package []
  (comp
    (download :url "https://raw.githubusercontent.com/alicelieutier/smoothScroll/0.2.2/smoothscroll.js"
              :checksum "626F3B9E60F9ADD4994C909F6F886BDE")

    (sift :move {#"^smoothscroll.js$" "mk/smoothscroll/development/smoothscroll.inc.js"})

    (minify :in "mk/smoothscroll/development/smoothscroll.inc.js"
      :out "mk/smoothscroll/production/smoothscroll.min.inc.js")

    (sift :include #{#"^mk"})
    (deps-cljs :name "mk.smoothscroll"
               :requires ["cljsjs.react"])
    (pom)
    (jar)))

(deftask clojars []
  (comp
    (package)
    (build-jar)
    (push-release)))
