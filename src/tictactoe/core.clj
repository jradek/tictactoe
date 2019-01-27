(ns tictactoe.core
  (:require [clojure.string])
  (:gen-class))

(defn new-tracker [board-size]
  (let [values (vec (replicate board-size 0))]
    {:rows  values
     :cols  values
     :diag  0
     :rdiag 0}))


(defn new-game [board-size]
  {:board          (vec (replicate (* board-size board-size) nil))
   :board-size     board-size
   :current-player :x
   :turns          []
   :win-tracker-x  (new-tracker board-size)
   :win-tracker-o  (new-tracker board-size)})

;--------------------------------------------------------------------
; PRINTING
;--------------------------------------------------------------------

(defn cell->str
  "Convert board cell value `c` to string"
  [c]
  (condp = c
    nil " "
    :x "X"
    :o "O"))


(defn row->str [row]
  (reduce str (interpose " | " (map cell->str row))))


(defn print-board
  "print `board` of size `board-size` * `board-size`"
  [board board-size]
  (let [row-strs (map row->str (partition board-size board))
        spacer (str (reduce str (replicate (dec board-size) "--+-")) "-")
        lines (interpose spacer row-strs)]
    (doseq [l lines]
      (println l))))

;(print-board [:x nil :x :o :o :o nil nil :x] 3)

(defn print-board-compact
  "print `board` of size `board-size` * `board-size` compact"
  [board board-size]
  (let [lines (map
                ;#(apply str %)
                clojure.string/join
                   (partition board-size (map cell->str board)))]
    (doseq [l lines]
      (println l))))


;(print-board-compact [:x nil :x :o :o :o nil nil :x] 3)


;--------------------------------------------------------------------
; LOGIC
;--------------------------------------------------------------------

(defn row-col->index
  "Convert `row` `col` coordinates of board to linear index"
  [row col board-size]
  (+ (* row board-size) col))


(defn mark-position [board bs r c player]
  (assoc board (row-col->index r c bs) player))


(defn update-diag?
  "Check if row `r` and col `c` are on diagonal and if so update tracker `val`."
  [val r c]
  (if (= r c)
    (inc val)
    val))


(defn update-rdiag?
  "Check if row `r` and col `c` are on reverse diagonal of board size `b`
  and if so update `val`."
  [val r c bs]
  (if (= (+ r c) (dec bs))
    (inc val)
    val))


(defn update-win-tracker
  "Update win tracker `tracker` with row `r` and col `c` of board size `bs`"
  [tracker r c bs]
  (-> tracker
      (update :rows #(update % r inc))
      (update :cols #(update % c inc))
      (update :diag update-diag? r c)
      (update :rdiag update-rdiag? r c bs)))


;(update-win-tracker
;  {:rows [0 0 0] :cols [0 0 0] :diag 0 :rdiag 0}
;  1 1 3)

(defn winning-condition?
  "Check if `tracker` of `board-size` has winning condition"
  [tracker board-size]
  (or (>= (reduce max (:rows tracker)) board-size)
      (>= (reduce max (:cols tracker)) board-size)
      (>= (:diag tracker) board-size)
      (>= (:rdiag tracker) board-size)))

(comment
  (winning-condition? {:rows [0 0 0] :cols [0 0 0] :diag 0 :rdiag 0} 3))


(defn turn [game row col]
  (let [player (:current-player game)
        bs (:board-size game)
        board (:board game)
        turns (:turns game)]
    (-> game
        ; update board
        (assoc :board (mark-position board bs row col player))
        ; record the turns
        (assoc :turns (conj turns {:p player :r row :c col}))
        ; track the winner
        (cond->
          (= player :x)
          (update :win-tracker-x update-win-tracker row col bs)

          (= player :o)
          (update :win-tracker-o update-win-tracker row col bs))
        ; switch player
        (assoc :current-player (player {:x :o :o :x})))))


;--------------------------------------------------------------------
; SIMULATION
;--------------------------------------------------------------------

(comment
  (def GAME (new-game 3)))

(comment
  ; simulated turns
  (def TURNS [[:x 0 0] [:x 1 1] [:x 2 2] [:o 0 2]]))

(comment
  (defn simulate-turn [g [player row col]]
    (-> g
        ; set the player, independent from actual rules
        (assoc :current-player player)
        ; make the turn
        (turn row col))))

(comment
  (defn show-game-state [{:keys [turns board] :as g}]
    (let [n (count turns)]
      (println "- Round: " n " -------------")
      (if (pos? n)
        ; fetch previous player from state, because round is already played
        (println "Player " (cell->str (:p (turns (dec n)))) " played")))
    (print-board-compact board 3)
    (when (winning-condition? (:win-tracker-x g) (:board-size g))
      (println "X won!!!"))
    (when (winning-condition? (:win-tracker-o g) (:board-size g))
      (println "O won!!!"))))
  ;(clojure.pprint/pprint g))

(comment
  ; play the game
  (map show-game-state (reductions simulate-turn GAME TURNS)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->> args
       (interpose " ")
       (apply str)
       (println "Executed with the following args: ")))
