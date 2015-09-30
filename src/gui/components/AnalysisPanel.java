/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;


import gui.images.Distributor;
import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.Math.*;
import tools.AnalysisFilter;
import tools.CannyEdgeDetector;



/**
 * 
 *
 * @author jasonkb
 */
public class AnalysisPanel extends JComponent implements Runnable, MouseListener, MouseMotionListener
{
    BufferedImage img;
    BufferedImage temp;
    Thread thread = null;
    int frame = 1;
    int componentWidth = 0;
    int componentHeight = 0;
    int multiplicationFactor = 1;
    private int x = 0;
    private int y = 0;
    protected static int PLOTTING_MODE = 1;
    protected static int CALIBRATION_MODE = 2;
    protected static int PROTRACTOR_MODE = 3;
    protected static int MARKER_MODE = 4;
    private int currentMode = 2;//default is to calibrate first up
    public boolean plotsVisible = true;
    public boolean anglesVisible = true;
    
    
    /**
     * consists of x1,x2,y1,y2,real world double distance, multiplication factor this was recorded at
     */
    private double[] calibrationData = new double[]{0,0,0,0,0.0,1};
    private boolean isCalibrated = false;//start out uncalibrated
    private int calibrationPoints = 0;
    private double metrePerPixel = 0.0;
    
    public Trace[] traces;
    private int currentTrace;
    
    //private double[][] currentPlotPoints;
    public int maxFrame = 0;
    
    public double[] markerX = new double[]{1.0,0.0};
    public double[] markerY = new double[]{1.0,0.0};
    
    public Angle[] angles;
    public Angle currentAngle = new Angle();
    
    
    private JLabel xyData;
    
    private boolean filtered = false;
    
    private CannyEdgeDetector filter;
    
    public DataTabbedPane tp;
    
    
    
    /**
     * this is the value each pixel has in meters
     */
    double scale= 1.0;
    
    public AnalysisPanel(JLabel xy)
    {
        xyData = xy;
        currentTrace = 0;
        traces = new Trace[1];
        
        
        try 
        {
            img = ImageIO.read(new File(PhysMo.workingDirectory+"/frame"+frame+".png"));
            
            
            this.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
            
            
        } 
        catch (IOException e) 
        {
            
        }
        
        addMouseMotionListener(this);
        addMouseListener(this);
        
        //thread = new Thread(this);
        //thread.start();
        
    }
    
    
    public BufferedImage getImage()
    {
        return this.img;
    }
    
    public void setImageFilter(CannyEdgeDetector f)
    {
       this.filter = f;
       setFiltered(true);
       this.repaint();
    }
    
    public void setFiltered(boolean filtration)
    {
        this.filtered = filtration;
    }
    
    public boolean isFiltered()
    {
        return this.filtered;
    }
   
     public Dimension getPreferredDimension()
    {
        return new Dimension(img.getWidth()*multiplicationFactor,img.getHeight()*multiplicationFactor);
    }
    
   @Override public Dimension getPreferredSize()
    {
        return new Dimension(img.getWidth()*multiplicationFactor,img.getHeight()*multiplicationFactor);
    }
   
   @Override public Dimension getMinimumSize()
   {
       return new Dimension(img.getWidth()*multiplicationFactor,img.getHeight()*multiplicationFactor);
   }
    
    @Override public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(img, 0, 0,img.getWidth()*multiplicationFactor,img.getHeight()*multiplicationFactor, null);
        
        
        String family = "ARIAL";
        int style = Font.PLAIN;
        int size = 12;
        Font font = new Font(family, style, size);
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();
        
