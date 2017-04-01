package fousfous;

import java.util.HashMap;

enum State {
    empty,
    black,
    white
}

public class Case {
    //  HashMaps that makes the correspondance from String to State
    static private HashMap<String, State> hmStringToState = new HashMap<String, State>();

    //  HashMap initialization
    static {
        hmStringToState.put("-", State.empty);
        hmStringToState.put("b", State.black);
        hmStringToState.put("r", State.white);
    }

    public static State stringToState(String strState) {    return hmStringToState.get(strState);   }
    public static String stateToString(State stState)  {
        switch (stState){
            case black:
                return new String("b");
            case white:
                return new String("r");
            case empty:
                return new String("-");
        }
        return null;
    }

    State state; // Defines the state of the cell (b = State.black, r = State.white, - = State.empty)

    /**
     * Constructor
     */
    public Case(State state) {
        this.state = state;
    }

    /** Constructor : Sets an State.empty cell
     */
    public Case() {
        this.state = State.empty;
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
        this.state = stringToState(state);
    }

    /**
     * Getters
     */

    /** Returns the state of the cell.
    */
    public State getState() { return this.state; }

    /** Returns the state of the cell as a string.
    */
    public String getStateAsString() { return stateToString(this.state); }

    public static State getInverseState(State st){
        return (st == State.empty) ? (State.empty) : (st == State.black ? State.white : State.black);
    }

    /**
     * Permet de transformer les coordoonées en String
     * @return
     */
    public static String getStringCoord(int i, int j) {    return Character.toString((char)(i + 'A')) + Integer.toString(j + 1);   }
}
