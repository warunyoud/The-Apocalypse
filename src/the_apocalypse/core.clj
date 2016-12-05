(ns the-apocalypse.core
  (:require [clojure.core.match :refer [match]]
    [clojure.string :as str])
  (:gen-class))

;prints out the text by typing it

(defn print-typing [text waittime]
	(doseq [i (range (count text))]
		(print (subs text i (+ i 1)))
	(flush)
	(Thread/sleep waittime))
	)

;prints out the text and goes to the next line

(defn println-typing [text waittime]
  (doseq [i (range (count text))]
    (print (subs text i (+ i 1)))
  (flush)
  (Thread/sleep waittime))
  (println "")
  )

;INSTRUCTIONS TO PLAY

(defn instructions [player]
  (println "
 ______________________________________
|                                      |
|         INSTRUCTIONS                 |
|                                      |
| DIRECTIONS                           |
| To move NORTH - Enter north or n     |
| To move SOUTH - Enter south or s     |
| To move EAST  - Enter east or e      |
| To move WEST  - Enter west or w      |
|                                      |
| MAP USAGE                            |
| To use the map - Enter use map       |
|______________________________________|")
  player)


(defn the_end [player]
  (println-typing 
    "
 /$$$$$$$$ /$$   /$$ /$$$$$$$$       /$$$$$$$$ /$$   /$$ /$$$$$$$ 
|__  $$__/| $$  | $$| $$_____/      | $$_____/| $$$ | $$| $$__  $$
   | $$   | $$  | $$| $$            | $$      | $$$$| $$| $$  \\ $$
   | $$   | $$$$$$$$| $$$$$         | $$$$$   | $$ $$ $$| $$  | $$
   | $$   | $$__  $$| $$__/         | $$__/   | $$  $$$$| $$  | $$
   | $$   | $$  | $$| $$            | $$      | $$\\  $$$| $$  | $$
   | $$   | $$  | $$| $$$$$$$$      | $$$$$$$$| $$ \\  $$| $$$$$$$/
   |__/   |__/  |__/|________/      |________/|__/  \\__/|_______/ 
                                                                                                          
" 10)
(println-typing "Thank you for playing!" 20)
  player)

;player definition

(def adventurer
  {:location :second_room
   :has-key false
   :has-map true
   :n 0})
(def adveturer2 
  {:location :second_player})

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))


; definition of the 3 mazes

(def the-maze
  {:first_room {:desc "first room in an unknown building"
              :title "first_room"
              :rightWall [0,0,1,0
                          1,1,0,0
                          0,1,0,0]
              :downWall [0,1,0,0
                         0,0,0,1
                         0,0,0,0]
              :width 4
              :height 3
          :exit 5
          :key 3
          :map 11
          :nextmap :second_room
          :nextn 3
          :monstn 0
          :monsters []
        }

  :second_room {:desc "second room in an unknown building. There is a monster here"
              :title "second_room"
              :rightWall [0,0,0,0,0
                          1,0,0,1,0
                          1,1,0,1,0
                          1,0,0,0,0
                          0,0,0,0,0]
              :downWall [0,1,1,1,0
                         0,0,1,0,0
                         0,0,1,0,0
                         1,0,0,0,1
                         0,0,0,0,0]
              :width 5
              :height 5
          :exit 1
          :key 3
          :map 5
          :nextmap :second_room
          :nextn 3 
          :monstn 1
          :monsters [{:index 0
                    :size 8
                    :path [17,16,11,6,7,8,13,18]
                    :alive true}]
        }
    
    :third_room {:desc "Third room in an unknown building. There are 2 monsters in this maze"
              :title "third_room"
              :rightWall [0,0,0,0
                          0,0,1,0
                          0,1,0,0
                          0,0,0,0
                          0,0,0,0]
              :downWall [1,1,1,0
                         0,0,1,0
                         0,0,1,0
                         1,1,1,0
                         0,0,0,0]
              :width 4
              :height 5
          :exit 0
          :key 6
          :map 16
          :nextn 3
          :monstn 1
          :monsters [{:index 0
                    :size 6
                    :path [16, 17, 18, 19, 15, 11]
                    :alive true}]
        }

  }
)


