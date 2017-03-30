package fousfous;

public class JoueurAleatoire implements IJoueur {

    // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    State color;
    int colorInt;

    @Override
    public void initJoueur(int mycolour){
    	this.color = mycolour == 1 ? State.black : State.white;
        this.colorInt = mycolour;
    }

    @Override
    public int getNumJoueur(){
    	return -1;   //  TODO
    }

    @Override
    public String choixMouvement(){
    	return null;   //  TODO
    }

    @Override
    public void declareLeVainqueur(int colour){
    	//	TODO
    }

    @Override
    public void mouvementEnnemi(String coup){
    	//	TODO
    }

    @Override
    public String binoName(){ return "Quentin Barroche et Jules Penuchot"; }

}
