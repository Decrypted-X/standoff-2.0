package standoff;

import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * Runs all player 'animations' in the game. I didn't know Swing is not 
 * thread safe until recently, which in other words means that you 
 * should not be modifying Swing objects from different Threads. For 
 * some reason my code works, even though I am modifying Swing objects. 
 * This is why I deprecated the use of bullet animations in my code, 
 * and I did not have enough time to re-code all threads using Swing 
 * objects (aka the ServerThread). This can be fixed by calling an
 * InvokeAndWait or an InvokeLater from the AWT EDT 
 * (Event Dispatcher Thread), but I did not have time to implement this. 
 * Click <a href="https://javarevisited.blogspot.com/2011/09/invokeandwait-invokelater-swing-example.html" target="_blank">here</a> for more information.
 */
public class PlayerHandler implements Runnable
{
    // Properties used to create working graphics.
    private JFrame gameFrame;
    private JPanel gamePanel;
    private Game game;
    private JLabel[] playerStances, enemyStances;
    private JLabel currentPlayerStance, currentEnemyStance;
    private JLabel[] playerBullets, enemyBullets;
    private int playerAction, enemyAction;
    private boolean inAction;

    // The exact width and height of the player and the enemy in pixels.
    private final int ENTITY_WIDTH = 52, ENTITY_HEIGHT = 68;

    // The exact position of the player and enemy in pixels.
    private final int PLAYER_X = 425, PLAYER_Y = 300;
    private final int ENEMY_X = 425, ENEMY_Y = 70;

    // The exact width and height of a bullet in pixels.
    @SuppressWarnings("unused")
    private final int BULLET_WIDTH = 40, BULLET_HEIGHT = 115;

    // The speed of a bullet.
    private final int BULLET_SPEED = 5;

    // The start and end positions for the player bullet in pixels.
    private final int PLAYER_BULLET1_START_X = 113;
    private final int PLAYER_BULLET2_START_X = 337;
    private final int PLAYER_BULLET_START_Y = 309, PLAYER_BULLET_END_Y = 194;

    // The start and end positions for the enemy bullet in pixels.
    private final int ENEMY_BULLET1_START_X = 113;
    private final int ENEMY_BULLET2_START_X = 337;
    private final int ENEMY_BULLET_START_Y = 304, ENEMY_BULLET_END_Y = 419;

