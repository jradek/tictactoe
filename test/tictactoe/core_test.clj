(ns tictactoe.core-test
  (:require [clojure.test :refer :all]
            [tictactoe.core :refer :all]))

(deftest diagonal-test
  (testing "diagonal"
    (is (= 1 (update-diag? 0 0 0)))
    (is (= 1 (update-diag? 0 1 1)))
    ; no diagonal
    (is (= 0 (update-diag? 0 1 0)))
    (is (= 0 (update-diag? 0 0 1)))))


(deftest reverse-diagonal-test
  (testing "reverse diagonal"
    (is (= 1 (update-rdiag? 0 2 0 3)))
    (is (= 1 (update-rdiag? 0 1 1 3)))
    (is (= 1 (update-rdiag? 0 0 2 3)))
    ; no reverse diagonal
    (is (= 0 (update-rdiag? 0 0 0 3)))
    (is (= 0 (update-rdiag? 0 2 2 3)))))


(deftest wining-condition-test
  (testing "Wining condition"
    (is (not (winning-condition?
               {:rows [0 0 0] :cols [0 0 0] :diag 0 :rdiag 0} 3)))
    (is (not (winning-condition?
               {:rows [1 1 1] :cols [0 0 0] :diag 0 :rdiag 0} 3)))
    (is (not (winning-condition?
               {:rows [0 0 0] :cols [0 0 0] :diag 2 :rdiag 1} 3)))
    (is (winning-condition?
          {:rows [3 0 0] :cols [0 0 0] :diag 0 :rdiag 0} 3))
    (is (winning-condition?
          {:rows [0 0 0] :cols [0 3 0] :diag 0 :rdiag 0} 3))
    (is (winning-condition?
          {:rows [0 0 0] :cols [0 0 0] :diag 0 :rdiag 3} 3))))