;Prints out the maze if the player finds the map in the maze

(defn print-maze [maze player]
  (let [loc (player :location)
        rightWall (-> maze loc :rightWall)
        downWall (-> maze loc :downWall)
        width (-> maze loc :width)
        height (-> maze loc :height)
        monster (-> maze loc :monsters)
        monstn (-> maze loc :monstn)]

  (doseq [i (range 0 width)] (print " ___"))

  (doseq [i (range 0 (* width height))]
    

    ;top
    (if (= (mod i width) 0) (do (if (= i 0) (println) (println "|")) (print "|")

    (doseq [j (range 0 width)] (cond (= (player :n) (+ j i)) (print "X  ") 
      ;monster
      (loop [myIndex 0] 
          (if (= myIndex (-> maze loc :monstn))
            false
            (if (= (nth ((nth monster myIndex) :path) ((nth monster myIndex) :index)) (+ j i))
              true
              (recur (+ 1 myIndex)))
                    
          )
        )(print "*U*")

      ;key
      (and (= (-> the-maze loc :key) (+ j i)) (not (player :has-key))) (print "key") 
      (= (-> the-maze loc :exit) (+ j i)) (print "ext")
      (and (= (-> the-maze loc :map) (+ j i)) (not (player :has-map))) (print "map") :else (print "   "))
    (if (= (nth rightWall (+ j i)) 1) (print "|") (if (not= j (- width 1)) (print " "))))

    (println "|") (print "|")))

    ;second
    (if (or (= (nth downWall i) 1) (>= (+ i width) (* width height)))(if (and (= (nth rightWall i) 0) (not= (mod (+ i 1) width) 0))(print "___ ") (print "___")))
    (if (= (nth rightWall i) 1) (if (and (= (nth downWall i) 0) (< (+ i width) (* width height)))  (print "   |") (print "|")))
    (if (and (= (nth downWall i) 0) (= (nth rightWall i) 0) (< (+ i width) (* width height))) (if (not= (mod (+ i 1) width) 0) (print "    ") (print "   ")))
  )
  (println "|"))

  (println))


;this function checks if the player can travel in the maze

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

(defn check-hit [maze player]
  (let [loc (player :location)
      monster (-> maze loc :monsters)
      monstn (-> maze loc :monstn)
      ] (if (loop [myIndex 0] 
            (if (= myIndex (-> maze loc :monstn))
              false
              (if (= (nth ((nth monster myIndex) :path) ((nth monster myIndex) :index)) (player :n))
                true
                (recur (+ 1 myIndex)))
                      
            )
          )(do (print "You are dead") player) player))
  )

(defn move-monsters [maze player]
  (let [loc (player :location)]

        (loop [myIndex 0
              mz maze] 
          (if (= myIndex (-> mz loc :monstn))
            mz
            (recur (inc myIndex) (if (= ((nth (-> mz loc :monsters) 0) :index) (- ((nth (-> mz loc :monsters) 0) :size) 1)) 
              (assoc-in mz [loc :monsters 0 :index] 0) (update-in mz [loc :monsters 0 :index] inc)))
                    
          )
        )

        ))

