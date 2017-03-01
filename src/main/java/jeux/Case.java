package jeux;

enum State {
    empty,
    black,
    white
}

//  HashMap from String to State
HashMap<String, State> hmStringToState = new HashMap<String, State>();

public class Case {
    State state; // Defines the state of the cell (b = State.black, r = State.white, - = State.empty)
    int x, y;

    /**
     * Constructor
     */
    public Case(State state, int x, int y) {
        this.state = state;
        this.x = x;
        this.y = y;
    }

    /** Constructor : Sets an State.empty cell
     * @param x : x position
     * @param y : y position
     */
    public Case(int x, int y) {
        this.state = State.empty;
        this.x = x;
        this.y = y;
    }

    /**
     * Setters
     */

    /** Sets the state of a cell.
    */
    public void setState(State state) {
        this.state = state;
    }

    public void setState(String state){
        if(state.equals("b"))
    }
    public void setX(int x)            { this.x = x ;}
    public void setY(int y)            { this.y = y ;}

    public void setInverseState() {
        this.state = (this.state == State.black) ? State.black : State.white;
    }

    /**
     * Getters
     */

    /** Returns the state of the cell.
    */
    public State getState() { return this.state; }

    /** Sets the horizontal position.
    */
    public int getX()        { return this.x; }

    /** Sets the vertical position.
    */
    public int getY()        { return this.y; }

    /** Returns the inverse state of a cell that is not empty.
    */
    public State getInverseState() {    return (this.state == State.black) ? State.black : State.white;    }


    /**
     * Permet de transformer les coordoonées en String
     * @return
     */
    public String getStringCoord() {    return Character.toString((char)(this.y + 'A')) + Integer.toString(this.x+1);   }
}
