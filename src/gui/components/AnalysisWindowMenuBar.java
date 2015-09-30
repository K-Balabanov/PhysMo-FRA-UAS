/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import gui.images.Distributor;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;

/**
 * 
 *
 * @author jasonkb
 */
public class AnalysisWindowMenuBar implements ActionListener
{
    JMenuBar mb;
    
    JMenu fileMenu;
    JMenuItem closeWindow;
    
    
    JMenu videoMenu;
    JMenuItem setTimebase;
    JMenuItem videoProperties;
    
    Distributor d;
    JMenu reportMenu;
    JMenuItem excelReport;
         
    
    public AnalysisWindowMenuBar()
    {
        d = new Distributor();
        
        mb = new JMenuBar();
        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        mb.add(fileMenu);
        
        closeWindow = new JMenuItem("Close Analysis Window", new ImageIcon(d.getCloseIcon()));
        closeWindow.setMnemonic('C');
        closeWindow.addActionListener(this);
        fileMenu.add(closeWindow);
        
        
        videoMenu = new JMenu("Video");
        videoMenu.setMnemonic('V');
        
        setTimebase = new JMenuItem("Set Timebase", new ImageIcon(d.getTimebase()));
        setTimebase.setMnemonic('T');
        setTimebase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        setTimebase.addActionListener(this);
        videoMenu.add(setTimebase);
        videoMenu.addSeparator();
        
        videoProperties = new JMenuItem("Video Information", new ImageIcon(d.getQuery()));
        videoProperties.setMnemonic('I');
        videoProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        videoProperties.addActionListener(this);
        videoMenu.add(videoProperties);
        
        mb.add(videoMenu);
        
        
        
        
        reportMenu = new JMenu("Report");
        reportMenu.setMnemonic('R');
        
        excelReport = new JMenuItem("Export Data to Excel", new ImageIcon(d.getExcelFileIcon()));
        excelReport.setMnemonic('E');
        excelReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        excelReport.addActionListener(this);
        reportMenu.add(excelReport);
        
        mb.add(reportMenu);
        
    }
    
    public JMenuBar getJMenuBar()
    {
        return this.mb;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == setTimebase)
        {
            new SetTimebaseDialog(null, true, "Set FPS");
            this.getJMenuBar().firePropertyChange("timebaseUpdate", -1, 0);
        }
        if(e.getSource() == videoProperties)
        {
            new VideoInformationDialog(null, true);
        }
        if(e.getSource() == closeWindow)
        {
            this.getJMenuBar().firePropertyChange("closeWindow", -1, 0);
        }
        
        if(e.getSource() == excelReport)
        {
            this.getJMenuBar().firePropertyChange("excelReport", -1, 0);
        }
        
    }
}
