public interface Partie1{
	/**	Initialise un plateau à partir d'un fichier texte
	*	@param fileName	le nom du fichier à lire
	*/
	public void setFromFile(String fileName);

	/**	Sauve la configuration d'un plateau dans un fichier
	*	@param fileName le nom du fichier à sauvegarder
	*
	*	Le format doit être compatible avec celui utilisé pour la lecture
	*/
	public void saveToFile(String fileName);

	/**	Indique si le coup <move> est valide pour le joueur <player> sur le plateau courant.
	*	@param	move	le coup à jouer sous la forme "A1-B2"
	*	@param	player	le joueur qui joue (représenté par "noir" ou "blanc")
	*/
	public boolean estValide(String move, String player);

	/**	Calcule les coups possibles pour le joueur <player> sur le plateau courant.
	*	@param	player	le joueur qui joue (représenté par "noir" ou "blanc")
	*/
	public String[] mouvementsPossibles(String player);

	/**	Modifie le plateau en jouant le coup move.
	*	@param	move	le coup à jouer sous la forme "A1-B2"
	*	@param	player	le joueur qui joue (représenté par "noir" ou "blanc")
	*/
	public void play(String move, String player);

	/**	Vrai lorsque le plateau correspond à une fin de partie.
	*/
	public boolean finDePartie();
}