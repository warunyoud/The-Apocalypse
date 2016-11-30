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



(def the-maze
  {:first_room {:desc "best building"
              :title "first_room"
              :rightWall [0,0,0,
                          1,1,0,
                          0,1,0]
              :downWall [0,1,0,
                         0,0,0
                         0,0,0]
              :width 3
              :height 3
          :connection [{:x1 2 :y1 3 :maze :second_room :x2 1 :y2 2
            },{:x1 1 :y 2 :maze :third_room :x2 0 :y2 2
            }
          ]
        }
  :second_room {:desc "best building"
              :title "second_room"
              :rightWall [0,0,0,
                          0,1,0,
                          0,0,0]
              :downWall [0,1,0,
                         0,0,0
                         0,0,0]
              :width 3
              :height 3
          :connection [{:x1 1 :y1 2 :maze :first_room :x2 0 :y2 0
            },{:x1 1 :y 2 :maze :third_room :x2 0 :y2 2
            }
          ]
        }
    :third_room {:desc "best building"
              :title "third_room"
              :rightWall [0,0,0,
                          0,1,0,
                          0,0,0]
              :downWall [0,1,0,
                         0,0,0
                         0,0,0]
              :width 3
              :height 3
          :connection [{:x1 1 :y1 2 :maze :first_room :x2 0 :y2 0
            },{:x1 1 :y 2 :maze :second_room :x2 0 :y2 2
            }
          ]
        }
    }
  )


(defn print-maze [player]
  (let [loc (player :location)
        rightWall (-> the-maze loc :rightWall)
        downWall (-> the-maze loc :downWall)
        width (-> the-maze loc :width)
        height (-> the-maze loc :height)]

  (doseq [i (range 0 width)] (print " ___"))

  (doseq [i (range 0 (* width height))]
    

    ;top
    (if (= (mod i width) 0) (do (if (= i 0) (println) (println "|")) (print "|")

    (doseq [j (range 0 width)] (print "   ")
    (if (= (nth rightWall (+ j i)) 1) (print "|") (if (not= j (- width 1)) (print " "))))

    (println "|") (print "|")))

    ;second
    (if (or (= (nth downWall i) 1) (>= (+ i width) (* width height)))(if (and (= (nth rightWall i) 0) (not= (mod (+ i 1) width) 0))(print "___ ") (print "___")))
    (if (= (nth rightWall i) 1) (if (and (= (nth downWall i) 0) (< (+ i width) (* width height)))  (print "   |") (print "|")))
    (if (and (= (nth downWall i) 0) (= (nth rightWall i) 0) (< (+ i width) (* width height))) (if (not= (mod (+ i 1) width) 0) (print "    ") (print "   ")))
  )
  (println "|"))

  (println))


; need a start location for first room
;this function checks if the player can move in a particular direction by using can_travel and makes him move accordingly
;also prompts the player for the direction to move in and updates accordingly
;need to decide if we are doing curr_loc as a single array from 0-n-1 or x and y coordinates
(defn p_movement [player dir curr_loc]
  (let [loc (player :location)
    dest (-> the-maze location :dir)]
    (cond (and (= dir 0) (can_travel player dir curr_loc)) (do (println-typing "Moving Left" 50) (- x 1))
          (and (= dir 1) (can_travel player dir curr_loc)) (do (println-typing "Moving Right" 50) (+ x 1))
          (and (= dir 2) (can_travel player dir curr_loc)) (do (println-typing "Moving Up" 50) (- y height))
          (and (= dir 3) (can_travel player dir curr_loc)) (do (println-typing "Moving Left" 50) (+ y height))
          :else (println-typing "Wrong Direction" 50))
    ;Here I have updated the location as per the x and y coordinates
  )
) 
          


;Here I have used the curr_location as a single vector location
(defn can_travel [player dir curr_loc]
  (let [loc (player :location)
        rightWall (-> the-maze loc :rightWall)
        downWall (-> the-maze loc :downWall)
        width (-> the-maze loc :width)
        height (-> the-maze loc :height)]
    (if (= dir 0) (if (or (= (mod curr_loc width) 0) (= (nth rightWall (- curr_loc 1)) 1)) (println-typing "Wall Ahead! Can't go left" 50) (println-typing "Can go left" 50)))
    (if (= dir 1) (if (or (= (mod (+ curr_loc 1) width) 0) (= (nth rightWall curr_loc) 1)) (println-typing "Wall Ahead! Can't go right" 50) (println-typing "Can go right" 50)))
    (if (= dir 2) (if (or (< curr_loc width) (= (nth downWall (- curr_loc width) 1))) (println-typing "Wall Ahead! Can't go up" 50) (println-typing "Can go up" 50)))
    (if (= dir 3) (if (or (< (+ curr_loc width) (* width height)) (= (nth downWall curr_loc) 1)) (println-typing "Wall Ahead! Can't go down" 50) (println-typing "Can go down" 50)))
    )
  ; 0 ->left
  ; 1 ->right
  ; 2 ->up
  ; 3 ->down
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

(defn status [player]
  ; (let [location (player :location)]
  ;   (print (str "You are " (-> the-map location :title) ". "))
  ;   (when-not ((player :seen) location)
  ;     (print (-> the-map location :desc)))
  ;   (update-in player [:seen] #(conj % location)))
  (player)
)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [local-maze the-maze
         local-player adventurer]
        (let [_ (println-typing "What do you want to do?" 50) 
          command (read-line)]
          (print-maze local-player)
      (recur local-maze local-player))))


  ;(print-maze arr 3 3)
  ; (print-many-lines 30)
  ; (println-typing "THE APOCALYPSE - LIFE AFTER CS 225" 50)
  ; (print-typing "Welcome to UIUC in the year of 2020" 50)
  ; (println-typing "..." 300)
  ; (println-typing "after the aftermath of CS 225" 100)





