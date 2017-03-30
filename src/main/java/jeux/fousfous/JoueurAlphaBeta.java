package fousfous;

public class JoueurAlphaBeta implements IJoueur {

    // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    State color;
    int colorInt;

    PlateauFouFou plateau;

    @Override
    public void initJoueur(int mycolour){
    	this.color = mycolour == 1 ? State.black : State.white;
        this.colorInt = mycolour;

        plateau = new PlateauFouFou();
    }

    @Override
    public int getNumJoueur(){
    	return this.colorInt;
    }

    @Override
    public String choixMouvement(){
    	return null;   //  TODO
    }

    @Override
    public void declareLeVainqueur(int colour){
    	if (colour == this.colorInt) {
            System.out.println("J'ai gagné : (" + colorInt + ")");
        }
    }

    @Override
    public void mouvementEnnemi(String coup){
    	this.plateauCourant.play(coup, Case.getInverseState(this.color));
    }

    @Override
    public String binoName(){ return "Quentin Barroche et Jules Penuchot"; }

}
