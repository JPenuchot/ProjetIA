# Rapport Foufou

## Introduction

Notre système s’organise autour de l’utilisation d’un tableau à une dimension ainsi que l’utilisation d’énumération comme State qui permet de connaitre le type d’une case. C’est-à-dire si elle contient une pièce de couleur Noir, Blanche ou vide. La partie suivant décrit l'architecture du programme ainsi qu'une breve description des classes.

Les interfaces vues en TP n'ont pas été gardées car notre code n'est en aucun cas une implémentation généralisée de l'algorithme AlphaBeta sur lequel notre IA est basée, nous n'avons gardé que l'interface IJoueur par souci de simplicité et d'efficacité afin d'utiliser des structures plus adaptées à la résolution de notre problème.

## Architecture du programme

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

```

Le package structure contient la structure même du jeu tandis que le package joueurs ne contient que les implémentations des différents joueurs et heuristiques contient les heuristiques. Nous allons détailler les classes principales du programme avant de nous pencher sur les fonctions sur lesquelles elles s'appuient.

### PlateauFouFou

PlateauFouFou est la colonne vertébrale de notre code. On y stocke les états des cases dans un tableau de **State**. C'est cette classe même qui implémente la mécanique du jeu et surtout la recherche de coups possibles. Elle a été coçue de manière à ne reposer que sur le tableau de **State** de manière à éviter les effets de bord et simplifier l'exploration dans JoueurAlphaBeta pour faciliter au maximum l'implémentation de l'algorithme negAlphaBeta.

### JoueurAlphaBeta

Il s'agit de la classe qui implémente l'exploration de l'arbre de jeu, c'est donc l'IA à proprement parler. Nous avions une implémentation de cet algorithme avec un système de mémorisation contenu dans BaseAlphaBeta et MemoAlphaBeta qui toutefois n'a pas été gardé car nous avons eu des problèmes avec l'implémentation de negAlphaBeta, nous avons donc supprimé la couche de mémorisation pour faciliter son debug.

### State : 

```java
public enum State {
    empty,
    black,
    white
}
```

Notre plateau de jeu est un tableau de State. On peut ici voir que State peut avoir trois « états » différents. Tous nos algorithmes, fonctions utilisent cette énumération. Au début, nous avions utilisé une classe nommé Case, qui nous permettait de stocker des informations supplémentaires pour chaque case comme leur position, mais nous avons décidé de la retirer afin de simplifier le code et de tourner vers un type enum, ce qui de plus nous permet d'utiliser les opérateurs =, !=, etc. pour les comparaisons et affectations (pas besoin de passer par des fonctions auxiliaires lourdes en coût et rendant le code illisible).

Pour manipuler les States, nous avons 4 méthodes implémenté dans une classe StateUtils :

-   stringToState : permet de convertir un string en state grâce à une hashMap 
-   stateToString : permet de convertir un state en string
-   getInverseState : permet de données l’inverse d’une state ( Blanc -> Noir, Noir -> Blanc, Empty -> Empty)

### Action

Action est une classe très simple permettant de décrire une action. Nous définissons une action par le changement d'état d'une case à un autre. Une action stocke donc l'état précédent de la case et son état courant. Elle dispose d'une fonction simple permettant d'intervertir les deux états pour faire un backtrack des actions lors de l'exploration. Ainsi seul un tableau est nécessaire pour toute l'exploration et on économise un nombre précieux de copies mémoires puisqu'elles se limitent à deux fois une paire d'entiers et une paire d'états. Les actions sont renvoyées à chaque appel de play() dans la structure PlateauFouFou.

### MemoAlphaBeta

MemoAlphaBeta est un élément de mémoire associé à un état. On l'utilise pour stocker les valeurs alpha/beta d'un noeud lors de l'exploration ainsi que la profondeur à laquelle cet état a été exploré.

### BaseAlphaBeta

BaseAlphaBeta assure la correspondance entre un état du jeu et le MemoAlphaBeta. La classe repose sur un HashMap ayant pour clé un array de State pour être sûrs que la correspondance est faite sur un état du plateau sans prendre en compte les éventuels effets de bord de la classe PlateauFouFou.

## Fonction MouvementsPossibles

Nous avons remplacé la fonction mouvementsPossibles par la nôtre utilisant un enum State au lieu d'un String, toujours pour simplifier le code.

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

La fonction mouvementsPossibles s'appuie donc sur searchMouvement qui vérifie les mouvements possibles depuis une case donnée et les retourne dans un ArrayList<String>.

Les lignes : 

 ```java
 cpUnique = this.searchMouvement(i, j);

 coupPossible.addAll(cpUnique);
 ```

permettent de stocker tous les mouvements possibles pour une pièce de coordonnées i , j dans la variables cp. Et ensuite d’ajouter à coupPossible tous les coups pour la pièce données. Nous détaillerons ensuite la fonction searchMouvement qui permet la recheche des mouvements possibles dans notre tableau.

Comme la fonction de retour est un tableau non dynamique de String, nous devons ensuite convertir notre ArrayList<String> grâce à la dernière ligne.

## Fonction searchMouvement

La fonction searchMouvement nous a donné beaucoup de mal. Nous avons pris le soin de faire une fonction presque optimale car le traitement est relativement lourd et la fonction est appelée à chaque noeud.

Elle se décline en deux parties (détaillées en commentaires)

##### La première exploration

```Java
int ni, nj;
State ami = this.plateau[i * pSize + j];
State ennemi = StateUtils.getInverseState(ami);

