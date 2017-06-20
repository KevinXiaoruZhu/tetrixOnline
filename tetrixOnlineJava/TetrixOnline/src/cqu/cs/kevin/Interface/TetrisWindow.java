package cqu.cs.kevin.Interface;

import cqu.cs.kevin.Tetri.Block;
import cqu.cs.kevin.Tetri.Tetromino;
import javafx.scene.layout.Background;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.lang.System;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.Timer;

/**
 * Created by xiaoru_zhu on 2017/5/15.
 */
public class TetrisWindow extends JPanel
{
    
    /**constant value*/
    private static final int offLineFrameX = 30;
    private static final int onLineFrameX = 650;
    
    private static final int BLO_SIZE = 30;
    private static final int ROW = 20;
    private static final int COLUMN = 10;
    private Block [][] wall = new Block[ROW][COLUMN];
    
    private int scores = 0; // local score
    private int remLines;
    
    private Tetromino currMino; // current tetris blocks
    private Tetromino nextMino; // the next tetris blocks
    
    private int speed = 1000; // timer's speed
    
    //private JFrame frame;
    private JButton Jquit = new JButton();
    private JButton Jpause = new JButton();
    private JButton Jcontinue = new JButton();
    private JButton JmusicOn = new JButton(); // background music
    
    private JLabel Title = new JLabel();
    //label to show the states of game
    private JLabel MScoreLabel = new JLabel();
    private JLabel EScoreLabel = new JLabel();
    private JLabel MjudgeLabel = new JLabel();
    private JLabel EjudgeLabel = new JLabel();
    
    //private boolean end = false;
    private int Iend = 0; // Game end of my side
    private int Eend = 0; // Game end of enemy side
    private final String winGame = "WIN";
    private final String loseGame = "LOSE";
    private final String tieGame = "TIED"; // two sides have the same score
    
    private boolean pause = false; // flag for the pause module
    private boolean gameOver = false; // flag for the gameOver module
    private Timer timer;  // clock rate
    
    private URL backGroudMusic = new File("src"+File.separator+"Music"+File.separator+"backGround.wav").toURI().toURL();
    private URL moveSound = new File("src"+File.separator+"Music"+File.separator+"moveSound.wav").toURI().toURL();
    private URL hardDropSound = new File("src"+File.separator+"Music"+File.separator+"hardDropSound.wav").toURI().toURL();
    private URL rotateSound = new File("src"+File.separator+"Music"+File.separator+"rotateSound.wav").toURI().toURL();
    /*private URL backGroudMusic = new URL("file:///Users/xiaoru_zhu/IdeaProjects/TetrixOnline/src/Music/backGround.wav");
    private URL moveSound = new URL("file:///Users/xiaoru_zhu/IdeaProjects/TetrixOnline/src/Music/moveSound.wav");
    private URL hardDropSound = new URL("file:///Users/xiaoru_zhu/IdeaProjects/TetrixOnline/src/Music/hardDropSound.wav");
    private URL rotateSound = new URL("file:///Users/xiaoru_zhu/IdeaProjects/TetrixOnline/src/Music/rotateSound.wav");*/
    private AudioClip BgmAc;
    private AudioClip moveAc;
    private AudioClip dropAc;
    private AudioClip rotateAc;
    private boolean bgmState = false;
    
    private KeyAdapter keyAdapter; // Key listener
    
    /**socket data*/
    private int [][] data = new int[ROW][COLUMN];
    private int [] sendInt = new int[ROW * COLUMN];
    /**socket data*/
    private int [][] remoteData = new int[ROW][COLUMN];
    private int [] receInt = new int[ROW * COLUMN];
    
    private String remoteScore = "0"; // enemy's score, received from socket
    
    private static Socket socket; // local client socket
    
