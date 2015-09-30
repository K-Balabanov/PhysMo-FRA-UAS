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
public class KeyAction extends AbstractAction
{
    PreviewPanel player;
    String keyAction;
    
    public KeyAction(PreviewPanel p, String action)
    {
        player = p;
        keyAction = action;
        
    }
    
    
    
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Key Action performed");
        if(keyAction.equals("VK_LEFT"))
        {
            player.backOne();
        }
        
        if(keyAction.equals("VK_RIGHT"))
        {
            player.advanceOne();
        }
    }
}
