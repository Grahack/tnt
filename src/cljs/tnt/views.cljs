(ns tnt.views
  (:require
   [re-frame.core :as re-frame]
   [clojure.string]
   ["@grahack/react-sheet-music-mirror" :as r-s-m :refer [default]]
   ))

(defn null-join [lst]
  (clojure.string/join "" lst))

(defn space-join [lst]
  (clojure.string/join " " lst))

(defn pipe-join [lst]
  (clojure.string/join "|" lst))

(defn nl-join [lst]
  (clojure.string/join "\n" lst))

(defn pipe-surround [txt]
  (str "|" txt "|"))

(defn score [id & lines]
  (let [first-line (first lines)
        with-meter (if (clojure.string/starts-with? first-line "M:")
                       lines
                       (cons "M:4/4" lines))
        flat (flatten with-meter)
        with-hertas  (map #(clojure.string/replace % "W" "\"R\"d/2\"L\"B/2")
                          flat)
        with-accents (map #(clojure.string/replace % "X" "!accent!d")
                          with-hertas)
        with-flams   (map #(clojure.string/replace % "Y" "{c}!accent!d")
                          with-accents)
        with-rolls   (map #(clojure.string/replace % "Z" "d/2d/2")
                          with-flams)
        final-lines with-rolls]
    [:> default {:id id :notation (nl-join final-lines)}]))

(defn tnt-8 [[d u t r]]  ; down up two rest
  [[d u r d] [d u u r] [u d r t] [r u u r]
   [t u d t] [t r d r] [t r d u] [d u u d]])

(defn tnt-16 [[ooox ooxo ooxx oxoo oxxo
               xooo xoox xoxx xxoo xxox]]
               ; missing: oooo oxox oxxx xoxo xxxo xxxx
  [[xoox ooxo xoox oxoo] [oxxo ooxx ooox oxoo]
   [xxox xoxx xxoo xooo] [xxoo xoox xoox oxxo]])

