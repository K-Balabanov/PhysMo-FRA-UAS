/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import gui.images.Distributor;
import tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import main.PhysMo;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 *
 * @author jasonkb
 */
public class FirstScreen extends JPanel implements ActionListener, WindowListener, Runnable
{  
    public JButton processVideo;
    public JButton existingProject;
    public JButton howtoUse;
    //public JButton aboutPhysmo;
    public JButton recordVideo;
    public JButton testCamera;
    public JButton scidavis;
    public JButton stopCam;
    public JFrame f;
    public FFmpegIO ffmpeg;
    public JProgressBar pb;
    public Distributor d = new Distributor();
    public JPanel mypanel;
    public Process testCamProc;
    
    public JFileChooser videoSelect;

    public AnalysisWindow projectWindow;
    
    public JMenuBar menuBar;
    public JMenu menuInformation;
    public JMenuItem menuItemInstructions, menuItemAbout;
    
    public FirstScreen(JFrame frame)
    {
        pb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 10);
        ffmpeg = new FFmpegIO(pb);
        f = frame;
        f.addWindowListener(this);
        this.setLayout(new BorderLayout());
        
        try
        {
            Runtime.getRuntime().exec("mkdir videos");
            Runtime.getRuntime().exec("mkdir workspace");
        }
        catch(java.io.IOException error)
        {
            return;
        } 
        
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
        
        menuItemAbout = new JMenuItem("Developers", new ImageIcon(d.getQuery()));
        menuItemAbout.setMnemonic(KeyEvent.VK_D);
        menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        menuItemAbout.getAccessibleContext().setAccessibleDescription("About the people behind the program.");
        menuInformation.add(menuItemAbout);
        //f.setJMenuBar(menuBar);
        //menuBar.add(Box.createRigidArea(new Dimension(5,0)));
        this.add(menuBar, BorderLayout.NORTH);
        
        processVideo = new JButton("Process Video", new ImageIcon(d.getFrameIcon()));
        processVideo.setMnemonic(KeyEvent.VK_P);
        //aboutPhysmo = new JButton("About PhysMo", new ImageIcon(d.getQuery()));
        //aboutPhysmo.setMnemonic(KeyEvent.VK_A);
        recordVideo = new JButton("Record Video", new ImageIcon(d.getVideoIcon()));
        recordVideo.setMnemonic(KeyEvent.VK_R);
        testCamera = new JButton("Test Camera", new ImageIcon(d.getSnapshot()));
        testCamera.setMnemonic(KeyEvent.VK_E);
        scidavis = new JButton("SciDAVis", new ImageIcon(d.getScidavisIcon()));
        scidavis.setMnemonic(KeyEvent.VK_S);
        stopCam = new JButton("Stop testing", new ImageIcon(d.getCloseIcon()));
        stopCam.setMnemonic(KeyEvent.VK_T);
        mypanel = new JPanel();
        mypanel.setLayout(new GridLayout(7,1,10,10));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mypanel.setBorder(BorderFactory.createTitledBorder("Options"));

        setLayout(new FlowLayout());
        mypanel.add(testCamera);
        mypanel.add(stopCam);
        mypanel.add(recordVideo);
        mypanel.add(processVideo);
        mypanel.add(scidavis);
        //mypanel.add(aboutPhysmo);
        mypanel.add(pb);
        this.add(mypanel, BorderLayout.CENTER);

        //pb.setVisible(false);
        
