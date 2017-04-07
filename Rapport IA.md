#Rapport Foufou




##Introduction : 

Notre système s’organise autour de l’utilisation d’un tableau à une dimension ainsi que l’utilisation d’énumération comme State qui permet de connaitre le type d’une case. C’est-à-dire si elle contient une pièce de couleur Noir, Blanche ou vide. La partie suivant décrit l'architecture du programme ainsi qu'une breve description des classes.


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
|    |    |    |    |    ├ Memo                                 - Dossier contenant la structure de mémorisation
|    |    |    |    |    |  ├ BaseAlphaBeta                     -
|    |    |    |    |    |  ├ MemoAlphaBeta                     -
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

Notre plateau de jeu est un tableau de State. On peut ici voir que State peut avoir trois « état » différents. Toutes nos algorithmes, fonctions utilise cette énumération. Au début, nous avions utilisé une classe nommé Case, qui nous permettait de stocker les informations sur chaque case, mais nous nous sommes vite rendu compte que ceci n’était pas indispensable et que donc nous utilisions de la mémoire inutilement. 

Pour manipuler les States, nous avons 4 méthodes implémenté dans une classe StateUtils.
stringToState : permet de convertir un string en state grâce à une hashMap 
stateToString : permet de convertir un state en string
getInverseState : permet de données l’inverse d’une state ( Blanc -> Noir, Noir -> Blanc, Empty -> Empty)
getStringCoord : permet de convertir une chaine « A2-B4 » en coordonnée i, j

Je ne détaillerais pas toutes les fonctions car nous avons tenu à commenter au mieux notre code pour que celui-ci soit lisible et pourquoi pas réutilisé par la suite.


##Fonction MouvementsPossibles : 

Comme nous l’avons expliqué au-dessus, notre programme utilise en majorité l’Enumération State. Or nous devions utiliser les fonctions implémenter par l’interface qui nous était donné. C’est pourquoi nous avons **surchargé** la fonction mouvementsPossibles. Ici on peut voir la fonction de base qui prends en paramètre un string.


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



Voici notre fonction surchargé mouvementsPossibles qui nous permet d’utiliser les States. Nous avons fait le choix d’utiliser une ArrayList<String> pour stocker nos coups plutôt qu’un tableau non dynamique. En effet, une ArrayList nous permet donc de ne pas alloué un tableau trop grand dès le début ou de ne pas faire une réallocation d’un tableau a la main. 
Notre variable coupPossibles sera donc une ArrayList de String contenant tous les coups possibles des pièces. La variable cpUnique contiendra tout les coups possibles pour une pièce données. Notre boucle nous permet de parcourir tous le tableau et d’ensuite chercher les cases contenant le State passer précédemment en paramètre. 

Les lignes : 

 ```java
 cpUnique = this.searchMouvement(i, j);

 coupPossible.addAll(cpUnique);
 ```

permettent de stocker tous les mouvements possibles pour une pièce de coordonnées i , j dans la variables cp. Et ensuite d’ajouter à coupPossible tous les coups pour la pièce données. Nous détaillerons ensuite la fonction searchMouvement qui permet la recheche des mouvements possibles dans notre tableau.

Comme la fonction de retour est un tableau non dynamique de String, nous devons ensuite convertir notre ArrayList<String> grâce à la dernière ligne.

##Fonction searchMouvement :

// TODO

##Fonction Play : 

De même que pour la fonction mouvementPossibles, nous avons surchager la fonction play pour qu’elle puisse correspondre à notre choix statégique (State, Action).

Je vais ici détailler la conversion des mouvements en coordonnées. Je prendrais comme exemple, le mouvement : **A2 – D5**

 ```java
 // Formattage du Move en Integer
        String[] moveTab = move.split("-");
        int jSource = this.convertStringToCoord(moveTab[0]);
        int iSource = Integer.parseInt((moveTab[0].split(""))[1]) - 1;
        int jDest = this.convertStringToCoord(moveTab[1]);
        int iDest = Integer.parseInt((moveTab[1].split(""))[1]) - 1;
```

D’abord, nous découpons les deux parties du mouvement : ["A2", "D5"]. Ensuite, nous nous occupons de la première partie qui corresponds aux coordonnées de la case source (iSource, jSource). iSource est calculé grâce à une fonction qui convertis les lettres (A, B, C …) en entier (1, 2, 3 …). Pour obtenir jSource, nous avons d’abord découper notre mouvement source en deux : ["A","2"], puis convertis l’entier 2 en integer pour ensuite lui soustraire 1. Cette soustraction vient du fait que nos coordonnées vont de 0 à 7 alors que les mouvements vont de 1 à 8. Voila comment nous transformons un mouvement en coordonnées.

Nous avons trois autres implémentations de la méthode play, voici leurs prototypes :

```java
public Action[] play(String move, State sPlayer)
public void play(Action[] actions)
public void play(Action act)
```

1.	Permet de retourner les mouvements sous forme d'Action apres les avoir joués
2.	Permet de jouer une Action
3.	Permet de jouer un tableau d’Actions


##Fonction isOver :