    private static boolean ready = false;
    
    
    public TetrisWindow() throws Exception
    {
        this.setLayout(null);
        Font font = new Font("Courier",1,32);
        Font JFont = new Font("Courier",0,22);
        
        //Title.setBounds();
        //Title.setBounds(12*30-15, 4*s30, 4*30, 3*30);
        
        Jpause.setBounds(12*30-15, 14*30, 4*30, 2*30);
        Jpause.setFont(JFont);
        Jpause.setHorizontalAlignment(SwingConstants.CENTER);
        Jpause.setText("Pause");
    
        Jcontinue.setBounds(16*30+15, 14*30, 4*30, 2*30);
        Jcontinue.setFont(JFont);
        Jcontinue.setHorizontalAlignment(SwingConstants.CENTER);
        Jcontinue.setText("Continue");
    
        JmusicOn.setBounds(12*30-15, 17*30, 4*30, 2*30);
        JmusicOn.setFont(JFont);
        JmusicOn.setHorizontalAlignment(SwingConstants.CENTER);
        JmusicOn.setText("Music");
    
        Jquit.setBounds(16*30+15, 17*30, 4*30, 2*30);
        Jquit.setFont(JFont);
        Jquit.setHorizontalAlignment(SwingConstants.CENTER);
        Jquit.setText("Quit");
        
        this.add(Jpause);
        this.add(Jcontinue);
        this.add(JmusicOn);
        this.add(Jquit);
        
        MScoreLabel.setBorder(BorderFactory.createTitledBorder("My Score"));
        MScoreLabel.setBounds(12*30-15, 4*30, 4*30, 3*30);
        MScoreLabel.setFont(font);
        MScoreLabel.setBackground(new Color(0xD1FDFF));
        MScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        MScoreLabel.setText(Integer.toString(scores));
        
        EScoreLabel.setBorder(BorderFactory.createTitledBorder("Enemy Score"));
        EScoreLabel.setBounds(16*30+15, 4*30, 4*30, 3*30);
        EScoreLabel.setFont(font);
        EScoreLabel.setBackground(new Color(0xD1FDFF));
        EScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        EScoreLabel.setText("0");
    
        MjudgeLabel.setBorder(BorderFactory.createTitledBorder("My Result"));
        MjudgeLabel.setBounds(12*30-15, 9*30, 4*30, 3*30);
        MjudgeLabel.setFont(new Font("Courier",1,24));
        MjudgeLabel.setBackground(new Color(0xF9FFD0));
        MjudgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        MjudgeLabel.setText("unknown");
        
        EjudgeLabel.setBorder(BorderFactory.createTitledBorder("Enemy Result"));
        EjudgeLabel.setBounds(16*30+15, 9*30, 4*30, 3*30);
        EjudgeLabel.setFont(new Font("Courier",1,24));
        EjudgeLabel.setBackground(new Color(0xF9FFD0));
        EjudgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        EjudgeLabel.setText("unknown");
        
        this.add(MScoreLabel);
        this.add(EScoreLabel);
        this.add(MjudgeLabel);
        this.add(EjudgeLabel);
        
        BgmAc = Applet.newAudioClip(backGroudMusic);
        moveAc = Applet.newAudioClip(moveSound);
        dropAc = Applet.newAudioClip(hardDropSound);
        rotateAc = Applet.newAudioClip(rotateSound);
        
        actionHandle();
    }
    
    private static void TCPClient() throws Exception
    {
        socket = new Socket("127.0.0.1", 10002);
    }
    
    private void transformSendData() throws Exception
    {
        int count = 0;
        for(int i = 0; i < ROW; ++i)
        {
            for (int j = 0; j < COLUMN; ++j)
            {
                sendInt[count] = data[i][j];
                ++count;
            }
        }
        StringBuffer sendStrBuf = new StringBuffer();
        for(int i = 0; i < sendInt.length; ++i)
        {
            sendStrBuf = sendStrBuf.append(sendInt[i] + ",");
        }
        sendStrBuf = sendStrBuf.append(Integer.toString(scores) + ","); //append score to String buffer
        sendStrBuf = sendStrBuf.append(Integer.toString(Iend)); //append state about if ends to String buffer
        String sendStr = sendStrBuf.toString();
        
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        dout.writeUTF(sendStr);
        dout.flush();
        //dout.close();
    }
    
    private void transformReceData() throws Exception
    {
        int count = 0;
        DataInputStream din = new DataInputStream(socket.getInputStream());
        String receStr = din.readUTF();
        //din.close();
        
        String [] receStrBuf = receStr.split(",");
        for(int i = 0; i < receInt.length; ++i)
        {
            receInt[i] = Integer.parseInt(receStrBuf[i]);
        }
    
        for(int i = 0; i < ROW; ++i)
        {
            for (int j = 0; j < COLUMN; ++j)
            {
                remoteData[i][j] = receInt[count];
                ++count;
            }
        }
        
        remoteScore = receStrBuf[receStrBuf.length - 2]; // the last second element of receStrBuf is score
        Eend =  Integer.parseInt(receStrBuf[receStrBuf.length - 1]);
    }
    
    private void sendScore() throws Exception // not be used
    {
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        dout.writeUTF(Integer.toString(scores));
        dout.flush();
    }
    
