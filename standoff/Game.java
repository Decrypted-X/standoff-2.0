package standoff;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Handles all Game properties and UI elements and handles all player actions.
 */
public class Game implements ActionListener
{
    // Declares a reference point to the main game JFrame.
    private volatile JFrame gameFrame;

    // Declares JPanel and all of its elements.
    private JPanel game;
    private ImageIcon background;
    private JLabel backgroundLabel, yourActionLabel, yourAction, enemyActionLabel, enemyAction, resultLabel, result, bulletAmtLabel, bulletAmt, endGameLabel, warningMessage;
    private JButton fireButton, reloadButton, blockButton, startButton, exitButton;
    private PlayerHandler playerHandler;

    // Declares the player's Bullet Amount.
    private int bulletAmount;

    // Declares Server and Client Instance Variables.
    private ServerThread serverThread;

    /**
     * Calls the setUpGame Method, calls the setUpServer Method, and handles all player actions.
     * 
     * @param gameFrame Gets the current game JFrame that is being used.
     * @param portNumber Gets the port number from the HostGame object.
     */
    public Game(JFrame gameFrame, int portNumber)
    {
        setUpGame(gameFrame);

        // Initializes the action commands for player buttons.
        fireButton.setActionCommand("fire");
        reloadButton.setActionCommand("reload");
        blockButton.setActionCommand("block");

        fireButton.addActionListener(this);
        reloadButton.addActionListener(this);
        blockButton.addActionListener(this);

        setUpServer(portNumber);
    }

    /**
     * Calls the setUpGame Method, calls the setUpClient Method, and handles all player actions.
     * 
     * @param gameFrame Gets the current game JFrame that is being used.
     * @param portNumber Gets the port number from the JoinGame object.
     * @param port Gets the port from the JoinGame object.
     * @param standOff Gets the current StandOff object that is running the game.
     */
    public Game(JFrame gameFrame, int portNumber, String port, StandOff standOff)
    {
        setUpGame(gameFrame);

        // Initializes the action commands for player buttons.
        fireButton.setActionCommand("fire");
        reloadButton.setActionCommand("reload");
        blockButton.setActionCommand("block");

        fireButton.addActionListener(this);
        reloadButton.addActionListener(this);
        blockButton.addActionListener(this);

        setUpClient(portNumber, port, standOff);
    }

