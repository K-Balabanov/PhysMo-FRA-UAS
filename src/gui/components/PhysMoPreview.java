/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import com.jgoodies.forms.factories.ButtonBarFactory;
import gui.images.Distributor;
import main.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Locale;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceAutumnLookAndFeel;




/**
 * 
 *
 * @author jasonkb
 */
public class PhysMoPreview extends JPanel implements ActionListener, PropertyChangeListener, MouseListener, ChangeListener
{    
    
    PreviewPanel p;
    JButton play;
    JButton stop;
    JButton fullBack;
    JButton fullForward;
    JButton advanceOne;
    JButton backOne;
    
    JSlider slider;
    JSpinner playbackSpeed;
    
    JToolBar buttonStrip;
    
    JPanel controls;
    Distributor d;
    
    
    
    
    
    public PhysMoPreview()
    {
        d = new Distributor();
        
        
        this.setMaximumSize(new Dimension(400,400));
        this.setMinimumSize(new Dimension(400,400));
        
        slider = new JSlider(JSlider.HORIZONTAL, 1, 50, 1);
        slider.setFocusable(false);
        slider.addMouseListener(this);
        //slider.setPreferredSize(new Dimension(slider.getPreferredSize().width,(int)(slider.getPreferredSize().height*.7)));
        //slider.setMaximumSize(new Dimension(slider.getPreferredSize().width,(int)(slider.getPreferredSize().height*.7)));
        play = new JButton(new ImageIcon(d.getPlay()));
        p = new PreviewPanel(play, slider);
        p.addPropertyChangeListener(this);
        
        this.setLayout(new BorderLayout());
        
        this.add(p, BorderLayout.CENTER);
        
        
    
        
         

        
        
        
        
        
        
        
        
        
        buttonStrip = new JToolBar(JToolBar.HORIZONTAL);
        buttonStrip.setFloatable(false);
        buttonStrip.setRollover(true);
        buttonStrip.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        play.addActionListener(this);
        play.setFocusPainted(false);
        //play.setToolTipText("Play/pause preview...");
        
        
        stop = new JButton(new ImageIcon(d.getStop()));
        stop.addActionListener(this);
        stop.setFocusPainted(false);
        //stop.setToolTipText("Stop preview");
        
        fullBack = new JButton(new ImageIcon(d.getStart()));
        fullBack.addActionListener(this);
        fullBack.setFocusPainted(false);
        //fullBack.setToolTipText("Rewind preview to start");
        
        fullForward = new JButton(new ImageIcon(d.getEnd()));
        fullForward.addActionListener(this);
        fullForward.setFocusPainted(false);
        
        backOne = new JButton(new ImageIcon(d.getRW()));
        backOne.addActionListener(this);
        backOne.setFocusPainted(false);
        
        advanceOne = new JButton(new ImageIcon(d.getFF()));
        advanceOne.addActionListener(this);
        advanceOne.setFocusPainted(false);
        
        buttonStrip.add(fullBack);
        buttonStrip.add(backOne);
        buttonStrip.add(play);
        buttonStrip.add(advanceOne);
        buttonStrip.add(fullForward);
        
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(2,1));
        
        //slider and spinner for playback rate panel
        JPanel sliderSpinnerPanel = new JPanel();
        
        String[] rates = new String[]{"0.25x","0.5x","1x","2x","4x"};
        playbackSpeed = new JSpinner(new SpinnerListModel(rates));
         JFormattedTextField tf =
    ((JSpinner.DefaultEditor)playbackSpeed.getEditor()).getTextField();
tf.setEditable(false);
tf.setBackground(Color.white);
        playbackSpeed.setPreferredSize(new Dimension(70,10));
        playbackSpeed.setToolTipText("Preview playback rate...");
        playbackSpeed.setValue("1x");
        playbackSpeed.addChangeListener(this);
        playbackSpeed.setToolTipText("Adjust preview playback speed...");
        