La méthode isOver est notre fonction d’arrêt. Elle permet donc de savoir quand arreter le jeu. Elle renvoie juste vrai lorsqu’on nous n’avons plus qu’une unique couleur de piece sur le plateau.


##Classe Action :

Lors des dernières implémentations des algorithme de recherche, nous etions embetés par le fait de devoir à chaque fois copier le plateau de jeu pour pouvoir ensuite faire des recheches dessus sans pour autant « modifier » le plateau (celui avant la recherche). Nous avons donc d’utiliser une classe Action qui va nous permettre de sauvegarder les mouvements effectués lors de la recherche et donc de pouvoir les inverser par la suite pour revenir à l’état initiale.

// TODO


##Optimisation:
###Mémorisation :

On sait que les algorithmes de recherche tels que AlphaBeta ou MinMax parcourt le plateau de jeu en jouant des coups et en calculant les heuristiques des feuilles. Or dans plusieurs cas, la configuration du plateau est la même que lors d'une exploration précédante. Nous avons donc voulu mettre en place un système de mémorisation. Ce systeme va stocker en mémoire les états du tableau ainsi que quelques paramètre associés (Valeurs d'alpha et beta, profondeur d'exploration...). Le but est donc de "checker" à chaque itération d'alphaBeta si on ne s'est pas deja retrouvé dans ce cas pour éviter de refaire un passage de l'algorithme et donc gagner un temps précieux. 

##Profondeur incrémentale :

Nous avons choisis de mettre en place un système de profondeur incrémentale en fonction du nombre de pièce restant sur le plateau. Ce qui nous permettra de faire une recherche approfondis vers la fin du jeu et donc d'obtenir de meilleurs résultat dans un temps raisonnable.


##Heuristiques :

###Heuristique DiffPions
Pour le choix des heuristiques, nous avons avant tout de suite penser à faire quelque chose de simple à calculer au vu du nombre de fois ou celle-ci sera calculée. Nous avons donc choisir de calculer la différence entre les pièces noires et les pièces blanches : 

```java
public float estimate(PlateauFouFou plateau, State joueur){
		float res = 0.f;
		for(int i = 0; i < PlateauFouFou.pSize * PlateauFouFou.pSize; i++){
			res += plateau.getStateArray()[i] == joueur ? 1.f : -1.f;
		}
		return res;
	}
```

Notre fonction estimate de l’Heuristique prends deux paramètres, le plateau actuelle ainsi que le joueurs qui appel celle-ci. On peut ici voir que la boucle parcourt tout notre plateau de jeux (pSize était notre largeur et hauteur). 

La ligne : 

```java
res += plateau.getStateArray()[i] == joueur ? 1.f : -1.f;
```

Ajoute +1 a res si la case qu’on explore contient un pion du Joueur et retire 1 sinon. Ce qui nous permet donc d’avoir la différence entre le nombre de pion noir et blanc sur le plateau.

###Heuristique Minimiser

Une autre heuristique assez simple à calculer est celle qui permet de minimiser les pions adverses.

```java
public float estimate(PlateauFouFou plateau, State joueur) {
        return -plateau.getNumberCaseState(StateUtils.getInverseState(joueur));
}
```
La méthode `getNumberCaseState` nous renvoie le nombre de pions d'un State passé en paramètre. Ici on retourne donc l'inverse pour rendre le nombre le plus possibles. Plus le resultat est grand, plus l'inverse est petit et donc on évite d'être dans ce cas.

##GitHub : 

Pour l'organisation du projet, nous avons décidé de mettre en place un GitHub. Grâce à celui-ci, nous avons pu mettre notre projet en commum et pouvoir donc travailler en même temps sans avoir à recopier apres tout ce qui avait été fait par le binome. Cela nous a donc appris à s'orgiser avec cette outils très utilisé et très répandu dans le monde du travail.

##Difficultés rencontrées :

Durant ce projet, nous avons rencontré plusieurs difficultées. Tout d'abord comment rechercher les mouvements possibles en temps optimal. C'est-à-dire ne pas faire quatre (dans chaque direction) explorations du plateau. Le but de ce projet, en plus de coder une intélligence artificielle, était d'avoir le code le plus optimal possible.


##Amélioration envisagées : 

1. L'intelligence artificielle et le machine learning étant étroitement liée, nous voulions essayer de coder un systeme de machine learning afin d'avoir une intélligence artificielle presque imbattable. Or nous n'avons pas eu le temps de le faire. Nous allons donc essayé d'apporter cette amélioration majeur dans les mois qui suivent.
2. Lors de l'exploration des arbres de jeu (avec alpha Beta par exemple), de nombre parcourt se ressemblent. Il faudrait donc sauvegarder l'etat de l'arbre ainsi que la valeurs des heuristiques sur les noeuds afin de gagner un temps precieux.


##Conclusion : 

Ce projet nous a appris beaucoup. En effet, nous avons du nous organiser dans un projet à plusieurs. Nous avons du être rigoureux dans l'organisation du github, savoir comment découper le travail etc. Nous avons aussi beaucoup appris sur l'intelligence artificielle car nous devions tout coder et donc nous avons choisis une statégie (qui on l'espère sera payante) pour résoudre le problème posé.


