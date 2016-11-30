(ns the-apocalypse.core
  (:require [clojure.core.match :refer [match]]
    [clojure.string :as str])
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
          :exit 5
          :key 2
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

    (doseq [j (range 0 width)] (if (= (player :n) (+ j i)) (print "X  ") (print "   "))
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
; (defn p_movement [player dir curr_loc]
;   (let [loc (player :location)
;     dest (-> the-maze loc :dir)]
;     (cond (and (= dir 0) (can_travel player dir curr_loc)) (do (println-typing "Moving Left" 50) (- x 1)) ;should I change this to n instead of x and 
;           (and (= dir 1) (can_travel player dir curr_loc)) (do (println-typing "Moving Right" 50) (+ x 1)) ;y coordinates?
;           (and (= dir 2) (can_travel player dir curr_loc)) (do (println-typing "Moving Up" 50) (- y height))
;           (and (= dir 3) (can_travel player dir curr_loc)) (do (println-typing "Moving Left" 50) (+ y height))
;           :else (println-typing "Wrong Direction" 50))
;     ;Here I have updated the location as per the x and y coordinates
;   )
; ) 
          


;Here I have used the curr_location as a single vector location
(defn can_travel [player dir]
  (let [loc (player :location)
        curr_loc (player :n)
        rightWall (-> the-maze loc :rightWall)
        downWall (-> the-maze loc :downWall)
        width (-> the-maze loc :width)
        height (-> the-maze loc :height)] 
    (cond (= dir 2) (not (or (= (mod curr_loc width) 0) (= (nth rightWall (- curr_loc 1)) 1)))

    (= dir 0) (not (or (= (mod (+ curr_loc 1) width) 0) (= (nth rightWall curr_loc) 1)))

    (= dir 3) (not (or (< curr_loc width) (= (nth downWall (- curr_loc width)) 1)))

    (= dir 1) (not (or (>= (+ curr_loc width) (* width height)) (= (nth downWall curr_loc) 1)))

    ))
  ; 0 ->right
  ; 1 ->down
  ; 2 ->left
  ; 3 ->up
  ; for downWall -check at n
  ; for upWall - check at n-width
  ; for leftwall - check at n-1 rightwall
  ; for rightwall - check at n rightwall
  ; for left boundaries - check at (n)%width 
  ; for right boundaries - check at (n+1)%width
  ; for up boundaries - check n < width
  ; for down boudaries - check at ?
)

    ; (cond (and (= dir 2) (or (= (mod curr_loc width) 0) (= (nth rightWall (- curr_loc 1)) 1))) 
    ;   ((do (println-typing "Wall Ahead! Can't go left" 50) false)) :else (do (println-typing "Can go left" 50) true))
    
    ; (cond (and (= dir 0) (or (= (mod (+ curr_loc 1) width) 0) (= (nth rightWall curr_loc) 1)))
    ;   ((do (println-typing "Wall Ahead! Can't go right" 50) false) (do (println-typing "Can go right" 50) true)))
 
(def adventurer
  {:location :first_room
   :inventory #{}
   :n 0})

(defn status [player]
  ; (let [location (player :location)]
  ;   (print (str "You are " (-> the-map location :title) ". "))
  ;   (when-not ((player :seen) location)
  ;     (print (-> the-map location :desc)))
  ;   (update-in player [:seen] #(conj % location)))
  (player)
)

(defn go [dir player]
  (let [loc (player :location)
        width (-> the-maze loc :width)] 
  (if (not (can_travel player dir)) (do (println "Can't go there. There is a wall") player)
  (cond (= dir 0) (update-in player [:n] inc)
    (= dir 1) (update-in player [:n] + width)
    (= dir 3) (update-in player [:n] - width)
    (= dir 2) (update-in player [:n] dec)

    ))))

(defn respond [player command]
  (match command
         ; [:look] (update-in player [:seen] #(disj % (-> player :location)))
         [:east] (go 0 player)
         [:south] (go 1 player)
         [:north] (go 3 player)
         [:west] (go 2 player)

         _ (do (println "I don't understand you.")
               player)

         )) 

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [local-maze the-maze
         local-player adventurer]
        (let [_ (print-maze local-player)
              _ (println-typing "What do you want to do?" 50) 
          command (read-line)]
          
      (recur local-maze (respond local-player (to-keywords command))))))


  ;(print-maze arr 3 3)
  ; (print-many-lines 30)
  ; (println-typing "THE APOCALYPSE - LIFE AFTER CS 225" 50)
  ; (print-typing "Welcome to UIUC in the year of 2020" 50)
  ; (println-typing "..." 300)
  ; (println-typing "after the aftermath of CS 225" 100)





