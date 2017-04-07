package heuristiques;

import structure.*;

public class SmartHeuri implements Heuristique{
	@Override
	public float estimate(PlateauFouFou plateau, State joueur){
		float res = 0.f;
		for(int i = 0; i < PlateauFouFou.pSize * PlateauFouFou.pSize; i++){
			res += plateau.getStateArray()[i] == joueur ? 0.f : -1.f;
		}
		return res;
	}
}