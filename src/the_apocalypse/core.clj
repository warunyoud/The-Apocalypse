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
|                                      |  
| NOTHING                              |
| To do nothing - Type nothing         |
|______________________________________|")
  player)


(defn the_end []
  (println-typing "Congratulations! You have found the Spaceship back to Earth." 20)
  (println
    "
                        ___\\____
                       0)__/... \\_
                      00)__)  .___)
     ___              _0__)\\_/ OOO/`':.
    0)_^'-._    __..-'`:  \\ | / ::  \\ o`:
    0)_\\ \\~_..-': \\ \\   :  \\|/   ::  |   \\
        \\ /      : | |  :    :  ::  /  _./>-
         (__ ))): /_/____.))_____//.-'`
          7 7~~''''            7 7
          L_L                 / /
         0) ^'-.__            |_|
         0)__.-'             0) ^'-.__
                             0)__.-'

    ")
  (println)
  (println)

  (println "

    ")
  ()
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
(println-typing "Thank you for playing!" 15))

;player definition

(def adventurer
  {:location :first_room
   :has-key false
   :has-map false
   :n 0})

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))

;(println "Yo whatsup")
; definition of the 3 mazes
(def the-maze
  {:first_room {:desc "You are now in Room1 of an unknown building in an unknown world."
              :title "Room1"
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
          :n 0
          :monstn 0
          :monsters []
        }

  :second_room {:desc "You are now in Room2 of an unknown building in an unknown world. You can hear noises of an unknown creature in a distance."
              :title "Room2"
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
          :exit 12
          :key 24
          :map 20
          :nextmap :third_room
          :n 5 
          :monstn 1
          :monsters [{:index 0
                    :size 8
                    :path [17,16,11,6,7,8,13,18]}]
        }
    
    :third_room {:desc "You are now in Room3 of an unknown building in an unknown world. You can hear noises of unknown number of creatures in a distance."
              :title "Room3"
              :rightWall [0,0,0,0
                          0,0,1,0
                          0,1,0,0
                          0,0,0,0
                          0,0,0,0]
              :downWall [1,1,1,0
                         0,1,1,0
                         0,1,1,0
                         1,1,1,0
                         0,0,0,0]
              :width 4
              :height 5
          :exit 0
          :key 6
          :nextmap :exit
          :map 10
          :n 2
          :monstn 2
          :monsters [{:index 0
                    :size 14
                    :path [16, 17, 18, 19, 15, 11, 7, 3, 7, 11, 15, 19, 18, 17]}
                    {:index 0
                    :size 10
                    :path [14, 13, 12, 8, 4, 5, 4, 8, 12, 13]}]
        }
  }
)

;(println "compiled till the maze, next is can_travel")

;this makes sure if the player can move in the direction specified
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

    ;double movement
    ;east east
    (= dir 8) (and (can_travel player 0) (not (or (= (mod (+ (+ curr_loc 1) 1) width) 0) (= (nth rightWall (+ curr_loc 1)) 1))))

    ;south south
    (= dir 9) (and (can_travel player 1) (not (or (>= (+ (+ curr_loc width) width) (* width height)) (= (nth downWall (+ curr_loc width)) 1))))

    ;west west
    (= dir 10) (and (can_travel player 2) (not (or (= (mod (- curr_loc 1) width) 0) (= (nth rightWall (- (- curr_loc 1) 1)) 1))))

    ;north north
    (= dir 11) (and (can_travel player 3) (not (or (< (- curr_loc width) width) (= (nth downWall (- (- curr_loc width) width)) 1))))

    ;northeast
    (= dir 4) (or (and (can_travel player 3) (not (or (= (mod (+ (- curr_loc width) 1) width) 0) (= (nth rightWall (- curr_loc width)) 1))))
        (and (can_travel player 0) (not (or (< (+ curr_loc 1) width) (= (nth downWall (- (+ curr_loc 1) width)) 1)))))

    ;southeast
    (= dir 5) (or (and (can_travel player 1) (not (or (= (mod (+ (+ curr_loc width) 1) width) 0) (= (nth rightWall (+ curr_loc width)) 1))))
        (and (can_travel player 0) (not (or (>= (+ (+ curr_loc 1) width) (* width height)) (= (nth downWall (+ curr_loc 1)) 1))))) 

    ;southwest
    (= dir 6) (or (and (can_travel player 1) (not (or (= (mod (+ curr_loc width) width) 0) (= (nth rightWall (- (+ curr_loc width) 1)) 1))))
        (and (can_travel player 2) (not (or (>= (+ (- curr_loc 1) width) (* width height)) (= (nth downWall (- curr_loc 1)) 1)))))

    ;northwest
    (= dir 7) (or (and (can_travel player 3) (not (or (= (mod (- curr_loc width) width) 0) (= (nth rightWall (- (- curr_loc width) 1)) 1))))
        (and (can_travel player 2) (not (or (< (- curr_loc 1) width) (= (nth downWall (- (- curr_loc 1) width)) 1)))))

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

;moves the player if he can_travel in the direction

(defn go [dir player]
  (let [loc (player :location)
        width (-> the-maze loc :width)] 
  (if (not (can_travel player dir)) (do (println "Can't go there. There is a wall.") player)
  (cond (= dir 0) (update-in player [:n] inc)
    (= dir 1) (update-in player [:n] + width)
    (= dir 3) (update-in player [:n] - width)
    (= dir 2) (update-in player [:n] dec)

    )))
)

;respond as per the give instruction or direction

;(println "Passed through can_travel, next is display")

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
      (println "=================================")
      (print (-> the-maze loc :title)) 
      (println " (Enter i for instructions)")
      (println "=================================")
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
        (and (or e east)) (println-typing "You cannot move forward. You are at a dead end. You have walls surrounding west, north and south." 40)
        (and (or s south)) (println-typing "You cannot move forward. You are at a dead end. You have walls surrounding west, north and east. " 40)
        (and (or w west)) (println-typing "You cannot move forward. You are at a dead end. You have walls surrounding east, north and south." 40)
        (and (or n north)) (println-typing "You cannot move forward. You are at a dead end. You have walls surrounding west, east and south." 40)
        )) player
)

;(println "Next is print-maze")
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

  (println)
)

;(println "Out of print-maze, next is respawn")
;respawns if eaten by a monster
(defn respawn [player]
  (let [loc (player :location)
        n (-> the-maze loc :n)]

        (println-typing "But, you were eaten by a monster. You are dead!" 20)

        (println)
        (println)

        (println"
                                           ,---._
                                         ,~~,-._  `._
                                          v'~   `-.  `.
                                               _,- ~.  \\
                                             .'  ,--`.  `\\_
                                             V-'~ ,'~~~`-. `-.
                                 ___             /_/~~~) ` `. `._
                        ____,---'   ;            V     `.__ `--, `;
                         `-._    ;  `.                ____)       :
                             `.;  ; .'              ,'        _    |
                              ; |   ;              ,';'~~~`--' `;  :
                             ,': .-'               `,'  __ __   :  |
                             )_  `-, ___     __        (__:__)   ; ;
                        _,---. \\___,'` ~`---;  `-.       |||    ;  ;
                    _,-/   :;     !   `     `|    `-.   |~~~~|   ; :
                _,-' /~   .,'  ;  !!  `..    ``.    `.  :    ;  | :
              ,'  ,-'    .;   `; !!   _,'-' ,--._    ====\\__/===: `.
            .'  ,-'   ,--.  ~~`-. !!  ~    ,'    `     `./  \\   |  |
           .'  :;   ,'    \\        !: .   ;--.__   `;.  |. ~.|   : ;
          .'  ,;    ' ,-'~~`-.     ,!  ;-'     #;   `;. \\____/  : `.
         .'  ,;      /__      `-._,'!!( _(0'~~`-'    `;.  `.     ; ;
        .'  ,;    ,'    `---._(0))  !! ~   _,-,        `;  `.   ;  :
        ;  ,;    ,' ;;-.__,-._~~~   !!__,--::::|.      `;:  :   `; )
        ;  :|   ,'  ;/;;; :::;;;;----'|:: |::;\\/#.      `;  |    ) ;
        :  |:  ,'  ,' :/  :;; \\/))):;;::/  ::' ##:      ;;  ;    ; |
        |  :|  :;  :      `'    \\/ \\/ `'   `'  ##;      ;  .'  ,'  ;
        :  |;  || .'        ;\\.   ____ __,--._ ##;     ;' .'--'   ;
    _,--`. `.  :: `./;   /\\/;:;,-'    `-.     `--.__     .'~   ,'~
   /     ;. `; ``. :::;\\;.-'~~`./~~\\/\\ ..    _  :::  --. ' ,-'~
  /    .  `. `; `   ~~~ ;~      ~~~~~~`--.__~~`-. :::   ) ~
 /'    ;`--`. `. `.    :;      `;       ;   `---`._    ,'
 `.  \\/      `-.` `_,_ `:,-'-. `.      :_,_    ;   `--'
  `.  `.        ` (___)-: ( ) :--,-'- -(___),'~~~`.
   {_  `.               `.___.' ( ; ;)      :((:)):
     `.  `                       `--'       `.___.'
       `. `.                 ;;:::;
        `-  `.              ;;;. .;
          `-. `.__        \\;;; - ;; //
             `. ` `--..___ \\,--v-, //
               `--._   ~~~~~`)____(//
                    )    ~~   ~~~~~~;
                    `.    ~~  ------;
                     `.~~_   ______,'
                      `. `.--';: |:
                       ;  `. Cc; Cc
                       `.  ;  __
                        `. `-'  ~\\
                         `-.__,--'~")
        (println)
        (println)
        (println-typing "Do you want to continue or exit? [Type exit/end to end the game or continue to respawn.]" 10)
        (match (to-keywords (read-line)) 
        [:exit] (do (assoc-in player [:location] :exit ))
        [:end] (do (assoc-in player [:location] :exit  ))
        [:continue] (do (display (assoc-in (assoc-in (assoc-in (assoc-in player [:has-map] false) 
                          [:has-key] false) [:location] loc) [:n] n))) 
        _ (do (println "I don't know what you're saying. You are going to be respawned this room again.") (display (assoc-in (assoc-in (assoc-in (assoc-in player [:has-map] false) 
                          [:has-key] false) [:location] loc) [:n] n)))))
    )

;this function checks if the player can travel in the maze
;(println "Out of respawn, next is check-hit")

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
          )(respawn player) player
  ))
)


(defn move-monsters [maze player]
  (let [loc (player :location)]

        (loop [myIndex 0
              mz maze] 
          (if (= myIndex (-> mz loc :monstn))
            mz
            (recur (inc myIndex) (if (= ((nth (-> mz loc :monsters) myIndex) :index) (- ((nth (-> mz loc :monsters) myIndex) :size) 1)) 
              (assoc-in mz [loc :monsters myIndex :index] 0) (update-in mz [loc :monsters myIndex :index] inc)))
                    
          )
        )
  ) 
)

;(println "Out of check-hit and move-monster, next is move-to")

(defn move-to [player]
  (let [loc (player :location)
    nextmap (-> the-maze loc :nextmap)
    n (-> the-maze nextmap :n)]

    

    (if (= nextmap :exit) (do (the_end) (assoc-in player [:location] nextmap))

    (do 
      (println)(println-typing (-> the-maze nextmap :desc) 10)
      (display (assoc-in (assoc-in (assoc-in (assoc-in player [:has-map] false) [:has-key] false) [:location] nextmap) [:n] n)) 
      )))
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
        )(respawn player) 
      (and (= keyloc (player :n)) (not (player :has-key))) (do (println-typing "There is a key on the floor." 20) (println 
"\n  ad8888888888ba
 dP'         `\"8b,
 8  ,aaa,       \"Y888a     ,aaaa,     ,aaa,  ,aa,
 8  8' `8           \"88baadP\"\"\"\"YbaaadP\"\"\"YbdP\"\"Yb
 8  8   8              \"\"\"        \"\"\"      \"\"    8b
 8  8, ,8         ,aaaaaaaaaaaaaaaaaaaaaaaaddddd88P
 8  `\"\"\"'       ,d8\"\"
 Yb,         ,ad8\"    
  \"Y8888888888P\"\n") (println-typing "Do you want to take it? (y/n)" 20) 
      (match (to-keywords (read-line)) 
        [:yes] (do (println-typing "Key taken!" 20) (assoc-in player [:has-key] true))
        [:y] (do (println-typing "Key taken!" 20) (assoc-in player [:has-key] true))
        _ player))

    (and (= maploc (player :n)) (not (player :has-map))) (do (println-typing "There is a map on the floor." 20)(println     
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

      (println-typing "Do you want to take it? (y/n)" 20) 
      (match (to-keywords (read-line)) 
        [:yes] (do (println-typing "You have obtained a map! to use the map type 'use map'" 20)(assoc-in player [:has-map] true))
        [:y] (do (println-typing "You have obtained a map! to use the map type 'use map'" 20)(assoc-in player [:has-map] true))
        _ player))

     (= exitloc (player :n)) (do (println-typing "You have found the EXIT!!" 20) (println-typing "Do you want to open it? (y/n)" 20) (match (to-keywords (read-line)) 
        [:yes] (do (if (player :has-key) 
          (do (println-typing "You have used your key to unlock the exit door." 20) (move-to player))
          (do (println-typing "It's locked!" 20) player)))
        [:y] (do (if (player :has-key) 
          (do (println-typing "You have used your key to unlock the exit door." 20) (move-to player))
          (do (println-typing "It's locked!" 20) player)))
        _ player))
     :else player)
  ;   (when-not ((player :seen) location)
  ;     (print (-> the-map location :desc)))
    )
  
)
;(println "Out of moveto and status, next is respond")

;responds as per the instructions
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
         [:nothing] (do (println "Good you are doing nothing like always.") player)
         [:use :map] (do (if (player :has-map) (print-maze maze player) (println "You don't have a map yet.")) player)
         _ (do (println "I don't understand you.")
               player)
    )
)

;(println "Out of all the functions, next is main program")

(defn report-monster [maze player]
  (let [loc (player :location)
    n (player :n)
    monster (-> maze loc :monsters)
    monstn (-> maze loc :monstn)
    width (-> maze loc :width)]

    (doseq [i (range monstn)]
      (cond 
        ;east
        (and (can_travel player 8) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) 2)) (println "WARNING: There is a monster east of you")
        (and (can_travel player 0) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) 1))(println "WARNING: Run!! There is a monster east of you")

        ;south
        (and (can_travel player 9) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (* 2 width)))(println "WARNING: There is a monster south of you")
        (and (can_travel player 1) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) width)) (println "WARNING: Run!! There is a monster south of you")

        ;west
        (and (can_travel player 10) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) -2)) (println "WARNING: There is a monster west of you")
        (and (can_travel player 2) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) -1)) (println "WARNING: Run!! There is a monster west of you")
        
        ;north
        (and (can_travel player 11) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (* -2 width))) (println "WARNING: There is a monster north of you")
        (and (can_travel player 3) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (* -1 width))) (println "WARNING: Run!! There is a monster north of you")

        ;northeast
        (and (can_travel player 4) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (+ (* -1 width) 1))) (println "WARNING: There is a monster northeast of you")

        ;northwest
        (and (can_travel player 7) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (- (* -1 width) 1))) (println "WARNING: There is a monster northwest of you")

        ;southeast
        (and (can_travel player 5) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (+ width 1))) (println "WARNING: There is a monster southeast of you")
        
        ;southwest
        (and (can_travel player 6) (= (- (nth ((nth monster i) :path) ((nth monster i) :index)) (player :n)) (- width 1))) (println "WARNING: There is a monster southwest of you")

        
        )
    )))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println)


  (println-typing " 

 ___      ___           __                                    __          
/   \\    /   \\  ____   |  |   ____   ____   _____   ____    _/  |_   ___  
\\    \\/\\/   /  / __ \\  |  |  / ___\\ /  _ \\ /     \\_/  __\\  |      \\ /  _ \\ 
 \\         /  \\  ___/  |  |_ \\ \\__  ( <_> )  Y Y  \\  ___/    |  |  (  <_> )
  \\__/\\   /    \\___ >  |____/ \\___ > ____/ |__| _| /\\___ >   |__|   \\____/ 
       \\_/        \\/             \\/              \\/   \\/                " 5)


  (println-typing "                              
 _________________________________________________________________________
|                                                                         |
|         === = = ===   .-. .-. .=. .== .-. .  '. .' .--. .-= .==         |
|          |  |=| |=    |=| |=' | | |   |=| |    |   |--' `-. |=          |
|          =  = = ===   = = =   `=' `== = = `==  =   =    =-' `==         |
|_________________________________________________________________________|" 
5)
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
| Have fun! and let us know if you have any feedback at sbansal6@illinois.edu |
| or dejudom2@illinois.edu.                                                   |
|_____________________________________________________________________________|" 5)
  
  (def hero (assoc-in adventurer [:n] ((the-maze (adventurer :location)) :n)))
  (println)
  (println-typing ((the-maze (adventurer :location)) :desc) 10)
  (loop [local-maze the-maze
         local-player hero]
         ;(println "hello")
        (let [pl (check-hit local-maze local-player)
              ;_ (println "hello2")
              mz (move-monsters local-maze pl)
              pll (status mz pl)
              ]
              (if(not (= (pll :location) :exit))(let [
                ;_ (print-maze mz pll)
              _ (report-monster mz pll)
              _ (println-typing "What do you want to do now?" 20)
          command (read-line)]
          
      (recur mz (respond mz pll (to-keywords command)))) "no" ))))

;(println "Compiled everything, WTF, Hi You are fucked because I am out of main and you still have the problem")