import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] Sx = new int[ALL_DOTS];
    private int[] Sy = new int[ALL_DOTS];
    private int  dots;
    private Timer timer;
    private boolean left, right, up, down  = false;
    private boolean inGame = true;

    public GameField(){
        startGame();
    }

    private void startGame(){
        setBackground(Color.BLACK);
        loadingImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame(){
        dots = 1;
        for(int i = 0; i<dots; i++){
            Sx[i] = 48 - i*DOT_SIZE;
            Sy[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    public void createApple(){
        appleX = new Random().nextInt(19)*DOT_SIZE;
        appleY = new Random().nextInt(19)*DOT_SIZE;
    }


    public void loadingImages(){
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, Sx[i], Sy[i], this);
            }
        }
        else {
            String str = "GAME OVER";
            String press = "PRESS N TO RESTART";
            //Font f = new Font("Arial", 14, Font.BOLD);
            g.setColor(Color.red);
            //g.setFont(f);
            g.drawString(str, SIZE/2, SIZE / 2);
            g.drawString(press, SIZE/2, SIZE / 3);

            addKeyListener(new FieldKeyListener());
            setFocusable(true);
        }
        
    }

    public void move(){
        for (int i = dots; i > 0 ; i--) {
            Sx[i] = Sx[i-1];
            Sy[i] = Sy[i-1];
        }

        if(left){
            Sx[0] -= DOT_SIZE;
        }
        if(right){
            Sx[0] += DOT_SIZE;
        }
        if(up){
            Sy[0] -= DOT_SIZE;
        }
        if(down){
            Sy[0] += DOT_SIZE;
        }
    }

    private void checkApple(){
        if(Sx[0] == appleX && Sy[0] == appleY){
            dots++;
            createApple();
        }
    }

    private void checkCollision(){
        for (int i = dots; i > 0; i--){
            if(i>4 && Sx[0] == Sx[i] && Sy[0] == Sy[i]){
                inGame = false;
            }
        }

        if(Sx[0]>SIZE){
            inGame = false;
        }
        if(Sx[0]<0){
            inGame = false;
        }
        if(Sy[0]>SIZE){
            inGame = false;
        }
        if(Sy[0]<0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollision();
            move();

        }

        repaint();
    }


    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP && !down) {
                left = false;
                up = true;
                right = false;
            }

            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN && !up) {
                left = false;
                down = true;
                right = false;
            }

            if (key == KeyEvent.VK_P) {
                timer.stop();
            }

            if (key == KeyEvent.VK_O) {
                timer.start();
            }

            if (key == KeyEvent.VK_N) {
                inGame = true;
                startGame();
            }
        }
    }


}
