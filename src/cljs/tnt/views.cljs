(ns tnt.views
  (:require
   [re-frame.core :as re-frame]
   [clojure.string]
   ["react-sheet-music" :as r-s-m :refer [default]]
   ))

(defn score [& lines]
  (let [with-wedges (map #(clojure.string/replace % "X" "!wedge!d") lines)
        final-lines with-wedges]
    [:> default {:notation (clojure.string/join "\n" final-lines)}]))

(defn main-panel []
  (let [l1 [3 3 2 3 2 4 1 4 1 4 2 3]
        l2 [1 2 1 2 1 1 1 3 4 1 3 3 1 3 2 1 2]]
    [:div
      [:h1 "Exo TNT"]
      [:h2 "Histoire"]
      [:p "A.O. (aka TNT) a partagé une feuille de type « Ago ». "
          "Il était loin de se douter des implications de ce geste. "
          "Cette page pousse le bouchon, tout en ne partant pas en cacahuète."]
      [:p "Voici la phrase de base (l’originale était notée avec des noires "
         "pointées et des liaisons) :"]
      (score "M:4/4"
             "L:1/8"
             "|d2 zd z2 d2|d2 zd zd z2|zd d2 z2 dd|z2 zd zd z2|"
             "|dd zd d2 dd|dd z2 d2 z2|dd z2 d2 zd|d2 zd zd d2|"

             "L:1/8"
             "|Xd dX dd Xd|Xd dX dX dd|dX Xd dd XX|dd dX dX dd|"
             "|XX dX Xd XX|XX dd Xd dd|XX dd Xd dX|Xd dX dX Xd|"

             "L:1/16"
             (str "|Xddd ddXd dddd Xddd|Xddd ddXd ddXd dddd"
                  "|ddXd Xddd dddd XdXd|dddd ddXd ddXd dddd|")
             (str "|XdXd ddXd Xddd XdXd|XdXd dddd Xddd dddd"
                  "|XdXd dddd Xddd ddXd|Xddd ddXd ddXd Xddd|")

             "L:1/16"
             "|XddX ddXd XddX dXdd|dXXd ddXX dddX dXdd|"
             "|XXdX XdXX XXdd Xddd|XXdd XddX XddX dXXd|"

             "L:1/32"
             (str "|Xddddddd ddddXddd dddddddd Xddddddd"
                  "|Xddddddd ddddXddd ddddXddd dddddddd|")
             (str "|ddddXddd Xddddddd dddddddd XdddXddd"
                  "|dddddddd ddddXddd ddddXddd dddddddd|")
             (str "|XdddXddd ddddXddd Xddddddd XdddXddd"
                  "|XdddXddd dddddddd Xddddddd dddddddd|")
             (str "|XdddXddd dddddddd Xddddddd ddddXddd"
                  "|Xddddddd ddddXddd ddddXddd Xddddddd|")

             "L:1/32"
              (str "|XdddddXd ddddXddd XdddddXd ddXddddd"
                   "|ddXdXddd ddddXdXd ddddddXd ddXddddd|")
              (str "|XdXdddXd XdddXdXd XdXddddd Xddddddd"
                   "|XdXddddd XdddddXd XdddddXd ddXdXddd|")
             
             "L:1/32"
             (str "|XddXddXd XddXdXdd dXXdddXX dddXdXdd"
                  "|XXdXXdXX XXddXddd XXddXddX XddXdXXd|")

             "L:1/16"
             (str "|(6Xddddd (6dddXdd (6dddddd (6Xddddd"
                  "|(6Xddddd (6dddXdd (6dddXdd (6dddddd"
                  "|(6dddXdd (6Xddddd (6dddddd (6XddXdd"
                  "|(6dddddd (6dddXdd (6dddXdd (6dddddd|")
             (str "|(6XddXdd (6dddXdd (6Xddddd (6XddXdd"
                  "|(6XddXdd (6dddddd (6Xddddd (6dddddd"
                  "|(6XddXdd (6dddddd (6Xddddd (6dddXdd"
                  "|(6Xddddd (6dddXdd (6dddXdd (6Xddddd|")

             "L:1/8"
             (str "|(3Xzd (3dzX (3dzd (3Xzd|(3Xzd (3dzX (3dzX (3dzd"
                  "|(3dzX (3Xzd (3dzd (3XzX|(3dzd (3dzX (3dzX (3dzd|")
             (str "|(3XzX (3dzX (3Xzd (3XzX|(3XzX (3dzd (3Xzd (3dzd"
                  "|(3XzX (3dzd (3Xzd (3dzX|(3Xzd (3dzX (3dzX (3Xzd|")

             "L:1/8"
             (str "|(5Xzzdz (5dzzXz (5dzzdz (5Xzzdz|(5Xzzdz (5dzzXz (5dzzXz (5dzzdz"
                  "|(5dzzXz (5Xzzdz (5dzzdz (5XzzXz|(5dzzdz (5dzzXz (5dzzXz (5dzzdz|")
             (str "|(5XzzXz (5dzzXz (5Xzzdz (5XzzXz|(5XzzXz (5dzzdz (5Xzzdz (5dzzdz"
                  "|(5XzzXz (5dzzdz (5Xzzdz (5dzzXz|(5Xzzdz (5dzzXz (5dzzXz (5Xzzdz|")

             "L:1/8"
             (str "|(3Xdd (3ddX (3ddd (3Xdd|(3Xdd (3ddX (3ddX (3ddd"
                  "|(3ddX (3Xdd (3ddd (3XdX|(3ddd (3ddX (3ddX (3ddd|")
             (str "|(3XdX (3ddX (3Xdd (3XdX|(3XdX (3ddd (3Xdd (3ddd"
                  "|(3XdX (3ddd (3Xdd (3ddX|(3Xdd (3ddX (3ddX (3Xdd|")

             "L:1/16"
             (str "|(6Xddddd (6ddddXd (6dddddd (6Xddddd"
                  "|(6Xddddd (6ddddXd (6ddddXd (6dddddd"
                  "|(6ddddXd (6Xddddd (6dddddd (6XdddXd"
                  "|(6dddddd (6ddddXd (6ddddXd (6dddddd|")
             (str "|(6XdddXd (6ddddXd (6Xddddd (6XdddXd"
                  "|(6XdddXd (6dddddd (6Xddddd (6dddddd"
                  "|(6XdddXd (6dddddd (6Xddddd (6ddddXd"
                  "|(6Xddddd (6ddddXd (6ddddXd (6Xddddd|")

             "L:1/8"
             (str "|(5Xdddd (5dddXd (5ddddd (5Xdddd|(5Xdddd (5dddXd (5dddXd (5ddddd"
                  "|(5dddXd (5Xdddd (5ddddd (5XddXd|(5ddddd (5dddXd (5dddXd (5ddddd|")
             (str "|(5XddXd (5dddXd (5Xdddd (5XddXd|(5XddXd (5ddddd (5Xdddd (5ddddd"
                  "|(5XddXd (5ddddd (5Xdddd (5dddXd|(5Xdddd (5dddXd (5dddXd (5Xdddd|")
      )

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
      ; done

      [:h4 "2.2 - Doubles croches"]
      [:p "Quatre notes par pulsation. Soit on ajoute une note sans accent "
          "entre les croches :"]
      ; done

      [:p "Soit on joue « deux fois plus vite » par rapport à la pulsation :"]
      ; done

      [:h4 "2.4 - Triples croches"]
      [:p "Huit notes par pulsation. Soit on ajoute une note sans accent "
          "entre les doubles croches de chacune des versions ci-dessus :"]
      ; done

      [:p "ou"]
      ; done

      [:p "Soit on joue « deux fois plus vite »."]
      ; done

      [:p "On ne va pas plus loin : pas de quadruple croche dans ce document."]

      [:h4 "2.3 - Sextolets, 2 groupes de 3"]
      [:p "Le contre-temps est sur le quatrième sextolet, comme si on mettait "
          "trois notes dans chaque croche de la phrase de départ."]
      ; done

      [:h4 "2.s3 - Swing sur les triolets"]
      [:p "Triolets purs auxquels on enlève une note par temps."]
      ; done

      [:h4 "2.s5 - Swing sur les quintolets"]
      [:p "Quintolets purs auxquels on enlève trois note par temps."]
      ; done

      [:h3 "3 - Triolets"]
      ; done

      [:h4 "3.2 - Sextolets, 3 groupes de 2"]
      [:p "Le contre-temps est sur le cinquième sextolet, comme si on mettait "
          "trois notes dans chaque croche de la phrase de départ."]
      ; done

      [:h4 "3.3 - Trois groupes de 3"]
      [:h3 "5 - Quintolets"]
      ; done

      [:h2 {:id "au-pad"}[:a {:href "#au-pad"} "Au pad"]]
      ; TODO roulés et flas
      [:h2 {:id "sur-le-kit"}[:a {:href "#sur-le-kit"} "Sur le kit"]]

      [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br]
      [:br] [:br] [:br] [:br] [:br] ]))
