import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Random; //this game was made for Ruby


public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    static final int DELAY = 100; //higher the delay,slower the game
    int highScore = 0;
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
    JButton restartButton;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        restartButton.setFocusable(false);
        restartButton.setVisible(false);  // Hide it initially
        restartButton.addActionListener(e -> startGame());
        this.add(restartButton);  // Add button to panel

        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        //restartButton.setVisible(false);
    }

    public void restartGame() {
        timer.stop(); // Stop any previous timer
        
        // Reset game variables
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        running = true;
        
        // Reset positions of the snake
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    
        // Generate a new apple position
        newApple();
        
        // Restart the timer
        timer = new Timer(DELAY, this);
        timer.start();
        
        // Refresh the display
        repaint();
        
        // Make sure the GamePanel has focus
        this.requestFocusInWindow(); 
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            Graphics2D g2d = (Graphics2D) g;
            Color transparentGray = new Color(169,169,169,70);
            g2d.setColor(transparentGray);

            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g2d.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g2d.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0;i<bodyParts;i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.lightGray);
            g.setFont(new Font("SansSerif",Font.PLAIN,15));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize()+20);

            g.setColor(Color.lightGray);
            g.setFont(new Font("SansSerif", Font.PLAIN, 15));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("HIGHSCORE: " + highScore, 
            (SCREEN_WIDTH - metrics3.stringWidth("HIGHSCORE: " + highScore)) / 2, 
            g.getFont().getSize()); // Adjust Y position if necessary
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

            default:
                break;
        }

    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        //check if head collides with body
        for(int i=bodyParts;i>0;i--){
            if((x[0] == x[i])&&(y[0] == y[i])){
                running = false;
            }
        }
        //checks if head touches left border
        if(x[0]<0){
            running = false;
        }
        //checks if head touches right border
        if(x[0]>SCREEN_WIDTH){
            running = false;
        }
        //checks if head touches top border
        if(y[0]<0){
            running = false;
        }
        //checks if head touches bottom border
        if(y[0]>SCREEN_HEIGHT){
            running = false;
        }

        if(!running){

            if(applesEaten>highScore){
                highScore = applesEaten;
            }
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Game Over text 
        g.setColor(Color.lightGray);
        g.setFont(new Font("SansSerif",Font.BOLD,50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER",(SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
        
        g.setColor(Color.lightGray);
        g.setFont(new Font("SansSerif",Font.PLAIN,15));
        FontMetrics newMetrics = getFontMetrics(g.getFont());
        g.drawString("press ctrl + R to restart",(SCREEN_WIDTH - newMetrics.stringWidth("press ctrl + R to restart"))/2, SCREEN_HEIGHT/2+60);

        g.setColor(Color.lightGray);
        g.setFont(new Font("SansSerif",Font.PLAIN,15));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("SCORE: "+applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize()+20); 

        g.setColor(Color.lightGray);
        g.setFont(new Font("SansSerif",Font.PLAIN,15));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("HIGHSCORE: "+highScore,(SCREEN_WIDTH - metrics3.stringWidth("HIGHSCORE: "+highScore))/2, g.getFont().getSize()); 

        this.requestFocusInWindow();

        //restartButton.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
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
            if(e.isControlDown()){
                if(e.getKeyCode() == KeyEvent.VK_R){
                    if(!running){
                        restartGame();
                    }
                }
            }
            
            else{
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if(direction != 'R'){
                            direction = 'L';
                        }
                        break;
    
                    case KeyEvent.VK_RIGHT:
                        if(direction != 'L'){
                            direction = 'R';
                        }
                        break;
                    
                    case KeyEvent.VK_UP:
                        if(direction != 'D'){
                            direction = 'U';
                        }
                        break;
    
                    case KeyEvent.VK_DOWN:
                        if(direction != 'U'){
                            direction = 'D';
                        }
                        break;
    
                    default:
                        break;
                }
            }

        }

    }
    
}
