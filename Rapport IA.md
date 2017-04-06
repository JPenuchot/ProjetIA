#Rapport Ia


Notre système s’organise autour de l’utilisation d’un tableau à une dimension ainsi que l’utilisation d’énumeration comme State qui permet de connaitre le type d’une case. C’est-à-dire si elle contient une piece de couleur Noir, Blanche ou vide.


##Architecture du programme :

```
src                                                             - Dossier source
├── main                                                        -
|    ├── java                                                   -
|    |    ├── jeux                                              -
|    |    |    ├── fousfous                                     -
|    |    |    |    ├── heuristiques                            - Dossier contenant toutes les heuristiques
|    |    |    |    |    ├── DiffPions.java                     - heuristique différence pion
|    |    |    |    |    ├── Heuristique.java                   - Interface heuristique
|    |    |    |    ├── joueurs                                 - Dossier contenant tous nos joueurs
|    |    |    |    |    ├── IJoueur.java                       - Interface Joueur
|    |    |    |    |    ├── JoueurAleatoire.java               - Joueur jouant aléatoirement
|    |    |    |    |    ├── JoueurAlphaBeta.java               - Joueur jouant AlphaBeta
|    |    |    |    ├── structure                               - Dossier contenant la structure du Jeu
|    |    |    |    |    ├── Action.java                        - Action decrivant un mouvement
|    |    |    |    |    ├── PlateauFouFou.java                 - Implémentation principale du Jeu
|    |    |    |    |    ├── PlateauUtils.java                  -
|    |    |    |    |    ├── State.java                         - Enumération State
|    |    |    |    |    ├── StateUtils.java                    - Manipulation des States
|    |    |    |    ├── Applet.java                             - Affichage Graphique
|    |    |    |    ├── ClientJeu.java                          - Connection Serveur
|    |    |    |    ├── PartieFoufou.java                       -
|    |    |    |    ├── Solo.java                               -
|    |    |    ├── Partie1.java                                 - Interface pour PlateauFoufou
```

##Énumération State : 

```java
public enum State {
    empty,
    black,
    white
}
```

Notre plateau de jeu est un tableau de State. On peut ici voir que State peut avoir trois « état » differents. Toutes nos algorithmes, fonctions utilise cette énumération. Au début, nous avions utilisé une classe nommé Case, qui nous permettait de stocker les informations sur chaque case, mais nous nous sommes vite rendu compte que ceci n’était pas indispensable et que donc nous utilisions de la mémoire inutilement. 

Pour manipuler les States, nous avons 4 méthodes implémenté dans une classe StateUtils.
stringToState : permet de convertir un string en state grâce à une hashMap 
stateToString : permet de convertir un state en string
getInverseState : permet de données l’inverse d’une state ( Blanc -> Noir, Noir -> Blanc, Empty -> Empty)
getStringCoord : permet de convertir une chaine « A2-B4 » en coordonnée i, j

Je ne détaillerais pas toutes les fonctions car nous avons tenu à commenter au mieux notre code pour que celui-ci soit lisible et pourquoi pas réutilisé par la suite.


##Fonction MouvementsPossibles : 

Comme je l’ai expliqué au dessus, notre programme utilise en majorité l’Enumération State. Or nous devions utiliser les fonctions implémenter par l’interface qui nous était donné. C’est pourquoi nous avons surchargé la fonction mouvementsPossibles. Ici on peut voir la fonction de base qui prends en parametre un string.

```java
@Override
    public String[] mouvementsPossibles(String sPlayer) {
        State player = State.empty;

        if(sPlayer.equals("noir"))  return mouvementsPossibles(State.black);
        if(sPlayer.equals("blanc")) return mouvementsPossibles(State.white);

        System.out.println("Erreur Paramètre (PlateauFouFou.mouvementPossibles)");
        System.out.println(Thread.currentThread().getStackTrace());

        return null;
    }
```

Nous récupérons donc ce string pour ensuite le convertir en State et pouvoir utiliser notre fonction surcharger.

```java
public String[] mouvementsPossibles(State player) {
        ArrayList<String> coupPossible = new ArrayList<String>();
        ArrayList<String> cpUnique = new ArrayList<String>();

        for(int i = 0; i < pSize; i++) {
            for (int j = 0; j < pSize; j++) {

                if (this.plateau[i * pSize + j] == player) {

                    cpUnique = this.searchMouvement(i, j);

                    coupPossible.addAll(cpUnique);

                }
            }
        }

        return coupPossible.toArray(new String[coupPossible.size()]);
    }
```