        //xmarker
        g.setColor(Color.WHITE);
        g.drawLine((int)(this.markerX[0]/this.markerX[1])*this.multiplicationFactor, 0, (int)(this.markerX[0]/this.markerX[1])*this.multiplicationFactor, (int)img.getHeight()*this.multiplicationFactor);
        //now the label
        g.drawString("Y axis", (int)(this.markerX[0]/this.markerX[1])*this.multiplicationFactor +3, (int)fontMetrics.getStringBounds("Y", g).getHeight());
        
        
        //ymarker
        g.drawLine(0, (int)(this.markerY[0]/this.markerY[1])*this.multiplicationFactor, img.getWidth()*this.multiplicationFactor, (int)(this.markerY[0]/this.markerY[1])*this.multiplicationFactor);
        g.drawString("X axis", (int)img.getWidth()*this.multiplicationFactor - (int)fontMetrics.getStringBounds("X axis", g).getWidth(), (int)(this.markerY[0]/this.markerY[1])*this.multiplicationFactor -3);
        
        
        //draw calibration data in topleft corner of image
        
        
        ///******************************* very important lines of code find me later and use- means re-calibration is not required again
        double origCalMagFactor = this.calibrationData[5];
        double correctedMetrePixel = (this.metrePerPixel*origCalMagFactor)/multiplicationFactor;
        PhysMo.unMagnifiedRealWorldDistance = this.metrePerPixel*origCalMagFactor;//sets PhysMo as a 1:1 original size cal
        
