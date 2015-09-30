/*
 * This is the main window to collect all components and tools in the same location
 */
package main;

import tools.*;
import gui.images.*;
import gui.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;


import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.skin.*;



/**
 *
 * @author jasonkb
 */
public class PhysMo extends JFrame
{
    public FirstScreen openScreen;
    public static String videoName = "";
    public static String videoPath = "";
    public static String workingDirectory = "./workspace";
    public static String videoInformation = "";
    public static String videoDirectory = "./videos";
    //public static String lastWorkingDirectory = "./workspace";
    public static double duration = 0.0;
    public static double fps = 0.0;
    public static double unMagnifiedRealWorldDistance = 0.0;
    public Distributor d = new Distributor();
    public static boolean windows;
    public static String videoInformationString = "FFMPEG OUTPUT";
    
    public static boolean isVisible = false;
    
    public PhysMo()
    {
        super("PhysMo v2.0 - FRA-UAS");
        super.setIconImage(d.getFrameIcon32());
               
        openScreen = new FirstScreen(this);
        SwingUtilities.updateComponentTreeUI(openScreen);
        this.add(openScreen, BorderLayout.CENTER);
        PhysMo.windows = isWindows();
        
    }
    
    public static double getFPS(){
        return fps;
    }
    
    public static boolean isWindows(){
 
		String os = System.getProperty("os.name").toLowerCase();
		//windows
	    return (os.indexOf( "win" ) >= 0); 
 
	}

    
    public static void main(String args[])
    {
            /*FileInputStream fstream = new FileInputStream("PhysMo.prop");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strline = "";
            
            //read in video folder
            strline = br.readLine();
            PhysMo.lastVideoDirectory = strline;*/
            System.out.println("Video directory: " + videoDirectory);
            
            
            //strline = br.readLine();
            //PhysMo.lastWorkingDirectory = strline;
            System.out.println("Last working directory: " + workingDirectory);
            
            //br.close();
        
        
        Runnable doWorkRunnable = new Runnable() 
                {
                    public void run() 
                    {
                        SubstanceSkin skin = new OfficeSilver2007Skin();
                        SubstanceLookAndFeel.setSkin(skin);
                        
                    }
                };
        SwingUtilities.invokeLater(doWorkRunnable);
        

        PhysMo f = new PhysMo();
        SwingUtilities.updateComponentTreeUI(f);
        Splash splash = new Splash(3000);

        splash.showSplashAndExit();
        
       
        
        f.pack();
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width- 380)/2;
        int y = (screen.height - 300)/2;
        f.setBounds(x,y,380,300);
        
        
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        f.setVisible(true);
        
        
        
      
        
        
    
    
}
    
}
