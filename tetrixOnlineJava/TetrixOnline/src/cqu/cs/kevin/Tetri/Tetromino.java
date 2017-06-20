package cqu.cs.kevin.Tetri;

import java.awt.*;
import java.util.Random;

/**
 * Created by xiaoru_zhu on 2017/5/15.
 */



public class Tetromino
{
    protected Block[] tetrBlocks = new Block[4];
    
    protected RotateMethod[] rMethod;
    
    private int rotaCount = 0;
    
    private int shape;
    
    public int getStyle() {
        return shape;
    }
    //protected int [][] matrix ;//= new int[4][4];
    
    public static Tetromino generate()
    {
        Random random = new Random();
        int style = random.nextInt(7);
        if (style == 0)
            return new Tstyle();
        else if (style == 1)
            return new Lstyle();
        else if (style == 2)
            return new Jstyle();
        else if (style == 3)
            return new Sstyle();
        else if (style == 4)
            return new Zstyle();
        else if (style == 5)
            return new Istyle();
        else
            return new Ostyle();
        
    }
    
    public Block[] getBlocks() {
        return this.tetrBlocks;
    }
    
    public RotateMethod[] getRotateMethod()
    {
        return rMethod;
    }
    
    public Color getColor()
    {
        return tetrBlocks[0].getColor();
    }
    
    public void moveToLeft()
    {
        for (Block blo : tetrBlocks)
        {
            blo.moveLeft();
        }
    }
    
    public void moveToRight()
    {
        for (Block blo : tetrBlocks)
        {
            blo.moveRight();
        }
    }
    
    public void moveToBottom()
    {
        for (Block blo : tetrBlocks)
        {
            blo.moveDown();
        }
    }
    
    public void rotate()
    {
        //int temp = this.rotaCount % (rMethod.length);
        tetrBlocks[1].setCurrRow(tetrBlocks[1].getCurrRow() + rMethod[rotaCount % (rMethod.length)].row1);
        tetrBlocks[1].setCurrColumn(tetrBlocks[1].getCurrColumn() + rMethod[rotaCount % (rMethod.length)].col1);
        tetrBlocks[2].setCurrRow(tetrBlocks[2].getCurrRow() + rMethod[rotaCount % (rMethod.length)].row2);
        tetrBlocks[2].setCurrColumn(tetrBlocks[2].getCurrColumn() + rMethod[rotaCount % (rMethod.length)].col2);
        tetrBlocks[3].setCurrRow(tetrBlocks[3].getCurrRow() + rMethod[rotaCount % (rMethod.length)].row3);
        tetrBlocks[3].setCurrColumn(tetrBlocks[3].getCurrColumn() + rMethod[rotaCount % (rMethod.length)].col3);
        ++this.rotaCount;
    }
    
    public void undoRotate()
    {
        if(this.rotaCount > 0)
        {
            --this.rotaCount;
            tetrBlocks[1].setCurrRow(tetrBlocks[1].getCurrRow() - rMethod[rotaCount % (rMethod.length)].row1);
            tetrBlocks[1].setCurrColumn(tetrBlocks[1].getCurrColumn() - rMethod[rotaCount % (rMethod.length)].col1);
            tetrBlocks[2].setCurrRow(tetrBlocks[2].getCurrRow() - rMethod[rotaCount % (rMethod.length)].row2);
            tetrBlocks[2].setCurrColumn(tetrBlocks[2].getCurrColumn() - rMethod[rotaCount % (rMethod.length)].col2);
            tetrBlocks[3].setCurrRow(tetrBlocks[3].getCurrRow() + rMethod[rotaCount % (rMethod.length)].row3);
            tetrBlocks[3].setCurrColumn(tetrBlocks[3].getCurrColumn() - rMethod[rotaCount % (rMethod.length)].col3);
        }
    }
    
}

class RotateMethod
{
    public int row0, row1, row2, row3, col0, col1, col2, col3;
    
    public RotateMethod(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3)
    {
        this.row0 = row0;
        this.col0 = col0;
        this.row1 = row1;
        this.col1 = col1;
        this.row2 = row2;
        this.col2 = col2;
        this.row3 = row3;
        this.col3 = col3;
    }
}

