(ns the-apocalypse.core
  (:gen-class))

(defn print-typing [text waittime]
	(doseq [i (range (count text))]
		(print (subs text i (+ i 1)))
	(flush)
	(Thread/sleep waittime))
	)


(defn println-typing [text waittime]
  (doseq [i (range (count text))]
    (print (subs text i (+ i 1)))
  (flush)
  (Thread/sleep waittime))
  (println "")
  )

(defn print-many-lines [n]
  (doseq [i (range n)]
    (println)
    ))

(defn print-maze [arr, width, height]
  (doseq [i (range 0 (* width height))]
    (if (= (mod i width) 0) (println) "no")
    (print (nth arr i)))
    (println)
  )

(defn read-maze [file-name, arr]
  (let [n 0]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (doseq [line (line-seq rdr)]
      (doseq [i (range (count line))]
        (= arr (assoc arr n (subs line i (+ i 1))))
        (= n (inc n))
        )
      )
    ) 
    (print-maze arr 3 3)
  ))
; (def maze-array
;   {:grainger {:array (read-maze "grainger.txt")
;           :entrance [{:x 5 :y 8 :map grainger-north-entrance
;             },{:x 10 :y 15 :map grainger-south-entrance
;             }
;           ]}
;   }
;   )


; (def the-map
;   {:tomb-north-entrance {:desc "The walls are freshly painted but do not have any pictures.  You get the feeling it was just created
; for a game or something."
;             :title "in the foyer"
;             :dir {:south tomb-north-entrance}
;             :maze {:array tomb :x 5 :y 8}
;             :contents #{:raw-egg}}
;     :grue-pen {:desc "It is very dark.  You are about to be eaten by a grue."
;               :title "in the grue pen"
;               :dir {:north :foyer}
;               :contents #{}}

;    })

; (def adventurer
;   {:location :foyer
;    :inventory #{}
;    :tick 0
;    :seen #{}
;    :x 0
;    :y 0})


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
<<<<<<< HEAD
  (def arr [0,0,0,0,0,0,0,0,0])
  (read-maze "src/the_apocalypse/text.txt" arr)
  ;(print-maze arr 3 3)
  ; (print-many-lines 30)
  ; (println-typing "THE APOCALYPSE - LIFE AFTER CS 225" 50)
  ; (print-typing "Welcome to UIUC in the year of 2020" 50)
  ; (println-typing "..." 300)
  ; (println-typing "after the aftermath of CS 225" 100)
=======
  (printTyping "Enter your name")
  (printTyping "Go back")
>>>>>>> eaef64612bf474d4e16c826f274c7bf2c1ac7a57
  )





