package jeux;

import java.io.*;


public class PlateauFouFou implements Partie1 {

    public Case[][] plateau;


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

    private void printPlateau() {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(this.plateau[i][j].getState());

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlateauFouFou p = new PlateauFouFou();

        p.saveToFile("test.txt");
        p.setFromFile("test.txt");
        p.printPlateau();

    }
}
