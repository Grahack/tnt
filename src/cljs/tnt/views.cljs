(ns tnt.views
  (:require
   [re-frame.core :as re-frame]
   [clojure.string]
   ["react-sheet-music" :as r-s-m :refer [default]]
   ))

(defn score [& lines]
  [:> default {:notation (clojure.string/join "\n" lines)}])

(defn main-panel []
  (let [rq  ["a4" "qr"] ; rest quarter
        r8  ["a4" "8r"] ; rest eighth
        hq   "b4"       ; hit  quarter
        h8  ["b4"  8]   ; hit  eighth
        h16 ["b4" 16]   ; hit  sixteenth
        h32 ["b4" 32]   ; hit  thirty-seconds
        xq   "x"        ; cross (unaccented)
        x8  ["x"  8]
        x16 ["x" 16]
        x32 ["x" 32]
        l1 [3 3 2 3 2 4 1 4 1 4 2 3]
        l2 [1 2 1 2 1 1 1 3 4 1 3 3 1 3 2 1 2]]
    [:div
      [:h1 "Exo TNT"]
      [:h2 "Histoire"]
      [:p "A.O. (aka TNT) a partagé une feuille de type « Ago ». "
          "Il était loin de se douter des implications de ce geste. "
          "Cette page pousse le bouchon, tout en ne partant pas en cacahuète."]
      [:p "Voici la phrase de base (l’originale était notée avec des noires "
         "pointées et des liaisons) :"]
      (score "L:1/8" "M:4/4"
             "|d2 zd z2 d2|d2 zd zd z2|zd d2 z2 dd|z2 zd zd z2|"
             "|dd zd d2 dd|dd z2 d2 z2|dd z2 d2 zd|d2 zd zd d2|"
      [:p "On peut aussi voir cette phrase comme deux listes de durées : "
          (map str l1) " et " (map str l2) ", ce qui va nous aider à "
          "l’orchestrer."]

      [:h2 "Mode d’emploi"]
      [:p "On a trois grandes sections : "
       [:a {:href "#debits"} "« les débits »"]
       " qui organisent la phrase sur des subdivisions de la pulsation, "
       [:a {:href "#au-pad"} "« au pad »"]
       " qui propose des doigtés (droite ou gauche) sur les débits, "
       [:a {:href "#sur-le-kit"} "« sur le kit »"]
       " qui propose des orchestrations sur le kit."]

      [:h2 {:id "debits"} [:a {:href "#debits"} "Débits"]]
      [:h3 "2 - Croches"]
      [:p "Deux notes par pulsation, c’est le débit de la phrase de départ, "
          "que l’on recopie ici en ajoutant des croches non accentuées "
          "dans les silences."]
      ;(score "L:1/8" "M:4/4"
      ;       "| h8 x8 x8 h8 x8 x8 h8 x8 |"
      ;       "| h8 x8 x8 h8 x8 h8 x8 x8 |"
      ;       "| x8 h8 h8 x8 x8 x8 h8 h8 |"
      ;       "| x8 x8 x8 h8 x8 h8 x8 x8 |"
      ;       "| h8 h8 x8 h8 h8 x8 h8 h8 |"
      ;       "| h8 h8 x8 x8 h8 x8 x8 x8 |"
      ;       "| h8 h8 x8 x8 h8 x8 x8 h8 |"
      ;       "| h8 x8 x8 h8 x8 h8 h8 x8 |")

      [:h4 "2.2 - Doubles croches"]
      [:p "Quatre notes par pulsation. Soit on ajoute une note sans accent "
          "entre les croches :"]
      ;(score "L:1/8" "M:4/4"
      ;       "| h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 |"
      ;       "| h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 h16 x16 x16 x16 x16 x16 |"
      ;       "| x16 x16 h16 x16 h16 x16 x16 x16 x16 x16 x16 x16 h16 x16 h16 x16 |"
      ;       "| x16 x16 x16 x16 x16 x16 h16 x16 x16 x16 h16 x16 x16 x16 x16 x16 |"
      ;       "| h16 x16 h16 x16 x16 x16 h16 x16 h16 x16 x16 x16 h16 x16 h16 x16 |"
      ;       "| h16 x16 h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 x16 x16 x16 x16 |"
      ;       "| h16 x16 h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 x16 x16 h16 x16 |"
      ;       "| h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 h16 x16 h16 x16 x16 x16 |")

      [:p "Soit on joue « deux fois plus vite » par rapport à la pulsation :"]
      ;(score "L:1/8" "M:4/4"
      ;       "| h16 x16 x16 h16 x16 x16 h16 x16 
      ;       "| h16 x16 x16 h16 x16 h16 x16 x16]
      ;       "| x16 h16 h16 x16 x16 x16 h16 h16
      ;       "| x16 x16 x16 h16 x16 h16 x16 x16]
      ;       "| h16 h16 x16 h16 h16 x16 h16 h16
      ;       "| h16 h16 x16 x16 h16 x16 x16 x16]
      ;       "| h16 h16 x16 x16 h16 x16 x16 h16
      ;       "| h16 x16 x16 h16 x16 h16 h16 x16])

      [:h4 "2.4 - Triples croches"]
      [:p "Huit notes par pulsation. Soit on ajoute une note sans accent "
          "entre les doubles croches de chacune des versions ci-dessus :"]
      ;(score "L:1/8" "M:4/4"
      ;       [h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
      ;        x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32]
      ;       [h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
      ;        x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32]
      ;       [x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32
      ;        x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32]
      ;       [x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
      ;        x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32]
      ;       [h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
      ;        h32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32]
      ;       [h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32
      ;        h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32]
      ;       [h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32
      ;        h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32]
      ;       [h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
      ;        x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32])

      [:p "ou"]
      ;(score "L:1/8" "M:4/4"
      ;       [h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 h32 x32 x32 x32
      ;        h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32]
      ;       [x32 x32 h32 x32 h32 x32 x32 x32 x32 x32 x32 x32 h32 x32 h32 x32
      ;        x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32]
      ;       [h32 x32 h32 x32 x32 x32 h32 x32 h32 x32 x32 x32 h32 x32 h32 x32
      ;        h32 x32 h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32]
      ;       [h32 x32 h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 h32 x32
      ;        h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 h32 x32 x32 x32])

      ;[:p "Soit on joue « deux fois plus vite »."]
      ;(score "L:1/8" "M:4/4"
      ;       [h32 x32 x32 h32 x32 x32 h32 x32
      ;        h32 x32 x32 h32 x32 h32 x32 x32
      ;        x32 h32 h32 x32 x32 x32 h32 h32
      ;        x32 x32 x32 h32 x32 h32 x32 x32]
      ;       [h32 h32 x32 h32 h32 x32 h32 h32
      ;        h32 h32 x32 x32 h32 x32 x32 x32
      ;        h32 h32 x32 x32 h32 x32 x32 h32
      ;        h32 x32 x32 h32 x32 h32 h32 x32])
      ;[:p "On ne va pas plus loin : pas de quadruple croche dans ce document."]

      ;[:h4 "2.3 - Sextolets, 2 groupes de 3"]
      ;[:p "Le contre-temps est sur le quatrième sextolet, comme si on mettait "
      ;    "trois notes dans chaque croche de la phrase de départ."]
      ;(score "L:1/8" "M:4/4"
      ;       [h8 x8 x8 h8 x8 x8 h8 x8]
      ;       [h8 x8 x8 h8 x8 h8 x8 x8]
      ;       [x8 h8 h8 x8 x8 x8 h8 h8]
      ;       [x8 x8 x8 h8 x8 h8 x8 x8]
      ;       [h8 h8 x8 h8 h8 x8 h8 h8]
      ;       [h8 h8 x8 x8 h8 x8 x8 x8]
      ;       [h8 h8 x8 x8 h8 x8 x8 h8]
      ;       [h8 x8 x8 h8 x8 h8 h8 x8])
      [:h4 "2.s3 - Swing sur les triolets"]
      [:h4 "2.s5 - Swing sur les quintolets"]
      [:h3 "3 - Triolets"]
      [:h4 "3.2 - Sextolets, 3 groupes de 2"]
      [:h4 "3.3 - Trois groupes de 3"]
      [:h3 "5 - Quintolets"]
      [:h2 {:id "au-pad"}[:a {:href "#au-pad"} "Au pad"]]
      [:h2 {:id "sur-le-kit"}[:a {:href "#sur-le-kit"} "Sur le kit"]]

      [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br]
      [:br] [:br] [:br] [:br] [:br] ]))
