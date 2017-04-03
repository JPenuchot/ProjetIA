package joueurs;

import java.util.Random;
import java.util.Date;

import structure.*;

/**
 * Implémente un joueur aléatoire.
 */
public class JoueurAleatoire implements IJoueur {

    // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    State player;
    int playerInt;
    PlateauFouFou plateau;

    Random rnd;

    @Override
    public void initJoueur(int mycolour){
    	this.player = mycolour == 1 ? State.black : State.white;
        this.playerInt = mycolour;
        this.plateau = new PlateauFouFou();

        Date dt = new Date();

        this.rnd = new Random(dt.getTime());
    }

    @Override
    public int getNumJoueur(){
        return this.playerInt;
    }

    @Override
    public String choixMouvement(){
    	String[] coups = plateau.mouvementsPossibles(player);
        String coup = coups[rnd.nextInt() % coups.length];
        plateau.play(coup, this.player);

        return coup;
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

}
