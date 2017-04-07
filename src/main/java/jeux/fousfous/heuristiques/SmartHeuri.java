package heuristiques;

import structure.*;

public class SmartHeuri implements Heuristique{
	@Override
	public int estimate(PlateauFouFou plateau, State joueur){
		int res = 0;
		for(int i = 0; i < PlateauFouFou.pSize * PlateauFouFou.pSize; i++){
			res += plateau.getStateArray()[i] == joueur ? 0 : -1;
		}
		return res;
	}
}