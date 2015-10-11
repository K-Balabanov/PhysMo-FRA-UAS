/*
 * This is the main window to collect all components and tools in the same location
 */
package main;

import gui.images.*;
import gui.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.skin.*;



/**
 *
 * @author jasonkb
 */
public class PhysMo extends JFrame implements ActionListener
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
    
    public JMenuBar menuBar;
    public JMenu menuInformation;
    public JMenuItem menuItemInstructions, menuItemAbout, menuItemOriginal;
    
    public PhysMo()
    {
        super("PhysMo v3.0 - FRA-UAS");
        super.setIconImage(d.getFrameIcon32());
               
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menuInformation = new JMenu("Information");
        menuInformation.setMnemonic(KeyEvent.VK_I);
        menuInformation.getAccessibleContext().setAccessibleDescription("Instructions and Information about the developers.");
        menuBar.add(menuInformation);

        //a group of JMenuItems
        menuItemInstructions = new JMenuItem("Instructions", new ImageIcon(d.getBook()));
        menuItemInstructions.setMnemonic(KeyEvent.VK_I);
        menuItemInstructions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
        menuItemInstructions.getAccessibleContext().setAccessibleDescription("Instructions on how to use the program.");
        menuInformation.add(menuItemInstructions);

        menuInformation.addSeparator();
        
        menuItemOriginal = new JMenuItem("Original notice", new ImageIcon(d.getFrameIcon()));
        menuItemOriginal.setMnemonic(KeyEvent.VK_O);
        menuItemOriginal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItemOriginal.getAccessibleContext().setAccessibleDescription("About the people behind the program.");
        menuInformation.add(menuItemOriginal);
        
        menuInformation.addSeparator();
        
        menuItemAbout = new JMenuItem("About", new ImageIcon(d.getQuery()));
        menuItemAbout.setMnemonic(KeyEvent.VK_A);
        menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuItemAbout.getAccessibleContext().setAccessibleDescription("About the people behind the program.");
        menuInformation.add(menuItemAbout);
        this.setJMenuBar(menuBar);

        menuItemAbout.addActionListener(this);
        menuItemInstructions.addActionListener(this);
        menuItemOriginal.addActionListener(this);
        
        openScreen = new FirstScreen(this);
        //SwingUtilities.updateComponentTreeUI(openScreen);
        this.add(openScreen, BorderLayout.CENTER);
        PhysMo.windows = isWindows();
        
    }
    
        public void actionPerformed(ActionEvent e)
    {
        
        if(e.getSource() == menuItemInstructions)
        {
            try
            {
                Runtime.getRuntime().exec("gnome-open KnowHow.pdf");
            }
            catch(java.io.IOException error)
            {
                //Handle an IOException here.
                return;
            } 
        }
        if(e.getSource() == menuItemAbout)
        {
            JDialog about = new aboutDialog(this);
            about.show();          
        }
        if(e.getSource() == menuItemOriginal)
        {
            try
            {
                Runtime.getRuntime().exec("gnome-open about.pdf");
            }
            catch(java.io.IOException error)
            {
                //Handle an IOException here.
                return;
            } 
        }
        
    }
    
    public static double getDuration(){
        return duration;
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
        //SwingUtilities.updateComponentTreeUI(f);
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
