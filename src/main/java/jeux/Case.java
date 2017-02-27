package jeux;

public class Case {
    String state; // permet de definir l'etat de la case (b = noir, r = blanc, - = vide)

    public Case() {
        this.state = "-"
    }

    public Case(String state) {
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

}