    private void receScore() throws Exception // not be used
    {
        DataInputStream din = new DataInputStream(socket.getInputStream());
        remoteScore = din.readUTF();
    }
    
    private void actionHandle() throws Exception
    {
        initGameAction();
        //repaint();
        
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                //super.keyPressed(e);
                int value = e.getKeyCode();
                if(value == KeyEvent.VK_Q)
                {
                    Iend = 1;
                    try{
                        transformSendData();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        System.out.println("Fail to send data");
                    }
                    finally {
                        repaint();
                        quitAction();
                    }
                }
                if(!pause && !gameOver)
                {
                    if(value == KeyEvent.VK_UP)
                    {
                        rotateAc.play();
                        rotateAction();
                    }
                    if(value == KeyEvent.VK_DOWN)
                    {
                        try{
                            moveAc.play();
                            autoDropAction();
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    if(value == KeyEvent.VK_SPACE)
                    {
                        dropAc.play();
                        artiDropAction();
                    }
                    if(value == KeyEvent.VK_LEFT)
                    {
                        moveAc.play();
                        moveLeftAction();
                    }
                    if(value == KeyEvent.VK_RIGHT)
                    {
                        moveAc.play();
                        moveRightAction();
                    }
                    if(value == KeyEvent.VK_P)
                    {
                        pauseAction();
                    }
                }
                
                if(pause && !gameOver)
                {
                    if(value == KeyEvent.VK_C)
                    {
                        continueAction();
                    }
                }
                if(gameOver)
                {
                    if(value == KeyEvent.VK_S)
                    {
                        initGameAction();
                    }
                }
                
                repaint();
            }
        };
        //this.setFocusable(true);
        //this.requestFocus(); // focus these input methods on the current component
        this.addKeyListener(keyAdapter);
        
        Jpause.addActionListener(e -> {
            if(!pause && !gameOver)
            {
                pauseAction();
            }
            this.requestFocus(); // keyboard should be focused again in the Panel
        });
        
        Jcontinue.addActionListener(e -> {
            if(pause && !gameOver)
            {
                continueAction();
            }
            this.requestFocus();
        });
        
        JmusicOn.addActionListener(e -> {
            if(bgmState)
            {
                BgmAc.stop();
                bgmState = false;
            }
            else
            {
                BgmAc.loop();
                bgmState = true;
            }
            this.requestFocus();
        });
        
        Jquit.addActionListener(e -> {
            Iend = 1;
            try{
                transformSendData();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally {
                this.requestFocus();
                repaint();
                quitAction();
            }
        });
    }
    
    private void initGameAction() // initialize operation
    {
        clearWall(); // clear the panel, and prepare to start the game
        //scores = 0;
        pause = false;
        gameOver = false;
        currMino = Tetromino.generate();
        nextMino = Tetromino.generate(); // obtain the relative mino
        timer = new Timer(); // initialize the timer
        
        try
        {
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    try{
                        autoDropAction();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    repaint(); // call the "paint()" method again
                }
            }, 1000, speed);  // per 1 seconds, refresh the panel
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    
        /**Create a new thread for receiving real time data*/
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (true)
                {
                    try {
                        transformReceData();
                        repaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }).start();
    
        /**Create a new thread for accelerating game timer*/
        new Thread(() ->
        {
            int minPeriod = 400; // the maximun speed of game
            new Timer().schedule(new TimerTask() {
                @Override
                public void run()
                {
                    if(speed > minPeriod)
                    {
                        speed -= 100;
                    }
                    else
                    {
                        cancel();
                    }
                }
            }, 1000,30000/*0.5 minute*/);
        }).start();
        
    }
    
    private void clearWall()  // clear the panel, and prepare to start the game
    {
        for(int i = 0; i < ROW; ++i)
        {
            Arrays.fill(wall[i], null);
            Arrays.fill(data[i], -1);
        }
    }
    
    private void autoDropAction() throws Exception
    {
        if(ableToDropContinue()) // if the current mino is able to drop, it will move down
        {
            currMino.moveToBottom();
            /**Send data to Server*/
            transformSendData();
        }
        else
        {
            if(gameOver()) // check if the palyer loses the game
            {
                gameOver = true;
                timer.cancel();
                repaint();
                return;
            }
            minoLand(); // when a mino cannot move anymore, it will land and fixed
            removeLine(); // check if there are full lines, and try to remove them
            currMino = nextMino; // change the current mino
            nextMino = Tetromino.generate(); // generate the next mino
        }
    }
    
