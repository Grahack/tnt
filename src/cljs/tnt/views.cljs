(ns tnt.views
  (:require
   [re-frame.core :as re-frame]
   ))

(defn main-panel []
  (let [name "TNT"]
    [:div
      [:h1 "Exo " name]
      [:h2 "Histoire"]
      [:p "A.O. (aka TNT) a partagé une feuille de type « Ago ». "
          "Il était loin de se douter des implications de ce geste. "
          "Cette page pousse le bouchon, tout en ne partant pas en cacahuète."]
      [:p "La phrase de base est :"]
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
      [:p "Deux notes par pulsation, c’est le débit de la phrase de départ."]
      [:h4 "2.2 - Doubles croches"]
      [:p "Quatre notes par pulsation. Soit on ajoute une note sans accent "
          "entre les croches, soit on joue « deux fois plus vite »."]
      [:h4 "2.3 - Sextolets, 2 groupes de 3"]
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