        String calData = "Metres/Pixel = "+correctedMetrePixel;
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int)fontMetrics.getStringBounds(calData, g).getWidth()+15, 15+(int)fontMetrics.getStringBounds(calData, g).getHeight());
        
        g.setColor(Color.RED);
        g.drawString(calData, 2, 2+fontMetrics.getAscent());
        //draw calibration points on screen
        if(isCalibrated || !isCalibrated)
        {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(2.5f));
            
            
            double calx1 = (this.calibrationData[0]/origCalMagFactor)*multiplicationFactor;
            double calx2 = (this.calibrationData[1]/origCalMagFactor)*multiplicationFactor;
            double caly1 = (img.getHeight()*multiplicationFactor)-((this.calibrationData[2]/origCalMagFactor)*multiplicationFactor);
            double caly2 = (img.getHeight()*multiplicationFactor)-((this.calibrationData[3]/origCalMagFactor)*multiplicationFactor);
            
            //draw crosses for cal point 1
            g2d.drawLine((int)calx1-10, (int)caly1, (int)calx1+10, (int)caly1);
            g2d.drawLine((int)calx1, (int)caly1-10, (int)calx1, (int)caly1+10);
            
            //draw crosses for cal point 2
            g2d.drawLine((int)calx2-10, (int)caly2, (int)calx2+10, (int)caly2);
            g2d.drawLine((int)calx2, (int)caly2-10, (int)calx2, (int)caly2+10);
            
        //int mouseY = img.getHeight()*multiplicationFactor-e.getY();
        }
        
        
        //draw the plotting points on screen if they are used.  values of -1 for x,y are un-used plotting points.
        
        if(plotsVisible && this.currentMode == this.PLOTTING_MODE || this.currentMode == AnalysisPanel.MARKER_MODE)
        {
            //System.out.println("plot painter");
            PlotPoint currentPoint = this.traces[this.currentTrace].getPlotPoint(frame-1);
            
            
            if(frame-2 >=0)
                {
                    PlotPoint previousPoint = this.traces[this.currentTrace].getPlotPoint(frame-2);
                    //previous plot point
                    if(previousPoint != null)
                    {
                        if(previousPoint.isSet()){
                        double ppx1 = previousPoint.getScaledXPoint(this.multiplicationFactor);
                        double ppy1 = (img.getHeight()*multiplicationFactor) -previousPoint.getScaledYPoint(this.multiplicationFactor);
                        g.setColor(Color.MAGENTA);
                        Graphics2D g2d = (Graphics2D)g;
                
                        g2d.setStroke(new BasicStroke(1.0f));
                        
                        g2d.drawLine((int)ppx1-10, (int)ppy1, (int)ppx1+10, (int)ppy1);
                        g2d.drawLine((int)ppx1, (int)ppy1-10, (int)ppx1, (int)ppy1+10); }
                    }
                }
            
            if(currentPoint != null)
            {
                if(currentPoint.isSet()){
                double plotx1 = currentPoint.getScaledXPoint(this.multiplicationFactor);
                double ploty1 = (img.getHeight()*multiplicationFactor) -currentPoint.getScaledYPoint(this.multiplicationFactor);
            
                
                
                g.setColor(Color.GREEN);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setStroke(new BasicStroke(2.5f));
                //this is the current frame.  paint it yellow...
                g2d.drawLine((int)plotx1-10, (int)ploty1, (int)plotx1+10, (int)ploty1);
                g2d.drawLine((int)plotx1, (int)ploty1-10, (int)plotx1, (int)ploty1+10); 
                
                size = 9;
                font = new Font(family, style, size);
                g2d.setStroke(new BasicStroke(1f));
                g2d.setFont(font);
            
                fontMetrics = g2d.getFontMetrics();
        
                g2d.drawString(this.traces[this.currentTrace].getTraceName(), (float)plotx1+12, (float)ploty1 +(fontMetrics.getAscent()/2));
                
                }
            }
            
            
              
            
            
           
                
        }
        
        if(anglesVisible && this.currentMode == AnalysisPanel.PROTRACTOR_MODE || this.currentMode == AnalysisPanel.MARKER_MODE || this.currentMode == AnalysisPanel.PLOTTING_MODE)//lets display the angles that we draw...
        {
            //System.out.println("inside angle painter");
            
            Angle a = this.angles[frame-1];
            if(a != null && a.isSet())
            {
            double anglex1 = (a.getFocusXPoint()/a.getOriginalMagnification())*multiplicationFactor;
            double anglex2 = (a.getRayXPoint()/a.getOriginalMagnification())*multiplicationFactor;
            double angley1 = (img.getHeight()*multiplicationFactor)-((a.getFocusYPoint()/a.getOriginalMagnification())*multiplicationFactor);
            double angley2 = (img.getHeight()*multiplicationFactor)-((a.getRayYPoint()/a.getOriginalMagnification())*multiplicationFactor);
            
            g.setColor(Color.MAGENTA);
            
            //String family = "ARIAL";
            //int style = Font.PLAIN;
            size = 12;
            font = new Font(family, style, size);
            g.setFont(font);
            
            fontMetrics = g.getFontMetrics();
        
            g.drawString(a.getValueDegreesT()+" Deg", (int)anglex1+3, (int)angley1 +fontMetrics.getAscent());
            
            
            
            Graphics2D g2d = (Graphics2D)g;
                
            g2d.setStroke(new BasicStroke(1.0f));
                        
            g2d.drawLine((int)anglex1, (int)angley1, (int)anglex2, (int)angley2);
            g2d.drawLine((int)anglex1, (int)angley1, (int)anglex1, (int)angley1-30); //the true north arm...}
        }
        }
        
        
    }
    
   @Override public void paint(Graphics g)
   {
       super.paint(g);
   }
   
   
   
   
   public void setCalibrationPoint(int calx, int caly)
   {
       
       
       if(calibrationPoints == 1)//insert the first calibration point to the data[] object
       {
           this.calibrationData[0] = calx;
           this.calibrationData[2] = caly;
           this.calibrationData[5] = this.multiplicationFactor;
       }
       if(calibrationPoints == 2)//second point and prepare if about to re calibrate
       {
           this.repaint();
           if((int)calibrationData[5] != this.multiplicationFactor)
           {
               //user changed multiplication factor during calibration. warn the result is now void.
               
               JOptionPane.showMessageDialog(this.getRootPane(),"Magnification was changed during calibration.  Result invalid!","Calibration error",JOptionPane.ERROR_MESSAGE);
           }
           
           this.calibrationData[1] = calx;
           this.calibrationData[3] = caly;
           
           
           
           //calibrationPoints = 0;
           this.isCalibrated = true;
           
           this.repaint();
           
           
       }
   }
   
   
    public void changeScale(int newScale)
    {
        this.multiplicationFactor = newScale;
        this.repaint();
    }
    
    public void initPlottingPoints(int rows)
    {
        System.out.println("row count at init of plotting points is: "+rows);
        
        this.traces[this.currentTrace] = new Trace(rows);
        this.angles = new Angle[rows];
        this.maxFrame = rows;
        //tp = new DataTabbedPane(rows);
    }
    
    /**
     * Create new trace and swap to it
     */
    public void createNewTrace()
    {
                    System.out.println("traces1");
        Trace t = new Trace(this.maxFrame);
        Trace[] temp = new Trace[(traces.length+1)];
        for(int i = 0; i<=(traces.length-1);)
        {
            System.out.println("traces loop "+ (traces.length+1));
            temp[i] = this.traces[i];
            i++;
        }
        temp[temp.length -1]= t;
        this.traces = temp;
        this.currentTrace = this.traces.length-1;        
    }
    public Trace getCurrentTrace()
    {
        return this.traces[this.currentTrace];
    }
    /**
     * update the store of traces with the current lot then swap for the selected one
     * @param trace 
     */
    public void swapCurrentTrace(int trace)
    {
        this.currentTrace = trace;
    }
    
    public void removePlotPoint()
    {
       if(this.getCurrentMode() == this.PLOTTING_MODE)
        {
            
            //get current trace out of array, and remove appropriate plot
            this.traces[this.currentTrace].getPlotPoint(frame-1).reset();
            
            //tp.updateCell(this.currentTrace, frame-1, 2, 0);//x col
            //tp.updateCell(this.currentTrace, frame-1, 3 0);//y col
            //tp.getJTabbedPane().repaint();
            this.repaint();
        }
    }
    
    public int getMouseX()
    {
        return this.x;
    }
    
    public int getMouseY()
    {
        return this.y;
    }
    
    public int getCurrentMode()
    {
        return this.currentMode;
    }
    public void setMode(int mode)
    {
        this.currentMode = mode;
    }
    
    public void updateView(int newFrame)
    {
        this.frame = newFrame;
        System.out.println("Frame to draw to output panel is: "+frame);
        
        try 
        {
            img = ImageIO.read(new File(PhysMo.workingDirectory+"/frame"+frame+".png"));
            if(this.isFiltered())
            {
                filter.setSourceImage(img);
                filter.process();
                img = filter.getEdgesImage();
            }
            
            this.setPreferredSize(new Dimension(img.getWidth()*multiplicationFactor,img.getHeight()*multiplicationFactor));
            
            
        } 
        catch (IOException e) 
        {
            
        }
        
        
        
        thread = new Thread(this);
        thread.start();
    }
    
    public boolean isFocusTraversable()
    {
        return true;
    }
    
    public void mouseEntered(MouseEvent e)
    {
       // System.out.println("mouse entered");
    }
    public void mouseExited(MouseEvent e)
    {
        //System.out.println("mouse exited");
    }
    public void mousePressed(MouseEvent e)
    {
        int mouseX = e.getX();
        int mouseY = img.getHeight()*multiplicationFactor-e.getY();
        
        if(this.currentMode == AnalysisPanel.CALIBRATION_MODE)
        {
            this.calibrationPoints++;
        }
        
        if(this.getCurrentMode() == AnalysisPanel.MARKER_MODE)
        {
            markerX[0] = e.getX();
            markerY[0] = e.getY();
            markerX[1] = this.multiplicationFactor;
            markerY[1] = this.multiplicationFactor;
            this.repaint();
        }
        
        if(this.getCurrentMode() == AnalysisPanel.PROTRACTOR_MODE)//lets measure an angle
        {
            double[] point = new double[]{mouseX, mouseY};
            currentAngle = new Angle();
            currentAngle.setFocusPoint(point);
            this.angles[frame-1] = currentAngle;
        }
        this.repaint();
    }
    public void mouseReleased(MouseEvent e)
    {
        //System.out.println("mouse released");
        int mouseX = e.getX();
        int mouseY = img.getHeight()*multiplicationFactor-e.getY();
        
        if(this.getCurrentMode() == AnalysisPanel.MARKER_MODE)
        {
            markerX[0] = e.getX();
            markerY[0] = e.getY();
            markerX[1] = this.multiplicationFactor;
            markerY[1] = this.multiplicationFactor;
            double[] origon = new double[]{mouseX/this.multiplicationFactor, mouseY/this.multiplicationFactor};
            DataTable.xOrigon = mouseX/this.multiplicationFactor;
            DataTable.yOrigon = mouseY/this.multiplicationFactor;
            
            //firePropertyChange("axesChange", 0, origon);
            this.repaint();
        }
        
        if(this.getCurrentMode() == AnalysisPanel.PROTRACTOR_MODE)//lets measure an angle
        {
            double[] point = new double[]{mouseX, mouseY};
            currentAngle.setRayPoint(point);
            currentAngle.setMagnification(this.multiplicationFactor);
            currentAngle.setSet();
            this.angles[frame-1] = currentAngle;
        }
        
        if(this.getCurrentMode() == this.PROTRACTOR_MODE && ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK))//remove the point in question...
        {
            this.currentAngle.reset();
            this.angles[frame-1] = currentAngle;
            this.repaint();
            System.out.println("Was a popup trigger");
        }
        
        
        
        if(this.getCurrentMode() == AnalysisPanel.CALIBRATION_MODE )
        {
            this.repaint();
                   
            this.setCalibrationPoint(mouseX, mouseY);
            
            if(this.calibrationPoints == 2)
            {
                SetCalibrationFactorDialog d = new SetCalibrationFactorDialog(null, true, "Calibrate me");
                int x1 = (int)calibrationData[0];
                int x2 = (int)calibrationData[1];
                int y1 = (int)calibrationData[2];
                int y2 = (int)calibrationData[3];
           
                this.calibrationData[4] = d.getCalibration();
                double distance = calibrationData[4];
           
            //find hypotenuse distance for us now...
                double hypotenuseDistance = Math.sqrt(Math.pow((x2-x1),2) +(Math.pow((y2-y1),2)));
                this.metrePerPixel = distance/hypotenuseDistance;
                this.calibrationPoints = 0;
            }
            
            
            
            this.repaint();
        }
        
        if(this.getCurrentMode() == this.PLOTTING_MODE )
        {
            if(mouseX<= img.getWidth()*multiplicationFactor && mouseY >= 0 && mouseY <= img.getHeight()*multiplicationFactor)
            {
                this.traces[this.currentTrace].setPlotPoint(frame-1,new PlotPoint(mouseX,mouseY,this.multiplicationFactor));
                firePropertyChange("Table Update x", -1, (int)mouseX/multiplicationFactor);
                firePropertyChange("Table Update y", -1, (int)mouseY/multiplicationFactor);
            }
            
            this.repaint();
        }
        
        if(this.getCurrentMode() == this.PLOTTING_MODE && ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK))//remove the point in question...
        {
            this.removePlotPoint();
            this.repaint();
            System.out.println("Was a popup trigger");
            firePropertyChange("Table Update x", -1, 0*(int)mouseX/multiplicationFactor);
            firePropertyChange("Table Update y", -1, 0*(int)mouseY/multiplicationFactor);
        }
        
        
        this.repaint();
    }
    public void mouseClicked(MouseEvent e)
    {
        //System.out.println("mouse clicked");
        
        
    }
    public void mouseMoved(MouseEvent e)
    {
        //System.out.println("mouse move");
        int mouseX = e.getX();
        int mouseY = img.getHeight()*multiplicationFactor-e.getY();
        
        //System.out.println("coords "+mouseX+" "+mouseY+" img.width "+img.getWidth()*multiplicationFactor+ " img.height "+img.getHeight()*multiplicationFactor);
        
       
        
        
        if(mouseX<= img.getWidth()*multiplicationFactor && mouseY >= 0 && mouseY <= img.getHeight()*multiplicationFactor)
        {
            if(this.getCurrentMode() == this.PLOTTING_MODE)
            {
                xyData.setText("Plotting:   X = "+mouseX+ "    Y = "+mouseY);
            }
            if(this.getCurrentMode() == this.CALIBRATION_MODE)
            {
                xyData.setText("Calibrating:   X = "+mouseX+ "    Y = "+mouseY);
            }
            if(this.getCurrentMode() == this.PROTRACTOR_MODE)
            {
                xyData.setText("Finding Angle:   X = "+mouseX+ "    Y = "+mouseY);
            }
        }
        xyData.repaint();
        
    }
    public void mouseDragged(MouseEvent e)
    {
        int mouseX = e.getX();
        int mouseY = img.getHeight()*multiplicationFactor-e.getY();
        
        if(this.getCurrentMode() == AnalysisPanel.MARKER_MODE)
        {
            markerX[0] = e.getX();
            markerY[0] = e.getY();
            markerX[1] = this.multiplicationFactor;
            markerY[1] = this.multiplicationFactor;
            this.repaint();
        }
        
        if(this.getCurrentMode() == this.CALIBRATION_MODE)
        {
            this.setCalibrationPoint(mouseX, mouseY);
            
            
            
            this.repaint();
        }
        
        if(this.getCurrentMode() == AnalysisPanel.PROTRACTOR_MODE)//lets measure an angle
        {
            double[] point = new double[]{mouseX, mouseY};
            currentAngle.setRayPoint(point);
            currentAngle.setMagnification(this.multiplicationFactor);
            currentAngle.setSet();
            this.angles[frame-1] = currentAngle;
            this.repaint();
        }
        
        if(this.getCurrentMode() == this.PLOTTING_MODE  && !e.isPopupTrigger())
        {
            if(mouseX<= img.getWidth()*multiplicationFactor && mouseY >= 0 && mouseY <= img.getHeight()*multiplicationFactor)
            {
                firePropertyChange("Table Update x", -1, (int)mouseX/multiplicationFactor);
                firePropertyChange("Table Update y", -1, (int)mouseY/multiplicationFactor);
                this.traces[this.currentTrace].setPlotPoint(frame-1,new PlotPoint(mouseX,mouseY,this.multiplicationFactor));
               
            }
            
            this.repaint();
        }
        if(this.getCurrentMode() == this.PLOTTING_MODE && ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK))
        {
            this.removePlotPoint();
            this.repaint();
            System.out.println("Was a popup trigger");
            firePropertyChange("Table Update x", -1, 0*(int)mouseX/multiplicationFactor);
            firePropertyChange("Table Update y", -1, 0*(int)mouseY/multiplicationFactor);
        }
        
        
        if(mouseX<= img.getWidth()*multiplicationFactor && mouseY >= 0)
        {
            if(this.getCurrentMode() == this.PLOTTING_MODE)
            {
                xyData.setText("Plotting:   X = "+mouseX+ "    Y = "+mouseY);
            }
            if(this.getCurrentMode() == this.CALIBRATION_MODE)
            {
                xyData.setText("Calibrating:   X = "+mouseX+ "    Y = "+mouseY);
            }
            if(this.getCurrentMode() == this.PROTRACTOR_MODE)
            {
                xyData.setText("Finding Angle:   X = "+mouseX+ "    Y = "+mouseY);
            }
        }
        xyData.repaint();
    }
    
    public void run()
    {
        while(thread != null)
        {
            //paint the component here
            
            this.repaint();
            thread = null;
        }
    }
}
