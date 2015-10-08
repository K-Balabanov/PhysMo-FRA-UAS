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
    public String pwd;
    public JFileChooser videoSelect;

    public AnalysisWindow projectWindow;
    
    public FirstScreen(JFrame frame)
    {
        pb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 10);
        ffmpeg = new FFmpegIO(pb);
        f = frame;
        f.addWindowListener(this);
        this.setLayout(new BorderLayout());
        pwd = getPWD();
        try
        {
            Runtime.getRuntime().exec("mkdir videos");
            Runtime.getRuntime().exec("mkdir workspace");
        }
        catch(java.io.IOException error)
        {
            return;
        } 
        
        processVideo = new JButton("Process Video", new ImageIcon(d.getFrameIcon()));
        processVideo.setMnemonic(KeyEvent.VK_P);
        recordVideo = new JButton("Record Video", new ImageIcon(d.getVideoIcon()));
        recordVideo.setMnemonic(KeyEvent.VK_R);
        testCamera = new JButton("Test Camera", new ImageIcon(d.getSnapshot()));
        testCamera.setMnemonic(KeyEvent.VK_E);
        scidavis = new JButton("SciDAVis", new ImageIcon(d.getScidavisIcon()));
        scidavis.setMnemonic(KeyEvent.VK_S);
        stopCam = new JButton("Stop testing", new ImageIcon(d.getCloseIcon()));
        stopCam.setMnemonic(KeyEvent.VK_T);
        mypanel = new JPanel();
        mypanel.setLayout(new GridLayout(6,1,10,10));
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
                               
                //... Update user interface.
                f.setTitle("PhysMo v3.0 - FRA-UAS - "+file.getName());
                PhysMo.videoName = file.getName();
                PhysMo.videoPath = file.getAbsolutePath();
                
                //parse the video information
                double[] videoProperties = ffmpeg.getProperties(PhysMo.videoPath);
                PhysMo.duration = videoProperties[0];
                PhysMo.fps = videoProperties[1];
                f.repaint();

                    processVideo.setEnabled(false);
                    recordVideo.setEnabled(false);
                    testCamera.setEnabled(false);

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
                return;
            }
            pb.setIndeterminate(false);                                                
            pb.setStringPainted(false);
            f.repaint();
        }
        
        if(e.getSource()== testCamera)
        {
            testCamera.setEnabled(false);
            recordVideo.setEnabled(false);
                pb.setIndeterminate(true);
                pb.setString("Testing camera");
                pb.setStringPainted(true);
                f.repaint();
                Runnable startCam = new Runnable() 
                {
                    public void run() 
                    {
                        try
                        {
                            //Process proc = Runtime.getRuntime().exec("pidof guvcview");
                            testCamProc = new ProcessBuilder("guvcview").start();
                            //BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        }
                        catch(java.io.IOException error)
                        {
                            return;
                        }
                    }
                };
                //Thread t1 = new Thread(startCam);
                //t1.start();
                SwingUtilities.invokeLater(startCam);
                synchronized(this){notifyAll();}
        }
                
        if(e.getSource() == recordVideo)
        {   
            testCamera.setEnabled(false);
            /* 
            String pwd = getPWD();
            try {
                p = new ProcessBuilder("ffmpeg", "-an", "-y", "-f", "v4l2", "-framerate", "30", "-video_size", "320x240", "-t", "0:0:3", "-i", "/dev/video1", pwd).start();
            } catch (IOException ex) {
                Logger.getLogger(FirstScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
                Runnable rec = new Runnable() 
                {
                    public void run() 
                    {
                        try
                        {
                            pb.setIndeterminate(true);                                                
                            pb.setString("Recording video");
                            pb.setStringPainted(true);
                            f.repaint();
                            String cmd = "ffmpeg -an -y -f v4l2 -framerate 30 -video_size 640x480 -t 0:0:2 -i /dev/video0 " + pwd;
                            System.out.println(pwd);
                            Runtime.getRuntime().exec(cmd);
                            try
                            {
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException ex)
                            {
                                Logger.getLogger(FirstScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            pb.setIndeterminate(false);                                                
                            pb.setStringPainted(false);
                            testCamera.setEnabled(true);
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
                recordVideo.setEnabled(true);
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
            System.out.println("uh oh");
            return "./freefall.avi";
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
                int frames = (int) (Math.ceil(PhysMo.getDuration()) * (int) PhysMo.getFPS() + 3);
                for(int k = 1; k <= frames; k++){
                    Path path = FileSystems.getDefault().getPath("./workspace/", "frame"+k+".png");
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ex) {
                        System.out.println("Nothing to delete with label frameX.png.");
                        break;
                    }
                }
                for(int k = 1; k <= frames; k++){
                    Path path = FileSystems.getDefault().getPath("./workspace/", "preview"+k+".png");
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ex) {
                        System.out.println("Nothing to delete with label previewX.png.");
                        break;
                    }
                }
                try {
                    Runtime.getRuntime().exec("rm -r videos");
                } catch (IOException ex) {
                    Logger.getLogger(FirstScreen.class.getName()).log(Level.SEVERE, null, ex);
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