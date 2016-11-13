(ns the-apocalypse.core
  (:gen-class))

(defn printTyping [text]
	(doseq [i (range (count text))]
		(print (subs text i (+ i 1)))
	(flush)
	(Thread/sleep 100))
	(println "")
	)
	
(defn print-message [pid pw] 
(println "PID : " pid)
 (println "PW : " pw))

(defn inp[]
(println "Enter your PID and password") 
(let[pid (read-line) pw (read-line)] 
(print-message pid pw) ))

(defrecord Person [fname health])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (printTyping "Enter your name")
  )





