package standoff;

import javax.swing.JFrame;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Image;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;

/**
 * Creates a new game and handles most game objects and most action events.
 * 
 * @author Ryan Neisius
 * @version 2.0.1
 */
public class StandOff
{
    // Declares the version number.
    private static String version;

    // Declares all game objects.
    private MainMenu mainMenu;
    private HostGame hostGame;
    private JoinGame joinGame;
    private Game game;

    // Declares main JFrame properties.
    private JFrame gameFrame;
    private Image logo;
    
    /** The exact size of the screen width in pixels. */
    public static final int SCREEN_WIDTH = 912;
    /** The exact size of the screen height in pixels. */
    public static final int SCREEN_HEIGHT = 485;

    /*
     * Declares the port number used for both clients and server threads.
     * Declares the port used for clients.
     */
    private static int portNumber;
    private static String port;

    /**
     * Creates a new StandOff object to start the game.
     */
    public static void main(String[] args)
    {
        version = "2.0.1";
        new StandOff(100, 100, true);
    }

    /**
     * Starts a new game and instantiates a new MainMenu object.
     * 
     * @param x Gets the x-value of where to put the JFrame compared to the screen.
     * @param y Gets the y-value of where to put the JFrame compared to the screen.
     * @param hasIntro Gets whether or not an intro is wanted.
     */
    public StandOff(int x, int y, boolean hasIntro)
    {
        // Declares all JButtons that need to be handled in the MainMenu object.
        JButton hostGameButton, joinGameButton, exitGameButton;

        gameFrame = new JFrame("Stand Off");

        // Sets up the properties of the main game JFrame.
        gameFrame.setLayout(null);
        gameFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        gameFrame.setResizable(false);
        gameFrame.setLocation(x, y);
        gameFrame.setFocusable(true);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);

        // Initializes an ImageIcon and sets the image as the StandOff JFrame IconImage.
        logo = new ImageIcon(this.getClass().getResource("images/standoff.png")).getImage();
        gameFrame.setIconImage(logo);

        // Initializes the MainMenu object with parameters x and y to set the position of the screen.
        mainMenu = new MainMenu(gameFrame);

        // Sets the intro of the MainMenu object.
        mainMenu.setIntro(hasIntro);

        // Initializes the JButtons that need to be handled.
        hostGameButton = mainMenu.getHostButton();
        joinGameButton = mainMenu.getJoinButton();
        exitGameButton = mainMenu.getExitButton();

