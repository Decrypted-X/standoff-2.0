package standoff;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Image;

/**
 * Handles all Main Menu properties and UI elements.
 */
public class MainMenu implements Runnable
{
    // Declares a reference point to the main game JFrame.
    private JFrame gameFrame;

    // Declares JPanel and all of its elements.
    private JPanel mainMenu;
    private ImageIcon background;
    private JLabel backgroundLabel, title, version, creator;
    private JButton hostGame, joinGame, exitGame;

    // Declares and initializes the title.
    private String currentTitle = "";
    private final char[] TITLE_LETTERS = 
        {
            'S', 't', 'a', 'n', 'd', ' ', 'O', 'f', 'f'
        };

    // Declares the thread object that will run the intro effect.
    private Thread thread;

    /**
     * Creates the 'Main Menu' screen and instantiates the MainMenu JPanel and all of its elements.
     * 
     * @param gameFrame Gets the current game JFrame that is being used.
     */
    public MainMenu(JFrame gameFrame)
    {
        // Initializes the GameFrame JFrame.
        this.gameFrame = gameFrame;

        // Initializes the MainMenu JPanel.
        mainMenu = new JPanel();
        mainMenu.setLayout(null);

        /*
         * Initializes the Title JLabel.
         * Sets up the properties of the Title JLabel.
         * Adds the Title JLabel to the MainMenu JPanel.
         */
        title = new JLabel();
        title.setBounds(0, 13, 900, 75);
        title.setFont(MyFonts.LARGE);
        title.setForeground(Color.WHITE);
        title.setFocusable(false);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.add(title);

        /*
         * Initializes the HostGame JButton.
         * Sets up the properties of the HostGame JButton.
         * Adds the HostGame JButton to the MainMenu JPanel.
         */
        hostGame = new JButton("Host Game");
        hostGame.setBounds(375, 125, 150, 38);
        hostGame.setFont(MyFonts.DEFAULT);
        hostGame.setForeground(Color.BLUE);
        hostGame.setFocusable(false);
        hostGame.setContentAreaFilled(false);
        hostGame.setHorizontalAlignment(SwingConstants.CENTER);
        hostGame.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.add(hostGame);

        /*
         * Initializes the JoinGame JButton.
         * Sets up the properties of the JoinGame JButton.
         * Adds the JoinGame JButton to the MainMenu JPanel.
         */
        joinGame = new JButton("Join Game");
        joinGame.setBounds(375, 225, 150, 38);
        joinGame.setFont(MyFonts.DEFAULT);
        joinGame.setForeground(Color.GREEN);
        joinGame.setFocusable(false);
        joinGame.setContentAreaFilled(false);
        joinGame.setHorizontalAlignment(SwingConstants.CENTER);
        joinGame.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.add(joinGame);

        /*
         *  Initializes the ExitGame JButton.
         *  Sets up the properties of the ExitGame JButton.
         *  Adds the ExitGame JButton to the MainMenu JPanel.
         */
        exitGame = new JButton("Exit Game");
        exitGame.setBounds(375, 325, 150, 38);
        exitGame.setFont(MyFonts.DEFAULT);
        exitGame.setForeground(Color.RED);
        exitGame.setFocusable(false);
        exitGame.setContentAreaFilled(false);
        exitGame.setHorizontalAlignment(SwingConstants.CENTER);
        exitGame.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.add(exitGame);

        /*
         * Initializes the Version JLabel.
         * Sets up the properties of the Version JLabel.
         * Adds the Version JLabel to the MainMenu JPanel.
         */
        version = new JLabel("Version: " + StandOff.getVersion());
        version.setBounds(25, 370, 175, 38);
        version.setFont(MyFonts.SMALL);
        version.setForeground(Color.WHITE);
        version.setFocusable(false);
        version.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.add(version);

        /*
         * Initializes the Creator JLabel.
         * Sets up the properties of the Creator JLabel.
         * Adds the Creator JLabel to the MainMenu JPanel.
         */
        creator = new JLabel("Made By: Ryan Neisius");
        creator.setBounds(25, 390, 175, 38);
        creator.setFont(MyFonts.SMALL);
        creator.setForeground(Color.WHITE);
        creator.setFocusable(false);
        creator.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.add(creator);

        Image tempBackground = new ImageIcon(this.getClass().getResource("images/background.png")).getImage().getScaledInstance(900, 450, Image.SCALE_AREA_AVERAGING);
        background = new ImageIcon(tempBackground);
        backgroundLabel = new JLabel(background);
        backgroundLabel.setSize(900, 450);
        backgroundLabel.setLocation(0, 0);
        mainMenu.add(backgroundLabel);
    }

    /**
     * Sets whether or not the Title JLabel is slowly revealed.
     * 
     * @param hasIntro Gets whether or not there is an intro.
     */
    public void setIntro(boolean hasIntro)
    {
        if(hasIntro)
        {
            gameFrame.setContentPane(mainMenu);
            gameFrame.revalidate();
            start();
        }
        else
        {
            title.setText("Stand Off");
            gameFrame.setContentPane(mainMenu);
            gameFrame.revalidate();
        }
    }

    /**
     * Sets up and starts the intro effect without pausing the main thread.
     */
    public void start()
    {
        if(thread == null)
            thread = new Thread(this, "Intro");

        thread.start();
    }

    /**
     * Runs the intro effect without pausing the main thread.
     */
    @Override
    public void run()
    {
        // Traverses through the TITLE_LETTERS array and slowly adds letters to the Title JLabel.
        for(int i = 0; i < TITLE_LETTERS.length; i++)
        {
            try 
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e) { return; }
            currentTitle += TITLE_LETTERS[i];
            title.setText(currentTitle);
        }
    }

    /**
     * Used to get a reference point of the Host button.
     * 
     * @return Gets the HostGame JButton.
     */
    public JButton getHostButton()
    {
        return hostGame;
    }

    /**
     * Used to get a reference point of the Join button.
     * 
     * @return Gets the JoinGame JButton.
     */
    public JButton getJoinButton()
    {
        return joinGame;
    }

    /**
     * Used to get a reference point of the Exit button.
     * 
     * @return Gets the ExitGame JButton.
     */
    public JButton getExitButton()
    {
        return exitGame;
    }
}