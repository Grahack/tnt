(ns tnt.views
  (:require
   [re-frame.core :as re-frame]
   ["react-vexflow" :as vf :refer [Score]]
   ))

(defn line [staves]
  [:> Score {:width 800 :height "x" :clef "percussion" :staves staves}])

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
      [:p "La phrase de base est (il faut interpréter la croix "
          "comme un silence, en attendant que j’arrive à faire fonctionner "
          [:code "react-vexflow"] ") :"]
      (line [[hq    r8 h8 rq    hq]
             [hq    r8 h8 r8 h8 rq]
             [r8 h8 hq    rq    h8 h8]
             [rq    r8 h8 r8 h8 rq]])
      (line [[h8 h8 r8 h8 hq    h8 h8]
             [h8 h8 rq    hq    rq]
             [h8 h8 rq    hq    r8 h8]
             [hq    r8 h8 r8 h8 hq] ])
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
      (line [[h8 x8 x8 h8 x8 x8 h8 x8]
             [h8 x8 x8 h8 x8 h8 x8 x8]
             [x8 h8 h8 x8 x8 x8 h8 h8]
             [x8 x8 x8 h8 x8 h8 x8 x8]])
      (line [[h8 h8 x8 h8 h8 x8 h8 h8]
             [h8 h8 x8 x8 h8 x8 x8 x8]
             [h8 h8 x8 x8 h8 x8 x8 h8]
             [h8 x8 x8 h8 x8 h8 h8 x8]])

      [:h4 "2.2 - Doubles croches"]
      [:p "Quatre notes par pulsation. Soit on ajoute une note sans accent "
          "entre les croches :"]
      (line [[h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 x16 x16 h16 x16 x16 x16]
             [h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 h16 x16 x16 x16 x16 x16]])
      (line [[x16 x16 h16 x16 h16 x16 x16 x16 x16 x16 x16 x16 h16 x16 h16 x16]
             [x16 x16 x16 x16 x16 x16 h16 x16 x16 x16 h16 x16 x16 x16 x16 x16]])
      (line [[h16 x16 h16 x16 x16 x16 h16 x16 h16 x16 x16 x16 h16 x16 h16 x16]
             [h16 x16 h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 x16 x16 x16 x16]])
      (line [[h16 x16 h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 x16 x16 h16 x16]
             [h16 x16 x16 x16 x16 x16 h16 x16 x16 x16 h16 x16 h16 x16 x16 x16]])

      [:p "Soit on joue « deux fois plus vite » par rapport à la pulsation :"]
      (line [[h16 x16 x16 h16 x16 x16 h16 x16
              h16 x16 x16 h16 x16 h16 x16 x16]
             [x16 h16 h16 x16 x16 x16 h16 h16
              x16 x16 x16 h16 x16 h16 x16 x16]])
      (line [[h16 h16 x16 h16 h16 x16 h16 h16
              h16 h16 x16 x16 h16 x16 x16 x16]
             [h16 h16 x16 x16 h16 x16 x16 h16
              h16 x16 x16 h16 x16 h16 h16 x16]])

      [:h4 "2.4 - Triples croches"]
      [:p "Huit notes par pulsation. Soit on ajoute une note sans accent "
          "entre les doubles croches de chacune des versions ci-dessus :"]
      (line [[h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
              x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32]])
      (line [[h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
              x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32]])
      (line [[x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32
              x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32]])
      (line [[x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
              x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32]])
      (line [[h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
              h32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32]])
      (line [[h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32
              h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32]])
      (line [[h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32
              h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32]])
      (line [[h32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 x32 h32 x32 x32 x32
              x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32]])

      [:p "ou"]
      (line [[h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 h32 x32 x32 x32
              h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32]])
      (line [[x32 x32 h32 x32 h32 x32 x32 x32 x32 x32 x32 x32 h32 x32 h32 x32
              x32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 x32 x32 x32 x32]])
      (line [[h32 x32 h32 x32 x32 x32 h32 x32 h32 x32 x32 x32 h32 x32 h32 x32
              h32 x32 h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 x32 x32]])
      (line [[h32 x32 h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 x32 x32 h32 x32
              h32 x32 x32 x32 x32 x32 h32 x32 x32 x32 h32 x32 h32 x32 x32 x32]])

      [:p "Soit on joue « deux fois plus vite »."]
      (line [[h32 x32 x32 h32 x32 x32 h32 x32
              h32 x32 x32 h32 x32 h32 x32 x32
              x32 h32 h32 x32 x32 x32 h32 h32
              x32 x32 x32 h32 x32 h32 x32 x32]])
      (line [[h32 h32 x32 h32 h32 x32 h32 h32
              h32 h32 x32 x32 h32 x32 x32 x32
              h32 h32 x32 x32 h32 x32 x32 h32
              h32 x32 x32 h32 x32 h32 h32 x32]])

      [:h4 "2.3 - Sextolets, 2 groupes de 3"]
      [:p "Le contre-temps est sur le quatrième sextolet, comme si on mettait "
          "trois notes dans chaque croche de la phrase de départ."]
      (line [[h8 x8 x8 h8 x8 x8 h8 x8]
             [h8 x8 x8 h8 x8 h8 x8 x8]
             [x8 h8 h8 x8 x8 x8 h8 h8]
             [x8 x8 x8 h8 x8 h8 x8 x8]])
      (line [[h8 h8 x8 h8 h8 x8 h8 h8]
             [h8 h8 x8 x8 h8 x8 x8 x8]
             [h8 h8 x8 x8 h8 x8 x8 h8]
             [h8 x8 x8 h8 x8 h8 h8 x8]])
      [:h4 "2.s3 - Swing sur les triolets"]
      [:h4 "2.s5 - Swing sur les quintolets"]
      [:h3 "3 - Triolets"]
      [:h4 "3.2 - Sextolets, 3 groupes de 2"]
      [:h4 "3.3 - Trois groupes de 3"]
      [:h3 "5 - Quintolets"]
      [:h2 {:id "au-pad"}[:a {:href "#au-pad"} "Au pad"]]
      [:h2 {:id "sur-le-kit"}[:a {:href "#sur-le-kit"} "Sur le kit"]]

      [:> Score {:width 600 :height "x" :clef "percussion"
      ; width fonctionne, un entier passé à height met 0 dans le svg, donc
      ; bidouille du "x" pour au moins qu’il n’y ait pas 0
      ; clefs:
      ; treble bass alto tenor percussion tab
      ; soprano mezzo-soprano baritone-c baritone-f subbass french
                 :staves [["f5" "f/3" ; durée par défaut: q (quarter) ou 4
                           ; mettre une liste semble splitter (no need for /)
                           ["f5" 8] ["f4" "8"] ; durée num = nbre ou txt
                                               ; 1 2 4 ... 128
                           ["a4" "h"] ; h pour half
                           ["x" 8]  ; "x" ; affiche une croix,
                           ["r" 8]] ; "r" ; affiche un si,
                                    ; indep de octave qd fourni
                                    ; (même hauteur que la croix)
                          ; silence: +r à la durée (1r et hr pas d’accord)
                          [["a4" "1r"] ["a4" "hr"] ["a4" "qr"] ["a4" "8r"]]
                          ; [] ; mesure vide (bizarre d’appeler ça "staff")
                          ; trop grave n’affiche pas
                          ; "" ; code: "BadArguments",
                          ;      message: "Invalid key name: UNDEFINED" }
                          ; lettres forcées en maj ligne 78 de tables.js
                          ; lettres possibles: ligne 126 de tables.js
                          ; "a" à "g" ;pas d’erreur mais rien sur la portée
                          ; "h" à "q" et "s" à "w" et "y" et "z" et chiffres
                          ;    erreur code: "BadArguments",
                          ;           message: "Invalid key name: H" }
                          ; ["f#4"] ; n’affiche rien alors que f4 oui
                          ; juste r à la prace de qr:
                          ;   code: "BadArguments",
                          ;   message: "The provided duration is not valid: r"
                          ; [["f4/h" ; Invalid key name: F4
                          ; [["f/q" ; pas d’erreur mais n’affiche rien
                          ; "f/4/q" code: "BadArguments",
                          ;  message: "Invalid note initialization data
                          ;  (No glyph found): {\"keys\":[\"f/4/q\"],
                          ;                     \"duration\":\"q\"}" }
                          ]}]
      [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br]
      [:br] [:br] [:br] [:br] [:br] ]))
