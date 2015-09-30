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



/**
 * 
 *
 * @author jasonkb
 */
public class PreviewPanel extends JComponent implements Runnable, ChangeListener
{
    int position = 1;
    BufferedImage img;
    Thread thread;
    int maxPosition = 1;
    Distributor d = new Distributor();
    JButton playButton;
    JSlider slider;
    int playSpeed = 33;//try and set up default to be 1:1 for a 30FPS film
    double playSpeedFactor = 1;//no alteration in time
    
    
    public PreviewPanel(JButton p, JSlider s)
    {
        maxPosition = (int)(PhysMo.duration * PhysMo.fps);
        playSpeed = (int)((1/(PhysMo.fps))*1000); // this should creat the inverse (ie the seconds per frame) and give milliseconds
        
        this.playButton = p;
        this.slider = s;
        slider.setMaximum(maxPosition);
        slider.setMinimum(1);
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.addChangeListener(this);
        
        
        this.setPreferredSize(new Dimension(300,300));
        
        
        System.out.println("Max frames are "+maxPosition);
        
        try 
        {
            img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview1.png"));
        } 
        catch (IOException e) 
        {
            
        }
        
    }
    
    public void setPlaySpeed(double fps)
    {
        playSpeed = (int)((1/(fps))*1000);
    }
    
    public void setPlaySpeedFactor(double factor)
    {
        playSpeedFactor = 1/factor;
    }
    
    
    
    public void playPreview()
    {
        if(playSpeed != (int)((1/(PhysMo.fps))*1000))//because a new timebase has been set...
        {
            playSpeed = (int)((1/(PhysMo.fps))*1000);//update the previewer's timebase
        }
        if(thread == null)
        {
            if(position >= maxPosition)
            {
                position = 1;
                
            }
            thread = new Thread(this);
            thread.start();
        }
        else
        {
            thread = null;
            System.out.println("Position is: "+position);
            slider.setValue(position);
            slider.repaint();
            
        }
    }
    
    public void advanceOne()
    {
        if(position >= maxPosition)
        {
            return;
        }
        
        if(position <maxPosition)
        {
            position++;
            slider.setValue(position);
            slider.repaint();
            
            try 
            {
                img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview"+position+".png"));
                this.repaint();
            } 
            catch (IOException e) 
            {
            
            }
            
        }
    }
    
    public void backOne()
    {
        if(position <=1)
        {
            slider.setValue(position);
            slider.repaint();
            return;
            
        }
        
        if(position >1)
        {
            position--;
            slider.setValue(position);
            slider.repaint();
            
            try 
            {
                img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview"+position+".png"));
                this.repaint();
            } 
            catch (IOException e) 
            {
            
            }
            
        }
    }
    
    public void fullBack()
    {
        position = 1;
        slider.setValue(position);
        slider.repaint();
        
        try 
        {
            img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview"+position+".png"));
            this.repaint();
        } 
        catch (IOException e) 
        {
           
        }
        
        
    }
    
    public void fullForward()
    {
        position = maxPosition;
        slider.setValue(position);
        slider.repaint();
        
        try 
        {
            img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview"+position+".png"));
            this.repaint();
        } 
        catch (IOException e) 
        {
           
        }
    }
    
    public void stopPreview()
    {
        thread = null;
        System.out.println("Position is: "+position);
        slider.setValue(position);
            slider.repaint();
            //firePropertyChange("positon", position-1, position);
    }
    
    public Dimension getPreferredDimension()
    {
        return new Dimension(300,300);
    }
    
   @Override public Dimension getPreferredSize()
    {
        return new Dimension(300,300);
    }
   
   @Override public Dimension getMinimumSize()
   {
       return new Dimension(300,300);
   }
   
   @Override public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(img, (getWidth()/2)-100, (getHeight()/2)-100, null);
        
        
        
        
        
        String family = "ARIAL";
        int style = Font.PLAIN;
        int size = 18;
        Font font = new Font(family, style, size);
        g.setColor(Color.WHITE);

        g.setFont(font);
    
        // Draw a text such that the top-left corner is at x, y
        
        FontMetrics fontMetrics = g.getFontMetrics();
        g.drawString("Frame: "+position, 10, 10+fontMetrics.getAscent());
        
        
        family = "ARIAL";
        style = Font.PLAIN;
        size = 12;
        font = new Font(family, style, size);
        
        g.setFont(font);
        
        //display film frame rate in bottom left corner
        g.drawString("FPS: "+PhysMo.fps, 10, this.getHeight()-10);
        
    }
    
   @Override public void paint(Graphics g)
   {
       super.paint(g);
   }
   
   public void stateChanged(ChangeEvent e)
   {
       JSlider sliderEvent=(JSlider)e.getSource();
       int size=sliderEvent.getValue();
       position = size;
       slider.setValue(size);
       
       if(thread == null)
       {
            try 
            {
                img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview"+position+".png"));
                this.repaint();
            } 
            catch (IOException err) 
            {
           
            }
       }
       
   }
   
    @Override public void run()
    {
        
        playButton.setIcon(new ImageIcon(d.getPause()));
        playButton.repaint();
        //slider.setEnabled(false);
        
        while(thread != null)
        {
            
            try
            {
                thread.sleep((int)(playSpeed*playSpeedFactor));
            }
            catch(InterruptedException e)
            {
               
            }
           
            try 
            {
                img = ImageIO.read(new File(PhysMo.workingDirectory+"/preview"+position+".png"));
                this.repaint();
            } 
            catch (IOException e) 
            {
            
            }
            
            position++;
            //firePropertyChange("positon", position-1, position);
            if(position >= maxPosition)
            {
                //System.out.println("Position is: "+position+" Max pos is: "+maxPosition);
                stopPreview();
                //firePropertyChange("positon", position-1, position);
            }
            //System.out.println("Position is: "+position+" Max pos is: "+maxPosition);
            slider.setValue(position);
            slider.repaint();
        }
        System.out.println("After thread position is: "+position);
        playButton.setIcon(new ImageIcon(d.getPlay()));
        playButton.repaint();
        //we want the end of the film to have an update of the analysis panel so fire propertyChange and propagate up the heirachy
        firePropertyChange("positon", position-1, position);
        
        
        slider.setValue(position);
        slider.repaint();
        
       
   }
   
}