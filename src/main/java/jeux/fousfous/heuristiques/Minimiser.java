package heuristiques;

import structure.*;

public class Minimiser implements Heuristique{
	@Override
	public int estimate(PlateauFouFou plateau, State joueur){

		//if(plateau.getNumberCaseState(StateUtils.getInverseState(joueur)) == 0) return 10000;
		return -plateau.getNumberCaseState(StateUtils.getInverseState(joueur));
	}
}