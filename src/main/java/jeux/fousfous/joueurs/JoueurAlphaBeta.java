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
    int getProf(){
        int plyAmt =   plateau.getNumberCaseState(player)
                        + plateau.getNumberCaseState(StateUtils.getInverseState(player));
        if(plyAmt <= 10)
            return 14;
        else if (plyAmt <= 16)
            return 8;
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
        float beta = Float.MAX_VALUE;
        float alpha = Float.MIN_VALUE;

        System.out.println("Coups Possible : ");

        for (String s : coupPossibles) {
            System.out.println(s);
        }

        if(coupPossibles.length != 1) {
            for(String c : coupPossibles) {
                Action[] ac = this.plateau.play(c, this.player);

                //System.out.println("DebutAlpha");
                
                float negAB = negAlphaBeta(this.getProf() - 1, -beta, -alpha, -1);

                if(- negAB > alpha){
                    System.out.println("Meilleur Coup Selectionné :" + c);
                    alpha = -negAB;
                    meilleurCoup = c;
                }
                //System.out.println("finAlpha : " + alpha);

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

            try {
                mem = BaseAlphaBeta.find(this.plateau);
            } catch (Exception e) {
                System.out.println("Erruer find : " + e);
            }

            if(mem == null) {
                String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);

                if(mem == null)
                        mem = BaseAlphaBeta.add(this.plateau);
                    else
                        mem = BaseAlphaBeta.find(this.plateau);

                mem.prof = p;

                for(String c : coupPossibles) {
                    Action[] ac;
                    ac = this.plateau.play(c, this.player);

                    alpha = Math.max(alpha, - negAlphaBeta(p-1, - beta, - alpha, - parite));

                    
                    try {
                        mem = BaseAlphaBeta.add(this.plateau);
                    } catch(Exception e) {
                        System.out.println("Erreur mem : " + e);
                    }

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
            } else if (mem.prof < p) {
                String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);

                // for(String c : coupPossibles) {
                //     Action[] ac = new Action[2];
                // }


                for(String c : coupPossibles) {
                    Action[] ac;
                    ac = this.plateau.play(c, this.player);

                    alpha = Math.max(alpha, - negAlphaBeta(p-1, - beta, - alpha, - parite));


                    mem.alpha = alpha;
                    mem.beta = beta;
                    mem.prof = p;

                    for(Action a : ac) {
                        a.reverse();
                        this.plateau.play(a);
                    }

                    if(alpha >= beta) {
                        mem.alpha = alpha;
                        return beta;
                    }
                }
            }
            else {
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