        sliderSpinnerPanel.setLayout(new BorderLayout());
        sliderSpinnerPanel.add(slider, BorderLayout.CENTER);
        sliderSpinnerPanel.add(playbackSpeed, BorderLayout.EAST);
        
        controls.add(sliderSpinnerPanel);
        controls.add(buttonStrip);
             
        this.add(controls, BorderLayout.SOUTH);
        //this.add(buttonStrip, BorderLayout.SOUTH);
        //this.add(slider, BorderLayout.CENTER);
        
        
        //this.getInputMap().put(KeyStroke.getKeyStroke("F2"),new KeyAction(p, "VK_LEFT"));
        //this.getInputMap().put(KeyStroke.getKeyStroke("VK_RIGHT"),new KeyAction(p, "VK_RIGHT"));
                
        
    }
    
    public int getPosition()
    {
        return p.position;
    }
    
    public int getMaxPosition()
    {
        return this.p.maxPosition;
    }
    
    public PreviewPanel getPanel()
    {
        return this.p;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        if(e.getSource() == slider)
        {
            slider.setValue(p.position);
        }
        if(e.getSource() == playbackSpeed)
        {
            JSpinner spinner = (JSpinner)e.getSource();
            String size= (String)(spinner.getModel().getValue());
            size = size.substring(0, size.length()-1);
            double magFac = Double.parseDouble(size);
            p.setPlaySpeedFactor(magFac);
        }
        
    }
    
    @Override public void propertyChange(PropertyChangeEvent e)
    {
        firePropertyChange("position", p.position-1, p.position);
    }
    
  @Override  public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()== play)
        {
            p.playPreview();
            if(p.thread == null)
            {
                //this means the thread is paused so update the analysis pane
                System.out.println("Thread paused frame is: "+p.position);
                firePropertyChange("position", p.position-1, p.position);
            }
        }
        if(e.getSource()== stop)
        {
            p.stopPreview();
            System.out.println("Thread stopped frame is: "+p.position);
            firePropertyChange("position", p.position-1, p.position);
        }
        if(e.getSource()== fullBack)
        {
            p.fullBack();
            System.out.println("Thread fullBack frame is: "+p.position);
            firePropertyChange("position", p.position-1, p.position);
        }
        if(e.getSource() == fullForward)
        {
            p.fullForward();
            System.out.println("Thread fullForward frame is: "+p.position);
            firePropertyChange("position", p.position-1, p.maxPosition);
        }
        if(e.getSource()== advanceOne)
        {
            p.advanceOne();
            System.out.println("Thread advanceOne frame is: "+p.position);
            fireNotifyAdvance();
        }
        if(e.getSource()== backOne)
        {
            p.backOne();
            System.out.println("Thread backOne frame is: "+p.position);
            fireNotifyBack();
        }
        
    }
  
  public void fireNotifyAdvance()
  {
      firePropertyChange("position", p.position-1, p.position);
  }
  
  public void fireNotifyBack()
  {
      firePropertyChange("position", p.position+1, p.position);
  }
  
  public boolean isFocusable()
  {
      return true;
  }
  
  public void mouseExited(MouseEvent e)
  {
      
  }
  public void mouseEntered(MouseEvent e)
  {
      
  }
  
  public void mousePressed(MouseEvent e)
  {
      
  }
  
  public void mouseReleased(MouseEvent e)
  {
      //use this on the slider to fire a change event
      if(p.thread == null)
      {
          //the preview window is not running so ok to update the main window...
          System.out.println("Thread mouseReleased frame is: "+p.position);
          firePropertyChange("position", -1, p.position);//if the case occurs where the slider is dragged to 1, then the equals() method shows the 2 results as being the same and wont fire the event... very annoying but this fix of -1 (an impossible value) means it works
          
      }
  }
  public void mouseClicked(MouseEvent e)
  {
      
  }
}



