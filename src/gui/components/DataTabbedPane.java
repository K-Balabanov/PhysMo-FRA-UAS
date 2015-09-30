/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;

/**
 * 
 *
 * @author jasonkb
 */
public class DataTabbedPane 
{
    DataTable[] traces;
    JTabbedPane tp;
    int rows;
    
    public static int trace = 0;
    public static int row = 0;
    
    public DataTabbedPane(int frames)
    {
        rows = frames;
        traces = new DataTable[1];
        tp = new JTabbedPane(JTabbedPane.TOP);
        
        String[] headers = new String[]{"Frame","Time[s]","Y[m]","Y[pixels]"};
        Object[][] data = new Object[rows][4];
        DataTable t = new DataTable(data, headers);
        t.setColumnSelectionAllowed(false);
        t.setRowSelectionAllowed(true);
        
        traces[0] = t;
        JScrollPane Sp = new JScrollPane(traces[0]);
        Sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        Sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        tp.addTab("Default Trace", Sp);
    }
    
    public void newTrace(String traceName)
    {
        String[] headers = new String[]{"Frame","Time[s]","Y[m]","Y[pixels]"};
        Object[][] data = new Object[rows][4];
        
        DataTable t = new DataTable(data, headers);
        t.setColumnSelectionAllowed(false);
        t.setRowSelectionAllowed(true);
        
        
        
        
        
        DataTable[] temp = new DataTable[traces.length+1];
        
        for(int i = 0; i<=(traces.length-1);)
        {
            temp[i] = traces[i];
            
            i++;
        }
        traces = temp;
        traces[temp.length-1] = t;
        
        
        JScrollPane Sp = new JScrollPane(traces[temp.length-1]);
        Sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        Sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        tp.addTab(traceName, Sp);
        tp.setSelectedIndex(tp.getTabCount()-1);
    }
    
    public void recalibrateAllPoints()
    {
        this.tp.repaint();
    }
    
    public void updateCell(int trace, int row, int col, Object value)
    {
        //tp.setSelectedIndex(trace);
        System.out.println("Update cell in r c t "+row+" "+col+" "+trace);
        System.out.println("traces[].length = "+traces.length);
        System.out.println("traces[trace] rows = "+traces[trace].getRowCount());
        System.out.println("traces[trace] cols = "+traces[trace].getColumnCount());
        traces[trace].setValueAt(value, row, col);
        
        traces[trace].scrollRectToVisible(new Rectangle(traces[trace].getCellRect(row, 0, true)));
        traces[trace].setRowSelectionInterval(row, row);
        


        
        traces[trace].repaint();
    }
    
    public void scrollToCurrent(final int row, final int trace)
    {
        
        Runnable doWorkRunnable = new Runnable() 
                {
                    
                    public void run() 
                    {
                        traces[trace].scrollRectToVisible(new Rectangle(traces[trace].getCellRect(row, 0, true)));
        traces[trace].setRowSelectionInterval(row, row);
                        
                    }
                };
        SwingUtilities.invokeLater(doWorkRunnable);
        
        
    }
    
    public JTabbedPane getJTabbedPane()
    {
        return this.tp;
    }
            
    
}
