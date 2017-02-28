package jeux;

public class PartieFouFou {
    public static void main(String[] args) {

        String jBlanc = "blanc";
        String jNoir = "noir";

        String[] lesJoueurs = new String[2];
        lesJoueurs[0] = jBlanc;
        lesJoueurs[1] = jNoir;

//        AlgoJeu AlgoJoueur[] = new AlgoJeu[2];
//        AlgoJoueur[0] = new Minimax(HeuristiquesDominos.hblanc, jBlanc, jNoir, 4); // Il faut remplir la méthode !!!
//        AlgoJoueur[1] = new Minimax(HeuristiquesDominos.hnoir, jNoir, jBlanc, 1);  // Il faut remplir la méthode !!!


        boolean jeufini = false;
        int jnum;

        PlateauFouFou plateauCourant = new PlateauFouFou();

        // A chaque itération de la boucle, on fait jouer un des deux joueurs
        // tour a tour
        jnum = 0; // On commence par le joueur Blanc (arbitraire)

        while (!jeufini) {
            System.out.println("Coups possibles pour" + lesJoueurs[jnum] + " : " + plateauCourant.mouvementsPossibles(lesJoueurs[jnum]));

            String[] meilleurCoup = plateauCourant.mouvementsPossibles(lesJoueurs[jnum]);

            if(meilleurCoup.length != 0 ) {
                System.out.println("Coup joué : " + meilleurCoup + " par le joueur " + lesJoueurs[jnum]);

                plateauCourant.play(meilleurCoup[0], lesJoueurs[jnum]);
                plateauCourant.printPlateau();


            jnum = 1 - jnum;

            } else {
                System.out.println("Le joueur " + lesJoueurs[jnum] + " ne peut plus jouer et abandone !");
                System.out.println("Le joueur " + lesJoueurs[1 - jnum] + " a gagné cette partie !");
                jeufini = true;
            }
        }
    }

}
