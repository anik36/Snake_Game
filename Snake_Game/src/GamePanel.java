import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;//Width of game window
    static final int SCREEN_HEIGHT = 600;//Height og game window
    static final int UNIT_SIZE = 20; //How big we want each grid size in game panel
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT)); // Setting up the game window dimension
        this.setBackground(Color.decode("#1A212B")); // Setting up the Game Window background color
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame(); // Starting the game
    }
    public void startGame(){
        newApple(); //Drawing the first new apple on the screen
        running=true; //Setting snake runnable
        timer = new Timer(DELAY,this); // Setting up the timer to run the snake
        timer.start();
    }
    public static void buttonGUI(JButton button){
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 15);
        button.setFont(font);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        button.setCursor(cursor);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //if game is running do below
        if(running){
            //Setting up the grid layout in game window (Optional)
            /*for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }*/
            g.setColor(Color.GREEN); //Apple Color
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            //Drawing the snake
            for(int i=0;i<bodyParts;i++){
                if(i==0){
                    g.setColor(Color.GREEN);
                    //Adding random color to the snake head
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    //g.fillOval(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }else{
                    g.setColor(new Color(45,180,0));
                    //Adding random color to the snake body
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    //g.fillOval(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }
            //Setting up the score on game window
            g.setColor(Color.RED);
            g.setFont(new Font("Arial",Font.BOLD,20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score : "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score : "+applesEaten))/2, g.getFont().getSize());
        }else {//if game stops or snake touches any border or it's body then do below
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void checkApple(){
        if((x[0]==appleX) && (y[0]==appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //To check collisions we are going to iterate through all of the body parts of the snake
        //check if head collides with body
        for(int i=bodyParts;i>0;i--){
            //Below is the case where head collided with the body
            if((x[0]==x[i]) && (y[0]==y[i])){
                running = false;
            }
        }
        //check if head touches left border
        if(x[0]<0){
            running=false;
        }
        //check if head touches right border
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //check if head touches top border
        if(y[0]<0){
            running=false;
        }
        //check if head touches bottom border
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Display final score on Game Over screen
        g.setColor(Color.RED);
        g.setFont(new Font("Arial",Font.BOLD,20));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score : "+applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score : "+applesEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }
    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'U' :
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D' :
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L' :
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R' :
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            //Controlling the snake to rotate only 90 degree angle
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}