        testCamera.addActionListener(this);
        stopCam.addActionListener(this);
        recordVideo.addActionListener(this);
        processVideo.addActionListener(this);
        scidavis.addActionListener(this);
        //aboutPhysmo.addActionListener(this);
        menuItemInstructions.addActionListener(this);
        menuItemAbout.addActionListener(this);
        //stopCam.setEnabled(false);
        f.pack();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
        if(e.getSource() == processVideo)
        {
            
            videoSelect = new JFileChooser(PhysMo.videoDirectory);
            videoSelect.setFileFilter(new MovieFileFilter());
            videoSelect.setFileView(new MovieFileView());
            videoSelect.setAcceptAllFileFilterUsed(false);
            videoSelect.setDialogTitle("Open a video file...");
            
            int retval = videoSelect.showOpenDialog(null);
            if (retval == JFileChooser.APPROVE_OPTION) 
            {
                //... The user selected a file, get it, use it.
                File file = videoSelect.getSelectedFile();
                //PhysMo.videoDirectory = file.getParent();
                
                
                //... Update user interface.
                f.setTitle("PhysMo v2.0 - FRA-UAS - "+file.getName());
                PhysMo.videoName = file.getName();
                PhysMo.videoPath = file.getAbsolutePath();
                
                //parse the video information
                double[] videoProperties = ffmpeg.getProperties(PhysMo.videoPath);
                PhysMo.duration = videoProperties[0];
                PhysMo.fps = videoProperties[1];
                f.repaint();
                //select working directory
                
                //workingDirectorySelect = new JFileChooser(PhysMo.workingDirectory);
                //workingDirectorySelect.setDialogTitle("Create a directory to work in...");
                //workingDirectorySelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
                //retval = workingDirectorySelect.showOpenDialog(null); 
                //if (retval == JFileChooser.APPROVE_OPTION) 
                //{
                    //... The user selected a file, get it, use it.
                    //file = workingDirectorySelect.getSelectedFile();
                    //... Update user interface.
                    //PhysMo.workingDirectory = file.getAbsolutePath();
                    //PhysMo.lastWorkingDirectory = file.getAbsolutePath();

                    processVideo.setEnabled(false);
                    recordVideo.setEnabled(false);
                    testCamera.setEnabled(false);
                
                    //FileWriter outFile = new FileWriter("./PhysMo.prop");
                    //PrintWriter out = new PrintWriter(outFile);
           
                    // Also could be written as follows on one line
                    // Printwriter out = new PrintWriter(new FileWriter(args[0]));
       
                    // Write text to file
                    //out.println(PhysMo.lastVideoDirectory);
                    //out.println(PhysMo.lastWorkingDirectory);
                    //out.close();

                    f.setVisible(true);

                    Thread t = new Thread(this);
            
                    t.start();        
                    synchronized(this){notifyAll();}
                                    
                
            }
   
        }
        if(e.getSource() == scidavis)
        {
            try
            {
                Runtime.getRuntime().exec("scidavis");
            }
            catch(java.io.IOException error)
            {
                //Handle an IOException here.
                return;
            }
            pb.setIndeterminate(false);                                                
            pb.setStringPainted(false);
            f.repaint();
        }
        
