package jeux;

public class Case {
    String state; // permet de definir l'etat de la case (b = noir, r = blanc, - = vide)
    int x, y;

    public Case() { // Ne pas utilisé le constructeur par defaut
        this.state = "-";
        this.x = 0;
        this.y = 0;
    }

    public Case(String state, int x, int y) {
        this.state = state;
        this.x = x;
        this.y = y;
    }

    public void setState(String state) {
        this.state = state;
    }
    public void setX(int x)            { this.x = x ;}

    public String getState() {
        return this.state;
    }
    public int getX()        { return this.x; }

}