    private void artiDropAction() // move mino to the bottom directly ("space" : keyboard)
    {
        while(ableToDropContinue())  // players can keep pressing the "down" key till the mino cannot drop
        {
            currMino.moveToBottom();
        }
        if(gameOver()) // check if the palyer loses the game
        {
            gameOver = true;
            timer.cancel();
            repaint();
            return;
        }
        minoLand(); // when a mino cannot move anymore, it will land and fixed
        removeLine(); // check if there are full lines, and try to remove them
        //repaint();
        currMino = nextMino; // change the current mino
        nextMino = Tetromino.generate(); // generate the next mino
    }
    
    private void moveLeftAction()
    {
        if(ableToTranslation(true))
        {
            currMino.moveToLeft();
            //repaint();
        }
        /**maybe there is a problem here*/
    }
    
    private void moveRightAction()
    {
        if(ableToTranslation(false))
        {
        currMino.moveToRight();
        //repaint();
    }
        /**maybe there is a problem here*/
    }
    
    private void rotateAction()
    {
        Block [] temp = currMino.getBlocks();
        //Tetromino te = new Tetromino();
        
        //Block [] bloArray = new Block[4];
        currMino.rotate();
        for(Block b : temp)
        {
            int y = b.getCurrRow(); // get row
            int x = b.getCurrColumn(); // get column
            if(wall[y][x] != null || x < 0 || x > 9 || y < 0 || y > 19)
            {
                try{
                    currMino.undoRotate();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    return;
                }
            }
        }
    }
    
    private void pauseAction()
    {
        timer.cancel();
        this.pause = true;
        repaint();
    }
    
