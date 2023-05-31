import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame implements KeyListener{
    public static Game game;
    
    //Makes the game variable and sets up key listening
    public Main(){
        this.game = new Game();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }
    
    
    public static void main(String[] args) throws InterruptedException{
        Main frame = new Main();
        frame.setResizable(false);
        frame.setSize(600, 600);
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    
    public void keyTyped(KeyEvent key){}
    
    //The code that detects keypresses and moves/resets the player.
    public void keyPressed(KeyEvent key){
        if (key.getKeyCode() == KeyEvent.VK_RIGHT || key.getKeyCode() == KeyEvent.VK_D){
            game.movePlayer(1,0);
            
        }
        else if (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyCode() == KeyEvent.VK_A){
            game.movePlayer(-1,0);                                                               
        }
        else if (key.getKeyCode() == KeyEvent.VK_DOWN || key.getKeyCode() == KeyEvent.VK_S){
            game.movePlayer(0,-1);
        }
        else if (key.getKeyCode() == KeyEvent.VK_UP || key.getKeyCode() == KeyEvent.VK_W){
            game.movePlayer(0,1);
        } 
        else if (key.getKeyCode() == KeyEvent.VK_R){
            game.reset();
        }
    }
    
    //This code detects key releases. 
    public void keyReleased(KeyEvent key){}
    
}