        if(e.getSource()== menuItemAbout)
        {
            pb.setIndeterminate(false);                                                
            pb.setStringPainted(false);
            f.repaint();
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
        
        if(e.getSource()== testCamera)
        {
                testCamera.setEnabled(false);
                //stopCam.setEnabled(true);
                pb.setIndeterminate(false);                                                
                pb.setStringPainted(false);
                f.repaint();
                Runnable startCam = new Runnable() 
                {
                    public void run() 
                    {
                        try
                        {
                            Process proc = Runtime.getRuntime().exec("pidof guvcview");
                            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                            if(stdInput.readLine() != null)
                            {
                                pb.setIndeterminate(true);                                                
                                pb.setString("Camera is already in use!");
                                pb.setStringPainted(true);
                                f.repaint();
                            }
                            else
                            {
                                Runtime.getRuntime().exec("guvcview --no_display");
                            }
                        }
                        catch(java.io.IOException error)
                        {
                            //Handle an IOException here.
                            return;
                        }
                    }
                };
                //Thread t1 = new Thread(startCam);
                //t1.start();
                SwingUtilities.invokeLater(startCam);
                synchronized(this){notifyAll();}
        }
                
        if(e.getSource() == recordVideo)//if testCamera is active it has to be turned off
        {                      /* String pwd = getPWD();
            try {
                p = new ProcessBuilder("ffmpeg", "-an", "-y", "-f", "v4l2", "-framerate", "30", "-video_size", "320x240", "-t", "0:0:3", "-i", "/dev/video1", pwd).start();
            } catch (IOException ex) {
                Logger.getLogger(FirstScreen.class.getName()).log(Level.SEVERE, null, ex);
            }*/
                Runnable rec = new Runnable() 
                {
                    public void run() 
                    {
                        try
                        {
                            String pwd = getPWD();
                            pb.setIndeterminate(true);                                                
                            pb.setString("Recording video");
                            pb.setStringPainted(true);
                            f.repaint();
                            String cmd = "ffmpeg -an -y -f v4l2 -framerate 30 -video_size 320x240 -t 0:0:2 -i /dev/video0 " + pwd;
                            Runtime.getRuntime().exec(cmd);
                            try
                            {
                                Thread.sleep(3000);
                            }
                            catch (InterruptedException ex)
                            {
                                Logger.getLogger(FirstScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            pb.setIndeterminate(false);                                                
                            pb.setStringPainted(false);
                            f.repaint();
                        }
                        catch(java.io.IOException error)
                        {
                            //Handle an IOException here.
                            return;
                        }
                    }
                };
                Thread recThr = new Thread(rec);
                recThr.start();
                synchronized(this){notifyAll();}
        }
        if(e.getSource() == stopCam){
                testCamera.setEnabled(true);
                //stopCam.setEnabled(false);
                try
                {
                    Process proc = Runtime.getRuntime().exec("pidof guvcview");
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    String str = "kill -9 " + stdInput.readLine();
                    Runtime.getRuntime().exec(str);
                }
                catch(java.io.IOException error)
                {
                    //Handle an IOException here.
                    return;
                }
                pb.setIndeterminate(false);                                                
                pb.setStringPainted(false);
                f.repaint();
            
        }
    }

    public String getPWD(){
        try
        {
            Process proc = Runtime.getRuntime().exec("pwd");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String str = stdInput.readLine() + "/videos/" + dateFormat.format(date) + ".avi";
            return str;
        }
        catch(java.io.IOException error)
        {
            return "/home/Videos/freefall.avi";
        }
    }
    
    public void run()
    {

                        pb.setIndeterminate(true);                                                
                        pb.setString("Decomposing video");
                        pb.setStringPainted(true);
                        f.repaint();
       
       
                ffmpeg.decomposeVideo(PhysMo.videoPath, PhysMo.workingDirectory, "png");
                
                        pb.setIndeterminate(true);                                                
                        pb.setString("Opening video");
                        pb.setStringPainted(true);
                        //f.toBack(); // move the window to the background
                
                
                try{
                Runnable doWorkRunnable3 = new Runnable() 
                {
                    public void run() 
                    {
                        projectWindow = new AnalysisWindow();
                        projectWindow.setVisible(true);
                        projectWindow.toFront();
                        projectWindow.setExtendedState(projectWindow.getExtendedState()|JFrame.MAXIMIZED_BOTH);
                        PhysMo.isVisible = true;
                         
                        
                    }
                };
                SwingUtilities.invokeAndWait(doWorkRunnable3);
                }
                catch(InterruptedException ie)
                {
                    
                }
                catch(InvocationTargetException iv)
                {
                    
                }
                projectWindow.addWindowListener(this);
               
                pb.setIndeterminate(false);                                                
                pb.setStringPainted(false);
                f.repaint();
                f.toBack();
                
    }
    
    public void windowOpened(WindowEvent e)
    {
        
    }
    
    public void windowClosing(WindowEvent e)
    {
        if(e.getSource() == projectWindow)
        {
            System.out.println("Window closing");
            PhysMo.isVisible = false;
            processVideo.setEnabled(true);
            recordVideo.setEnabled(true);
            testCamera.setEnabled(true);
            processVideo.repaint();
            recordVideo.repaint();
            testCamera.repaint();
        }
        
        if(e.getSource() == f)
        {
            int i = JOptionPane.showConfirmDialog(null, "This will close PhysMo and clean the workspace! \nAre you sure?","Close PhysMo",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(i == JOptionPane.YES_OPTION)
            {
                int frames = 2 * (int) PhysMo.getFPS() + 1;
                for(int k = 1; k <= frames; k++){
                    Path path = FileSystems.getDefault().getPath("./workspace/", "frame"+k+".png");
                    try {
                        Files.delete(path);
                    } catch (IOException ex) {
                        System.out.println("Nothing to delete with label frameX.png.");
                        break;
                    }
                }
                for(int k = 1; k <= frames; k++){
                    Path path = FileSystems.getDefault().getPath("./workspace/", "preview"+k+".png");
                    try {
                        Files.delete(path);
                    } catch (IOException ex) {
                        System.out.println("Nothing to delete with label previewX.png.");
                        break;
                    }
                }
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            else
            {
                f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        }
        
    }
    
    public void windowClosed(WindowEvent e)
    {
        System.out.println("Window closed");
    }
    
    public void windowIconified(WindowEvent e)
    {
        
    }
    
    public void windowDeiconified(WindowEvent e)
    {
        
    }
    
    public void windowActivated(WindowEvent e)
    {
        
    }
    
    public void windowDeactivated(WindowEvent e)
    {
        
    }
    
    
}