(defn prefix-adder-to-elts [prefix] #(map (fn [s] (str prefix s)) %))

(defn tnt [subdivision per-line prefix elements]
  (let [measures { :8 (tnt-8  elements)
                  :16 (tnt-16 elements)}]
    (->> (subdivision measures)
         (map (prefix-adder-to-elts prefix))
         (partition per-line)
         (map #(pipe-join (map space-join %)))
         (map pipe-surround)
         (nl-join))))

(defn phrase [per-line & elements]
  (tnt :8 per-line "" elements))

(defn debit-1 [per-line prefix & elements]
  (tnt :8 per-line prefix elements))

(defn debit-2 [per-line prefix & elements]
  (tnt :16 per-line prefix elements))

(defn sticking-adder-to-elts [sticking]
  (fn [measure]
    (let [notes-per-quarter (count (first measure))
          sticking-length (count sticking)
          sticked-notes #(str "\""
                              (get sticking (mod (first %) sticking-length))
                              "\""
                              (second %))]
      (->> measure
           (null-join)               ; flatten
           (map vector (range))      ; add numbers
           (map sticked-notes)       ; add stickings
           (null-join)               ; was still a list of notes, flatten

           (partition 4)             ; 4 because "S"n (sticking and note)
           (map null-join)           ; flatten internal lists

           (partition notes-per-quarter)      ; group by quarters
           (map null-join)                    ; flatten internal lists
           ))))

(defn tnt-sticking [subdivision per-line sticking prefix elements]
  (let [measures { :8 (tnt-8 elements)
                  :16 (tnt-16 elements)}]
    (->> (subdivision measures)
         (map (sticking-adder-to-elts sticking))
         (map (prefix-adder-to-elts prefix))
         (partition per-line)
         (map #(pipe-join (map space-join %)))
         (map pipe-surround)
         (nl-join))))

(defn sticking-1 [per-line sticking prefix & elements]
    (tnt-sticking :8 per-line sticking prefix elements))

(defn sticking-2 [per-line sticking prefix & elements]
    (tnt-sticking :16 per-line sticking prefix elements))

(def tnt-lengths {:eights   [[3 3 2   3 2 4   1 4 1 4 2 3]
                             [1 2 1 2 1 1    1 3 4    1 3 3 1   3 2 1 2]]
                  :triplets [[5 4 3   5 3 6   1 6 2 6 3 4]
                             [2 3 1 3 2 1   4 6   2 4 5 1   5 3 1 3]]})

(defn lengths-lines [subdivision per-quarter per-line prefix patterns]
  (->> tnt-lengths
       (subdivision)                  ; select eights or triplets
       (flatten)                      ; concat the two lists
       (map #(get patterns (- % 1)))  ; replace lengths by relevant patterns
       ; j’ai une liste de listes [[...] [...]]
       ; là on pourrait grouper les motifs, mais on va grouper par noire
       (flatten)                      ; je flatten
       (partition per-quarter)        ; pour regrouper par noire
       (map null-join)                ; et stemer les notes
       (partition 4)                  ; toujours 4 noires par mes
       (map space-join)
       (partition per-line)
       (map pipe-join)
       (map pipe-surround)))

(defn lengths [id L M subdivision per-measure per-line prefix patterns]
  [:div {:id id}
    [:p "Motif pour chaque durée :"]
    (score (str id "-score-explanation")
           (str "L:" L)
           (->> patterns (map null-join) (pipe-join) (pipe-surround)))
    [:p "Phrase :"]
    (score (str id "-score")
           (str "M:" M)
           (str "L:" L)
           (let [per-quarter (subdivision {:eights (if (= L "1/16") 4 2)
                                           :triplets 3})
                 sub subdivision]
             (lengths-lines sub per-quarter per-line prefix patterns)))])

(defn main-panel []
  (let [l1 (first  (:eights tnt-lengths))
        l2 (second (:eights tnt-lengths))
        l1-3 (first  (:triplets tnt-lengths))
        l2-3 (second (:triplets tnt-lengths))]
    [:div
      #_(map #(identity [:pre (str %)]) (lengths-lines :eights 8 2 ""
                     [["\"L\"d"]
                      ["\"L\"d" "\"K\"E"]
                      ["\"L\"d" "\"R\"G" "\"K\"E"]
                      ["\"L\"d" "\"R\"G" "\"K\"E" "\"K\"E"]]))
      [:div {:id "links"}
        [:p
          [:a {:href "#debits"} "Débits"] " - "
          [:a {:href "#au-pad"} "Pad"] " - "
          [:a {:href "#sur-le-kit"} "Kit"]]
        [:p
          [:a {:href "mailto:christophegragnic@gmail.com"} "Email"] " - "
          [:a {:href "https://github.com/grahack/tnt" :target "_blank"}
              "Github"]]]
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
       "la phrase sur différentes subdivisions avec des accents."]
      [:p "La section " [:a {:href "#au-pad"} "au pad"]
       " propose des doigtés sur les débits de la section précédente. "
       "Ils sont pour l’instant pour les droitiers, "
       "contactez-moi pour les versions pour gaucher."]
      [:p "Les orchestrations " [:a {:href "#sur-le-kit"} "sur le kit"]
       " proposent des orchestrations sur le kit de batterie des phrases "
       "de la section précédente."]

      [:p "Dans ce document, les pulsations sont écrites avec des noires."]

      [:h2 {:id "debits"} [:a {:href "#debits"} "Débits"]]
      [:ul
        [:li "Basés sur la phrase en croches"
          [:ul
            [:li [:a {:href "#croches"} "2 - croches"]]
            [:li [:a {:href "#doubles-croches"} "2.2 - doubles-croches"]]
            [:li [:a {:href "#doubles-croches-deux-fois-plus-vite"}
                     "2.2 bis doubles croches deux fois plus vite"]]
            [:li [:a {:href "#triples-croches"} "2.4 - triples croches"]]
            [:li [:a {:href "#triples-croches-deux-fois-plus-vite"}
                     "2.4 bis - triples croches deux fois plus vite"]]
            [:li [:a {:href "#triples-croches-quatre-fois-plus-vite"}
                     "2.4 ter - triples croches quatre fois plus vite"]]
            [:li [:a {:href "#sextolets-deux-groupes-de-trois"}
                     "2.3 - sextolets deux groupes de trois"]]
            [:li [:a {:href "#swing-sur-triolets"} "2.s3 - swing sur triolets"]]
            [:li [:a {:href "#swing-sur-double-croches"}
                     "2.s4 - swing sur double croches"]]
            [:li [:a {:href "#swing-sur-quintolets"}
                     "2.s5 - swing sur quintolets"]]]
        [:li "Basés sur la phrase en triolets"
          [:ul
            [:li [:a {:href "#triolets"} "3 - triolets"]]
            [:li [:a {:href "#sextolets-trois-groupes-de-deux"}
                     "3.2 - sextolets trois groupes de deux"]]
            [:li [:a {:href "#sextolets-deux-fois-plus-vite"}
                     "3.d - sextolets deux fois plus vite"]]
            [:li [:a {:href "#trois-groupes-de-trois"}
                     "3.3 - trois groupes de trois"]]]]
        [:li "Divers"
          [:ul
            [:li [:a {:href "#quintolets"} "5 - quintolets"]]
            [:li [:a {:href "#bonus"} "Bonus : sur les noires"]]]]]]

      [:h3 "2 - Croches"]
      [:p "Deux notes par pulsation, c’est le débit de la phrase de départ, "
          "que l’on recopie ici en ajoutant des croches non accentuées "
          "dans les silences."]
      (score "croches"
             "L:1/8"
             (debit-1 4 "" "Xd" "dX" "XX" "dd"))

      [:h4 "2.2 - Doubles croches"]
      [:p "Quatre notes par pulsation. Soit on ajoute une note sans accent "
          "entre les croches :"]
      (score "doubles-croches"
             "L:1/16"
             (debit-1 2 "" "Xddd" "ddXd" "XdXd" "dddd"))

      [:p "Soit on joue « deux fois plus vite » par rapport à la pulsation :"]
      (score "doubles-croches-deux-fois-plus-vite"
             "L:1/16"
             (debit-2 2 "" "dddX" "ddXd" "ddXX" "dXdd" "dXXd"
                           "Xddd" "XddX" "XdXX" "XXdd" "XXdX"))

      [:h4 "2.4 - Triples croches"]
      [:p "Huit notes par pulsation. Soit on ajoute une note sans accent "
          "entre les doubles croches de chacune des versions ci-dessus :"]
      (score "triples-croches"
             "L:1/32"
             (debit-1 2 "" "Xddddddd" "ddddXddd" "XdddXddd" "dddddddd"))

      [:p "ou"]
      (score "triples-croches-deux-fois-plus-vite"
             "L:1/32"
             (debit-2 2 ""
                      "ddddddXd" "ddddXddd" "ddddXdXd" "ddXddddd" "ddXdXddd"
                      "Xddddddd" "XdddddXd" "XdddXdXd" "XdXddddd" "XdXdddXd"))

      [:p "Soit on joue « deux fois plus vite » que les doubles-croches."]
      (score "triples-croches-quatre-fois-plus-vite"
             "L:1/32"
             "|XddXddXd XddXdXdd dXXdddXX dddXdXdd|"
             "|XXdXXdXX XXddXddd XXddXddX XddXdXXd|")

      [:p "On ne va pas plus loin : pas de quadruple croche dans ce document."]

      [:h4 "2.3 - Sextolets, 2 groupes de 3"]
      [:p "Le contre-temps est sur le quatrième sextolet, comme si on mettait "
          "trois notes dans chaque croche de la phrase de départ."]
      (score "sextolets-deux-groupes-de-trois"
             "L:1/16"
             (debit-1 2 "(6" "Xddddd" "dddXdd" "XddXdd" "dddddd"))

      [:h4 "2.s3 - Swing sur les triolets"]
; TODO: version débit plutôt que phrase
      [:p "Triolets purs dont une note n’est pas jouée, "
          "ici par exemple noté en 12:8 (on a groupé les triolets même si "
          "ce n’était pas nécessaire, voire impropre)."]
          ; TODO: voir triolets plus bas
      (score "swing-sur-triolets"
             "M:12/8"
             "L:1/8"
             (phrase 4 "d3" "(3::2z2d" "(3dzd" "z3"))
      [:p {:id "triolets-durees"}
          "Liste des durées avec la phrase en triolets : "
          (map str l1-3) " et " (map str l2-3) "."]

      [:h4 "2.s4 - Swing sur les doubles croches"]
; TODO: version débit plutôt que phrase
      [:p "Doubles croches dont deux notes ne sont pas jouées."]
          ; TODO: voir dbles plus haut
      (score "swing-sur-double-croches"
             "L:1/16"
             (phrase 4 "d4" "z3d" "dz2d" "z4"))

      [:h4 "2.s5 - Swing sur les quintolets"]
; TODO: version débit plutôt que phrase
      [:p "Quintolets purs dont trois notes ne sont pas jouées."]
          ; TODO: voir quintolets plus bas
      (score "swing-sur-quintolets"
             "L:1/16"
             (phrase 4 "d4" "(5::3z3dz" "(5::4dz2dz" "z4"))

      [:h3 "3 - Triolets"]
      [:p "On pourrait aussi mettre le contre-temps sur la deuxième "
          "croche, mais ce n’est pas traité dans ce document."]
          ; pourrait être noté en 12:8
      (score "triolets"
             "L:1/8"
             (debit-1 4 "(3" "Xdd" "ddX" "XdX" "ddd"))

      [:h4 "3.2 - Sextolets, 3 groupes de 2"]
      [:p "Le contre-temps est sur le cinquième sextolet, comme si on mettait "
          "deux notes dans chaque croche de la phrase interprétée en triolets "
          "ci-dessus."]
      (score "sextolets-trois-groupes-de-deux"
             "L:1/16"
             (debit-1 2 "(6" "Xddddd" "ddddXd" "XdddXd" "dddddd"))

      [:h4 "3.d - Sextolets, deux fois plus vite"]
      [:p "On joue « deux fois plus vite » la phrase version triolets."]
      (score "sextolets-deux-fois-plus-vite"
             "L:1/16"
             (debit-2 2 "(6"
                      "dddddX" "dddXdd" "dddXdX" "ddXddd" "ddXXdd"
                      "Xddddd" "XddddX" "XddXdX" "XdXddd" "XdXddX"))

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
             (debit-1 2 "(5" "Xdddd" "dddXd" "XddXd" "ddddd"))

      [:h2 {:id "au-pad"}[:a {:href "#au-pad"} "Au pad"]]
      [:ul
        [:li "Frisés, roulés et flas"
          [:ul
            [:li [:a {:href "#croches-frise"} "Croches"]]
            [:li [:a {:href "#triolets-frise"} "Triolets"]]]]
        [:li "Ternaires"
          [:ul
            [:li [:a {:href "#rll"} "RLL"]]
            [:li [:a {:href "#rllrrl"} "RLLRRL"]]
            [:li [:a {:href "#rrlrll"} "RRLRLL"]]]]
        [:li "Moulins variables"
          [:ul
            [:li [:a {:href "#doubles-croches-diddles"} "Doubles croches"]]
            [:li [:a {:href "#sextolets-diddles"} "Sextolets"]]]]]

      [:h3 "Frisé, roulés et flas"]
      [:p "Pour chaque débit proposé ci-dessus en croches ou triolets de "
          "croches (sauf peut-être le « trois groupes de trois »), "
          "on peut jouer en « frisé » (alternance des mains), "
          "on peut « rouler » les notes non " "accentuées, "
          "et on peut jouer des « flas » sur les accents. "
          "La feuille initiatrice de ce document proposait seulement de le "
          "faire sur la phrase en croche, puis laissait la phrase en triolets "
          "en frisé. Ici on ajoute roulés et flas sur la phrase en triolet."]
      [:h4 {:id "croches-frise"} "Croches en frisé"]
      (score "croches-frise-score"
             "L:1/8"
             (sticking-1 2 "RL" "" "Xd" "dX" "XX" "dd"))
      [:h4 "Croches et roulés"]
      (score "croches-rolls"
             "L:1/8"
             (sticking-1 2 "RL" "" "XZ" "ZX" "XX" "ZZ"))
      [:p "C’est par exemple ici que la liste des durées prend tout son sens."]
      (score "croches-rolls-explanation"
             "L:1/16"
             (str "|\"durée de 1\"d2|d2\"durée de 2\"dd"
                  "|d2\"durée de 3\"dddd|d2\"durée de 4\"dddddd|"))
      [:p "Attention, suivant le nombre de roulés, on change de côté ou non."]
      [:h4 "Croches et flas"]
      (score "croches-flas"
             "L:1/8"
             (sticking-1 2 "RL" "" "Yd" "dY" "YY" "dd"))
      [:h4 {:id "triolets-frise"} "Triolets en frisé"]
      (score "triolets-frise-score"
             "L:1/8"
             (sticking-1 2 "RL" "(3" "Xdd" "ddX" "XdX" "ddd"))
      [:h4 "Triolets et roulés"]
      (score "triolets-rolls"
             "L:1/8"
             (sticking-1 2 "RL" "(3" "XZZ" "ZZX" "XZX" "ZZZ"))
      [:p "Ici la liste des durées est "
          [:a {:href "#triolets-durees"} "celle des triolets"] " et on utilise "
          "les mêmes motifs que "
          [:a {:href "#croches-rolls-explanation"} "pour les croches"] "."]
      [:h4 "Triolets et flas"]
      (score "triolets-flas"
             "L:1/8"
             (sticking-1 2 "RL" "(3" "Ydd" "ddY" "YdY" "ddd"))

      [:h3 {:id "rll"} "Triolets et sextolets en RLL"]
      [:h4 "Triolets en RLL"]
      [:p "Intéressant ce deuxième coup accentué sur la main faible."]
      (score "triolets-rll"
             "L:1/8"
             (sticking-1 2 "RLL" "(3" "Xdd" "ddX" "XdX" "ddd"))
      [:h4 "Sextolets deux groupes de trois en RLL"]
      (score "sextolets-deux-groupes-de-trois-rll"
             "L:1/16"
             (sticking-1 2 "RLL" "(6" "Xddddd" "dddXdd" "XddXdd" "dddddd"))
      [:p "C’est tout. Les sextolets trois groupes de deux n’ont que peu "
          "de sens en RLL."]
      [:h4 "Sextolets deux fois plus vite en RLL"]
      [:p "Donc même doigtés que triolets en RLL"]
      (score "sextolets-deux-fois-plus-vite-rll"
             "L:1/16"
             (sticking-2 2 "RLL" "(6"
                      "dddddX" "dddXdd" "dddXdX" "ddXddd" "ddXXdd"
                      "Xddddd" "XddddX" "XddXdX" "XdXddd" "XdXddX"))

      [:h3 {:id "rllrrl"} "Triolets et sextolets RLLRRL"]
      [:h4 "Triolets en RLLRRL"]
      [:p "Deuxième coup accentué sur la main faible, et premier sur la "
          "main forte."]
      (score "triolets-rllrrl"
             "L:1/8"
             (sticking-1 2 "RLLRRL" "(3" "Xdd" "ddX" "XdX" "ddd"))
      [:h4 "Sextolets deux groupes de 3 en RLLRRL"]
      (score "sextolets-deux-groupes-de-trois-rllrrl"
             "L:1/16"
             (sticking-1 2 "RLLRRL" "(6" "Xddddd" "dddXdd" "XddXdd" "dddddd"))
      [:p "C’est tout. Les sextolets trois groupes de deux n’ont que peu "
          "de sens en RLLRRL."]
      [:h4 "Sextolets deux fois plus vite en RLLRRL"]
      [:p "Donc même doigtés que triolets en RLLRRL"]
      (score "sextolets-deux-fois-plus-vite-rllrrl"
             "L:1/16"
             (sticking-2 2 "RLLRRL" "(6"
                      "dddddX" "dddXdd" "dddXdX" "ddXddd" "ddXXdd"
                      "Xddddd" "XddddX" "XddXdX" "XdXddd" "XdXddX"))

      [:h3 {:id "rrlrll"} "Triolets et sextolets RRLRLL"]
      [:h4 "Triolets en RRLRLL"]
      [:p "Deuxième coup accentué sur la main faible, et premier sur la "
          "main forte."]
      (score "triolets-rrlrll"
             "L:1/8"
             (sticking-1 2 "RRLRLL" "(3" "Xdd" "ddX" "XdX" "ddd"))
      [:h4 "Sextolets deux groupes de 3 en RRLRLL"]
      (score "sextolets-deux-groupes-de-trois-rrlrll"
             "L:1/16"
             (sticking-1 2 "RRLRLL" "(6" "Xddddd" "dddXdd" "XddXdd" "dddddd"))
      [:p "C’est tout. Les sextolets trois groupes de deux n’ont que peu "
          "de sens en RRLRLL."]
      [:h4 "Sextolets deux fois plus vite en RRLRLL"]
      [:p "Donc même doigtés que triolets en RRLRLL"]
      (score "sextolets-deux-fois-plus-vite-rrlrll"
             "L:1/16"
             (sticking-2 2 "RRLRLL" "(6"
                      "dddddX" "dddXdd" "dddXdX" "ddXddd" "ddXXdd"
                      "Xddddd" "XddddX" "XddXdX" "XdXddd" "XdXddX"))

      [:h3 "Moulins variables"]
      [:p "L’effet est celui des roulés mais sans trou. "
          "On se sert des listes des durées pour construire les doigtés. "
          "On aura, respectivement, pour les durées 1, 2, 3 et 4 "
          "(idem pour 5 et 6) :"]
      (score "diddles-explanation"
             "L:1/16"
             (str "|\"R\"!accent!d\"L\"d"
                  "|\"R\"!accent!d\"L\"d\"R\"d\"R\"d"
                  "|\"R\"!accent!d\"L\"d\"R\"d\"R\"d\"L\"d\"L\"d"
                  "|\"R\"!accent!d\"L\"d\"R\"d\"R\"d\"L\"d\"L\"d\"R\"d\"R\"d|"))
      [:p "Et bien sûr, si on doit changer de côté :"]
      (score "diddles-explanation-left-lead"
             "L:1/16"
             (str "|\"L\"!accent!d\"R\"d"
                  "|\"L\"!accent!d\"R\"d\"L\"d\"L\"d"
                  "|\"L\"!accent!d\"R\"d\"L\"d\"L\"d\"R\"d\"R\"d"
                  "|\"L\"!accent!d\"R\"d\"L\"d\"L\"d\"R\"d\"R\"d\"L\"d\"L\"d|"))
      [:p "Les doigtés ne sont pas indiqués sur les deux partitions suivantes, "
          "à vous de les « calculer » ! "
          "Et pour une raison que je vous laisse découvrir, "
          "le doigté est inversé à chaque tour sur les deux versions, "
          "ce qui est très bon pour les adeptes de la Secte des Symétriques."]
      [:h4 {:id "doubles-croches-diddles"} "Doubles croches à moulins variables"]
      [:p "Le son est celui des croches auxquelles on a ajouté une double "
          "croche sans accent."]
      (score "doubles-croches-diddles-score"
             "L:1/16"
             (debit-1 2 "" "Xddd" "ddXd" "XdXd" "dddd"))
      [:h4 {:id "sextolets-diddles"} "Sextolets à moulins variables"]
      [:p "Le son est celui des sextolets à trois groupes de deux."]
      (score "sextolets-diddles-score"
             "L:1/16"
             (debit-1 2 "(6" "Xddddd" "ddddXd" "XdddXd" "dddddd"))

      [:h2 {:id "sur-le-kit"}[:a {:href "#sur-le-kit"} "Sur le kit"]]
      [:ul
        [:li [:a {:href "#notation"} "Notation"]]
        [:li "Croches"
          [:ul
            [:li [:a {:href "#croches-lrk"} "LRK"]]
            [:li [:a {:href "#hertas-rlrk"} "Hertas RLRK"]]
            [:li [:a {:href "#sextolets-lkk"} "Sextolets LKK"]]]]
        [:li "Doubles croches"
          [:ul
            [:li [:a {:href "#doubles-croches-rlrkrk"} "RLRKRK"]]
            [:li [:a {:href "#doubles-croches-rlkkkk"}
                     "RLKKKK (double pédale)"]]]
        [:li "Triolets"
          [:ul
            [:li [:a {:href "#triolets-rlrk"} "RLRK"]]
            [:li [:a {:href "#sextolets-lkk-lkl"} "Sextolets LKK/LKL"]]]]
        [:li "Divers"
          [:ul
            [:li [:a {:href "#chabada"} "Chabada"]]]]]]

      [:h3 {:id "notation"} "Notation"]
      [:p "Le système de notation utilisé ici ayant quelques limitations, "
          "une correspondance « maison » est utilisée :"]
      (score "kit-explanation"
             "L:1/4"
             (str "|\"cymbale (*)\"f"
                  "|\"caisse\"d"
                  "|\"rack tom\"B"
                  "|\"floor tom\"G"
                  "|\"gr. caisse\"E|"))
      [:p "(*) Cymbale quelconque, charley, ride ou autre."]
      [:p "De plus, toutes les notes d’une mesure sont pour l’instant "
          "groupées. À l’avenir, seuls les motifs seront groupés."]

      [:h3 {:id "croches-lrk"} "Croches LRK"]
      (lengths "croches-lrk" "1/8" "4/4" :eights 8 2 ""
               [["\"L\"d"]
                ["\"L\"d" "\"K\"E"]
                ["\"L\"d" "\"R\"G" "\"K\"E"]
                ["\"L\"d" "\"R\"G" "\"K\"E" "\"K\"E"]])

      [:h3 {:id "hertas-rlrk"} "Hertas RLRK"]
      (lengths "hertas-rlrk" "1/8" "4/4" :eights 8 2 ""
               [["\"R\"d"]
                ["W" "\"R\"G"]
                ["W" "\"R\"G" "\"K\"E"]
                ["W" "\"R\"G" "\"K\"E" "\"K\"E"]])

      [:h3 {:id "sextolets-lkk"} "Sextolets LKK"]
      (score "sextolets-lkk-score"
             "L:1/16"
             (debit-1 2 "(6" "XEEdEE" "dEEXEE" "XEEXEE" "dEEdEE"))

      [:h3 {:id "doubles-croches-rlrkrk"} "Doubles croches RLRKRK"]
      (lengths "doubles-croches-rlrkrk" "1/16" "4/4" :eights 16 2 ""
               [["\"R\"d" "\"K\"E" ]
                ["\"R\"d" "\"L\"B" "\"R\"G" "\"K\"E"]
                ["\"R\"d" "\"L\"B" "\"R\"G" "\"K\"E" "\"R\"G" "\"K\"E"]
                ["\"R\"d" "\"L\"B" "\"R\"G" "\"K\"E"
                 "\"R\"G" "\"K\"E" "\"R\"G" "\"K\"E"]])

      [:h3 {:id "doubles-croches-rlkkkk"}
           "Doubles croches RLKKKK (avec une double pédale)"]
      (lengths "doubles-croches-rlkkkk" "1/16" "4/4" :eights 16 2 ""
               [["\"R\"!accent!d" "\"L\"d" ]
                ["\"R\"!accent!d" "\"L\"d" "\"K\"E" "\"K\"E"]
                ["\"R\"!accent!d" "\"L\"d" "\"K\"E" "\"K\"E" "\"K\"E" "\"K\"E"]
                ["\"R\"!accent!d" "\"L\"d" "\"K\"E" "\"K\"E"
                 "\"K\"E" "\"K\"E" "\"K\"E" "\"K\"E"]])

      [:h3 {:id "triolets-rlrk"} "Triolets RLRK"]
      (lengths "triolets-rlrk" "1/8" "4/4" :triplets 12 2 ""
               [["\"R\"d"]
                ["\"R\"d" "\"K\"E"]
                ["\"R\"d" "\"R\"G" "\"K\"E"]
                ["\"R\"d" "\"L\"B" "\"R\"G" "\"K\"E"]
                ["\"R\"d" "\"L\"B" "\"R\"G" "\"K\"E" "\"K\"E"]
                ["\"R\"d" "\"L\"B" "\"R\"G" "\"K\"E" "\"R\"G" "\"K\"E"]])

      [:h3 {:id "sextolets-lkk-lkl"} "Sextolets LKK/LKL"]
      (score "sextolets-lkk-lkl-score"
             "L:1/16"
             (debit-2 2 "(6"
                      "dEEdEX" "dEEXEE" "dEEXEX" "dEXdEE" "dEXXEE"
                      "XEEdEE" "XEEdEX" "XEEXEX" "XEXdEE" "XEXdEX"))

      [:h3 {:id "chabada"} "Chabada"]
      (score "chabada-score"
             "L:1/8"
             "V:1 name=\"Ride\" snm=\"R\""
             "V:2 name=\"Snare\" snm=\"S\""
             "V:1"
             "|f2 ff f2 ff|f2 ff f2 ff|f2 ff f2 ff|f2 ff f2 ff|"
             "V:2"
             "|d2 zd z2 d2|d2 zd zd z2|zd d2 z2 dd|z2 zd zd z2|"
             "V:1"
             "|f2 ff f2 ff|f2 ff f2 ff|f2 ff f2 ff|f2 ff f2 ff|"
             "V:2"
             "|dd zd d2 dd|dd z2 d2 z2|dd z2 d2 zd|d2 zd zd d2|")

      [:h2 {:id "bonus"}[:a {:href "#bonus"} "Bonus"]]
      [:p "En bonus, voici la phrase de départ, jouée "
          " « deux fois plus lentement » par rapport à la pulsation. "
          "Version « phrase » puis version « débit »."]
      (score "noires-phrase"
             "M:8/4"
             "L:1/4"
             (phrase 4 "d2" "zd" "dd" "z2"))

      (score "noires"
             "M:8/4"
             "L:1/4"
             (debit-1 4 "" "Xd" "dX" "XX" "dd"))

      [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br] [:br]
      [:br] [:br] [:br] [:br] [:br] ]))
