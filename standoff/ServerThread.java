package standoff;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

/**
 * Handles all actions between the Host and the Client.
 */
public class ServerThread extends Thread
{
    // Declares a reference point to the game object.
    private Game game;

    // Declares ServerThread Instance Variables.
    private Socket socket;
    private ObjectOutputStream myAction;
    private ObjectInputStream enemyAction;
    private String myActionString, enemyActionString;

    // Declares the thread object that will wait for a response.
    private Thread thread;

    private boolean inAction;

    /**
     * Initializes the game object and the socket object.
     * 
     * @param game Gets the current Game object.
     * @param socket Gets the socket from the Game object.
     */
    public ServerThread(Game game, Socket socket)
    {
        inAction = false;

        // Instantiates the game object.
        this.game = game;

        // Instantiates the socket object.
        this.socket = socket;
    }

    /**
     * Sends an action to the other player and calls the waitForClient Method.
     * 
     * @param action Gets the action from the Game object.
     */
    public synchronized void setAction(String action)
    {
        // Sets the server's action to null before initialization to ensure it is the correct action.
        myAction = null;
        myActionString = null;

        // Initializes the String to the given action.
        myActionString = action;
    }

    /**
     * Sets up and starts to wait for the other player to make a move without pausing the main thread.
     */
    @Override
    public void start()
    {
        thread = new Thread(this, "Response");

        thread.start();
    }

    /**
     * Waits for the other player to make a move without pausing the main thread.
     */
    @Override
    public void run()
    {
        inAction = true;

        /*
         * Initializes the ObjectOutputStream to a new ObjectOutputStream with the given Socket's output stream.
         * Sends the action to the server.
         */
        try
        {
            myAction = new ObjectOutputStream(socket.getOutputStream());
            myAction.writeObject(myActionString);
        } 
        catch (IOException e1) { System.err.println(e1.getMessage()); }

        // Sets the other player's action to null before initialization to ensure it is the correct action.
        enemyAction = null;
        enemyActionString = null;

        // Checks if the enemy sent an action.
        do
        {
            if(Thread.interrupted())
                return;

            // Initializes the ObjectInputStream to a new ObjectInputStream with the given Socket's input stream.
            try
            {
                enemyAction = new ObjectInputStream(socket.getInputStream());
            } 
            catch (IOException e1) { System.err.println(e1.getMessage()); }

            // Initializes the String to the given enemy action.
            try 
            {
                enemyActionString = (String) enemyAction.readObject();
            }
            catch (ClassNotFoundException | IOException e1) { System.err.println(e1.getMessage()); }
        }
        while(!socket.isClosed() && isNotAction(enemyActionString));

        if(enemyActionString == null)
            return;

        /*
         * Switch Statement taking in the enemy's action.
         * Sets the EnemyAction text in the Game object to the enemy's action.
         */
        switch(enemyActionString)
        {
            case Actions.BLOCK: 
            game.setEnemyAction("Enemy blocked.");
            break;
            case Actions.RELOAD: 
            game.setEnemyAction("Enemy reloaded.");
            break;
            case Actions.FIRE: 
            game.setEnemyAction("Enemy fired.");
            break;
            default: break;
        }

        // Sets the Game result text with the getResult method.
        game.setResult(getResult());

        inAction = false;
        synchronized(game)
        {
            game.notifyAll();
        }

        checkGameOver();
    }

    /**
     * Calculates the result of the actions taken by the player and the enemy.
     * 
     * @return Gets the result given the player's action and the enemy's action.
     */
    private synchronized String getResult()
    {
        if(myActionString.equals(Actions.BLOCK) && enemyActionString.equals(Actions.BLOCK))
            return "You both blocked nothing.";
        else if(myActionString.equals(Actions.RELOAD) && enemyActionString.equals(Actions.RELOAD))
            return "You both reloaded.";
        else if(myActionString.equals(Actions.FIRE) && enemyActionString.equals(Actions.FIRE))
            return "You both fired your weapons.";
        else if(myActionString.equals(Actions.BLOCK) && enemyActionString.equals(Actions.RELOAD))
            return "You blocked nothing and the other player reloaded.";
        else if(myActionString.equals(Actions.BLOCK) && enemyActionString.equals(Actions.FIRE))
            return "You blocked the enemy's shot.";
        else if(myActionString.equals(Actions.RELOAD) && enemyActionString.equals(Actions.BLOCK))
            return "You reloaded and the enemy blocked nothing.";
        else if(myActionString.equals(Actions.RELOAD) && enemyActionString.equals(Actions.FIRE))
            return "You were shot while reloading your weapon.";
        else if(myActionString.equals(Actions.FIRE) && enemyActionString.equals(Actions.BLOCK))
            return "The enemy blocked your shot!";
        else if(myActionString.equals(Actions.FIRE) && enemyActionString.equals(Actions.RELOAD))
            return "You shot the enemy while they were reloading.";
        else return "";
    }

    /**
     * Checks if the actions taken by the player and the enemy result in a game over.
     */
    private synchronized void checkGameOver()
    {
        if(myActionString.equals(Actions.RELOAD) && enemyActionString.equals(Actions.FIRE))
        {
            endGame(0);
            return;
        }
        else if(myActionString.equals(Actions.FIRE) && enemyActionString.equals(Actions.RELOAD))
        {
            endGame(1);
            return;
        }
        else if(myActionString.contentEquals(Actions.FIRE) && enemyActionString.equals(Actions.FIRE))
        {
            endGame(2);
            return;
        }
    }

    /**
     * Sets who won the game.
     * <ul>
     *   <li>0 - You Lose</li>
     *   <li>1 - You Win</li>
     *   <li>2 - No One Wins</li>
     * </ul>
     * 
     * @param winStatus Gets whether or not the player won the game.
     */
    private synchronized void endGame(int winStatus)
    {
        /*
         * Switch Statement taking in the given Win Status.
         * Sets the EndGameLabel text in the Game object to the given Win Status.
         */
        switch(winStatus)
        {
            case 0: game.endGame("You Lose!");
            break;
            case 1: game.endGame("You Win!");
            break;
            case 2: game.endGame("No One Wins!");
            break;
            default: break;
        }

        // Closes the socket (Ends connection to the other player).
        try 
        {
            socket.close();
        }
        catch (IOException e1) { System.err.println(e1.getMessage()); }
    }

    /**
     * Checks to see if the given input is a valid action.
     * 
     * @param input Gets the action that will be checked for validation.
     * @return Return true if valid, otherwise false.
     */
    private synchronized boolean isNotAction(String input)
    {	
        Actions actions = new Actions();

        for(String action : actions.getAllActions())
            if(action.equals(input))
                return false;

        return true;
    }

    /**
     * Used by the Game object to check if it is still waiting for input from the enemy.
     * 
     * @return True if the ServerThread thread is running, otherwise false.
     */
    public synchronized boolean isInAction()
    {
        return inAction;
    }

    /**
     * Used by the Game object to check what 'animations' to use from the player.
     * 
     * @return The last action taken by the player.
     */
    public synchronized String getLastPlayerAction()
    {
        return myActionString;
    }

    /**
     * Used by the Game object to check what 'animations' to use from the enemy.
     * 
     * @return The last action taken by the enemy.
     */
    public synchronized String getLastEnemyAction()
    {
        return enemyActionString;
    }
}