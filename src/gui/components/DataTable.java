/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import main.PhysMo;


/**
 * 
 *
 * @author jasonkb
 */
public class DataTable extends JTable
{
    Object[][] data;
    String[] headers;
    public double realWorldDist = 0;
    public static int xOrigon = 0;
    public static int yOrigon = 0;
    
    public DataTable(Object[][] d, String[] h)
    {
        super(d,h);
        data = d;
        headers = h;
        
    }
    
    public Class getColumnClass(int column)
    {
        if(column == 0)
        {
            //frame number
            return Integer.class;
        }
        if(column == 1)
        {
            //time in seconds
            return Double.class;
            
        }
        if(column == 2)
        {
            //y real dist in Metre from bottom left of image
            return Double.class;
            
        }
        if(column == 3)
        {
            //ypix
            return Integer.class;
            
        }
        return String.class;
    }
    
    public void setValueAt(Object value, int row, int col)
    {

        if(col == 3)
        {
            int y = Integer.parseInt(value.toString());
            super.setValueAt((-1)*(y-yOrigon), row, col);
        }
        else
        {
            super.setValueAt(value, row, col);
        }
               
    }
    
    public Object getValueAt(int row, int col)
    {
        if(col == 2)
        {           
            if(this.getValueAt(row,3) != null)
            {
                int y = Integer.parseInt(this.getValueAt(row, 3).toString());
                return y*PhysMo.unMagnifiedRealWorldDistance;
            }
            else
            {
                return 0.0;
            }
        }              
        
        if(col == 1)
        {
            int frame = Integer.parseInt(this.getValueAt(row, 0).toString());
            return frame*(1/PhysMo.fps);
        }
        
        if(col == 0)
        {
            return row +1;
        }
        return super.getValueAt(row, col);
        
    }
    
    
}
