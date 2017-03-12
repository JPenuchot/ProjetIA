package jeux;

import java.io.*;
import java.util.ArrayList;

public class PlateauFouFou implements Partie1 {

    public Case[][] plateau;
    final int pSize = 8;
    static final String[] letters = { //  TODO : Générer dynamiquement
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H"
    };

    /**
     * Default Construtor
     */
    public PlateauFouFou() {
        this.plateau = new Case[pSize][pSize];

        // Permet de dessiner la grille de depart
        for(int i = 0; i < pSize; i++) {
            for(int j = 0; j < pSize; j++) {
                if(i % 2 == 1 && j % 2 == 0)
                    this.plateau[i][j] = new Case(State.black, i, j); // b -> pion noir
                else if (i % 2 == 0 && j % 2 == 1)
                    this.plateau[i][j] = new Case(State.white, i, j); // r -> pion blanc
                else
                    this.plateau[i][j] = new Case(State.empty, i, j); // - -> case vide
            }
        }
    }

    @Override
    public void setFromFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            int i = 0; // permet de parcourir les lignes

            while(line != null) {
                line = reader.readLine();
                if(line == null) // permet de ne pas afficher la derniere ligne
                    continue;
                if(!line.startsWith("%")) { // n'affiche pas les lignes "commentaire"
                    String[] formatLine = line.split(" ")[1].split(""); // Barbu Line ! ^^ permet de retirer les chiffre des lignes puis de decouper chaque case

                    for(int j = 0; j < pSize; j++)
                        this.plateau[i][j].setState(formatLine[j]);

                    i++;
                }
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not Found ");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveToFile(String fileName) {
        try {
            PrintWriter file = new PrintWriter(fileName);

            file.write("% ABCDEFGH\n");

            for(int i = 0; i < pSize; i++) {
                file.write((i + 1) + " ");
                for(int j = 0; j < pSize; j++)
                    file.write(this.plateau[i][j].getStateAsString()); // A changer quand le tableau sera un objet de Cellule
                file.write(" " + (i + 1) + "\n");
            }

            file.write("% ABCDEFGH\n");

            file.flush();
            file.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not Found ");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean estValide(String move, String player) {
        //  TODO
        return true;
    }

    @Override
    public String[] mouvementsPossibles(String sPlayer) {
        State player = State.empty;

        if(sPlayer.equals("noir")) player = State.black;
        else if(sPlayer.equals("blanc")) player = State.white;
        else {
            System.out.println("Erreur Paramètre (PlateauFouFou.mouvementPossibles)");
            System.out.println(Thread.currentThread().getStackTrace());
            return null;
        }

        int nombrePiece = this.getNumberCaseState(State.black) + this.getNumberCaseState(State.white);

        String[] coupPossible = new String[nombrePiece];
        int compt = 0;

        for(int i = 0; i < pSize; i++) {
            for (int j = 0; j < pSize; j++) {
                Case currentCase = this.plateau[i][j];
                if (currentCase.getState() == player) {
                    coupPossible[compt] = this.searchMouvement(currentCase);
                    compt++;
                }
            }
        }

        return coupPossible;
    }

    /**
     * Fonction de recherche de coup en diagonal (juste en haut a gauche pour l'instant
     * @param c la case de recherche de depart
     * @return un string de toutes les coups possibles
     */
    public String[] searchMouvement(Case c) {
        State mangeable = c.getInverseState(); // Couleurs mangeable par le joueur sur la case c;
        int x = c.getX() , y = c.getY();
        int ni, nj;

        ArrayList<String> res = new ArrayList<String>();

        //  Première étape : Recherche d'adversaires aux diagonales et activation des cases
        //  ----
        //  La progression se fait avec rad qui représente le rayon
        //  et dir qui itère parmi les quatre directions.
        //  dirTab permet d'indiquer quelles sont les directions à explorer
        //  (on s'arrête dans une direction donnée lorsqu'on a rencontré un ennemi)
        for(int rad = 0; rad < pSize; rad++){
            boolean dirTab[] = new boolean[4];
            for(int dir = 0; dir < 4; dir++){
                //  ni et nj : Case explorée
                ni = x + ((((dir >> 1) % 2) * 2) - 1) * rad;    //  Permet d'alterner entre x + i et x - i deux fois sur quatre en fonction de dir
                nj = y + (((dir % 2)        * 2) - 1) * rad;    //  Permet d'alterner entre y + j et y - j une fois sur deux en fonction de dir
                if(!dirTab[dir] && ni < pSize && nj < pSize && ni >= 0 && nj >= 0){
                    //  Si on trouve un ennemi, inverser dirTab[dir] et ajouter la position (ni; nj) à la liste des coups possibles
                    if(this.plateau[ni][nj].getState() == mangeable){
                        //  Ajout de la position dans le tableau de résultats
                        res.add(convertCoordToString(ni, nj));  //  TODO : Corriger
                        dirTab[dir] = !dirTab[dir];
                    }
                }
            }
        }

        //  On retourne le tableau dans le cas d'une menace
        if(!res.isEmpty()){
            String[] arrRes = new String[res.size()];
            res.toArray(arrRes);
            return arrRes;
        }

        //  Deuxième passe : double exploration (héhé)

        //  Première imbrication
        //  On ne vérifie plus la présence d'ennemis sur le chemin car on sait qu'il n'y en a pas

        int ni_, nj_;

        for(int rad = 0; rad < pSize; rad++){
            for(int dir = 0; dir < 4; dir++){
                //  ni et nj : Case explorée
                ni = x + ((((dir >> 1) % 1) * 2) - 1) * rad;    //  Permet d'alterner entre x + i et x - i deux fois sur quatre en fonction de dir
                nj = y + (((dir % 1)        * 2) - 1) * rad;    //  Permet d'alterner entre y + j et y - j une fois sur deux en fonction de dir
                if(ni < pSize && nj < pSize && ni >= 0 && nj >= 0){
                    //  Deuxième imbrication (parcours des diagonales depuis la position (ni; nj))
                    for(int rad_ = 0; rad_ < pSize; rad_++){
                        boolean dirTab[] = new boolean[4];
                        for(int dir_ = 0; dir_ < 4; dir_++){
                            ni_ = ni + ((((dir_ >> 1) % 1) * 2) - 1) * rad_;
                            nj_ = nj + (((dir_ % 1)        * 2) - 1) * rad_;
                            if(!dirTab[dir_] && ni_ < pSize && nj_ < pSize && ni_ >= 0 && nj_ >= 0){
                                if(this.plateau[ni_][nj_].getState() == mangeable){
                                    res.add(convertCoordToString(ni_, nj_));  //  TODO : Corriger
                                    dirTab[dir_] = !dirTab[dir_];
                                }
                            }
                        }
                    }
                    //  Fin deuxième imbrication
                }
            }
        }

        String[] arrRes = new String[res.size()];
        res.toArray(arrRes);
        return arrRes;
    }

    @Override
    public void play(String move, String sPlayer) {
        // Formattage du Player
        State player;
        if(sPlayer.equals("noir")) player = State.black;
        else if(sPlayer.equals("blanc")) player = State.white;
        else {
            System.out.println("Erreur Paramètre (PlateauFouFou.play)");
            System.out.println(Thread.currentThread().getStackTrace());
            return;
        }

        // Formattage du Move en Integer
        String[] moveTab = move.split("-");
        int xSource = this.convertStringToCoord(moveTab[0]);
        int ySource = Integer.parseInt((moveTab[0].split(""))[1]);
        int xDest = this.convertStringToCoord(moveTab[1]);
        int yDest = Integer.parseInt((moveTab[1].split(""))[1]);

        this.plateau[ySource-1][xSource].setState(State.empty);
        this.plateau[yDest-1][xDest].setState(player);
    }

    @Override
    public boolean finDePartie() {
        return (getNumberCaseState(State.black) == 0 || getNumberCaseState(State.white) == 0);
    }

    private int getNumberCaseState(State state) {

        int compt = 0;

        for(int i = 0; i < pSize; i++)
            for(int j = 0; j < pSize; j++)
                if(this.plateau[i][j].getState() == state)
                    compt++;

        return compt;
    }


    /**
     * Converti les lettres (A, B, C ..) en coordonnées numérique (1, 2 ,3)
     * @param move le move a convertir sous la forme : (Ex : B2)
     * @return le numero de la colonne (Ex : 1)
     */
    public static int convertStringToCoord(String move) {
        String[] x = move.split("");

        if(x[0].equals("A")) return 0;
        else if(x[0].equals("B")) return 1;
        else if(x[0].equals("C")) return 2;
        else if(x[0].equals("D")) return 3;
        else if(x[0].equals("E")) return 4;
        else if(x[0].equals("F")) return 5;
        else if(x[0].equals("G")) return 6;
        else if(x[0].equals("H")) return 7;
        else                      return -1;
    }

    public static String convertCoordToString(int i, int j){
        return letters[i] + j;
    }

    /**
     * Fonction d'affichage du plateau
     */
    public void printPlateau() {
        for(int i = 0; i < pSize; i++) {
            for (int j = 0; j < pSize; j++)
                System.out.print(this.plateau[i][j].getState());

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlateauFouFou p = new PlateauFouFou();

        //p.saveToFile("test.txt");
        p.setFromFile("test.txt");
        p.printPlateau();
//        p.mouvementsPossibles("blanc");
        p.play("C6-B5", "blanc");
        System.out.println("PRINT NEW TABLEAU");
        p.printPlateau();
    }
}
