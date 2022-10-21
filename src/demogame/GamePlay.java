package demogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements ActionListener, KeyListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;
    private int playerX = 350;
    private MapGenerator map;

    public GamePlay(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        timer = new Timer(delay,this);
        timer.start();

        map = new MapGenerator(3,7);
    }

    public  void paint(Graphics g){
        //black canvas
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        //border
        g.setColor(Color.YELLOW);
        g.fillRect(0,0,692,3);
        g.fillRect(0,3,3,592);
        g.fillRect(683,3,3,592);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX,550,100,8);

        //bricks
        map.draw((Graphics2D) g);

        //ball
        g.setColor(Color.red);
        g.fillOval(ballPosX,ballPosY,20,20);

        //score
        g.setColor(Color.green);
        g.setFont(new Font("serif",Font.BOLD,20));
        g.drawString("Score : "+score,550,30);

        //gameOver
        if(ballPosY>=570){
            play = false;
            ballXDir = 0;
            ballYDir = 0;

            g.setColor(Color.green);
            g.setFont(new Font("serif",Font.BOLD,40));
            g.drawString("Game Over!! Score : "+score,150,300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press Enter to Restart ",210,350);
        }

        if(totalBricks<=0){
            play = false;
            ballXDir = 0;
            ballYDir = 0;

            g.setColor(Color.green);
            g.setFont(new Font("serif",Font.BOLD,40));
            g.drawString("YOU WON!! Score : "+score,150,300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press Enter to Restart ",210,350);
        }

    }

    private void moveLeft(){
        play = true;
        playerX-=20;
    }
    private void moveRight(){
        play = true;
        playerX+=20;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(play){

            if(ballPosX<=0){
                ballXDir = -ballXDir;
            }
            if(ballPosX>=665){
                ballXDir = -ballXDir;
            }
            if(ballPosY<=0){
                ballYDir = -ballYDir;
            }

            Rectangle ballRect = new Rectangle(ballPosX,ballPosY,20,20);
            Rectangle paddleRect = new Rectangle(playerX,550,100,8);

            if(ballRect.intersects(paddleRect)){
                ballYDir = -ballYDir;
            }

            A:for(int i=0;i<map.map.length;i++){
                for(int j=0;j<map.map[i].length;j++){
                    if(map.map[i][j]>0){

                        int width = map.brickWidth;
                        int height = map.brickHeight;
                        int brickXPos = 80+j*width;
                        int brickYPos = 50+i*height;

                        Rectangle brickRect = new Rectangle(brickXPos,brickYPos,width,height);

                        if(ballRect.intersects(brickRect)){
                            map.setBrick(0,i,j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX+19 <= brickXPos || ballPosX+1>=brickXPos+width){
                                ballXDir = -ballXDir;
                            }else{
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }

                    }
                }
            }




            ballPosX+=ballXDir;
            ballPosY+=ballYDir;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX<=0)
                playerX = 0;
            else
                moveLeft();
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX>=600)
                playerX = 600;
            else
                moveRight();
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                score = 0;
                totalBricks = 21;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerX = 320;

                map = new MapGenerator(3,7);
            }
        }

        repaint();
    }









    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
