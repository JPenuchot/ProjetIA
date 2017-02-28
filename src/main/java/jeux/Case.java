package jeux;

public class Case {
    String state; // permet de definir l'etat de la case (b = noir, r = blanc, - = vide)
    int x, y;

    /**
     * Default Constructor
     */
    public Case() { // Ne pas utilisé le constructeur par defaut
        this.state = "-";
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor
     */
    public Case(String state, int x, int y) {
        this.state = state;
        this.x = x;
        this.y = y;
    }

    /**
     * Setteurs
     */
    public void setState(String state) {
        this.state = state;
    }
    public void setX(int x)            { this.x = x ;}
    public void setY(int y)            { this.y = y ;}

    public void setInverseState() {
        this.state = (this.state.equals("b")) ? "r" : "b";
    }

    /**
     * Getteurs
     * @return
     */
    public String getState() { return this.state; }
    public int getX()        { return this.x; }
    public int getY()        { return this.y; }

    public String getInverseState() {
        return (this.state.equals("b")) ? "r" : "b";
    }


    // A optimiser
    public String getStringCoord() {
        String alpha = null;

        switch (this.y) {
            case 0:
                alpha = "A";
                break;
            case 1:
                alpha = "B";
                break;
            case 2:
                alpha = "C";
                break;
            case 3:
                alpha = "D";
                break;
            case 4:
                alpha = "E";
                break;
            case 5:
                alpha = "F";
                break;
            case 6:
                alpha = "G";
                break;
            case 7:
                alpha = "H";
                break;
        }

        return alpha.concat(Integer.toString(this.x+1));
    }

}
