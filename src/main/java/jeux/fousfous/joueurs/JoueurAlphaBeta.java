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
            return 28;
        else if (plyAmt <= 16)
            return 14;
        else if (plyAmt <= 25)
            return 8;
        else 
            return 4;
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
        this.profondeur = 4;
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
        String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);
        String meilleurCoup = coupPossibles[0];
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        if(coupPossibles.length != 1) {
            for(String c : coupPossibles) {
                Action[] ac = this.plateau.play(c, this.player);
                
                int negAB = negAlphaBeta(this.getProf() - 1, -beta, -alpha, -1);

                if(- negAB > alpha){
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
    public int negAlphaBeta(int p, int alpha, int beta, int parite) {
        if (p <= 0 || this.plateau.isOver()) {
            alpha = parite * this.h.estimate(this.plateau, this.player);
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
