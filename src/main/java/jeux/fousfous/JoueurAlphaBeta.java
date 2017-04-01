package fousfous;

public class JoueurAlphaBeta implements IJoueur {

    // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    State player;
    int playerInt;

    PlateauFouFou plateau;

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
    	return null;   //  TODO
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

    private float explore(PlateauFouFou plateau, int prof){
        /*  TODO :
         *      -   Mémorisation des chemins explorés
         */
        return 0.f;
    }

}
