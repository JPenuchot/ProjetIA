package jeux;

import java.io.*;

public class PlateauFouFou implements Partie1 {

    public String[][] plateau; //  A changer en tableau d'une classe CELLULE


    public PlateauFouFou() {
        this.plateau = new String[8][8];


        // Permet de dessiner la grille de depart
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(i % 2 == 1 && j % 2 == 0)
                    this.plateau[i][j] = "b"; // b -> pion noir
                else if (i % 2 == 0 && j % 2 == 1)
                    this.plateau[i][j] = "r"; // r -> pion blanc
                else
                    this.plateau[i][j] = "-"; // - -> case vide
            }
        }
    }

    @Override
    public void setFromFile(String fileName) {

    }

    @Override
    public void saveToFile(String fileName) {
        
    }


    @Override
    public boolean estValide(String move, String player) {
        return true;
    }

    @Override
    public String[] mouvementsPossibles(String player) {
        return null;
    }

    @Override
    public void play(String move, String player) {

    }

    @Override
    public boolean finDePartie() {
        return true;
    }

    public void printPlateau() {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(this.plateau[i][j]);

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