    /**
     * Creates the 'Game' Screen and instantiates the Game JPanel and all of its elements.
     * 
     * @param gameFrame Gets the current game JFrame that is being used.
     */
    private void setUpGame(JFrame gameFrame)
    {
        // Initializes the GameFrame JFrame.
        this.gameFrame = gameFrame;

        // Initializes the Game JPanel.
        game = new JPanel();
        game.setLayout(null);

        /*
         * Initializes the StartButton JButton.
         * Sets up the properties of the StartButton JButton.
         * Adds the StartButton JButton to the Game JPanel.
         */
        startButton = new JButton("Start Game");
        startButton.setBounds(375, 250, 150, 38);
        startButton.setFont(MyFonts.DEFAULT);
        startButton.setForeground(Color.GREEN);
        startButton.setFocusable(false);
        startButton.setContentAreaFilled(false);
        startButton.setHorizontalAlignment(SwingConstants.CENTER);
        startButton.setVerticalAlignment(SwingConstants.CENTER);
        game.add(startButton);

        /*
         * Initializes the ExitButton JButton.
         * Sets up the properties of the ExitButton JButton.
         * Adds the ExitButton JButton to the Game JPanel.
         */
        exitButton = new JButton("Exit Game");
        exitButton.setBounds(375, 250, 150, 38);
        exitButton.setFont(MyFonts.DEFAULT);
        exitButton.setForeground(Color.RED);
        exitButton.setFocusable(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setVerticalAlignment(SwingConstants.CENTER);
        exitButton.setVisible(false);
        exitButton.setEnabled(false);
        game.add(exitButton);

        /*
         * Initializes the YourActionLabel JLabel.
         * Sets up the properties of the YourActionLabel JLabel.
         * Adds the YourActionLabel JLabel to the Game JPanel.
         */
        yourActionLabel = new JLabel("Your Move:");
        yourActionLabel.setBounds(13, 100, 175, 38);
        yourActionLabel.setFont(MyFonts.DEFAULT);
        yourActionLabel.setForeground(Color.DARK_GRAY);
        yourActionLabel.setFocusable(false);
        yourActionLabel.setVerticalAlignment(SwingConstants.CENTER);
        game.add(yourActionLabel);

        /*
         * Initializes the EnemyActionLabel JLabel.
         * Sets up the properties of the EnemyActionLabel JLabel.
         * Adds the EnemyActionLabel JLabel to the Game JPanel.
         */
        enemyActionLabel = new JLabel("Enemy Move:");
        enemyActionLabel.setBounds(13, 150, 175, 38);
        enemyActionLabel.setFont(MyFonts.DEFAULT);
        enemyActionLabel.setForeground(Color.DARK_GRAY);
        enemyActionLabel.setFocusable(false);
        enemyActionLabel.setVerticalAlignment(SwingConstants.CENTER);
        game.add(enemyActionLabel);

        /*
         * Initializes the ResultLabel JLabel.
         * Sets up the properties of the ResultLabel JLabel.
         * Adds the ResultLabel JLabel to the Game JPanel.
         */
        resultLabel = new JLabel("Result:");
        resultLabel.setBounds(13, 200, 175, 38);
        resultLabel.setFont(MyFonts.DEFAULT);
        resultLabel.setForeground(Color.DARK_GRAY);
        resultLabel.setFocusable(false);
        resultLabel.setVerticalAlignment(SwingConstants.CENTER);
        game.add(resultLabel);

        /*
         * Initializes the BulletAmtLabel JLabel.
         * Sets up the properties of the BulletAmtLabel JLabel.
         * Adds the BulletAmtLabel JLabel to the Game JPanel.
         */
        bulletAmtLabel = new JLabel("Bullets:");
        bulletAmtLabel.setBounds(13, 275, 175, 38);
        bulletAmtLabel.setFont(MyFonts.DEFAULT);
        bulletAmtLabel.setForeground(Color.DARK_GRAY);
        bulletAmtLabel.setFocusable(false);
        bulletAmtLabel.setVerticalAlignment(SwingConstants.CENTER);
        game.add(bulletAmtLabel);

        /*
         * Initializes the YourAction JLabel.
         * Sets up the properties of the YourAction JLabel.
         * Adds the YourAction JLabel to the Game JPanel.
         */
        yourAction = new JLabel();
        yourAction.setBounds(150, 100, 750, 38);
        yourAction.setFont(MyFonts.DEFAULT);
        yourAction.setForeground(Color.DARK_GRAY);
        yourAction.setFocusable(false);
        yourAction.setVerticalAlignment(SwingConstants.CENTER);
        game.add(yourAction);

        /*
         * Initializes the EnemyAction JLabel.
         * Sets up the properties of the EnemyAction JLabel.
         * Adds the EnemyAction JLabel to the Game JPanel.
         */
        enemyAction = new JLabel();
        enemyAction.setBounds(175, 150, 725, 38);
        enemyAction.setFont(MyFonts.DEFAULT);
        enemyAction.setForeground(Color.DARK_GRAY);
        enemyAction.setFocusable(false);
        enemyAction.setVerticalAlignment(SwingConstants.CENTER);
        game.add(enemyAction);

        /*
         * Initializes the Result JLabel.
         * Sets up the properties of the Result JLabel.
         * Adds the Result JLabel to the Game JPanel.
         */
        result = new JLabel();
        result.setBounds(100, 200, 788, 38);
        result.setFont(MyFonts.DEFAULT);
        result.setForeground(Color.DARK_GRAY);
        result.setFocusable(false);
        result.setVerticalAlignment(SwingConstants.CENTER);
        game.add(result);

        /*
         * Initializes the BulletAmt JLabel.
         * Sets up the properties of the BulletAmt JLabel.
         * Adds the BulletAmt JLabel to the Game JPanel.
         */
        bulletAmt = new JLabel("0");
        bulletAmt.setBounds(100, 275, 788, 38);
        bulletAmt.setFont(MyFonts.DEFAULT);
        bulletAmt.setForeground(Color.DARK_GRAY);
        bulletAmt.setFocusable(false);
        bulletAmt.setVerticalAlignment(SwingConstants.CENTER);
        game.add(bulletAmt);

        /*
         * Initializes the FireButton JButton.
         * Sets up the properties of the FireButton JButton.
         * Adds the FireButton JButton to the Game JPanel.
         */
        fireButton = new JButton("FIRE");
        fireButton.setBounds(175, 375, 150, 38);
        fireButton.setFont(MyFonts.DEFAULT);
        fireButton.setForeground(Color.DARK_GRAY);
        fireButton.setFocusable(false);
        fireButton.setContentAreaFilled(false);
        fireButton.setHorizontalAlignment(SwingConstants.CENTER);
        fireButton.setVerticalAlignment(SwingConstants.CENTER);
        fireButton.setEnabled(false);
        game.add(fireButton);

        /*
         * Initializes the ReloadButton JButton.
         * Sets up the properties of the ReloadButton JButton.
         * Adds the ReloadButton JButton to the Game JPanel.
         */
        reloadButton = new JButton("RELOAD");
        reloadButton.setBounds(375, 375, 150, 38);
        reloadButton.setFont(MyFonts.DEFAULT);
        reloadButton.setForeground(Color.DARK_GRAY);
        reloadButton.setFocusable(false);
        reloadButton.setContentAreaFilled(false);
        reloadButton.setHorizontalAlignment(SwingConstants.CENTER);
        reloadButton.setVerticalAlignment(SwingConstants.CENTER);
        reloadButton.setEnabled(false);
        game.add(reloadButton);

        /*
         * Initializes the BlockButton JButton.
         * Sets up the properties of the BlockButton JButton.
         * Adds the BlockButton JButton to the Game JPanel.
         */
        blockButton = new JButton("BLOCK");
        blockButton.setBounds(575, 375, 150, 38);
        blockButton.setFont(MyFonts.DEFAULT);
        blockButton.setForeground(Color.DARK_GRAY);
        blockButton.setFocusable(false);
        blockButton.setContentAreaFilled(false);
        blockButton.setHorizontalAlignment(SwingConstants.CENTER);
        blockButton.setVerticalAlignment(SwingConstants.CENTER);
        blockButton.setEnabled(false);
        game.add(blockButton);

        /*
         * Initializes the WarningMessage JLabel.
         * Sets up the properties of the WarningMessage JLabel.
         * Adds the WarningMessage JLabel to the Game JPanel.
         */
        warningMessage = new JLabel("You do not have any bullets!");
        warningMessage.setBounds(13, 415, 875, 38);
        warningMessage.setFont(MyFonts.SMALL);
        warningMessage.setForeground(Color.RED);
        warningMessage.setFocusable(false);
        warningMessage.setHorizontalAlignment(SwingConstants.CENTER);
        warningMessage.setVerticalAlignment(SwingConstants.CENTER);
        warningMessage.setVisible(false);
        game.add(warningMessage);

        /*
         * Initializes the EndGameLabel JLabel.
         * Sets up the properties of the EndGameLabel JLabel.
         * Adds the EndGameLabel JLabel to the Game JPanel.
         */
        endGameLabel = new JLabel();
        endGameLabel.setBounds(0, 13, 900, 75);
        endGameLabel.setFont(MyFonts.LARGE);
        endGameLabel.setForeground(Color.WHITE);
        endGameLabel.setFocusable(false);
        endGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endGameLabel.setVerticalAlignment(SwingConstants.CENTER);
        game.add(endGameLabel);

        // Initializes the player's bullet amount.
        bulletAmount = 0;

        playerHandler = new PlayerHandler(this.gameFrame, game, this);

        // Initializes and sets up the background image.
        Image tempBackground = new ImageIcon(this.getClass().getResource("images/background.png")).getImage().getScaledInstance(900, 450, Image.SCALE_AREA_AVERAGING);
        background = new ImageIcon(tempBackground);
        backgroundLabel = new JLabel(background);
        backgroundLabel.setSize(900, 450);
        backgroundLabel.setLocation(0, 0);
        game.add(backgroundLabel);

        // Makes the GUI elements be placed in front of the characters and background.
        game.setComponentZOrder(startButton, 13);
        game.setComponentZOrder(exitButton, 13);
        game.setComponentZOrder(endGameLabel, 15);

        // Updates the Game JFrame to display the Game JPanel.
        this.gameFrame.setContentPane(game);
        this.gameFrame.revalidate();
    }

    /**
     * Sets up the server-side game mechanics.
     * 
     * @param portNumber Gets the port number from the HostGame object.
     */
    private void setUpServer(int portNumber)
    {
        ServerSocket serverSocket = null;

        // Instantiates a new ServerSocket with the given port number.
        try 
        {
            serverSocket = new ServerSocket(portNumber);
        } 
        catch (IOException e1) {}

        // Declares a new Socket.
        Socket socket = null;

        try
        {
            socket = serverSocket.accept();
        }
        catch (IOException e1) {}

        // Initializes a new ServerThread with the given socket.
        serverThread = new ServerThread(this, socket);

        try 
        {
            serverSocket.close();
        }
        catch(IOException e) {}
    }

    /**
     * Sets up the client-side game mechanics.
     * 
     * @param portNumber Gets the port number from the JoinGame object.
     * @param port Gets the port from the JoinGame object.
     */
    private void setUpClient(int portNumber, String port, StandOff standOff)
    {
        // Declares and initializes a Socket with the given port and port number.
        Socket socket = null;

        try
        {
            socket = new Socket(port, portNumber);
        } 
        catch (IOException e1) 
        {
            standOff.setUpClient("Could not find a host on " + portNumber + " from " + port + ".");
        }

        // Initializes a new Client.
        serverThread = new ServerThread(this, socket);
    }	

    /**
     * Handles all actions from the player.
     * 
     * @param e Gets input from the user.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if("fire".equals(e.getActionCommand()))
        {
            // Plays the button click sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/click-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            // Gives the button sound time to play before other actions.
            try
            {
                Thread.sleep(150);
            }
            catch(InterruptedException e1) {}

            clip.stop();

            // Makes sure that the player has bullets.
            if(bulletAmount > 0)
            {
                warningMessage.setVisible(false);
                // Decrements the player's bullet amount.
                bulletAmount--;
                // Sets the text of the player's BulletAmt.
                bulletAmt.setText("" + bulletAmount);
                // Sets the text of the player's Action.
                yourAction.setText("You fired.");
                enemyAction.setText("");
                result.setText("");

                serverThread.setAction(Actions.FIRE);
                serverThread.start();

                finishTurn();
            }
            else
                warningMessage.setVisible(true);
        }
        else if("reload".equals(e.getActionCommand()))
        {
            // Plays the button click sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/click-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            // Gives the button sound time to play before other actions.
            try
            {
                Thread.sleep(150);
            }
            catch(InterruptedException e1) {}

            clip.stop();

            warningMessage.setVisible(false);
            // Increments the player's bullet amount.
            bulletAmount++;
            // Sets the text of the player's BulletAmt.
            bulletAmt.setText("" + bulletAmount);
            // Sets the text of the player's Action.
            yourAction.setText("You reloaded.");
            enemyAction.setText("");
            result.setText("");

            serverThread.setAction(Actions.RELOAD);
            serverThread.start();

            finishTurn();
        }
        else if("block".equals(e.getActionCommand()))
        {
            // Plays the button click sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/click-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            // Gives the button sound time to play before other actions.
            try
            {
                Thread.sleep(150);
            }
            catch(InterruptedException e1) {}

            clip.stop();

            warningMessage.setVisible(false);
            // Sets the text of the player's Action.
            yourAction.setText("You blocked.");
            enemyAction.setText("");
            result.setText("");

            serverThread.setAction(Actions.BLOCK);
            serverThread.start();

            finishTurn();
        }
    }

    /**
     * Enables all action JButtons.
     */
    private void playTurn()
    {
        gameFrame.revalidate();
        gameFrame.repaint();
    }

    /**
     * Disables all action JButtons.
     */
    private void endTurn()
    {
        gameFrame.revalidate();
        gameFrame.repaint();
    }

    /**
     * Finishes the player's turn by waiting for the server thread to respond 
     * and sending the results to the PlayerHandler, which will show the player 
     * the result of that action.
     */
    private synchronized void finishTurn()
    {
        // Ends the player's turn.
        endTurn();

        // Waits for a response from the enemy before continuing.
        while(serverThread.isInAction())
        {
            try
            {
                Game.this.wait();
            }
            catch(InterruptedException e1) { System.err.println(e1.getMessage()); }
        }

        String playerActionString = serverThread.getLastPlayerAction();
        String enemyActionString = serverThread.getLastEnemyAction();
        int playerAction = -1, enemyAction = -1;

        // Finds the last action given by the player.
        switch(playerActionString)
        {
            case Actions.FIRE:
            playerAction = 1;
            break;
            case Actions.RELOAD:
            playerAction = 2;
            break;
            case Actions.BLOCK:
            playerAction = 3;
            break;
            default: break;
        }

        // Finds the last action given by the enemy.
        switch(enemyActionString)
        {
            case Actions.FIRE:
            enemyAction = 1;
            break;
            case Actions.RELOAD:
            enemyAction = 2;
            break;
            case Actions.BLOCK:
            enemyAction = 3;
            break;
            default: break;
        }

        if(playerAction != -1 && enemyAction != -1)
        {
            // Sets actions given by the player and the enemy to the PlayerHandler.
            playerHandler.setActions(playerAction, enemyAction);

            // Starts a new thread that plays the animation according to the actions given.
            Thread animation = new Thread(playerHandler);
            animation.start();

            // Waits for the animations to finish before continuing.
            while(playerHandler.isInAction())
            {
                try
                {
                    Game.this.wait();
                }
                catch(InterruptedException e1) { System.err.println(e1.getMessage()); }
            }
        }
    }

    /**
     * Starts a new game, disables the StartButton JButton, enables all JLabels and JButtons needed to start the game, and calls the playTurn Method.
     */
    public void startGame()
    {
        startButton.setVisible(false);
        startButton.setEnabled(false);

        yourActionLabel.setForeground(Color.GREEN);
        enemyActionLabel.setForeground(Color.RED);
        resultLabel.setForeground(Color.BLUE);
        bulletAmtLabel.setForeground(Color.WHITE);
        yourAction.setForeground(Color.GREEN);
        enemyAction.setForeground(Color.RED);
        result.setForeground(Color.BLUE);
        bulletAmt.setForeground(Color.WHITE);
        fireButton.setForeground(Color.RED);
        fireButton.setEnabled(true);
        reloadButton.setForeground(Color.BLUE);
        reloadButton.setEnabled(true);
        blockButton.setForeground(Color.GREEN);
        blockButton.setEnabled(true);

        gameFrame.revalidate();

        playTurn();
    }

    /**
     * Ends the game, closes the ServerSocket (if there is any), disables all JLabels and JButtons needed to play the game, and enables the ExitButton JButton.
     * 
     * @param gameStatus Gets who won the game.
     */
    public void endGame(String gameStatus)
    {
        endGameLabel.setText(gameStatus);

        yourActionLabel.setForeground(Color.DARK_GRAY);
        enemyActionLabel.setForeground(Color.DARK_GRAY);
        resultLabel.setForeground(Color.DARK_GRAY);
        bulletAmtLabel.setForeground(Color.DARK_GRAY);
        yourAction.setForeground(Color.DARK_GRAY);
        enemyAction.setForeground(Color.DARK_GRAY);
        result.setForeground(Color.DARK_GRAY);
        bulletAmt.setForeground(Color.DARK_GRAY);

        for(ActionListener listener : fireButton.getActionListeners())
            fireButton.removeActionListener(listener);
        for(ActionListener listener : reloadButton.getActionListeners())
            reloadButton.removeActionListener(listener);
        for(ActionListener listener : blockButton.getActionListeners())
            blockButton.removeActionListener(listener);

        fireButton.setEnabled(false);
        reloadButton.setEnabled(false);
        blockButton.setEnabled(false);

        exitButton.setVisible(true);
        exitButton.setEnabled(true);

        // Updates the Game JFrame.
        gameFrame.revalidate();
        gameFrame.repaint();
    }

    /**
     * Used to get a reference point of the Start button.
     * 
     * @return Gets the StartButton JButton.
     */
    public JButton getStartButton()
    {
        return startButton;
    }

    /**
     * Used to get a reference point of the Exit button.
     * 
     * @return Gets the ExitButton JButton.
     */
    public JButton getExitButton()
    {
        return exitButton;
    }

    /**
     * Sets the action text of the EnemyAction JLabel.
     * 
     * @param action Gets the action in which to set the text of the EnemyAction JLabel.
     */
    public void setEnemyAction(String action)
    {
        enemyAction.setText(action);
    }

    /**
     * Sets the result text of the Result JLabel.
     * 
     * @param result Gets the result in which to set the text of the Result JLabel.
     */
    public void setResult(String result)
    {
        this.result.setText(result);
    }

    /**
     * Used to get a reference point of the serverThread ServerThread.
     * 
     * @return Gets the serverThread ServerThread.
     */
    public ServerThread getServerThread()
    {
        return serverThread;
    }
}