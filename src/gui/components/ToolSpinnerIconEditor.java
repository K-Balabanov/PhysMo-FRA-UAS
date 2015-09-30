/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;


import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.images.*;


/**
 * 
 *
 * @author jasonkb
 */
public class ToolSpinnerIconEditor extends JLabel implements ChangeListener
{
    private JSpinner spinner;
    private Icon icon;
    
    public ToolSpinnerIconEditor(JSpinner s)
    {
        super((Icon) s.getValue(), CENTER);
        icon = (Icon) s.getValue();
        spinner = s;
        spinner.addChangeListener(this);
    }
    
    
    
    public void stateChanged(ChangeEvent ce) {
    icon = (Icon) spinner.getValue();
    setIcon(icon);
  }
    
    public JSpinner getSpinner() {
    return spinner;
  }
    
    
   
  public Icon getIcon() {
    return icon;
  }
}



  

  

           
         