String sOrigin = convertCoordToString(i, j);

ArrayList<String> res = new ArrayList<String>();

//  Première étape : Recherche d'adversaires aux diagonales et activation des cases
//  ----
//  La progression se fait avec rad qui représente le rayon
//  et dir qui itère parmi les quatre directions.
//  dirTab permet d'indiquer quelles sont les directions à explorer
//  (on s'arrête dans une direction donnée lorsqu'on a rencontré un ennemi)
boolean dirTab[] = new boolean[4];

for(int rad = 1; rad < pSize; rad++){
    for(int dir = 0; dir < 4; dir++){
        //  ni et nj : Case explorée
        ni = i + ((((dir >> 1) % 2) * 2) - 1) * rad;    //  Permet d'alterner entre x + i et x - i deux fois sur quatre en fonction de dir
        nj = j + (((dir % 2)        * 2) - 1) * rad;    //  Permet d'alterner entre y + j et y - j une fois sur deux en fonction de dir
        if(!dirTab[dir] && ni < pSize && nj < pSize && ni >= 0 && nj >= 0){
            //  Si on trouve un ennemi, inverser dirTab[dir] et ajouter la position (ni; nj) à la liste des coups possibles
            if(this.plateau[ni * pSize + nj] == ennemi){
                //  Ajout de la position dans le tableau de résultats
                res.add(sOrigin + "-" + convertCoordToString(ni, nj));
                dirTab[dir] = true;
            }
            else if(this.plateau[ni * pSize + nj] == ami){
                //  Un ami bloque l'exploration
                dirTab[dir] = true;
            }
        }
    }
}
```

Bien que le code soit assez dur à comprendre le principe est assez simple. La première boucle ```for(int rad = 1; rad < pSize; rad++)``` itère de 0 à la taille du tableau pour faire augmenter le rayon d'exploration, quant à ```for(int dir = 0; dir < 4; dir++)```, elle permet d'itérer parmi les 4 directions possibles. Ainsi cette partie du code permet d'explorer les 4 directions avec un rayon incrémental.

```
ni = i + ((((dir >> 1) % 2) * 2) - 1) * rad;
nj = j + (((dir % 2)        * 2) - 1) * rad;
```

En itérant rad de 0 à 3 on passe par les valeurs binaires suivantes : 0b00, 0b01, 0b10, 0b11

On multiplie le premier bit par 2 puis on lui soustrait 1 afin qu'il alterne entre -1 et 1, de même pour le deuxième bit, et on les multiplie par le rayon d'exploration pour itérer parmi les 4 cases pour un rayon. La condition ```if(!dirTab[dir] && ni < pSize && nj < pSize && ni >= 0 && nj >= 0)``` nous permet de nous assurer que l'on reste bien dans les dimensions.

```dirTab[]``` quant à lui sert à stocker les directions qu'il faut ou non continuer à explorer. Les valeurs sont à 0 par défaut, on arrête d'explorer une direction si sa valeur passe à true (facile).

Enfin si on tombe sur un ami, on arrête d'explorer la direction en question et si on tombe sur un ennemi, on rajoute le coup menant à lui depuis les coordonnées i j dans le tableau résultant.

##### La deuxième exploration (Si la première n'a pas trouvé de coup possible)

La deuxième exploration est identique à ceci près qu'elle est double : on utilise le même algo pour explorer les diagonales du joueur, puis pour chaque case explorée les directions perpendiculaires pour trouver un ennemi. On prend soin de ne pas explorer une direction perpendiculaire grâce à ces lignes :

```
if(dir_ == dir || (dir_ ^ dir) == 3)
    continue;
