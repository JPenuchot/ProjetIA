package fousfous;

/**
 * Décrit une action. Cet objet est utilisé pour décrire un changement sur le plateau afin de mettre en place un système de backtracking
 * et ainsi économiser des accès mémoires lors des appels récursifs d'explore() dans la classe JoueurAlphaBeta.
 */
public class Action{
	public int i;
	public int j;
	public State before;
	public State after;

	/**
	 * Reverts the action.
	 */
	public void reverse(){
		State st = this.before;
		before = after;
		after = st;
	}
}