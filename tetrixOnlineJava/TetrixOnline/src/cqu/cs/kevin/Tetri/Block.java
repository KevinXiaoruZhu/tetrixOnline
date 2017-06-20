package cqu.cs.kevin.Tetri;

import java.awt.*;

/**
 * Created by xiaoru_zhu on 2017/5/15.
 */
public class Block
{
    private int currRow;
    private int currColumn;
    private Color tinct;
    
    
    public Block(int currRow, int currColumn, Color tinct)
    {
        this.currRow = currRow;
        this.currColumn = currColumn;
        this.tinct = tinct;
    }
    
    public void setCurrRow(int currRow)
    {
        this.currRow = currRow;
    }
    public int getCurrRow()
    {
        return currRow;
    }
    
    public void setCurrColumn(int currColumn)
    {
        this.currColumn = currColumn;
    }
    public int getCurrColumn()
    {
        return currColumn;
    }
    
    public Color getColor()
    {
        return tinct;
    }
    public void setColor(Color tinct)
    {
        this.tinct = tinct;
    }
    
    public void moveDown()
    {
        ++currRow;
    }
    public void moveLeft()
    {
        --currColumn;
    }
    public void moveRight()
    {
        ++currColumn;
    }
}
