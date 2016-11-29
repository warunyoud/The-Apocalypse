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

(defn print-maze [player]
  (let []
  (doseq [i (range 0 (* width height))]
    (if (= (mod i width) 0) (println) "no")
    (print (nth arr i)))
    (println)
  ))

(def the-maze
  {:first_room {:desc "best building"
              :rightWall [0,0,0,
                          0,1,0,
                          0,0,0]
              :downWall [0,1,0,
                         0,0,0
                         0,0,0]
              :width 3
              :height 3
          :connection [{:x1 2 :y1 3 :maze second_room :x2 1 :y2 2
            },{:x1 1 :y 2 :maze third_room :x2 0 :y2 2
            }
          ]
        }
    }
  {:second_room {:desc "best building"
              :rightWall [0,0,0,
                          0,1,0,
                          0,0,0]
              :downWall [0,1,0,
                         0,0,0
                         0,0,0]
              :width 3
              :height 3
          :connection [{:x1 1 :y1 2 :maze second_room :x2 0 :y2 0
            },{:x1 1 :y 2 :maze third_room :x2 0 :y2 2
            }
          ]
        }
    }
  )

(defn can_travel [player]
  ;for downWall -check at n
  ;for upWall - check at n-width
  ;for leftwall - check at n-1 rightwall
  ;for rightwall - check at n rightwall
  ;for left boundaries - check at (n)%width 
  ;for right boundaries - check at (n+1)%width
  ;for up boundaries - check n < width
  ;for down boudaries - check at ?
  )

(def adventurer
  {:location :first_room
   :inventory #{}
   :seen #{}
   :x
   :y})


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [local-maze the-maze
         local-player adventurer]
    (let [_ (print-maze local-player)
          _ (println-typing "What do you want to do?" 50)
          command (read-line)]
      (recur local-maze local-player))))

  ;(print-maze arr 3 3)
  ; (print-many-lines 30)
  ; (println-typing "THE APOCALYPSE - LIFE AFTER CS 225" 50)
  ; (print-typing "Welcome to UIUC in the year of 2020" 50)
  ; (println-typing "..." 300)
  ; (println-typing "after the aftermath of CS 225" 100)





