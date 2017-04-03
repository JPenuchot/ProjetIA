package structure;

import java.util.HashMap;

public class StateUtils {
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

    public static State getInverseState(State st){
        return (st == State.empty) ? (State.empty) : (st == State.black ? State.white : State.black);
    }

    /**
     * Permet de transformer les coordoonées en String
     * @return
     */
    public static String getStringCoord(int i, int j) {    return Character.toString((char)(i + 'A')) + Integer.toString(j + 1);   }
}
