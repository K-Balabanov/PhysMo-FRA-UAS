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
public class ToolSpinner extends JSpinner
{
    private Icon[] i;
    private SpinnerListModel lm;
    
    public ToolSpinner(Icon[] icons)
    {
        super(new SpinnerListModel(icons));
        this.i = icons;
        lm = new SpinnerListModel(i);
        this.setEditor(new ToolSpinnerIconEditor(this));
        
    }
}


