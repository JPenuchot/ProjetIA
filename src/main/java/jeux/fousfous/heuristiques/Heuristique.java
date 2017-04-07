package heuristiques;

import structure.*;

public interface Heuristique{
	/**
	 * Renvoie une estimation de la situation pour un joueur sur un plateau donné.
	 *
	 * @param      plateau  Le plateau en question
	 * @param      joueur   Le joueur
	 *
	 * @return     Estimation de la situation du joueur
	 */
	public int estimate(PlateauFouFou plateau, State joueur);
}