package memo;

import structure.PlateauFouFou;
import structure.State;
import structure.StateUtils;

import java.util.HashMap;

public class BaseAlphaBeta{
	static HashMap<State[], MemoAlphaBeta> base = new HashMap<State[], MemoAlphaBeta>();

	public static MemoAlphaBeta find(PlateauFouFou plat){
		if(base.containsKey(plat.getStateArray()))
			return base.get(plat.getStateArray());
		return null;
	}

	public static void add(PlateauFouFou plat){
		base.put(plat.getStateArray(), new MemoAlphaBeta(plat));
	}
}