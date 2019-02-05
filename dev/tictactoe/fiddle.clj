(ns tictactoe.fiddle
  "Playground namespace to test things.
  Maybe this is a better solution than the (comment) forms in actual source code."
  (:require [tictactoe.core :as tttc]))

(tttc/cell->str nil)

(tttc/print-board [:x nil :x :o :o :o nil nil :x] 3)

(def MY_GAME (tttc/new-game 10))