```

Lors de la deuxième boucle d'exploration, si on rencontre un ennemi, on rajoute donc la case de départ de cette deuxième imbrication comme point d'arrivée pour notre coup qui sera ajouté à la liste des coups possibles.

## Fonction PlateauFouFou.play

La fonction play a été surchargée de manière à pouvir jouer un mouvement décrit par un String ou un mouvement décrit par des objets Action. Un mouvement effectué avec un String renvoie les actions effectuées pour le backtracking dans l'exploration de negAlphaBeta.

Je vais ici détailler la conversion des mouvements en coordonnées. Je prendrais comme exemple, le mouvement : **A2 – D5**

 ```java
 // Formattage du Move en Integer
        String[] moveTab = move.split("-");
        int jSource = this.convertStringToCoord(moveTab[0]);
        int iSource = Integer.parseInt((moveTab[0].split(""))[1]) - 1;
        int jDest = this.convertStringToCoord(moveTab[1]);
        int iDest = Integer.parseInt((moveTab[1].split(""))[1]) - 1;
```

D’abord, nous découpons les deux parties du mouvement : ["A2", "D5"]. Ensuite, nous nous occupons de la première partie qui corresponds aux coordonnées de la case source (iSource, jSource). iSource est calculé grâce à une fonction qui convertis les lettres (A, B, C …) en entier (1, 2, 3 …). Pour obtenir jSource, nous avons d’abord découper notre mouvement source en deux : ["A","2"], puis convertis l’entier 2 en integer pour ensuite lui soustraire 1. Cette soustraction vient du fait que nos coordonnées vont de 0 à 7 alors que les mouvements vont de 1 à 8. Voilà comment nous transformons un mouvement en coordonnées.

## JoueurAlphaBeta.negAlphaBeta

```Java
public int negAlphaBeta(int p, int alpha, int beta, int parite) {
    if (p <= 0 || this.plateau.isOver()) {
        alpha = parite * this.h.estimate(this.plateau, this.player);
    } else {
        
        String[] coupPossibles = this.plateau.mouvementsPossibles(this.player);

        for(String c : coupPossibles) {
            Action[] ac;
            ac = this.plateau.play(c, this.player);
            
            alpha = Math.max(alpha, - negAlphaBeta(p-1, - beta, - alpha, - parite));
            for(Action a : ac) {
                a.reverse();
                this.plateau.play(a);
            }
            if(alpha >= beta) {
                return beta;
            }
        }
    }
    return alpha;
}
```

Cette méthode n'est que l'implémentation de l'algo negAlphaBeta dans le cadre du jeu. On utilise le système de backtrack proposé par notre classe PlateauFouFou pour éviter les copies à chaque noeud ce qui nous fait gagner un temps considérable dû à l'absence de copies de tableaux. Notre implémentation de base de cette fonction utilisait les structures en place pour mémoriser le meilleur alpha mais nous avons retiré la mémorisation pour débugger cette fonction plus efficacement afin de nous assurer que l'IA marche bel et bien. Nous n'avons pas réimplémenté la mémorisation par manque de temps.

## JoueurAlphaBeta.choixMouvement

Cette fonction est une variante de negAlphaBeta qui retient le meilleur coup possible pour le rendre. Cette fonction amorce l'exploration et permet d'implémenter l'interface IJoueur pour intégrer notre IA à la structure en place pour l'affrontement des programmes.

## Optimisation

### Mémorisation

On sait que les algorithmes de recherche tels que AlphaBeta ou MinMax parcourt le plateau de jeu en jouant des coups et en calculant les heuristiques des feuilles. Or dans plusieurs cas, la configuration du plateau est la même que lors d'une exploration précédante. Nous avons donc voulu mettre en place un système de mémorisation. Les bases de ce système sont présentes mais il a été retiré de l'exploration pour des raisons de debug, et aussi parce-que l'algorithme negAlphaBeta permet à lui seul d'explorer assez loin pour gagner facilement.

### Profondeur incrémentale

Nous avons choisi de mettre en place un système de profondeur incrémentale en fonction du nombre de pièce restant sur le plateau. Ce qui nous permettra de faire une recherche approfondis vers la fin du jeu et donc d'obtenir de meilleurs résultat dans un temps raisonnable.

## Heuristiques

### Heuristique DiffPions

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

### Heuristique Minimiser

Une autre heuristique assez simple à calculer est celle qui permet de minimiser les pions adverses.

```java
public float estimate(PlateauFouFou plateau, State joueur) {
        return -plateau.getNumberCaseState(StateUtils.getInverseState(joueur));
}
```
La méthode `getNumberCaseState` nous renvoie le nombre de pions d'un State passé en paramètre. Ici on retourne donc l'inverse pour rendre le nombre le plus possibles. Plus le resultat est grand, plus l'inverse est petit et donc on évite d'être dans ce cas.

## GitHub

Pour l'organisation du projet, nous avons décidé de mettre en place un GitHub. Grâce à celui-ci, nous avons pu mettre notre projet en commum et pouvoir donc travailler en même temps sans avoir à recopier apres tout ce qui avait été fait par le binome. Cela nous a donc appris à s'orgiser avec cette outils très utilisé et très répandu dans le monde du travail.

## Difficultés rencontrées

Durant ce projet, nous avons rencontré plusieurs difficultées. Tout d'abord comment rechercher les mouvements possibles en temps optimal. C'est-à-dire ne pas faire quatre (dans chaque direction) explorations du plateau. Le but de ce projet, en plus de coder une intélligence artificielle, était d'avoir le code le plus optimal possible.


## Amélioration envisagées

1. L'intelligence artificielle et le machine learning étant étroitement liée, nous voulions essayer de coder un systeme de machine learning afin d'avoir une intélligence artificielle presque imbattable. Or nous n'avons pas eu le temps de le faire. Nous allons donc essayé d'apporter cette amélioration majeur dans les mois qui suivent.
2. Lors de l'exploration des arbres de jeu (avec alpha Beta par exemple), de nombre parcourt se ressemblent. Il faudrait donc sauvegarder l'etat de l'arbre ainsi que la valeurs des heuristiques sur les noeuds afin de gagner un temps precieux.


## Conclusion : 

Ce projet nous a appris beaucoup. En effet, nous avons du nous organiser dans un projet à plusieurs. Nous avons du être rigoureux dans l'organisation du github, savoir comment découper le travail etc. Nous avons aussi beaucoup appris sur l'intelligence artificielle car nous devions tout coder et donc nous avons choisis une statégie (qui on l'espère sera payante) pour résoudre le problème posé.