    private void continueAction()
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                try{
                    autoDropAction();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                repaint();
            }
        },500, speed);
        pause = false;
        repaint();  /*????????*/
    }
    
    private void quitAction()
    {
        System.exit(0);
    }
    
    
    private boolean ableToTranslation(boolean left) // judge if the mino can still move left or right
    {
        Block [] temp = currMino.getBlocks();
        for(Block b : temp)
        {
            int y = b.getCurrRow(); // get row
            int x = b.getCurrColumn(); // get column
            if(left) // operation of moving to left side
            {
                if(x == 0)
                {
                    return false;
                }
                if(wall[y][x - 1] != null)
                {
                    return false;
                }
            }
            else // operation of moving to right side
            {
                if(x == COLUMN - 1)
                {
                    return false;
                }
                if(wall[y][x + 1] != null)
                {
                    return false;
                }
            } // end if else
        }
        return true;
    }
    
    private boolean ableToDropContinue()
    {
        Block [] temp = currMino.getBlocks();
        for(Block b : temp)
        {
            int y = b.getCurrRow();
            int x = b.getCurrColumn();
            if(y == ROW - 1) // if reach the bottom of the panel
            {
                return false;
            }
            if(wall[y+1][x] != null) // if there has been a block existed blew the current mino
            {
                return false;
            }
        }
        return true;
    }
    
    private void minoLand() // when a "mino" cannot move anymore, it will land and fixed
    {
        Block [] temp = currMino.getBlocks();
        for(Block b : temp)
        {
            int x = b.getCurrColumn();
            int y = b.getCurrRow();
            if(x >= 0 && y >= 0)
            {
                wall[y][x] = b;
            }
        }
    }
    
    
    private boolean fullLine(int row) // judge if the line is full?
    {
        Block [] line = wall[row];
        for (Block b : line)
        {
            if(b == null)
                return false;
        }
        return true;
    }
    
    private void deleteLine(int row)  // delete line when filled
    {
        for(int i = row; i >= 1; --i)
        {
            /*Block [] src = wall[i-1];
            Block [] des = wall[i];
            for(int b = 0; b < src.length; ++b)
            {
                des[b] = src[b];
            }*/
            System.arraycopy(wall[i-1], 0, wall[i], 0, COLUMN);
        }
        Arrays.fill(wall[0], null);
    }
    
    private void removeLine() // if some of lines are full, they will be removed and score will be increase
    {
        int removedLineCount = 0;
        for(int row = 0; row < ROW; ++row) // search all the lines
        {
            if(fullLine(row))
            {
                deleteLine(row);
                repaint(); // ??????????
                ++removedLineCount;
            }
        }
        this.scores += removedLineCount * 10;
        
        
        
    }
    
    private boolean gameOver()
    {
        Block [] line0 = wall[0];
        for(Block b : line0)
        {
            if(b != null)
            {
                Iend = 1; // set end with "1".
                return true;
            }
                
        }
        return false;
        /**..................there are some problems !!!!*/
    }
    
    
    @Override
    public void paint(Graphics gra)
    {
        super.paint(gra);
        paintFrame(gra);
        paintWall(gra); // paint the panel
        paintCurrMino(gra);  // the mino painted is relative to the current Tetriomino object
        //paintNextMino(gra);
        paintScores(gra);
        //judgeIfLand(gra); // if there are some mino which has been land, paint the blocks in its color
        
        paintReomteFrame(gra);
        paintRemoteWall(gra);
    
        try {
            transformSendData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void paintFrame(Graphics gra)
    {
        Graphics2D g2d = (Graphics2D)gra; // compulsorily convert to subclass
        g2d.translate(30,30);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3)); // set the game frame's thickness
        g2d.drawRect(0, 0, COLUMN*BLO_SIZE,ROW*BLO_SIZE); // paint the frame of the game's panel
        //gra.setColor(Color.LIGHT_GRAY);
        //gra.translate(20,20);
        //gra.drawRect(0, 0, COLUMN*BLO_SIZE,ROW*BLO_SIZE);
    }
    
    private void paintReomteFrame(Graphics gra)
    {
        Graphics2D g2d = (Graphics2D)gra; // compulsorily convert to subclass
        g2d.translate(600,0);
        g2d.setColor(Color.BLACK);
        //g2d.setStroke(new BasicStroke(3)); // set the game frame's thickness
        g2d.drawRect(0, 0, COLUMN*BLO_SIZE,ROW*BLO_SIZE); // paint the frame of the game's panel
    }
    
    private void paintWall(Graphics gra)
    {
        for(int r = 0; r < wall.length; ++r) // row
        {//Block [] line = wall[r];
            for(int c = 0; c < wall[r].length; ++c) // columns
            {
                //Block elem = line[c];
                Block b = wall[r][c];
                
                int x = c * BLO_SIZE;
                int y = r * BLO_SIZE;
                if(b == null)
                {
                    gra.setColor(Color.WHITE);
                    //gra.drawRect(x, y, BLO_SIZE, BLO_SIZE);
                    //gra.draw3DRect(x, y, BLO_SIZE, BLO_SIZE,true);
                    gra.fill3DRect(x, y, BLO_SIZE, BLO_SIZE,true);
                    data[r][c] = Color.WHITE.getRGB();// pass the color data to socket
                }
                else
                {
                    gra.setColor(b.getColor());
                    gra.fill3DRect(x, y, BLO_SIZE, BLO_SIZE,true);
                    data[r][c] = b.getColor().getRGB();// pass the color data to socket
                }
                
            }
        }
    }
    
    private void paintRemoteWall(Graphics gra)
    {
            for(int r = 0; r < wall.length; ++r) // row
            {//Block [] line = wall[r];
                for(int c = 0; c < wall[r].length; ++c) // columns
                {
                    int x = c * BLO_SIZE;
                    int y = r * BLO_SIZE;
                    Color color = new Color(remoteData[r][c]);
                    gra.setColor(color);
                    //gra.drawRect(x, y, BLO_SIZE, BLO_SIZE);
                    //gra.draw3DRect(x, y, BLO_SIZE, BLO_SIZE,true);
                    gra.fill3DRect(x, y, BLO_SIZE, BLO_SIZE,true);
                
                }
            }
    }
    
    private void paintMino(Graphics gra)
    {
        //currMino = Tetromino.generate();
        Block [] tempBlocks = currMino.getBlocks();
        gra.setColor(currMino.getColor());
        for(Block b : tempBlocks)
        {
            int x = b.getCurrColumn() * BLO_SIZE;
            int y = b.getCurrRow() * BLO_SIZE;
            if(x >= 0 && y >= 0)
            {
                gra.fill3DRect(x, y, BLO_SIZE,BLO_SIZE,true);
                data[b.getCurrRow()][b.getCurrColumn()] = b.getColor().getRGB();
            }
        }
    }
    private void paintCurrMino(Graphics gra)
    {
        paintMino(gra);
    }
    private void paintNextMino(Graphics gra)
    {
        //currMino = Tetromino.generate();
        paintMino(gra);
    }
    
    private void paintScores(Graphics gra)
    {
        MScoreLabel.setText(Integer.toString(scores));
        EScoreLabel.setText(remoteScore);
        if(Eend == 1 && Iend == 1)
        {
            if(scores > Integer.parseInt(remoteScore))
            {
                MjudgeLabel.setText(winGame);
                EjudgeLabel.setText(loseGame);
            }
            else if(scores < Integer.parseInt(remoteScore))
            {
                MjudgeLabel.setText(loseGame);
                EjudgeLabel.setText(winGame);
            }
            else
            {
                MjudgeLabel.setText(tieGame);
                EjudgeLabel.setText(tieGame);
            }
        }
    }
    
    private void judgeIfLand(Graphics gra)
    {
        for(int i = ROW - 1; i >= 0; --i)
        {
            for(int j = COLUMN - 1; j >= 0; --j)
            {
                if(wall[i][j] != null)
                {
                    Block b = wall[i][j];
                    gra.setColor(b.getColor());
                    gra.fill3DRect(j*BLO_SIZE, i*BLO_SIZE, BLO_SIZE, BLO_SIZE, true);
                }
            }
        }
    }
    
    
    public static void initFrame() throws Exception
    {
        JFrame frame = new JFrame("Tetris-Online");
        TCPClient();
        
        JPanel waitInterface =  new JPanel();
        waitInterface.setLayout(null);
        
        JLabel jLabel = new JLabel();
        Font waitFont = new Font("TimesRoman",1,30);
        jLabel.setText("Waiting For The Second Player Connecting...");
        jLabel.setFont(waitFont);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setForeground(new Color(0x2E75AB));
        jLabel.setBounds(65, 280, 800, 30);
        waitInterface.add(jLabel);
        
        frame.setContentPane(waitInterface);
        frame.setSize(1000,700);
        frame.setLocationRelativeTo(null); // at medium
    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
        
        while (ready == false)
        {
            DataInputStream din = new DataInputStream(socket.getInputStream());
            String str = din.readUTF();
            if(str.equals("ready"))
                ready = true;
        }
        
        for (int i = 3; i > 0; --i)
        {
            jLabel.setText("Game Starts in " + Integer.toString(i) + " s");
            Thread.currentThread().sleep(1000);
        }
        
        
        TetrisWindow t = new TetrisWindow();
        //t.setSize(600,600);
        t.setBounds(0,0,1000,700);
        frame.setContentPane(t);
        frame.validate();
        frame.repaint();
        t.setFocusable(true);
        t.requestFocus(); // focus these input methods on the current component
        //RemoteTetrisWindow rt = new RemoteTetrisWindow();
        //frame.setContentPane(rt);
    }
}

