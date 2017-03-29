package fousfous;

public JoueurAlphaBeta implements IJoueur {

    // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    @Override
    public void initJoueur(int mycolour){
    	//	TODO
    }

    @Override
    public int getNumJoueur(){
    	//	TODO
    }

    @Override
    public String choixMouvement(){
    	//	TODO
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
