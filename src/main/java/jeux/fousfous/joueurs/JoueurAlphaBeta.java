package joueurs;

import structure.*;
import heuristiques.*;
import memo.*;

public class JoueurAlphaBeta implements IJoueur {

    // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    State player;
    int playerInt;

    PlateauFouFou plateau;
    Heuristique h;
    int profondeur;

    /**
     * Détermine la profondeur de recherche pour le plateau actuel.
     *
     * @return     Profondeur de recherche
     */
    public int getProf(){
        int plyAmt =   plateau.getNumberCaseState(player)
                        + plateau.getNumberCaseState(StateUtils.getInverseState(player));
        if(plyAmt <= 10)
            return 22;
        else if (plyAmt <= 16)
            return 16;
        return 10;
    }

    /**
     * Initialise le joueur (profondeur initiale etc)
     *
     * @param      mycolour  Couleur du joueur en int (passée par l'arbitre)
     */
    @Override
    public void initJoueur(int mycolour){
    	this.player = mycolour == 1 ? State.black : State.white;
        this.playerInt = mycolour;
        this.profondeur = 6;
        this.h = new Minimiser();

        plateau = new PlateauFouFou();
    }

    @Override
    public int getNumJoueur(){
    	return this.playerInt;
    }

    /**
     * Choisit un mouvement en se basant sur les résultats d'un negAlphaBeta
     *
     * @return     Mouvement choisi
     */
    @Override
    public String choixMouvement(){
        //  TODO : Profondeur variable (en fonction du nombre de pions ou de choix)

        String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);
        String meilleurCoup = coupPossibles[0];
        float alpha = Float.MIN_VALUE;
        float beta = Float.MAX_VALUE;

        System.out.println("Coups possibles : ");

        for (String s : coupPossibles) {
            System.out.println(s);
        }

        if(coupPossibles.length != 1) {
            for(String c : coupPossibles) {
                Action[] ac = this.plateau.play(c, this.player);
                
                float negAB = negAlphaBeta(this.getProf() - 1, -beta, -alpha, -1);

                System.out.println(c + " - NegAlphaBeta = " + negAB);

                if(- negAB > alpha){
                    System.out.println("Nouveau meilleur coup : " + c);
                    alpha = - negAB;
                    meilleurCoup = c;
                }

                for(Action a : ac) {
                        a.reverse();
                        this.plateau.play(a);
                }
            }
        } else {
            meilleurCoup = coupPossibles[0];
        }

        System.out.println("Meilleur Coup parmi les " + coupPossibles.length + " : " + meilleurCoup);
        plateau.play(meilleurCoup, this.player);

        return meilleurCoup;
    }

    /**
     * Implémentation de l'algorithme negAlphaBeta
     *
     * @param      p       Profondeur d'exploration
     * @param      alpha   Valeur alpha
     * @param      beta    Valeur beta
     * @param      parite  Parité
     *
     * @return     Retourne une estimation de la qualité du noeud
     */
    public float negAlphaBeta(int p, float alpha, float beta, int parite) {
        if (p <= 0 || this.plateau.isOver()) {
            alpha = parite * this.h.estimate(this.plateau, this.player);
            System.out.println("Je remonte " + alpha);
        } else {
            
            String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);

            for(String c : coupPossibles) {
                Action[] ac;
                ac = this.plateau.play(c, this.player);
                alpha = Math.max(alpha, - negAlphaBeta(p-1, - beta, - alpha, - parite));

                for(Action a : ac) {
                    a.reverse();
                    this.plateau.play(a);
                }

                if(alpha >= beta) {
                    return beta;
                }
            }
        }

        return alpha;
    }

    /**
     * Déclare le vainqueur (paramètre colour passé par l'arbitre)
     *
     * @param      colour  Couleur du vainqueur
     */
    @Override
    public void declareLeVainqueur(int colour){
    	if (colour == this.playerInt) {
            System.out.println("J'ai gagné : (" + playerInt + ")");
        }
    }

    /**
     * Procédure appelée par l'arbitre pour transmettre le coup de l'adversaire.
     *
     * @param      coup  Le coup de l'adversaire
     */
    @Override
    public void mouvementEnnemi(String coup){
    	this.plateau.play(coup, StateUtils.getInverseState(this.player));
    }

    @Override
    public String binoName(){ return "Quentin Barroche et Jules Penuchot"; }
}
