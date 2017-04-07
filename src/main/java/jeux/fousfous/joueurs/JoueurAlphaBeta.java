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
     * Initialise le joueur (profondeur initiale etc)
     *
     * @param      mycolour  Couleur du joueur en int (passée par l'arbitre)
     */
    @Override
    public void initJoueur(int mycolour){
    	this.player = mycolour == 1 ? State.black : State.white;
        this.playerInt = mycolour;
        this.profondeur = 6;
        this.h = new DiffPions();

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
        float max = Float.MIN_VALUE;
        float alphaBeta = Float.MIN_VALUE;

        System.out.println("Coups Possible : ");

        for (String s : coupPossibles) {
            System.out.println(s);
        }

        if(coupPossibles.length != 1) {
            for(String c : coupPossibles) {
                Action[] ac = this.plateau.play(c, this.player);

                //System.out.println("DebutAlpha");
                alphaBeta = negAlphaBeta(this.profondeur - 1, Float.MIN_VALUE, Float.MAX_VALUE, 1);
                //System.out.println("finAlpha : " + alphaBeta);

                for(Action a : ac) {
                        a.reverse();
                        this.plateau.play(a);
                }

                if(max < alphaBeta) {
                    System.out.println("Meilleur Coup Selectionné :" + c);
                    max = alphaBeta;
                    meilleurCoup = c;
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
     * @param      beta    Valeur bet
     * @param      parite  Parité
     *
     * @return     Retourne une estimation de la qualité du noeud
     */
    public float negAlphaBeta(int p, float alpha, float beta, int parite) {

        //System.out.println("AlphaBeta, p : " + p + "\nalpha :" + alpha + "\nbeta : " + beta + "\npartite : " + parite);
        
        MemoAlphaBeta mem = BaseAlphaBeta.find(this.plateau);

        if (p <= 0 || this.plateau.isOver()) {  //  TODO : Mémorisation de la valeur heuristique
            alpha = parite * this.h.estimate(this.plateau, this.player);
        } else {    //  TODO : Mémorisation de l'alpha
                    //  /!\ EN FONCTION DE LA PROFONDEUR DEJA EXPLOREE
                    //  Si on explore plus profond que la valeur déjà explorée
                    //  alors on s'en branle et on recalcule
                    //  
                    //  Deuxième phase d'opti :
                    //  Trier les coups en fonction de leurs valeurs retournées
                    //  Mais on n'aura pas le temps donc bon... Tant pis I guess

            mem = BaseAlphaBeta.find(this.plateau);

            if(mem == null || mem.prof < p) {
                String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);

                for(String c : coupPossibles) {
                    Action[] ac = new Action[2];

                    ac = this.plateau.play(c, this.player);

                    alpha = Math.max(alpha, -1 * negAlphaBeta(p-1, -1 * beta, -1 * alpha, - parite));

                    mem = BaseAlphaBeta.add(this.plateau);
                    mem.alpha = alpha;
                    mem.beta = beta;
                    mem.prof = p;


                    for(Action a : ac) {
                        a.reverse();
                        this.plateau.play(a);
                    }

                    if(alpha >= beta) {
                        return beta;
                    }
                }
            } else {
                System.out.println("Mem deja trouvé : " + mem.alpha);
                alpha = mem.alpha;
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
