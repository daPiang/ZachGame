import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PongGame extends Frame {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private int paddle1Y = HEIGHT / 2;
    private int paddle2Y = HEIGHT / 2;
    private int ballX = WIDTH / 2;
    private int ballY = HEIGHT / 2;
    private int ballSpeedX;
    private int ballSpeedY;
    private int score1 = 0;
    private int score2 = 0;
    private Image tripleBuffer;


    public PongGame() {
        setTitle("Pong Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);

        tripleBuffer = createImage(WIDTH, HEIGHT);

        Random random = new Random();
        ballSpeedX = random.nextBoolean() ? -3 : 3; // Randomly sets the initial X direction
        ballSpeedY = random.nextBoolean() ? -2 : 2; // Randomly sets the initial Y direction


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        // Create a separate thread for game logic
        Thread gameThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    moveBall();
                    repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gameThread.start();
    }        

    public void paint(Graphics g) {
        // Draw graphics on the triple buffer
        Graphics bufferGraphics = tripleBuffer.getGraphics();
    
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(10, paddle1Y - 40, 10, 80);
        bufferGraphics.fillRect(WIDTH - 20, paddle2Y - 40, 10, 80);
        bufferGraphics.fillOval(ballX - 10, ballY - 10, 20, 20);

        bufferGraphics.setColor(Color.GRAY);
        bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 20));
        bufferGraphics.drawString("Player 1: " + score1, 20, 60);
        bufferGraphics.drawString("Player 2: " + score2, 20, 90);

        // Draw the buffer to the screen
        g.drawImage(tripleBuffer, 0, 0, this);
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX < 0) {
            score2++;
            resetBall();
        } else if (ballX > WIDTH) {
            score1++;
            resetBall();
        }

        if (ballX < 30 && ballX > 20 && ballY >= paddle1Y - 40 && ballY <= paddle1Y + 40) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballX > WIDTH - 30 && ballX < WIDTH - 20 && ballY >= paddle2Y - 40 && ballY <= paddle2Y + 40) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballY < 10 || ballY > HEIGHT - 10) {
            ballSpeedY = -ballSpeedY;
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && paddle1Y > 40) {
            paddle1Y -= 20;
        } else if (e.getKeyCode() == KeyEvent.VK_S && paddle1Y < HEIGHT - 40) {
            paddle1Y += 20;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP && paddle2Y > 40) {
            paddle2Y -= 20;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && paddle2Y < HEIGHT - 40) {
            paddle2Y += 20;
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
    
        Random random = new Random();
        ballSpeedX = random.nextBoolean() ? -3 : 3; // Randomly sets the new X direction
        ballSpeedY = random.nextBoolean() ? -2 : 2; // Randomly sets the new Y direction
    }
    

    public static void main(String[] args) {
        new PongGame();
    }
}