    /**
     * Sets up the properties and images for the player graphics and animations.
     * 
     * @param gameFrame Gets the current Game JFrame being used.
     * @param gamePanel Gets the current Game JPanel being used.
     * @param game Gets the current Game object being used.
     */
    public PlayerHandler(JFrame gameFrame, JPanel gamePanel, Game game)
    {
        this.gameFrame = gameFrame;
        this.gamePanel = gamePanel;
        this.game = game;

        // Temporary objects used to set up the graphics.
        Image imageSetter = null;
        ImageIcon image = null;

        //		playerBullets = new JLabel[2];
        //		enemyBullets = new JLabel[2];
        //		
        //		imageSetter = new ImageIcon(this.getClass().getResource("images/player-bullet.png")).getImage().getScaledInstance(BULLET_WIDTH, BULLET_HEIGHT, Image.SCALE_AREA_AVERAGING);
        //		image = new ImageIcon(imageSetter);
        //		
        //		playerBullets[0] = new JLabel(image);
        //		playerBullets[0].setSize(BULLET_WIDTH, BULLET_HEIGHT);
        //		playerBullets[0].setLocation(PLAYER_BULLET1_START_X, PLAYER_BULLET_START_Y);
        //		
        //		playerBullets[1] = new JLabel(image);
        //		playerBullets[1].setSize(BULLET_WIDTH, BULLET_HEIGHT);
        //		playerBullets[1].setLocation(PLAYER_BULLET2_START_X, PLAYER_BULLET_START_Y);
        //		
        //		imageSetter = new ImageIcon(this.getClass().getResource("images/enemy-bullet.png")).getImage().getScaledInstance(BULLET_WIDTH, BULLET_HEIGHT, Image.SCALE_AREA_AVERAGING);
        //		image = new ImageIcon(imageSetter);
        //		
        //		enemyBullets[0] = new JLabel(image);
        //		enemyBullets[0].setSize(BULLET_WIDTH, BULLET_HEIGHT);
        //		enemyBullets[0].setLocation(ENEMY_BULLET1_START_X, ENEMY_BULLET_START_Y);
        //		
        //		enemyBullets[1] = new JLabel(image);
        //		enemyBullets[1].setSize(BULLET_WIDTH, BULLET_HEIGHT);
        //		enemyBullets[1].setLocation(ENEMY_BULLET2_START_X, ENEMY_BULLET_START_Y);

        // Player Stances //

        playerStances = new JLabel[5];
        enemyStances = new JLabel[5];

        imageSetter = new ImageIcon(this.getClass().getResource("images/player.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        playerStances[0] = new JLabel(image);
        playerStances[0].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        playerStances[0].setLocation(PLAYER_X, PLAYER_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/player-shoot.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        playerStances[1] = new JLabel(image);
        playerStances[1].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        playerStances[1].setLocation(PLAYER_X, PLAYER_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/player-reload.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        playerStances[2] = new JLabel(image);
        playerStances[2].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        playerStances[2].setLocation(PLAYER_X, PLAYER_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/player-block.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        playerStances[3] = new JLabel(image);
        playerStances[3].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        playerStances[3].setLocation(PLAYER_X, PLAYER_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/death.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        playerStances[4] = new JLabel(image);
        playerStances[4].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        playerStances[4].setLocation(PLAYER_X, PLAYER_Y);

        // Enemy Stances //

        imageSetter = new ImageIcon(this.getClass().getResource("images/enemy.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        enemyStances[0] = new JLabel(image);
        enemyStances[0].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        enemyStances[0].setLocation(ENEMY_X, ENEMY_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/enemy-shoot.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        enemyStances[1] = new JLabel(image);
        enemyStances[1].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        enemyStances[1].setLocation(ENEMY_X, ENEMY_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/enemy-reload.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        enemyStances[2] = new JLabel(image);
        enemyStances[2].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        enemyStances[2].setLocation(ENEMY_X, ENEMY_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/enemy-block.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        enemyStances[3] = new JLabel(image);
        enemyStances[3].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        enemyStances[3].setLocation(ENEMY_X, ENEMY_Y);

        imageSetter = new ImageIcon(this.getClass().getResource("images/death.png")).getImage().getScaledInstance(ENTITY_WIDTH, ENTITY_HEIGHT, Image.SCALE_AREA_AVERAGING);
        image = new ImageIcon(imageSetter);
        enemyStances[4] = new JLabel(image);
        enemyStances[4].setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        enemyStances[4].setLocation(ENEMY_X, ENEMY_Y);

        // Adds the player to the content pane.
        currentPlayerStance = playerStances[0];
        this.gamePanel.add(currentPlayerStance);
        this.gamePanel.setComponentZOrder(currentPlayerStance, 11);

        // Adds the enemy to the content pane.
        currentEnemyStance = enemyStances[0];
        this.gamePanel.add(currentEnemyStance);
        this.gamePanel.setComponentZOrder(currentEnemyStance, 11);

        // Tells the JFrame that a component has been updated.
        this.gamePanel.repaint();

        // Sets up default properties and actions for the player and enemy.
        playerAction = 0;
        enemyAction = 0;
        inAction = false;
    }

    /**
     * Used to set the actions that will be taken by the player and enemy when the thread is ran.
     * 
     * @param playerAction Sets the action given by the player.
     * @param enemyAction Sets the action given by the enemy.
     */
    public synchronized void setActions(int playerAction, int enemyAction)
    {
        this.playerAction = playerAction;
        this.enemyAction = enemyAction;
    }

    /**
     * An overridden method implemented by the Runnable interface that allows the object to be ran as a thread.
     */
    @Override
    public void run()
    {
        inAction = true;

        // Shows the current action given by the player.
        gamePanel.remove(currentPlayerStance);
        currentPlayerStance = playerStances[playerAction];
        gamePanel.add(currentPlayerStance);
        gamePanel.setComponentZOrder(currentPlayerStance, 11);

        // Shows the current action given by the enemy.
        gamePanel.remove(currentEnemyStance);
        currentEnemyStance = enemyStances[enemyAction];
        gamePanel.add(currentEnemyStance);
        gamePanel.setComponentZOrder(currentEnemyStance, 11);

        // Tells the JFrame that a component has been updated.
        gameFrame.repaint();

        //		if(playerAction == 1 && enemyAction == 1)
        //		{
        //			startBullets(0);
        //			while(updateBullets(0))
        //			{
        //				System.out.println("update");
        //				try
        //				{
        //					TimeUnit.MILLISECONDS.sleep(100);
        //				}
        //				catch (InterruptedException e1) { System.err.println(e1.getMessage()); }
        //			}
        //			resetBullets(0);
        //		}
        //		else if(playerAction == 1)
        //		{
        //			startBullets(1);
        //			while(updateBullets(1))
        //			{
        //				System.out.println("update");
        //				try
        //				{
        //					TimeUnit.MILLISECONDS.sleep(100);
        //				}
        //				catch (InterruptedException e1) { System.err.println(e1.getMessage()); }
        //			}
        //			resetBullets(1);
        //		}
        //		else if(enemyAction == 1)
        //		{
        //			startBullets(2);
        //			while(updateBullets(2))
        //			{
        //				System.out.println("update");
        //				try
        //				{
        //					TimeUnit.MILLISECONDS.sleep(100);
        //				}
        //				catch (InterruptedException e1) { System.err.println(e1.getMessage()); }
        //			}
        //			resetBullets(2);
        //		}
        //		else
        //		{

        // Gives the effect that the player and enemy are taking their actions.
        //			try
        //			{
        //				TimeUnit.MILLISECONDS.sleep(500);
        //			}
        //			catch (InterruptedException e1) { System.err.println(e1.getMessage()); }
        //		}

        if(playerAction == 1 && enemyAction == 1)
        {
            // Plays the firing sound effect.
            AudioInputStream audioIn = null;
            Clip clip1 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/shoot-sound.wav"));
                clip1 = AudioSystem.getClip();
                clip1.open(audioIn);
                clip1.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(400);
            }
            catch(InterruptedException e1) {}

            // Plays the firing sound effect.
            audioIn = null;
            Clip clip2 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/shoot-sound.wav"));
                clip2 = AudioSystem.getClip();
                clip2.open(audioIn);
                clip2.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(800);
            }
            catch(InterruptedException e1) {}

            clip1.stop();
            clip2.stop();
        }
        else if((playerAction == 1 && enemyAction == 2) || (playerAction == 2 && enemyAction == 1))
        {
            // Plays the firing sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/shoot-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(800);
            }
            catch(InterruptedException e1) {}

            clip.stop();
        }
        else if((playerAction == 1 && enemyAction == 3) || (playerAction == 3 && enemyAction == 1))
        {
            // Plays the firing sound effect.
            AudioInputStream audioIn = null;
            Clip clip1 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/shoot-sound.wav"));
                clip1 = AudioSystem.getClip();
                clip1.open(audioIn);
                clip1.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(600);
            }
            catch(InterruptedException e1) {}

            // Plays the block sound effect.
            audioIn = null;
            Clip clip2 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/block-sound.wav"));
                clip2 = AudioSystem.getClip();
                clip2.open(audioIn);
                clip2.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(1200);
            }
            catch(InterruptedException e1) {}

            clip1.stop();
            clip2.stop();
        }
        else if(playerAction == 2 && enemyAction == 2)
        {
            // Plays the reload sound effect.
            AudioInputStream audioIn = null;
            Clip clip1 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/reload-sound.wav"));
                clip1 = AudioSystem.getClip();
                clip1.open(audioIn);
                clip1.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            catch(InterruptedException e1) {}

            // Plays the reload sound effect.
            audioIn = null;
            Clip clip2 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/reload-sound.wav"));
                clip2 = AudioSystem.getClip();
                clip2.open(audioIn);
                clip2.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            catch(InterruptedException e1) {}

            clip1.stop();
            clip2.stop();
        }
        else if((playerAction == 2 && enemyAction == 3) || (playerAction == 3 && enemyAction == 2))
        {
            // Plays the reload sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/reload-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            catch(InterruptedException e1) {}

            clip.stop();
        }
        else
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(1200);
            }
            catch(InterruptedException e1) {}
        }

        // Resets the action of the player and enemy.
        if(playerAction == 1 && enemyAction == 1)
        {
            setActions(4, 4);

            // Plays the death sound effect.
            AudioInputStream audioIn = null;
            Clip clip1 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/death-sound.wav"));
                clip1 = AudioSystem.getClip();
                clip1.open(audioIn);
                clip1.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}

            try
            {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            catch(InterruptedException e1) {}

            // Plays the death sound effect.
            audioIn = null;
            Clip clip2 = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/death-sound.wav"));
                clip2 = AudioSystem.getClip();
                clip2.open(audioIn);
                clip2.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}
        }
        else if(playerAction == 1 && enemyAction != 3)
        {
            setActions(0, 4);

            // Plays the death sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/death-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}
        }
        else if(playerAction != 3 && enemyAction == 1)
        {
            setActions(4, 0);

            // Plays the death sound effect.
            AudioInputStream audioIn = null;
            Clip clip = null;
            try
            {
                audioIn = AudioSystem.getAudioInputStream(this.getClass().getResource("sounds/death-sound.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
            catch(UnsupportedAudioFileException | LineUnavailableException | IOException e1) {}
        }
        else setActions(0, 0);

        // Resets the action given to the player. 
        gamePanel.remove(currentPlayerStance);
        currentPlayerStance = playerStances[playerAction];
        gamePanel.add(currentPlayerStance);
        gamePanel.setComponentZOrder(currentPlayerStance, 11);

        // Resets the action given to the enemy.
        gamePanel.remove(currentEnemyStance);
        currentEnemyStance = enemyStances[enemyAction];
        gamePanel.add(currentEnemyStance);
        gamePanel.setComponentZOrder(currentEnemyStance, 11);

        // Tells the JFrame that a component has been updated.
        gameFrame.repaint();
        inAction = false;

        // Tells the Game object to continue with execution of the game.
        synchronized(game)
        {
            game.notifyAll();
        }
    }

    /**
    * @deprecated
    */
    @SuppressWarnings("unused")
    private void startBullets(int type)
    {
        synchronized(gamePanel)
        {
            if(type == 0)
            {
                gamePanel.add(playerBullets[0]);
                gamePanel.setComponentZOrder(playerBullets[0], 7);
                gamePanel.add(playerBullets[1]);
                gamePanel.setComponentZOrder(playerBullets[1], 7);

                gamePanel.add(enemyBullets[0]);
                gamePanel.setComponentZOrder(enemyBullets[0], 5);
                gamePanel.add(enemyBullets[1]);
                gamePanel.setComponentZOrder(enemyBullets[1], 5);
            }
            else if(type == 1)
            {
                gamePanel.add(playerBullets[0]);
                gamePanel.setComponentZOrder(playerBullets[0], 7);
                gamePanel.add(playerBullets[1]);
                gamePanel.setComponentZOrder(playerBullets[1], 7);
            }
            else if(type == 2)
            {
                gamePanel.add(enemyBullets[0]);
                gamePanel.setComponentZOrder(enemyBullets[0], 5);
                gamePanel.add(enemyBullets[1]);
                gamePanel.setComponentZOrder(enemyBullets[1], 5);
            }
        }

        synchronized(gameFrame)
        {
            gameFrame.revalidate();
            gameFrame.repaint();
        }
    }

    /**
    * @deprecated
    */
    @SuppressWarnings("unused")
    private synchronized boolean updateBullets(int type)
    {
        if(type == 0)
        {
            playerBullets[0].setLocation(playerBullets[0].getX(), playerBullets[0].getY() + BULLET_SPEED);
            playerBullets[1].setLocation(playerBullets[1].getX(), playerBullets[1].getY() + BULLET_SPEED);

            enemyBullets[0].setLocation(enemyBullets[0].getX(), enemyBullets[0].getY() + BULLET_SPEED);
            enemyBullets[1].setLocation(enemyBullets[1].getX(), enemyBullets[1].getY() + BULLET_SPEED);

            if(playerBullets[0].getY() > PLAYER_BULLET_END_Y)
                gamePanel.remove(playerBullets[0]);

            if(playerBullets[1].getY() > PLAYER_BULLET_END_Y)
                gamePanel.remove(playerBullets[1]);

            if(enemyBullets[0].getY() < ENEMY_BULLET_END_Y)
                gamePanel.remove(enemyBullets[0]);

            if(enemyBullets[1].getY() < ENEMY_BULLET_END_Y)
                gamePanel.remove(enemyBullets[1]);

            gameFrame.revalidate();
            gameFrame.repaint();

            if(playerBullets[0].getY() <= PLAYER_BULLET_END_Y &&
            playerBullets[1].getY() <= PLAYER_BULLET_END_Y &&
            enemyBullets[0].getY() >= ENEMY_BULLET_END_Y &&
            enemyBullets[1].getY() >= ENEMY_BULLET_END_Y)
                return false;
        }
        else if(type == 1)
        {
            playerBullets[0].setLocation(playerBullets[0].getX(), playerBullets[0].getY() + BULLET_SPEED);
            playerBullets[1].setLocation(playerBullets[1].getX(), playerBullets[1].getY() + BULLET_SPEED);

            if(playerBullets[0].getY() > PLAYER_BULLET_END_Y)
                gamePanel.remove(playerBullets[0]);

            if(playerBullets[1].getY() > PLAYER_BULLET_END_Y)
                gamePanel.remove(playerBullets[1]);

            gameFrame.revalidate();
            gameFrame.repaint();

            if(playerBullets[0].getY() <= PLAYER_BULLET_END_Y &&
            playerBullets[1].getY() <= PLAYER_BULLET_END_Y)
                return false;
        }
        else if(type == 2)
        {
            enemyBullets[0].setLocation(enemyBullets[0].getX(), enemyBullets[0].getY() + BULLET_SPEED);
            enemyBullets[1].setLocation(enemyBullets[1].getX(), enemyBullets[1].getY() + BULLET_SPEED);

            if(enemyBullets[0].getY() < ENEMY_BULLET_END_Y)
                gamePanel.remove(enemyBullets[0]);

            if(enemyBullets[1].getY() < ENEMY_BULLET_END_Y)
                gamePanel.remove(enemyBullets[1]);

            gameFrame.revalidate();
            gameFrame.repaint();

            if(enemyBullets[0].getY() >= ENEMY_BULLET_END_Y &&
            enemyBullets[1].getY() >= ENEMY_BULLET_END_Y)
                return false;
        }

        return true;
    }

    /**
    * @deprecated
    */
    @SuppressWarnings("unused")
    private synchronized void resetBullets(int type)
    {
        if(type == 0)
        {
            playerBullets[0].setLocation(PLAYER_BULLET1_START_X, PLAYER_BULLET_START_Y);
            playerBullets[1].setLocation(PLAYER_BULLET2_START_X, PLAYER_BULLET_START_Y);

            enemyBullets[0].setLocation(ENEMY_BULLET1_START_X, ENEMY_BULLET_START_Y);
            enemyBullets[1].setLocation(ENEMY_BULLET2_START_X, ENEMY_BULLET_START_Y);
        }
        else if(type == 1)
        {
            playerBullets[0].setLocation(PLAYER_BULLET1_START_X, PLAYER_BULLET_START_Y);
            playerBullets[1].setLocation(PLAYER_BULLET2_START_X, PLAYER_BULLET_START_Y);
        }
        else if(type == 2)
        {
            enemyBullets[0].setLocation(ENEMY_BULLET1_START_X, ENEMY_BULLET_START_Y);
            enemyBullets[1].setLocation(ENEMY_BULLET2_START_X, ENEMY_BULLET_START_Y);
        }
    }

    /**
     * Used by the Game object to check if an 'animation' is in progress.
     * 
     * @return True if the PlayerHandler thread is running, otherwise false.
     */
    public synchronized boolean isInAction()
    {
        return inAction;
    }
}