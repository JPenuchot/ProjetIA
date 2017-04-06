package heuristiques;

import structure.*;

public class Minimiser implements Heuristique{
	@Override
	public float estimate(PlateauFouFou plateau, State joueur){
		return -plateau.getNumberCaseState(StateUtils.getInverseState(joueur));
	}
}