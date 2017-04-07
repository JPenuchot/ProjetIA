package heuristiques;

import structure.*;

public class Minimiser implements Heuristique{
	@Override
	public float estimate(PlateauFouFou plateau, State joueur){

		if(plateau.getNumberCaseState(StateUtils.getInverseState(joueur)) == 0) return 10000;


		//System.out.println(plateau.getNumberCaseState(joueur) + " : " + plateau.getNumberCaseState(StateUtils.getInverseState(joueur)));
		return -plateau.getNumberCaseState(StateUtils.getInverseState(joueur));
	}
}