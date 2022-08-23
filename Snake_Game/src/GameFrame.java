import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        this.add(new GamePanel()); // making reference to the GamePanel class
        this.setTitle("Snake Mania"); // Setting game window title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Stopping the program when user clicks on close button
        this.setResizable(false); // Making game window not resizable
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); //Making game window to appear in middle of the screen
    }
}