/*class frameThread implements Runnable {
    private ServerSocket ss;
    private int port;
    public frameThread(ServerSocket ss, int port)
    {
        this.ss = ss;
        this.port = port;
    }
    
    @Override
    public void run()
    {
        
    }
}*/

class Main
{
    public static void main(String args[])
    {
        try
        {
            TetrisWindow.initFrame();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       /* Color c = new Color(-1);
        System.out.println(c.getRGB());*/
    }
}

/**所遇到的问题及解决：
 *
 * 1、t.requestFocus()
 *          ----focus焦点失败，是由于在将最初将该方法写进构造方法里，构造方法是在加入到frame之前运行的，所以失败
 *
 * 2、满行删除实现出现bug，即满行后不能自动消除
 *          ------查看异常信息后发现抛出的是indexOutOfBound类，说明数组越界，检查方法deleteLine，
 *          ------发现for循环中的判断条件写错导致出现index为-1
 *
 * 3、发现暂停操作后操作上下左右按钮mino仍然会移动
 *          ------检查键盘监听部分代码，应该在判断游戏控制操作键之前加上一层if，以判断是否游戏是暂停状态
 *
 * 4、super.paint()没有调用导致JPanel的组件无法显示
 *
 * 5、网络发送延迟处理，多开一个线程进行接收数据，发送数据写在paint方法里
 *
 * 6、多线程传数据存在错乱，把所有数据append到一个buffer上，再逐一进行解析
 *
 * 7、对方游戏情况黑屏，通过调试发现抛出空指针异常，是由于客户端1连接上且客户端2未连接时游戏已经开始，客户端1接收不到2所传数据，
 * 所以，需要多写一个判断，当服务器检测到两边客户端都连接成功时，会给每个客户端发送"ready"信息，此时游戏才能开始。
 *
 * 8、游戏结束后报异常，数组越界，检查对应代码，进行着陆操作时没有预先判断俄罗斯方块的坐标值是否大于零。
 * */