        // Creates an ActionListener for the hostGameButton.
        hostGameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the setUpHost method to initialize and handle the HostGame object.
                    setUpHost();
                    mainMenu = null;
                }
            });

        // Creates an ActionListener for the joinGameButton.
        joinGameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the setUpClient method to initialize and handle the JoinGame object.
                    setUpClient("");
                    mainMenu = null;
                }
            });

        // Creates an ActionListener for the exitGameButton.
        exitGameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) 
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

                    // Gives the button sound time to play before exiting.
                    try
                    {
                        Thread.sleep(150);
                    }
                    catch(InterruptedException e1) {}

                    clip.stop();

                    // Exits the game.
                    System.exit(0);
                }
            });

        // Sets up and plays background music.
        AudioInputStream audioIn = null;
        Clip clip = null;
        try
        {
            audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/background-music.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}
    }

    /**
     * Starts a new game and instantiates a new MainMenu object.
     * 
     * @param hasIntro Gets whether or not an intro is wanted.
     * @throws InterruptedException
     */
    private void startGame(boolean hasIntro)
    {
        // Declares all JButtons that need to be handled in the MainMenu object.
        JButton hostGameButton, joinGameButton, exitGameButton;

        // Initializes the MainMenu object with parameters x and y to set the position of the screen.
        mainMenu = new MainMenu(gameFrame);

        // Sets the intro of the MainMenu object.
        mainMenu.setIntro(hasIntro);

        // Initializes the JButtons that need to be handled.
        hostGameButton = mainMenu.getHostButton();
        joinGameButton = mainMenu.getJoinButton();
        exitGameButton = mainMenu.getExitButton();

        // Creates an ActionListener for the hostGameButton.
        hostGameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the setUpHost method to initialize and handle the HostGame object.
                    setUpHost();
                    mainMenu = null;
                }
            });

        // Creates an ActionListener for the joinGameButton.
        joinGameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the setUpClient method to initialize and handle the JoinGame object.
                    setUpClient("");
                    mainMenu = null;
                }
            });

        // Creates an ActionListener for the exitGameButton.
        exitGameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) 
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

                    // Gives the button sound time to play before exiting.
                    try
                    {
                        Thread.sleep(150);
                    }
                    catch(InterruptedException e1) {}

                    clip.stop();

                    // Exits the game.
                    System.exit(0);
                }
            });
    }

    /**
     * Instantiates the HostGame object.
     */
    private void setUpHost()
    {
        // Declares all JButtons that need to be handled in the HostGame object.
        JButton hostStartButton, hostBackButton;

        // Initializes the HostGame object with parameters x and y to set the position of the screen.
        hostGame = new HostGame(gameFrame);

        // Initializes the JButtons that need to be handled.
        hostStartButton = hostGame.getStartButton();
        hostBackButton = hostGame.getBackButton();

        // Creates an ActionListener for the hostStartButton.
        hostStartButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    /*
                     * Try Statement
                     * Attempts to host a new game.
                     * Gets the port number that the user has entered.
                     * Clears any error messages.
                     */
                    try
                    {
                        portNumber = Integer.parseInt(hostGame.getPortNumberInput().trim());

                        if(portNumber < 0 || portNumber > 65535)
                        {
                            hostGame.setErrorMessage("Please enter a port number that is within the range of 0-65535.");
                            hostGame.setErrorMessageVisible(true);
                            return;
                        }

                        hostGame.setErrorMessage("");
                        hostGame.setErrorMessageVisible(false);
                        // Calls the startServer method to initialize and handle the server-side Game object.
                        startServer(portNumber);
                        hostGame = null;
                    }
                    catch(NumberFormatException ex1)
                    {
                        // Sets the error message if the port number was entered incorrectly.
                        if(hostGame.getPortNumberInput().equals(""))
                            hostGame.setErrorMessage("Please type in a port number before starting a game.");
                        else
                            hostGame.setErrorMessage("Make sure the port number you entered is only numbers.");

                        hostGame.setErrorMessageVisible(true);
                    }
                    catch(Exception ex2)
                    {
                        ex2.printStackTrace();
                        // Sets the error message if there was an unknown error.
                        hostGame.setErrorMessage("Unknown error. Maybe try changing to a different port number.");
                        hostGame.setErrorMessageVisible(true);
                    }
                }
            });

        /*
         *  Creates an ActionListener for the hostBackButton.
         *  Takes you back to the MainMenu.
         */
        hostBackButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the startGame method to initialize and handle the MainMenu object.
                    startGame(false);
                    hostGame = null;
                }
            });
    }

    /**
     * Instantiates the JoinGame object.
     * 
     * @param errorMessage Tells the player whether or not there was an error joining a game.
     */
    public void setUpClient(String errorMessage)
    {
        // Declares all JButtons that need to be handled in the JoinGame object.
        JButton clientStartButton, clientBackButton;

        // Initializes the JoinGame object with parameters x and y to set the position of the screen.
        joinGame = new JoinGame(gameFrame);

        // If there is an error, show the player.
        if(!errorMessage.equals(""))
        {
            joinGame.setErrorMessage(errorMessage);
            joinGame.setErrorMessageVisible(true);
        }

        // Initializes the JButtons that need to be handled.
        clientStartButton = joinGame.getStartButton();
        clientBackButton = joinGame.getBackButton();

        // Creates an ActionListener for the clientStartButton.
        clientStartButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Makes sure that the port number JTextField is not empty.
                    String tempPortNumber = joinGame.getPortNumberInput().trim();
                    int tempPortNumberInt = -1;

                    try
                    {
                        tempPortNumberInt = Integer.parseInt(tempPortNumber);
                    }
                    catch(NumberFormatException e1)
                    {
                        joinGame.setErrorMessage("You cannot enter a port number that is not an integer!");
                        joinGame.setErrorMessageVisible(true);
                        return;
                    }

                    // Makes sure that the port JTextField is not empty.
                    String tempPort = joinGame.getPortInput().trim();

                    if(tempPortNumber.equals("") && tempPort.equals(""))
                    {
                        joinGame.setErrorMessage("Please type in a port number and a port before starting a game.");
                        joinGame.setErrorMessageVisible(true);
                    } 
                    else if(tempPortNumber.equals(""))
                    {
                        joinGame.setErrorMessage("Please type in a port number before starting a game.");
                        joinGame.setErrorMessageVisible(true);
                    }
                    else if(tempPort.equals(""))
                    {
                        joinGame.setErrorMessage("Please type in a port before starting a game.");
                        joinGame.setErrorMessageVisible(true);
                    }
                    else if(tempPortNumberInt < 0 || tempPortNumberInt > 65535)
                    {
                        joinGame.setErrorMessage("Please enter a port number that is within the range of 0-65535.");
                        joinGame.setErrorMessageVisible(true);
                    }
                    else
                    {
                        /* 
                         * Try Statement
                         * Attempts to join a new game.
                         * Clears any error messages.
                         */
                        try
                        {
                            portNumber = Integer.parseInt(joinGame.getPortNumberInput().trim());
                            port = joinGame.getPortInput().trim();
                            joinGame.setErrorMessage("");
                            joinGame.setErrorMessageVisible(false);
                            // Calls the joinServer method to initialize and handle the client-side Game object.
                            joinServer(portNumber, port);
                        }
                        catch(NumberFormatException ex1)
                        {
                            // Sets the error message if the port number was entered incorrectly.
                            joinGame.setErrorMessage("Make sure the port number you entered is only numbers.");
                            joinGame.setErrorMessageVisible(true);
                        }
                        catch(Exception ex2)
                        {
                            System.err.println(ex2.getMessage());
                            // Sets the error message if there was an unknown error.
                            joinGame.setErrorMessage("Unknown error. Maybe try changing to a different port number and make sure you entered the correct IPv4 address.");
                            joinGame.setErrorMessageVisible(true);
                        }
                    }
                }
            });

        /*
         * Creates an ActionListener for the clientBackButton.
         * Takes you back to the MainMenu.
         */
        clientBackButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the startGame method to initialize and handle the MainMenu object
                    startGame(false);
                    joinGame = null;
                }
            });
    }

    /**
     * Instantiates the server-side Game object.
     * 
     * @param portNumber Gets the port number that the user entered in the HostGame object.
     * @throws IOException
     */
    private void startServer(int portNumber) throws IOException
    {
        // Declares all JButtons that need to be handled in the Game object.
        JButton startButton, exitButton;

        // Initializes the server-side Game object.
        game = new Game(gameFrame, portNumber);

        // Initializes the JButtons that need to be handled.
        startButton = game.getStartButton();
        exitButton = game.getExitButton();

        // Creates an ActionListener for the startButton.
        startButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the startGame method to start the game.
                    game.startGame();
                }
            });

        /*
         * Creates an ActionListener for the exitButton.
         * Takes you back to the MainMenu.
         */
        exitButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the startGame method to initialize and handle the MainMenu object.
                    startGame(false);	
                    game = null;
                }
            });
    }

    /**
     * Used to get the current game object within other classes that run the game.
     * 
     * @return Gets the Game object.
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * Instantiates the client-side Game object.
     * 
     * @param portNumber Gets the port number that the user entered in the JoinGame object.
     * @param port Gets the port that the user entered in the JoinGame object.
     * @throws IOException
     */
    private void joinServer(int portNumber, String port) throws IOException
    {
        // Declares all JButtons that need to be handled in the Game object.
        JButton startButton, exitButton;

        // Initializes the client-side Game object.
        game = new Game(gameFrame, portNumber, port, this);

        // Initializes the JButtons that need to be handled.
        startButton = game.getStartButton();
        exitButton = game.getExitButton();

        // Creates an ActionListener for the startButton.
        startButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the startGame method to start the game.
                    game.startGame();
                }
            });

        /*
         * Creates an ActionListener for the exitButton.
         * Takes you back to the MainMenu.
         */
        exitButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
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

                    // Calls the startGame method to initialize and handle the MainMenu object.
                    startGame(false);
                    game = null;
                }
            });
    }

    /**
     * Used to get the current version of the game.
     * 
     * @return Gets the current version of the game.
     */
    public static String getVersion()
    {
        return version;
    }
}