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

    static State stringToState(String strState) {    return hmStringToState.get(strState);   }
    static String stateToString(State stState)  {
        switch (stState){
            case State.black:
                return "b";
            case State.white:
                return "r";
            case State.empty:
                return "-";
        }
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

    public void setInverseState() {
        this.state = (this.state == State.black) ? State.black : State.white;
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

    /** Returns the inverse state of a cell that is not empty.
    */
    public State getInverseState() {    return (this.state == State.black) ? State.black : State.white;    }

    public static State getInverseState(State st){
        return (st == State.empty) ? (State.empty) : (st == State.black ? State.white : State.black);
    }

    /**
     * Permet de transformer les coordoonées en String
     * @return
     */
    public static String getStringCoord(int i, int j) {    return Character.toString((char)(i + 'A')) + Integer.toString(j + 1);   }
}
