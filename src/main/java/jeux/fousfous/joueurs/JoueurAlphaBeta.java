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

    public JoueurAlphaBeta() {
        System.out.println("ATTENTION constructeur par defaut JAB");
    }


    // TODO
    public JoueurAlphaBeta(State joueur, PlateauFouFou p, Heuristique h) {
        this.player = joueur;

    }

    @Override
    public void initJoueur(int mycolour){
    	this.player = mycolour == 1 ? State.black : State.white;
        this.playerInt = mycolour;

        plateau = new PlateauFouFou();
    }

    @Override
    public int getNumJoueur(){
    	return this.playerInt;
    }

    @Override
    public String choixMouvement(){

    }

    public float negAlphaBeta(int p, float alpha, float beta) {
        if (p == 0 || this.plateau.isOver()) {
            alpha = h.estimate(this.plateau, this.player);
        } else {

            String[] coupPossibles = this.plateau.mouvementsPossibles(StateUtils.stateToString(this.player));

            for(String c : coupPossibles) {
                Action[] ac = this.plateau.play(c, this.player);

                alpha = Math.max(alpha, -1 * negAlphaBeta(p-1, -1 * beta, -1 * alpha));

                for(Action a : ac) {
                    a.reverse();
                }

                if(alpha >= beta) {
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
