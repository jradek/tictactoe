(ns tictactoe.core
  (:gen-class))

(defn tracker [board-size]
  (let [values (vec (replicate board-size 0))]
    {:rows  values
     :cols  values
     :diag  0
     :rdiag 0}))


(defn create-game [board-size]
  {:board          (vec (replicate (* board-size board-size) nil))
   :board-size     board-size
   :current-player nil
   :win-tracker-x  (tracker board-size)
   :win-tracker-o  (tracker board-size)})


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


;; (print-board [:x nil :x :o :o :o nil nil :x] 3)


;(let [g (create-game 3)
;      g2 (assoc-in g [:board 4] :x)))
;  (tc/print-board (:board g2) (:board-size g)))

(defn row-col->index
  "Convert `row` `col` coordinates of board to linear index"
  [row col board-size]
  (+ (* row board-size) col))


(defn mark-position [board bs r c player]
  (assoc board (row-col->index r c bs) player))


(defn update-diag?
  "Check if row `r` and col `c` are on diagonal and if so update tracker `t`"
  ; TODO: is a tracker really required here?
  ; TODO: This 'else' branch looks ugly, there needs to be a better way
  [t r c]
  (if (= r c)
    (update t :diag inc)
    t))


(defn update-rdiag? [t r c bs]
  "Check if row `r` and col `c` are on reverse diagonal of board size `b`
  and if so update tracker `t`"
  (if (= (+ r c) (dec bs))
    (update t :rdiag inc)
    t))


(defn update-win-tracker
  "Update win tracker `tracker` with row `r` and col `c` of board size `bs`"
  [tracker r c bs]
  (-> tracker
      (update :rows #(update % r inc))
      (update :cols #(update % c inc))
      (update-diag? r c)
      (update-rdiag? r c bs)))


(defn track-winner
  "Track the winner for current player in `game`"
  [{:keys [current-player board-size] :as game} row col]
  ; select the correct field in game ...
  (let [field (current-player {:x :win-tracker-x :o :win-tracker-o})]
    ; and update that tracker
    (update game field update-win-tracker row col board-size)))


(defn winning-condition?
  "Check if `tracker` of `board-size` has winning condition"
  [tracker board-size]
  (or (>= (reduce max (:rows tracker)) board-size)
      (>= (reduce max (:cols tracker)) board-size)
      (>= (:diag tracker) board-size)
      (>= (:rdiag tracker)) board-size))


(defn turn [game row col]
  (let [player (:current-player game)
        board-size (:board-size game)
        new-board (mark-position (:board game) board-size row col player)]
    (assoc game :board new-board
                ; switch player
                :current-player (player {:x :o :o :x}))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
