/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;
import gui.images.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Event.*;
import java.net.URL;


/**
 * 
 *
 * @author jasonkb
 */
public class Splash extends JWindow
{
    
     private int duration;
    
    public Splash(int d) {
        duration = d;
    }
    
    // A simple little method to show a title screen in the center
    // of the screen for the amount of time given in the constructor
    public void showSplash() {
        
        JPanel content = (JPanel)getContentPane();
        content.setBackground(Color.white);
        
        // Set the window's bounds, centering the window
        int width = 520;
        int height =200;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
        
        // Build the splash screen
        Distributor d = new Distributor();
            
        
        JLabel label = new JLabel(new ImageIcon(d.getSplash()));
        JLabel copyrt = new JLabel
                ("Version 3.0 Licence: GPLv3", JLabel.CENTER);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 16));
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        Color oraRed = new Color(156, 20, 20,  255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 10));
        
        // Display it
        setVisible(true);
        
        // Wait a little while, maybe while loading resources
        try { Thread.sleep(duration); } catch (Exception e) {}
        
        copyrt.setText("Copyright - Dr. Jason Barraclough");
        try { Thread.sleep(duration); } catch (Exception e) {}
        setVisible(false);
        
    }
    
    public void showSplashAndExit() {
        
        showSplash();
        
    }
    
}
