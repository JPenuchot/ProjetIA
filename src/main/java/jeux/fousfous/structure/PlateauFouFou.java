package structure;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import jeux.Partie1;

public class PlateauFouFou implements Partie1 {
    public State[] plateau;
    public final static int pSize = 8;

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
        this.plateau = new State[pSize * pSize];

        // Permet de dessiner la grille de depart
        for(int i = 0; i < pSize; i++) {
            for(int j = 0; j < pSize; j++) {
                if(i % 2 == 1 && j % 2 == 0)
                    this.plateau[i * pSize + j] = State.black; // b -> pion noir
                else if (i % 2 == 0 && j % 2 == 1)
                    this.plateau[i * pSize + j] = State.white; // r -> pion blanc
                else
                    this.plateau[i * pSize + j] = State.empty; // - -> case vide
            }
        }
    }

    /**
     * Builds a new instance using an existing array.
     *
     * @param      plateau  The array the new Plateau object will be built from
     */
    public PlateauFouFou(State[] plateau){
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
                        this.plateau[i * pSize + j] = StateUtils.stringToState(formatLine[j]);

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
                    file.write(StateUtils.stateToString(this.plateau[i * pSize + j])); // A changer quand le tableau sera un objet de Cellul
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


    /**
     * Fonction de recherche de coup en diagonal (juste en haut a gauche pour l'instant
     * @param c la case de recherche de depart
     * @return un string de toutes les coups possibles
     */
    public ArrayList<String> searchMouvement(int i, int j) {
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

        //  On retourne le tableau dans le cas d'une menace
        if(!res.isEmpty()){
            //String[] arrRes = new String[res.size()];
            //res.toArray(arrRes);
            return res;
        }

        //  Deuxième passe : double exploration (héhé)

        //  Première imbrication
        //  On ne vérifie plus la présence d'ennemis sur le chemin car on sait qu'il n'y en a pas

        int ni_, nj_;
        boolean found = false;

        dirTab = new boolean[4];
        for(int rad = 1; rad < pSize; rad++){
            for(int dir = 0; dir < 4; dir++){
                //  ni et nj : Case explorée
                ni = i + ((((dir >> 1) % 2) * 2) - 1) * rad;    //  Permet d'alterner entre x + i et x - i deux fois sur quatre en fonction de dir
                nj = j + (((dir % 2)        * 2) - 1) * rad;    //  Permet d'alterner entre y + j et y - j une fois sur deux en fonction de dir
                found = false;

                if(!dirTab[dir] && ni < pSize && nj < pSize && ni >= 0 && nj >= 0){
                    if(this.plateau[ni * pSize + nj] == ami)    //  On inverse si on tombe sur un ami
                    dirTab[dir] = true;

                    //  Deuxième imbrication (parcours des diagonales depuis la position (ni; nj))
                    boolean dirTab_[] = new boolean[4];
                    for(int rad_ = 1; rad_ < pSize; rad_++){
                        for(int dir_ = 0; dir_ < 4; dir_++){    //  On n'explore pas la direction de la première imbrication.
                            if(dir_ == dir || (dir & dir_) != 0)
                                continue;

                            ni_ = ni + ((((dir_ >> 1) % 2) * 2) - 1) * rad_;
                            nj_ = nj + (((dir_ % 2)        * 2) - 1) * rad_;
                            if(!dirTab_[dir_] && ni_ < pSize && nj_ < pSize && ni_ >= 0 && nj_ >= 0){
                                if(this.plateau[ni_ * pSize + nj_] == ennemi){
                                    res.add(sOrigin + "-" + convertCoordToString(ni, nj));
                                    found = true;   //  On casse les deux boucles si on trouve un ennemi lors de la 2ème exploration.
                                    break;
                                }
                                else if(this.plateau[ni_ * pSize + nj_] == ami){
                                    dirTab_[dir_] = true;
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

        // String[] arrRes = new String[res.size()];
        // res.toArray(arrRes);
        return res;
    }

    @Override
    public void play(String move, String sPlayer) {
        // Formattage du Player
        State player;
        if(sPlayer.equals("noir")) player = State.black;
        else if(sPlayer.equals("blanc")) player = State.white;
        else {
            return;
        }

        // Formattage du Move en Integer
        String[] moveTab = move.split("-");
        int iSource = this.convertStringToCoord(moveTab[0]);
        int jSource = Integer.parseInt((moveTab[0].split(""))[1]) - 1;
        int iDest = this.convertStringToCoord(moveTab[1]);
        int jDest = Integer.parseInt((moveTab[1].split(""))[1]) - 1;

        this.plateau[iSource * pSize + jSource] = State.empty;
        this.plateau[iDest * pSize + jDest] = player;
    }

    /**
     * Joue un mouvement donné et retourne les actions faites avec les états précédents
     * pour faire du backtracking.
     *
     * @param      move     Le mouvement décrit par un String
     * @param      sPlayer  Le player qui effectue le mouvement
     *
     * @return     Actions décrites avec les états précédents
     */
    public Action[] play(String move, State sPlayer) {
        // Formattage du Move en Integer
        String[] moveTab = move.split("-");
        int jSource = this.convertStringToCoord(moveTab[0]);
        int iSource = Integer.parseInt((moveTab[0].split(""))[1]) - 1;
        int jDest = this.convertStringToCoord(moveTab[1]);
        int iDest = Integer.parseInt((moveTab[1].split(""))[1]) - 1;

        //  Description des actions pour le backtracking
        Action[] res = new Action[2];

        res[0] = new Action();
        res[1] = new Action();

        res[0].i = iSource;
        res[0].j = jSource;

        res[0].before   = plateau[iSource * pSize + jSource];
        res[0].after    = State.empty;

        res[1].i = iDest;
        res[1].j = jDest;
        res[1].before   = plateau[iDest * pSize + jDest];
        res[1].after    = sPlayer;

        this.plateau[iSource * pSize + jSource] = State.empty;
        this.plateau[iDest * pSize + jDest] = sPlayer;

        return res;
    }

    /**
     * Joue un ensemble d'instructions décrit dans actions
     *
     * @param      actions  Liste d'actions
     */
    public void play(Action[] actions){
        for(Action act : actions)
            plateau[act.i * pSize + act.j] = act.after;
    }

    /**
     * Joue une action décrite par act
     *
     * @param      act   L'action décrite
     */
    public void play(Action act){
        plateau[act.i * pSize + act.j] = act.after;
    }

    @Override
    public boolean finDePartie() {
        return (getNumberCaseState(State.black) == 0 || getNumberCaseState(State.white) == 0);
    }

    private int getNumberCaseState(State state) {

        int compt = 0;

        for(int i = 0; i < pSize; i++)
            for(int j = 0; j < pSize; j++)
                if(this.plateau[i * pSize + j] == state)
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
        return letters[j] + (i+1);
    }

    /**
     * Fonction d'affichage du plateau
     */
    public void printPlateau() {
        for(int i = 0; i < pSize; i++) {
            for (int j = 0; j < pSize; j++)
                if(this.plateau[i * pSize + j] == State.black)
                    System.out.print("b");
                else if(this.plateau[i * pSize + j] == State.white)
                    System.out.print("w");
                else
                    System.out.print("-");

            System.out.println();
        }
    }

    public State[] getStateArray(){
        return this.plateau;
    }

    /**
     * Determines if over.
     *
     * @return     True if over, False otherwise.
     */
    public boolean isOver() {
        if (this.getNumberCaseState(State.black) == 0 || this.getNumberCaseState(State.white) == 0) {
            System.out.println("Is Over");
            return true;
        }

        return false;
    }
}
