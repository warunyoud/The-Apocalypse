(ns the-apocalypse.core
  (:gen-class))

(defn printTyping [text]
	(doseq [i (range (count text))]
		(print (subs text i (+ i 1)))
	(flush)
	(Thread/sleep 100))
	(println "")
	)
	

(defrecord Person [fname health])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (printTyping "Enter your name")
  (printTyping "Go back")
  )





