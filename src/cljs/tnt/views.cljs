(ns tnt.views
  (:require
   [re-frame.core :as re-frame]
   [clojure.string]
   ["@grahack/react-sheet-music-mirror" :as r-s-m :refer [default]]
   ))

(defn score [id & lines]
  (let [first-line (first lines)
        with-meter (if (clojure.string/starts-with? first-line "M:")
                       lines
                       (cons "M:4/4" lines))
        flat (flatten with-meter)
        with-accents (map #(clojure.string/replace % "X" "!accent!d") flat)
        final-lines with-accents]
    [:> default {:id id :notation (clojure.string/join "\n" final-lines)}]))

(defn space-join [lst]
  (clojure.string/join " " lst))

(defn pipe-surround [txt]
  (str "|" txt "|"))

(defn phrase [per-lines d u t r]  ; down up two rest
  (let [measures [[d u r d] [d u u r] [u d r t] [r u u r]
                  [t u d t] [t r d r] [t r d u] [d u u d]]]
    (->> measures (partition per-lines)
                  (map #(clojure.string/join "|" (map space-join %)))
                  (map pipe-surround)
                  (clojure.string/join "\n"))))

(defn main-panel []
  (let [l1 [3 3 2 3 2 4 1 4 1 4 2 3]
        l2 [1 2 1 2 1 1 1 3 4 1 3 3 1 3 2 1 2]]
    [:div
      [:div {:id "links"}
       [:a {:href "mailto:christophegragnic@gmail.com"} "Email"] " - "
       [:a {:href "https://github.com/grahack/tnt" :target "_blank"} "Github"]]
      [:h1 "Exo TNT"]
      [:h2 "Histoire"]
      [:p "A.O. (aka TNT) a partagé une feuille de type « Ago ». "
          "Il était loin de se douter des implications de ce geste. "
          "Cette page pousse le bouchon, tout en ne partant pas en cacahuète."]
      [:p "Voici la phrase de base (l’originale était notée avec des noires "
         "pointées et des liaisons) :"]
      (score "phrase"
             "L:1/8"
             (phrase 4 "d2" "zd" "dd" "z2"))



      [:p "On peut aussi voir cette phrase comme deux listes de durées : "
          (map str l1) " et " (map str l2) ", ce qui va nous aider à "
          "trouver des doigtés sur le pad et à l’orchestrer sur le kit."]

      [:h2 {:id "mode-d-emploi"} "Mode d’emploi"]
      [:p "On a trois grandes sections : "
       [:a {:href "#debits"} "« les débits »"] ", "
       [:a {:href "#au-pad"} "« au pad »"] ", "
       [:a {:href "#sur-le-kit"} "« sur le kit »"] "."]

      [:p [:a {:href "#debits"} "Les débits"]
       " sont des manières abstraites (mais musicales) de réorganiser "
       "la phrase. Différentes subdivisions sont utilisées et "
       "les nouvelles phrases sont présentées plutôt dans l’ordre "
       "de la sections suivante."]
      [:p "La section " [:a {:href "#au-pad"} "au pad"]
       " propose des doigtés sur les débits de la section précédente. "
       "Ils sont pour l’instant pour les droitiers, "
       "contactez-moi pour les versions pour gaucher."]
      [:p "Les orchestrations " [:a {:href "#sur-le-kit"} "sur le kit"]
       " proposent des orchestrations sur le kit de batterie des phrases "
       "de la section précédente."]

      [:p "Dans ce document, les pulsations sont écrites avec des noires."]

      [:h2 {:id "debits"} [:a {:href "#debits"} "Débits"]]
      [:h3 "2 - Croches"]
      [:p "Deux notes par pulsation, c’est le débit de la phrase de départ, "
          "que l’on recopie ici en ajoutant des croches non accentuées "
          "dans les silences."]
      (score "croches"
             "L:1/8"
             (phrase 4 "Xd" "dX" "XX" "dd"))

      [:h4 "2.2 - Doubles croches"]
      [:p "Quatre notes par pulsation. Soit on ajoute une note sans accent "
          "entre les croches :"]
      (score "doubles-croches"
             "L:1/16"
             (phrase 4 "Xddd" "ddXd" "XdXd" "dddd"))


      [:p "Soit on joue « deux fois plus vite » par rapport à la pulsation :"]
      (score "doubles-croches-deux-fois-plus-vite"
             "L:1/16"
             "|XddX ddXd XddX dXdd|dXXd ddXX dddX dXdd|"
             "|XXdX XdXX XXdd Xddd|XXdd XddX XddX dXXd|")

      [:h4 "2.4 - Triples croches"]
      [:p "Huit notes par pulsation. Soit on ajoute une note sans accent "
          "entre les doubles croches de chacune des versions ci-dessus :"]
      (score "triples-croches"
             "L:1/32"
             (phrase 2 "Xddddddd" "ddddXddd" "XdddXddd" "dddddddd"))

      [:p "ou"]
      (score "triples-croches-deux-fois-plus-vite"
             "L:1/32"
              (str "|XdddddXd ddddXddd XdddddXd ddXddddd"
                   "|ddXdXddd ddddXdXd ddddddXd ddXddddd|")
              (str "|XdXdddXd XdddXdXd XdXddddd Xddddddd"
                   "|XdXddddd XdddddXd XdddddXd ddXdXddd|"))

      [:p "Soit on joue « deux fois plus vite » que les doubles-croches."]
      (score "triples-croches-quatre-fois-plus-vite"
             "L:1/32"
             (str "|XddXddXd XddXdXdd dXXdddXX dddXdXdd"
                  "|XXdXXdXX XXddXddd XXddXddX XddXdXXd|"))

      [:p "On ne va pas plus loin : pas de quadruple croche dans ce document."]

      [:h4 "2.3 - Sextolets, 2 groupes de 3"]
      [:p "Le contre-temps est sur le quatrième sextolet, comme si on mettait "
          "trois notes dans chaque croche de la phrase de départ."]
      (score "sextolets-deux-groupes-de-trois"
             "L:1/16"
             (phrase 4 "(6Xddddd" "(6dddXdd" "(6XddXdd" "(6dddddd"))

      [:h4 "2.s3 - Swing sur les triolets"]
      [:p "Triolets purs dont une note n’est pas jouée, "
          "ici par exemple noté en 12:8."]
      (score "swing-sur-triolets"
             "M:12/8"
             "L:1/8"
             (phrase 4 "d3" "(3::2z2d" "(3dzd" "z3"))

      [:h4 "2.s4 - Swing sur les doubles croches"]
      [:p "Doubles croches dont deux notes ne sont pas jouées."]
      (score "swing-sur-double-croches"
             "L:1/16"
             (phrase 4 "d4" "z3d" "dz2d" "z4"))

      [:h4 "2.s5 - Swing sur les quintolets"]
      [:p "Quintolets purs dont trois notes ne sont pas jouées."]
      (score "swing-sur-quintolets"
             "L:1/16"
             (phrase 4 "d4" "(5::3z3dz" "(5::4dz2dz" "z4"))

      [:h3 "3 - Triolets"]
      [:p "On pourrait aussi mettre le contre-temps sur la deuxième "
          "croche, mais ce n’est pas traité dans ce document."]
      (score "triolets"
             "L:1/8"
             (str "|(3Xdd (3ddX (3ddd (3Xdd|(3Xdd (3ddX (3ddX (3ddd"
                  "|(3ddX (3Xdd (3ddd (3XdX|(3ddd (3ddX (3ddX (3ddd|")
             (str "|(3XdX (3ddX (3Xdd (3XdX|(3XdX (3ddd (3Xdd (3ddd"
                  "|(3XdX (3ddd (3Xdd (3ddX|(3Xdd (3ddX (3ddX (3Xdd|"))

      [:h4 "3.2 - Sextolets, 3 groupes de 2"]
      [:p "Le contre-temps est sur le cinquième sextolet, comme si on mettait "
          "deux notes dans chaque croche de la phrase interprétée en triolets "
          "ci-dessus."]
      (score "sextolets-trois-groupes-de-deux"
             "L:1/16"
             (str "|(6Xddddd (6ddddXd (6dddddd (6Xddddd"
                  "|(6Xddddd (6ddddXd (6ddddXd (6dddddd|")
             (str "|(6ddddXd (6Xddddd (6dddddd (6XdddXd"
                  "|(6dddddd (6ddddXd (6ddddXd (6dddddd|")
             (str "|(6XdddXd (6ddddXd (6Xddddd (6XdddXd"
                  "|(6XdddXd (6dddddd (6Xddddd (6dddddd|")
             (str "|(6XdddXd (6dddddd (6Xddddd (6ddddXd"
                  "|(6Xddddd (6ddddXd (6ddddXd (6Xddddd|"))

      [:h4 "3.d - Sextolets, deux fois plus vite"]
      [:p "On joue « deux fois plus vite » la phrase version triolets."]
      (score "sextolets-deux-fois-plus-vite"
             "L:1/16"
             (str "|(6XddddX (6dddXdd (6XddddX (6ddXddd"
                  "|(6ddXXdd (6dddXdX (6dddddX (6ddXddd|")
             (str "|(6XdXddX (6XddXdX (6XdXddd (6Xddddd"
                  "|(6XdXddd (6XddddX (6XddddX (6ddXXdd|"))

      [:h4 "3.3 - Trois groupes de 3"]
      [:p "On part de la phrase en triolets et on ajoute deux notes "
          "sur chaque croche."]
      (score "trois-groupes-de-trois"
             "M:9/8"
             "L:1/8"
              (str "|(3Xdd (3ddd (3ddd|(3ddd (3ddd (3Xdd"
                   "|(3ddd (3ddd (3ddd|(3Xdd (3ddd (3ddd|")
              (str "|(3Xdd (3ddd (3ddd|(3ddd (3ddd (3Xdd"
                   "|(3ddd (3ddd (3Xdd|(3ddd (3ddd (3ddd|")
              (str "|(3ddd (3ddd (3Xdd|(3Xdd (3ddd (3ddd"
                   "|(3ddd (3ddd (3ddd|(3Xdd (3ddd (3Xdd|")
              (str "|(3ddd (3ddd (3ddd|(3ddd (3ddd (3Xdd"
                   "|(3ddd (3ddd (3Xdd|(3ddd (3ddd (3ddd|")
              (str "|(3Xdd (3ddd (3Xdd|(3ddd (3ddd (3Xdd"
                   "|(3Xdd (3ddd (3ddd|(3Xdd (3ddd (3Xdd|")
              (str "|(3Xdd (3ddd (3Xdd|(3ddd (3ddd (3ddd"
                   "|(3Xdd (3ddd (3ddd|(3ddd (3ddd (3ddd|")
              (str "|(3Xdd (3ddd (3Xdd|(3ddd (3ddd (3ddd"
                   "|(3Xdd (3ddd (3ddd|(3ddd (3ddd (3Xdd|")
              (str "|(3Xdd (3ddd (3ddd|(3ddd (3ddd (3Xdd"
                   "|(3ddd (3ddd (3Xdd|(3Xdd (3ddd (3ddd|"))

      [:h3 "5 - Quintolets"]
      (score "quintolets"
             "L:1/16"
             (str "|(5Xdddd (5dddXd (5ddddd (5Xdddd"
                  "|(5Xdddd (5dddXd (5dddXd (5ddddd|")
             (str "|(5dddXd (5Xdddd (5ddddd (5XddXd"
                  "|(5ddddd (5dddXd (5dddXd (5ddddd|")
             (str "|(5XddXd (5dddXd (5Xdddd (5XddXd"
                  "|(5XddXd (5ddddd (5Xdddd (5ddddd|")
             (str "|(5XddXd (5ddddd (5Xdddd (5dddXd"
                  "|(5Xdddd (5dddXd (5dddXd (5Xdddd|"))

      [:h2 {:id "au-pad"}[:a {:href "#au-pad"} "Au pad"]]
      [:h3 "Frisé, roulés et flas"]
      [:p "Pour chaque débit proposé ci-dessus en croches ou triolets de "
          "croches (sauf peut-être le « trois groupes de trois »), "
          "on peut jouer en « frisé » (alternance des mains), "
          "on peut « rouler » les notes non " "accentuées, "
          "et on peut jouer des « flas » sur les accents. "
          [:br]
          "Voici ces trois versions pour « croches » et « triolets "
          "de croches »."]
      ; TODO
             ; croches frisé
             ; croches roulés
             ; croches flas
             ; triolets de croches frisé
             ; triolets de croches roulés
             ; triolets de croches flas

      [:h2 {:id "sur-le-kit"}[:a {:href "#sur-le-kit"} "Sur le kit"]]
      [:p "TODO"]

      [:h2 {:id "bonus"}[:a {:href "#bonus"} "Bonus"]]
      [:p "En bonus, voici la phrase de départ, jouée "
          " « deux fois plus lentement » par rapport à la pulsation. "
          "Version « phrase » puis version « débit »."]
      (score "noire-phrase"
             "M:8/4"
             "L:1/4"
             (phrase 4 "d2" "zd" "dd" "z2"))

      (score "noire-débit"
             "M:8/4"
             "L:1/4"
             "|Xd dX dd Xd|Xd dX dX dd|dX Xd dd XX|dd dX dX dd|"
             "|XX dX Xd XX|XX dd Xd dd|XX dd Xd dX|Xd dX dX Xd|")

      [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br]
      [:br] [:br] [:br] [:br] [:br] ]))
