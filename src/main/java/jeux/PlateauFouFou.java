package jeux;

import java.io.*;


public class PlateauFouFou implements Partie1 {

    public Case[][] plateau;

    /**
     * Default Construtor
     */
    public PlateauFouFou() {
        this.plateau = new Case[8][8];

        // Permet de dessiner la grille de depart
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 1 && j % 2 == 0)
                    this.plateau[i][j] = new Case("b", i, j); // b -> pion noir
                else if (i % 2 == 0 && j % 2 == 1)
                    this.plateau[i][j] = new Case("r", i, j); // r -> pion blanc
                else
                    this.plateau[i][j] = new Case("-", i, j); // - -> case vide
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

                    for(int j = 0; j < 8; j++)
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

            for(int i = 0; i < 8; i++) {
                file.write((i + 1) + " ");
                for(int j = 0; j < 8; j++)
                    file.write(this.plateau[i][j].getState()); // A changer quand le tableau sera un objet de Cellule
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
    public String[] mouvementsPossibles(String player) {
        if(player.equals("noir")) player = "b";
        else if(player.equals("blanc")) player = "r";
        else {
            System.out.println("Erreur Paramètre (PlateauFouFou.mouvementPossibles)");
        }

        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Case currentCase = this.plateau[i][j];
                if (currentCase.getState().equals(player))
                    this.searchMouvement(currentCase);
            }
        }

        return null;
    }

    /**
     * Fonction de recherche de coup en diagonal (juste en haut a gauche pour l'instant
     * @param c la case de recherche de depart
     * @return un string de toutes les coups possibles
     */
    public String searchMouvement(Case c) {

        String tabCoup = "";
        String mangeable = c.getInverseState(); // Couleurs mangeable par le joueur sur la case c;
        int i = c.getX() , j = c.getY();

        /************************************************************************************
         * BOUCLE DIAG HAUT GAUCHE
         ************************************************************************************/
        while((i > 0 && j > 0)) {
            i--;
            j--;
            if (this.plateau[i][j].getState().equals("-")) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
            } else if(this.plateau[i][j].getState().equals(mangeable)) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
                break;
            }
            else {
               // System.out.println("else : " + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord());
                break; // doit sortir des deux boucles
            }
        }


        /************************************************************************************
         * BOUCLE DIAG BAS DROITE
         ************************************************************************************/
        // Réallocation des variables avant la seconde boucle
        i = c.getX();
        j = c.getY();

        while(i < 7 && j < 7) {
            i++;
            j++;
            if (this.plateau[i][j].getState().equals("-")) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
            } else if(this.plateau[i][j].getState().equals(mangeable)) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
                break;
            }
            else {
                break; // doit sortir de la boucle
            }
        }

        /************************************************************************************
         * BOUCLE DIAG HAUT DROITE
         ************************************************************************************/
        // Réallocation des variables avant la seconde boucle
        i = c.getX();
        j = c.getY();


        while(i > 0 && j < 7) {
            i--;
            j++;
            if (this.plateau[i][j].getState().equals("-")) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
            } else if(this.plateau[i][j].getState().equals(mangeable)) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
                break;
            }
            else {
                break; // doit sortir de la boucle
            }
        }

        /************************************************************************************
         * BOUCLE DIAG BAS GAUCHE
         ************************************************************************************/
        // Réallocation des variables avant la seconde boucle
        i = c.getX();
        j = c.getY();


        while(i < 7 && j > 0) {
            i++;
            j--;
            if (this.plateau[i][j].getState().equals("-")) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
            } else if(this.plateau[i][j].getState().equals(mangeable)) {
                tabCoup = tabCoup + c.getStringCoord() + "-" + this.plateau[i][j].getStringCoord() + " ,";
                break;
            }
            else {
                break; // doit sortir de la boucle
            }
        }

        System.out.println(tabCoup);

        return tabCoup;
    }

    @Override
    public void play(String move, String player) {

    }

    @Override
    public boolean finDePartie() {
        return (getNumberCaseState("b") == 0 || getNumberCaseState("r") == 0);
    }

    private int getNumberCaseState(String state) {

        int compt = 0;

        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(this.plateau[i][j].getState().equals(state))
                    compt++;

        return compt;
    }

    /**
     * Fonction d'affichage du plateau
     */
    private void printPlateau() {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(this.plateau[i][j].getState());

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlateauFouFou p = new PlateauFouFou();

        //p.saveToFile("test.txt");
        p.setFromFile("test.txt");
        //p.printPlateau();
        p.mouvementsPossibles("blanc");

    }
}
