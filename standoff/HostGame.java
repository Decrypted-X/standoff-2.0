package standoff;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Image;

/**
 * Handles all HostGame properties and UI elements.
 */
public class HostGame
{
    // Declares a reference point to the main game JFrame.
    private JFrame gameFrame;

    // Declares JPanel and all of its elements.
    private JPanel hostGame;
    private ImageIcon background;
    private JLabel backgroundLabel, title, portNumberJL, warningMessage, errorMessage;
    private JTextField portNumberJTF;
    private JButton start, back;

    /**
     * Creates the 'Host Game' screen and instantiates the HostGame JPanel and all of its elements.
     * 
     * @param gameFrame Gets the current game JFrame that is being used.
     */
    public HostGame(JFrame gameFrame)
    {
        // Initializes the GameFrame JFrame.
        this.gameFrame = gameFrame;

        // Initializes the HostGame JPanel.
        hostGame = new JPanel();
        hostGame.setLayout(null);

        /*
         * Initializes the Title JLabel.
         * Sets up the properties of the Title JLabel.
         * Adds the Title JLabel to the HostGame JPanel.
         */
        title = new JLabel("Host Game");
        title.setBounds(0, 13, 900, 75);
        title.setFont(MyFonts.LARGE);
        title.setForeground(Color.WHITE);
        title.setFocusable(false);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        hostGame.add(title);

        /*
         * Initializes the PortNumberJL JLabel.
         * Sets up the properties of the PortNumberJL JLabel.
         * Adds the PortNumberJL JLabel to the HostGame JPanel.
         */
        portNumberJL = new JLabel("Port Number:");
        portNumberJL.setBounds(325, 125, 150, 38);
        portNumberJL.setFont(MyFonts.DEFAULT);
        portNumberJL.setForeground(Color.WHITE);
        portNumberJL.setFocusable(false);
        portNumberJL.setVerticalAlignment(SwingConstants.CENTER);
        hostGame.add(portNumberJL);

        /*
         * Initializes the PortNumberJTF JTextField.
         * Sets up the properties of the PortNumberJTF JTextField.
         * Adds the PortNumberJTF JTextField to the HostGame JPanel.
         */
        portNumberJTF = new JTextField();
        portNumberJTF.setBounds(325, 175, 263, 38);
        portNumberJTF.setFont(MyFonts.DEFAULT);
        portNumberJTF.setForeground(Color.BLACK);
        portNumberJTF.setBackground(Color.WHITE);
        portNumberJTF.setBorder(null);
        portNumberJTF.setFocusable(true);
        hostGame.add(portNumberJTF);

        /*
         * Initializes the Back JButton.
         * Sets up the properties of the Back JButton.
         * Adds the Back JButton to the HostGame JPanel.
         */
        back = new JButton("Back");
        back.setBounds(150, 300, 150, 38);
        back.setFont(MyFonts.DEFAULT);
        back.setForeground(Color.RED);
        back.setFocusable(false);
        back.setContentAreaFilled(false);
        back.setHorizontalAlignment(SwingConstants.CENTER);
        back.setVerticalAlignment(SwingConstants.CENTER);
        hostGame.add(back);

        /*
         * Initializes the Start JButton.
         * Sets up the properties of the Start JButton.
         * Adds the Start JButton to the HostGame JPanel.
         */
        start = new JButton("Start");
        start.setBounds(625, 300, 150, 38);
        start.setFont(MyFonts.DEFAULT);
        start.setForeground(Color.GREEN);
        start.setFocusable(false);
        start.setContentAreaFilled(false);
        start.setHorizontalAlignment(SwingConstants.CENTER);
        start.setVerticalAlignment(SwingConstants.CENTER);
        hostGame.add(start);

        /*
         * Initializes the WarningMessage JLabel.
         * Sets up the properties of the WarningMessage JLabel.
         * Adds the WarningMessage JLabel to the HostGame JPanel.
         */
        warningMessage = new JLabel("* If you are planning to play with someone on an apple computer, please use a port number above 1024 *"); 
        warningMessage.setBounds(13, 375, 875, 38);
        warningMessage.setFont(MyFonts.SMALL);
        warningMessage.setForeground(Color.WHITE);
        warningMessage.setFocusable(false);
        warningMessage.setHorizontalAlignment(SwingConstants.CENTER);
        warningMessage.setVerticalAlignment(SwingConstants.CENTER);
        hostGame.add(warningMessage);

        /*
         * Initializes the ErrorMessage JLabel.
         * Sets up the properties of the ErrorMessage JLabel.
         * Adds the ErrorMessage JLabel to the HostGame JPanel.
         */
        errorMessage = new JLabel("");
        errorMessage.setBounds(13, 225, 875, 38);
        errorMessage.setFont(MyFonts.SMALL);
        errorMessage.setForeground(Color.RED);
        errorMessage.setFocusable(false);
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setVerticalAlignment(SwingConstants.CENTER);
        errorMessage.setVisible(false);
        hostGame.add(errorMessage);

        Image tempBackground = new ImageIcon(this.getClass().getResource("images/background.png")).getImage().getScaledInstance(900, 450, Image.SCALE_AREA_AVERAGING);
        background = new ImageIcon(tempBackground);
        backgroundLabel = new JLabel(background);
        backgroundLabel.setSize(900, 450);
        backgroundLabel.setLocation(0, 0);
        hostGame.add(backgroundLabel);

        this.gameFrame.setContentPane(hostGame);
        this.gameFrame.revalidate();
    }

    /**
     * Used to get a reference point of the Start button.
     * 
     * @return Gets the Start JButton.
     */
    public JButton getStartButton()
    {
        return start;
    }

    /**
     * Used to get a reference point of the Back button.
     * 
     * @return Gets the Back JButton.
     */
    public JButton getBackButton()
    {
        return back;
    }

    /**
     * Used to get a reference point of the PortNumberInput JTextField.
     * 
     * @return Gets the PortNumberJTF JTextField text.
     */
    public String getPortNumberInput()
    {
        return portNumberJTF.getText();
    }

    /**
     * Sets the error message text of the ErrorMessage JLabel.
     * 
     * @param error Gets the error in which to set the text of the ErrorMessage JLabel.
     */
    public void setErrorMessage(String error)
    {
        errorMessage.setText(error);
    }

    /**
     * Sets the error message visibility of the ErrorMessage JLabel.
     * 
     * @param state Gets the state in which to set the visibility of the ErrorMessage JLabel.
     */
    public void setErrorMessageVisible(boolean state)
    {
        errorMessage.setVisible(state);
    }
}