class Tstyle extends Tetromino
{
    public Tstyle()
    {
        tetrBlocks[0] = new Block(0, 5, Color.BLUE);
        tetrBlocks[1] = new Block(0, 4, Color.BLUE);
        tetrBlocks[2] = new Block(0, 6, Color.BLUE);
        tetrBlocks[3] = new Block(-1, 5, Color.BLUE);
        rMethod = new RotateMethod []
                    {new RotateMethod(0,0,-1,1,1,-1,1,1),
                     new RotateMethod(0,0,1,1,-1,-1,1,-1),
                     new RotateMethod(0,0,1,-1,-1,1,-1,-1),
                     new RotateMethod(0,0,-1,-1,1,1,-1,1)};
    }
}

class Lstyle extends Tetromino
{
    public Lstyle()
    {
        tetrBlocks[0] = new Block(0, 4, Color.RED);
        tetrBlocks[1] = new Block(0, 3, Color.RED);
        tetrBlocks[2] = new Block(0, 5, Color.RED);
        tetrBlocks[3] = new Block(-1, 3, Color.RED);
        rMethod = new RotateMethod []
                {new RotateMethod(0,0,-1,1,1,-1,0,2),
                 new RotateMethod(0,0,1,1,-1,-1,2,0),
                 new RotateMethod(0,0,1,-1,-1,1,0,-2),
                 new RotateMethod(0,0,-1,-1,1,1,-2,0)};
    }
    
}

class Jstyle extends Tetromino
{
    public Jstyle()
    {
        tetrBlocks[0] = new Block(0, 4, Color.YELLOW);
        tetrBlocks[1] = new Block(0, 3, Color.YELLOW);
        tetrBlocks[2] = new Block(0, 5, Color.YELLOW);
        tetrBlocks[3] = new Block(-1, 5, Color.YELLOW);
        rMethod = new RotateMethod []
                    {new RotateMethod(0,0,-1,1,1,-1,2,0),
                     new RotateMethod(0,0,1,1,-1,-1,0,-2),
                     new RotateMethod(0,0,1,-1,-1,1,-2,0),
                     new RotateMethod(0,0,-1,-1,1,1,0,2)};
    }
    
}

class Sstyle extends Tetromino
{
    public Sstyle()
    {
        tetrBlocks[0] = new Block(-1, 4, Color.GREEN);
        tetrBlocks[1] = new Block(-1, 5, Color.GREEN);
        tetrBlocks[2] = new Block(0, 4, Color.GREEN);
        tetrBlocks[3] = new Block(0, 3, Color.GREEN);
        rMethod = new RotateMethod []
                {new RotateMethod(0,0,-1,-1,-1,1,0,2),
                 new RotateMethod(0,0,1,1,1,-1,0,-2)};
    }
}
class Zstyle extends Tetromino
{
    public Zstyle()
    {
        tetrBlocks[0] = new Block(-1, 4, Color.GRAY);
        tetrBlocks[1] = new Block(-1, 3, Color.GRAY);
        tetrBlocks[2] = new Block(0, 4, Color.GRAY);
        tetrBlocks[3] = new Block(0, 5, Color.GRAY);
        rMethod = new RotateMethod []
                {new RotateMethod(0,0,-1,1,-1,-1,0,-2),
                 new RotateMethod(0,0,1,-1,1,1,0,2)};
    }
}

class Istyle extends Tetromino
{
    public Istyle()
    {
        tetrBlocks[0] = new Block(0, 4, Color.ORANGE);
        tetrBlocks[1] = new Block(-1, 4, Color.ORANGE);
        tetrBlocks[2] = new Block(1, 4, Color.ORANGE);
        tetrBlocks[3] = new Block(2, 4, Color.ORANGE);
        rMethod = new RotateMethod []
                {new RotateMethod(0,0,1,-1,-1,1,-2,2),
                 new RotateMethod(0,0,-1,1,1,-1,2,-2)};
    }
}

class Ostyle extends Tetromino
{
    public Ostyle()
    {
        tetrBlocks[0] = new Block(-1, 4, Color.CYAN);
        tetrBlocks[1] = new Block(-1, 5, Color.CYAN);
        tetrBlocks[2] = new Block(0, 4, Color.CYAN);
        tetrBlocks[3] = new Block(0, 5, Color.CYAN);
        rMethod = new RotateMethod []
                {new RotateMethod(0,0,0,0,0,0,0,0)};
    }
}