Voici notre fonction surchargé mouvementsPossibles qui nous permet d’utiliser les States. Nous avons fait le choix d’utiliser une ArrayList<String> pour stocker nos coups plutôt qu’un tableau non dynamique. En effet, une ArrayList nous permet donc de ne pas alloué un tableau trop grand des le debut ou de ne pas faire une réallocation d’un tableau a la main. 
Notre variable coupPossibles sera donc une ArrayList de String contenant tout les coups possibles des pieces. La variable cpUnique contiendra tout les coups possibles pour une piece données. Notre boucle nous permet de parcourir tout le tableau et d’ensuite chercher les cases contenant le State passer précedamment en parametre. 

Les lignes : 

 ```java
 cpUnique = this.searchMouvement(i, j);

 coupPossible.addAll(cpUnique);
 ```

permetttent de stocker tout les mouvement possibles pour une piece de coordonnées i , j dans la variables cp. Et ensuite d’ajouter a coupPossible tout les coups pour la piece données. Nous détaillerons ensuite la fonction searchMouvement qui permet la recheche des mouvements possibles dans notre tableau.

Comme la fonction de retour est un tableau non dynamique de String, nous devons ensuite convertir notre ArrayList<String> grâce à la derniere ligne.

##Fonction searchMouvement :

// TODO

##Fonction Play : 

De même que pour la fonction mouvementPossibles, nous avons surchager la fonction play pour qu’elle puisse correspondre a notre choix statégique (State, Action).

Je vais ici detailler la conversion des mouvements en coordonnées. Je prendrais ici comme exemples, le mouvement : A2 – D5

 ```java
 // Formattage du Move en Integer
        String[] moveTab = move.split("-");
        int jSource = this.convertStringToCoord(moveTab[0]);
        int iSource = Integer.parseInt((moveTab[0].split(""))[1]) - 1;
        int jDest = this.convertStringToCoord(moveTab[1]);
        int iDest = Integer.parseInt((moveTab[1].split(""))[1]) - 1;
```

D’abord, nous decoupons les deux parties du mouvement : ["A2", "D5"]. Ensuite, nous nous occupons de la premiere partie qui correspond aux coordonnées de la case source (iSource, jSource). iSource est calculer grace a une fonction qui convertis les lettres (A, B, C …) en entier (1, 2, 3 …). Pour obtenir jSource, nous avons d’abord decouper notre mouvement source en deux : ["A","2"], puis convertis l’entier 2 en integer pour ensuite lui soustraire 1. Cette soustraction viens du fait que nos coordonnées vont de 0 à 7 alors que les mouvements vont de 1 à 8. Voila comment nous transformons un mouvement en coordonnées.

Nous avons trois autres implémentations de la méthode play, voici leurs prototypes :

```java
public Action[] play(String move, State sPlayer)
public void play(Action[] actions)
public void play(Action act)
```

1.	Permet de retourner les mouvements sous forme d’etat apres les avoir jouer
2.	Permet de jouer une action
3.	Permet de jouer un tableau d’actions


##Fonction isOver :

La méthode isOver est notre fonction d’arret. Elle permet donc de savoir quand arreter le jeu. Elle renvoi juste vrai lorsqu’on nous n’avons plus qu’une unique couleur de piece sur le plateau.


##Classe Action :

Lors des dernieres implémentation des algorithme de recherche, nous etions embeter par le fait de devoir a chaque fois copier le plateau de jeux pour pouvoir ensuite faire des recheche dessus sans pour autant « modifier » le plateau (celui avant la recherche). Nous avons donc d’utiliser une classe Action qui va nous permettre de sauvegarder les mouvements effectué lors de la recherche et donc de pouvoir les inverser par la suite pour revenir à l’etat initiale.

// TODO



##Heuristiques :

Pour le choix des heuristiques, nous avons avant tous penser à faire quelque chose de simple à calculer au vu du nombre de fois ou celle-ci sera calculer. Nous avons donc choisir de calculer la difference entre les pieces noir et les pieces blanche : 

```java
public float estimate(PlateauFouFou plateau, State joueur){
		float res = 0.f;
		for(int i = 0; i < PlateauFouFou.pSize * PlateauFouFou.pSize; i++){
			res += plateau.getStateArray()[i] == joueur ? 1.f : -1.f;
		}
		return res;
	}
```

Notre fonction estimate de l’Heuristique prends deux paramètre, le plateau actuelle ainsi que le joueurs qui appel celle-ci. On peut ici voir que la boucle parcourt tout notre plateau de jeux (pSize était notre largeur et hauteur). 

La ligne : 

```java
res += plateau.getStateArray()[i] == joueur ? 1.f : -1.f;
```

Ajoute +1 a res si la case qu’on explore contient un pion du Joueur et retire 1 sinon. Ce qui nous permet donc d’avoir la différence entre le nombre de pion noir et blanc sur le plateau.
