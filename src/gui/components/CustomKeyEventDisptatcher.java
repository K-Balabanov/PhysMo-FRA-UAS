/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * 
 *
 * @author jasonkb
 */
public class CustomKeyEventDisptatcher implements KeyEventDispatcher
{
    private PreviewPanel p;
    private PhysMoPreview play;
    
    public CustomKeyEventDisptatcher(PreviewPanel panel, PhysMoPreview player)
    {
        p = panel;
        play = player;
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) 
    {
        if (e.getID() == KeyEvent.KEY_PRESSED) 
        {
            System.out.println("Performing command");
            if(e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                play.backOne.doClick();
                //p.backOne();
                //play.fireNotifyBack();
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                play.advanceOne.doClick();
                //p.advanceOne();
                //play.fireNotifyAdvance();
                
            }
        }
        else if (e.getID() == KeyEvent.KEY_RELEASED) 
        {
            //System.out.println("2test2");
        }
        else if (e.getID() == KeyEvent.KEY_TYPED) 
        {
            
            
        }
        return false;
    }
}