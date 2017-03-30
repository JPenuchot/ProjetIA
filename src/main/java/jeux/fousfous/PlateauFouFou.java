package fousfous;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import jeux.Partie1;

public class PlateauFouFou implements Partie1 {
    public Case[] plateau;
    final int pSize = 8;

    static final String[] letters = {
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
        this.plateau = new Case[pSize * pSize];

        // Permet de dessiner la grille de depart
        for(int i = 0; i < pSize; i++) {
            for(int j = 0; j < pSize; j++) {
                if(i % 2 == 1 && j % 2 == 0)
                    this.plateau[i * pSize + j] = new Case(State.black, i, j); // b -> pion noir
                else if (i % 2 == 0 && j % 2 == 1)
                    this.plateau[i * pSize + j] = new Case(State.white, i, j); // r -> pion blanc
                else
                    this.plateau[i * pSize + j] = new Case(State.empty, i, j); // - -> case vide
            }
        }
    }

    /**
     * Builds a new instance using an existing array.
     *
     * @param      plateau  The array the new Plateau object will be built from
     */
    public PlateauFouFou(Case[] plateau){
        this.plateau = plateau;
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
                        this.plateau[i * pSize + j].setState(formatLine[j]);

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
                    file.write(this.plateau[i * pSize + j].getStateAsString()); // A changer quand le tableau sera un objet de Cellul
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
        
        return true;
    }

    @Override
    public String[] mouvementsPossibles(String sPlayer) {
        State player = State.empty;

        if(sPlayer.equals("noir"))  return mouvementsPossibles(State.black);
        if(sPlayer.equals("blanc")) return mouvementsPossibles(State.white);
        
        System.out.println("Erreur Paramètre (PlateauFouFou.mouvementPossibles)");
        System.out.println(Thread.currentThread().getStackTrace());
        
        return null;
    }

    public String[] mouvementsPossibles(State player) {
        int nombrePiece = this.getNumberCaseState(State.black) + this.getNumberCaseState(State.white);

        String[] coupPossible = new String[100];
        String[] cp = new String[50];
        int compt = 0;

        for(int i = 0; i < pSize; i++) {
            for (int j = 0; j < pSize; j++) {
                Case currentCase = this.plateau[i * pSize + j];
                if (currentCase.getState() == player) {
                    cp = this.searchMouvement(currentCase);
                    for(int k = 0; k < cp.length; k++){
                        coupPossible[compt] = cp[k];
                        compt++;
                    }
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
        String sOrigin = convertCoordToString(x, y);

        ArrayList<String> res = new ArrayList<String>();

        //  Première étape : Recherche d'adversaires aux diagonales et activation des cases
        //  ----
        //  La progression se fait avec rad qui représente le rayon
        //  et dir qui itère parmi les quatre directions.
        //  dirTab permet d'indiquer quelles sont les directions à explorer
        //  (on s'arrête dans une direction donnée lorsqu'on a rencontré un ennemi)
        for(int rad = 1; rad < pSize; rad++){
            boolean dirTab[] = new boolean[4];
            for(int dir = 0; dir < 4; dir++){
                //  ni et nj : Case explorée
                ni = x + ((((dir >> 1) % 2) * 2) - 1) * rad;    //  Permet d'alterner entre x + i et x - i deux fois sur quatre en fonction de dir
                nj = y + (((dir % 2)        * 2) - 1) * rad;    //  Permet d'alterner entre y + j et y - j une fois sur deux en fonction de dir
                if(!dirTab[dir] && ni < pSize && nj < pSize && ni >= 0 && nj >= 0){
                    //  Si on trouve un ennemi, inverser dirTab[dir] et ajouter la position (ni; nj) à la liste des coups possibles
                    if(this.plateau[ni * pSize + nj].getState() == mangeable){
                        //  Ajout de la position dans le tableau de résultats
                        res.add(sOrigin + "-" + convertCoordToString(ni, nj));
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
        boolean found = false;

        for(int rad = 1; rad < pSize; rad++){
            for(int dir = 0; dir < 4; dir++){
                //  ni et nj : Case explorée
                ni = x + ((((dir >> 1) % 2) * 2) - 1) * rad;    //  Permet d'alterner entre x + i et x - i deux fois sur quatre en fonction de dir
                nj = y + (((dir % 2)        * 2) - 1) * rad;    //  Permet d'alterner entre y + j et y - j une fois sur deux en fonction de dir
                found = false;

                if(ni < pSize && nj < pSize && ni >= 0 && nj >= 0){
                    //  Deuxième imbrication (parcours des diagonales depuis la position (ni; nj))
                    for(int rad_ = 1; rad_ < pSize; rad_++){
                        for(int dir_ = 0; dir_ < 4 && dir_ != dir && (dir & dir_) == 0; dir_++){    //  On n'explore pas la direction de la première imbrication.
                            ni_ = ni + ((((dir_ >> 1) % 2) * 2) - 1) * rad_;
                            nj_ = nj + (((dir_ % 2)        * 2) - 1) * rad_;
                            if(ni_ < pSize && nj_ < pSize && ni_ >= 0 && nj_ >= 0){
                                if(this.plateau[ni_ * pSize + nj_].getState() == mangeable){
                                    res.add(sOrigin + "-" + convertCoordToString(ni, nj));
                                    found = true;   //  On casse les deux boucles si on trouve un ennemi lors de la 2ème exploration.
                                    break;
                                }
                            }
                        }
                        if(found)   //  Plus besoin de continuer l'exploration si on a trouvé un ennemi.
                            break;
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
        int iSource = this.convertStringToCoord(moveTab[0]);
        int jSource = Integer.parseInt((moveTab[0].split(""))[1]);
        int iDest = this.convertStringToCoord(moveTab[1]);
        int jDest = Integer.parseInt((moveTab[1].split(""))[1]);

        this.plateau[iSource * pSize + jSource].setState(State.empty);
        this.plateau[iDest * pSize + jDest].setState(player);
    }

    @Override
    public boolean finDePartie() {
        return (getNumberCaseState(State.black) == 0 || getNumberCaseState(State.white) == 0);
    }

    private int getNumberCaseState(State state) {

        int compt = 0;

        for(int i = 0; i < pSize; i++)
            for(int j = 0; j < pSize; j++)
                if(this.plateau[i * pSize + j].getState() == state)
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
                if(this.plateau[i * pSize + j].getState() == State.black)
                    System.out.print("b");
                else if(this.plateau[i * pSize + j].getState() == State.white)
                    System.out.print("w");
                else
                    System.out.print("-");

            System.out.println();
        }
    }
}
