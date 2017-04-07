package memo;

import structure.PlateauFouFou;
import structure.State;
import structure.StateUtils;

/**
 * Mémoire associée à un état rencontré stocké dans BaseAlphaBeta.
 */
public class MemoAlphaBeta{
	/**
     * Constructeur de la classe ajouté en cas de nécessité. A garder vide de préférence.
     *
     * @param      plat  Le plateau associé à la mémoire.
     */
    public MemoAlphaBeta(PlateauFouFou plat){
		
	}

    /*
	public static int stateToInt(State stState){
        switch (stState){
            case black:
                return 0;
            case white:
                return 1;
            case empty:
                return 2;
        }
        return -1;
    }*/

    //  Stockage des mouvements possibles

    public String[] listBlackMoves = null;
    public String[] listWhiteMoves = null;

    //  Stockage des valeurs d'heuristiques

    public float heurValWhite = 0.f;
    public float heurValBlack = 0.f;

    //  Stockage des valeurs remontées par l'exploration
    
    public int prof = 0;    //  Profondeur max explorée après le noeud

    //  TODO : Rajouter des valeurs (alpha/beta, max, meilleur coup etc.)

    public float alpha = 0;
    public float beta = 0;

}