package memo;

import structure.PlateauFouFou;
import structure.State;
import structure.StateUtils;

import java.util.HashMap;

/**
 * Base de connaissances sur les états rencontrés lors de l'exploration de l'IA.
 */
public class BaseAlphaBeta{
	static HashMap<State[], MemoAlphaBeta> base = new HashMap<State[], MemoAlphaBeta>();

	/**
	 * Permet d'accéder à la mémoire liée à un plateau donné.
	 *
	 * @param      plat  Le plateau en question
	 *
	 * @return     Mémoire liée au plateau (null si inexistante).
	 */
	public static MemoAlphaBeta find(PlateauFouFou plat){
		if(base.containsKey(plat.getStateArray()))
			return base.get(plat.getStateArray());
		return null;
	}

	/**
	 * Ajoute un plateau à la base de connaissances.
	 *
	 * @param      plat  Le plateau en question.
	 */
	public static MemoAlphaBeta add(PlateauFouFou plat){
		return base.put(plat.getStateArray(), new MemoAlphaBeta(plat));
	}
}