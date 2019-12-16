package standoff;

import java.util.ArrayList;

/**
 * Handles all Actions.
 */
public class Actions
{
    /** Action used to represent a player 'firing'. */
    public static final String FIRE = "fire";
    /** Action used to represent a player 'reloading'. */
    public static final String RELOAD = "reload";
    /** Action used to represent a player 'blocking'. */
    public static final String BLOCK = "block";

    // ArrayList<String> filled with all the actions possible.
    private ArrayList<String> ALL_ACTIONS = new ArrayList<String>();

    /** 
     * Instantiates the ALL_ACTIONS list.
     */
    public Actions()
    {
        // Adds all actions to the ALL_ACTIONS ArrayList<String>.
        ALL_ACTIONS.add(FIRE);
        ALL_ACTIONS.add(RELOAD);
        ALL_ACTIONS.add(BLOCK);
    }

    /**
     * Used to get a reference point of the ALL_ACTIONS list.
     * 
     * @return Gets the ALL_ACTIONS ArrayList<String> that has all possible actions.
     */
    public ArrayList<String> getAllActions()
    {
        return ALL_ACTIONS;
    }
}