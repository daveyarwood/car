(set-env! 
  :dependencies #(into % '[[jarohen/nomad "0.7.1"]
                           [defun "0.2.0-RC"]
                           [adzerk/bootlaces "0.1.11" :scope "test"]])
  :source-paths #{"src"})

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")
(bootlaces! +version+)

(task-options!
  pom {:project 'car
       :version +version+
       :description "A CLI tool to help you maintain your car"
       :url "https://github.com/daveyarwood/car"
       :scm {:url "https://github.com/daveyarwood/car"}
       :license {"name" "Eclipse Public License"
                 "url" "http://www.eclipse.org/legal/epl-v10.html"}})