(defn display [player]
  (let [loc (player :location)
        east (can_travel player 0)
        south (can_travel player 1)
        west (can_travel player 2)
        north (can_travel player 3)
        e (can_travel player 0)
        s (can_travel player 1)
        w (can_travel player 2)
        n (can_travel player 3)]
    (println)
      (println "=============================")
      (println (-> the-maze loc :title))
      (println "=============================")
       (cond (and (or east e) (or south s) (or west w) (or north n)) (println-typing "You are in an open area." 40)
        (and (or s south) (or w west) (or n north)) (println-typing "You are at an edge. There is a wall east of you." 40)
        (and (or e east) (or west w) (or n north)) (println-typing "You are at an edge. There is a wall south of you." 40)
        (and (or e east) (or s south) (or n north)) (println-typing "You are at an edge. There is a wall west of you." 40)
        (and (or e east) (or s south) (or w west)) (println-typing "You are at an edge. There is a wall north of you." 40)
        (and (or e east) (or s south)) (println-typing "You are at a corner. There are walls surrounding north and west." 40)
        (and (or e east) (or n north)) (println-typing "You are at a corner. There are walls surrounding south and west." 40)
        (and (or w west) (or s south)) (println-typing "You are at a corner. There are walls surrounding north and east." 40)
        (and (or w west) (or n north)) (println-typing "You are at a corner. There are walls surrounding south and east." 40)
        (and (or s south) (or n north)) (println-typing "You are in an alley. Either head north or south" 40)
        (and (or w west) (or e east)) (println-typing "You are in an alley. Either head east or west." 40)
        (and (or e east)) (println-typing "You cannot move forward. You are at a dead end." 40)
        (and (or s south)) (println-typing "You cannot move forward. You are at a dead end." 40)
        (and (or w west)) (println-typing "You cannot move forward. You are at a dead end." 40)
        (and (or n north)) (println-typing "You cannot move forward. You are at a dead end." 40)
        )) player)

(defn move-to [player]
  (let [loc (player :location)
    nextmap (-> the-maze loc :nextmap)
    nextn (-> the-maze loc :nextn)]

    (display (assoc-in (assoc-in (assoc-in (assoc-in player [:has-map] false) [:has-key] false) [:location] nextmap) [:n] nextn)))
  )


(defn status [maze player]
  (let [loc (player :location)
      keyloc (-> maze loc :key)
      maploc (-> maze loc :map)
      exitloc (-> maze loc :exit)
      monster (-> maze loc :monsters)
      monstn (-> maze loc :monstn)
      ]

      (display player)
      (print)

    (cond 
      (loop [myIndex 0] 
          (if (= myIndex (-> maze loc :monstn))
            false
            (if (= (nth ((nth monster myIndex) :path) ((nth monster myIndex) :index)) (player :n))
              true
              (recur (+ 1 myIndex)))
                    
          )
        )(do (print "You are dead") player)
      (and (= keyloc (player :n)) (not (player :has-key))) (do (println-typing "There is a key on the floor." 40) (println 
"\n  ad8888888888ba
 dP'         `\"8b,
 8  ,aaa,       \"Y888a     ,aaaa,     ,aaa,  ,aa,
 8  8' `8           \"88baadP\"\"\"\"YbaaadP\"\"\"YbdP\"\"Yb
 8  8   8              \"\"\"        \"\"\"      \"\"    8b
 8  8, ,8         ,aaaaaaaaaaaaaaaaaaaaaaaaddddd88P
 8  `\"\"\"'       ,d8\"\"
 Yb,         ,ad8\"    
  \"Y8888888888P\"\n") (println-typing "Do you want to take it?" 20) 
      (match (to-keywords (read-line)) 
        [:yes] (do (println-typing "Key taken!" 40) (assoc-in player [:has-key] true))
        _ player))

    (and (= maploc (player :n)) (not (player :has-map))) (do (println-typing "There is a map on the floor." 40)(println     
"     
     _____________________________________________
()==(                                            (@==()
    (____________________________________________'|
       |                                          |
       |          _ __ ___    ___ _ __            |
       | (•_•)   | '_ ` _ \\ / _  | _  \\           |
       | <)  )╯  | | | | | ( |_| | |_) \\          |  
       |  /  \\   |_| |_| |_|\\__,_| .__/           |
       |                         | |              |
       |                         |_|              |
       |                                          |
     __)__________________________________________|
()==(                                            (@==()
     '--------------------------------------------'      \n")

      (println-typing "Do you want to take it?" 20) 
      (match (to-keywords (read-line)) 
        [:yes] (do (println-typing "You have obtained a map! to use the map type 'use map'" 40)(assoc-in player [:has-map] true))
        _ player))

     (= exitloc (player :n)) (do (println "You have found the EXIT!!") (println "Do you want to open it?") (match (to-keywords (read-line)) 
        [:yes] (do (if (player :has-key) (do (println "You have used your key to unlock the exit door.") (move-to player)) (do (println "It's locked!") player)))
        _ player))
     :else player)
  ;   (when-not ((player :seen) location)
  ;     (print (-> the-map location :desc)))
    )
  
)

