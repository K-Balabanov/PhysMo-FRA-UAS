/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

/**
 * 
 *
 * @author jasonkb
 */
public class DataTableModel extends DefaultTableModel
{
    public DataTableModel()
    {
        
    }
    
    public Class getTableClass(int column)
    {
        return Boolean.class;
    }
    
}
