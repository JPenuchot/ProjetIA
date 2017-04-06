package joueurs;

import structure.*;
import heuristiques.*;

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

    @Override
    public void initJoueur(int mycolour){
    	this.player = mycolour == 1 ? State.black : State.white;
        this.playerInt = mycolour;
        this.profondeur = 3;
        this.h = new DiffPions();

        plateau = new PlateauFouFou();
    }

    @Override
    public int getNumJoueur(){
    	return this.playerInt;
    }

    @Override
    public String choixMouvement(){
        String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);
        String meilleurCoup = "";
        float max = Float.MIN_VALUE;
        float alphaBeta = Float.MIN_VALUE;

        for(String c : coupPossibles) {
            Action[] ac = this.plateau.play(c, this.player);

            //System.out.println("DebutAlpha");
            alphaBeta = negAlphaBeta(this.profondeur, Float.MIN_VALUE, Float.MAX_VALUE, 1);
            //System.out.println("finAlpha : " + alphaBeta);

            for(Action a : ac) {
                    a.reverse();
                    this.plateau.play(a);
            }

            if(max < alphaBeta) {
                //System.out.println("Meilleur Coup Selectionné :" + c);
                max = alphaBeta;
                meilleurCoup = c;
            }
        }

        System.out.println("Meilleur Coup : " + meilleurCoup);
        plateau.play(meilleurCoup, this.player);

        return meilleurCoup;
    }

    public float negAlphaBeta(int p, float alpha, float beta, float parite) {

        // System.out.println("AlphaBeta, p : " + p + "\nalpha :" + alpha + "\nbeta : " + beta + "\npartite : " + parite);


        if (p == 0 || this.plateau.isOver()) {
            //System.out.println("Lancement de l'heuristiques");
            if(parite == 1) {
                alpha = this.h.estimate(this.plateau, this.player);
                // System.out.println("Fin Heuristique, (" + this.player + ")alpha : " + alpha);
            }
            else {
                alpha = this.h.estimate(this.plateau, StateUtils.getInverseState(this.player));
                // System.out.println("Fin Heuristique, (" + StateUtils.getInverseState(this.player) + ")alpha : " + alpha);
            }
        } else {

            String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);

            for(String c : coupPossibles) {
                //System.out.println("Init Action");
                Action[] ac = new Action[2];
                //System.out.println("FinInit Action");
                ac = this.plateau.play(c, this.player);
                //System.out.println("Play Action");

                alpha = Math.max(alpha, -1 * negAlphaBeta(p-1, -1 * beta, -1 * alpha, -1 * parite));

                for(Action a : ac) {
                    //System.out.println("Deb Reverse");
                    a.reverse();
                    this.plateau.play(a);
                    //System.out.println("Fin Reverse");
                }

                if(alpha >= beta) {
                    //System.out.println("Return beta : " + beta);
                    return beta;
                }
            }
        }

        return alpha;
    }




    @Override
    public void declareLeVainqueur(int colour){
    	if (colour == this.playerInt) {
            System.out.println("J'ai gagné : (" + playerInt + ")");
        }
    }

    @Override
    public void mouvementEnnemi(String coup){
    	this.plateau.play(coup, StateUtils.getInverseState(this.player));
    }

    @Override
    public String binoName(){ return "Quentin Barroche et Jules Penuchot"; }

    /**
     * Implémentation de l'algorithme AlphaBeta.
     *
     * @param      plateau  Plateau en cours
     * @param      h        Heuristique
     * @param      prof     Profondeur max
     *
     * @return     Coup à jouer
     */
    private String alphaBeta(PlateauFouFou plateau, Heuristique h, int prof){
        /*  TODO :
         *      -   Mémorisation des chemins explorés
         */
        return null;
    }

}