(defn go [dir player]
  (let [loc (player :location)
        width (-> the-maze loc :width)] 
  (if (not (can_travel player dir)) (do (println "Can't go there. There is a wall.") player)
  (cond (= dir 0) (update-in player [:n] inc)
    (= dir 1) (update-in player [:n] + width)
    (= dir 3) (update-in player [:n] - width)
    (= dir 2) (update-in player [:n] dec)

    ))))

(defn respond [maze player command]
  (match command
         ; [:look] (update-in player [:seen] #(disj % (-> player :location)))
         [:east] (go 0 player)
         [:south] (go 1 player)
         [:north] (go 3 player)
         [:west] (go 2 player)
         [:e] (go 0 player)
         [:s] (go 1 player)
         [:n] (go 3 player)
         [:w] (go 2 player)
         [:i] (instructions player)
         [:use :map] (do (if (player :has-map) (print-maze maze player) (println "You don't have a map yet.")) player)
         _ (do (println "I don't understand you.")
               player)

         ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println)
  (let[local-maze the-maze
        local-player adventurer
        pl(local-maze local-player)]
        (print-maze local-maze pl))
)

  (println-typing " 

 ___      ___           __                                    __          
/   \\    /   \\  ____   |  |   ____   ____   _____   ____    _/  |_   ___  
\\    \\/\\/   /  / __ \\  |  |  / ___\\ /  _ \\ /     \\_/  __\\  |      \\ /  _ \\ 
 \\         /  \\  ___/  |  |_ \\ \\__  ( <_> )  Y Y  \\  ___/    |  |  (  <_> )
  \\__/\\   /    \\___ >  |____/ \\___ > ____/ |__| _| /\\___ >   |__|   \\____/ 
       \\_/        \\/             \\/              \\/   \\/                " 10)


  (println-typing "                              
 _________________________________________________________________________
|                                                                         |
|         === = = ===   .-. .-. .=. .== .-. .  '. .' .--. .-= .==         |
|          |  |=| |=    |=| |=' | | |   |=| |    |   |--' `-. |=          |
|          =  = = ===   = = =   `=' `== = = `==  =   =    =-' `==         |
|_________________________________________________________________________|" 
10)
  (println)

(println-typing "                        
 _____________________________________________________________________________
|                                                                             |
| The Apocalypse is a game of adventure, danger and monsters.                 |
| In this world you will explore some of the most amazing puzzles             |
| and mazes ever seen by mortal man.                                          |
|                                                                             |
| In Apocalypse the intrepid explorer finds himself in a lost labyrinth       |
| of another world, searching for a door that will take him back              |
| to his own world, his loved ones. But to find this door,                    |
| the adventurer has to go through different mazes filled with                |
| unknown creatures and traps!                                                |
|                                                                             |
| This game has been created by Shashank Bansal and Boom Dej-Udom.            |
| Have fun! and let us know if you have any feedback at sbansal6@illinois.edu.|
|                                                                             |
|_____________________________________________________________________________|" 10)
  
  (loop [local-maze the-maze
         local-player adventurer]
        (let [pl (check-hit local-maze local-player)
              mz (move-monsters local-maze pl)
              pll (status mz pl)

              _ (println-typing "What do you want to do now?" 20)
          command (read-line)]
      (recur mz (respond mz pll (to-keywords command))))))

