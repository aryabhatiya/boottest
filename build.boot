(set-env!
 :source-paths #{"src" "task"})

(require '[adzerd.upper :refer :all])


(deftask foo
  [a arg ARGVAL int "The argument."]
  (let [state (atom arg)]
    (fn [next-handler]
      (fn [fileset]
        (println "Hello World"
                 (swap! state inc))
        (next-handler fileset)))))

(deftask bar
  []
  (comp (foo :arg 1) (foo :arg 2)))


(deftask hello
  [a arg ARGVAL int "the arghello."]
  (let [state (atom arg)]
    (fn [next-handler]
      (fn [fileset]
        (println "Hello World"
                 (swap! state inc))
        (next-handler fileset))))
  )

(deftask hellobar
  []
  (comp (hello :arg 1 )
         (hello :arg 2)))

(deftask foofoo
  [a arg ARGVAL int "The argument."]
  (let [state (atom arg)]
    (with-pre-wrap fileset
      (println "Hello World"
               (swap! state inc))
      fileset)
    ))
