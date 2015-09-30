/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;


import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;


/**
 * 
 *
 * @author jasonkb
 */
public class PNGFileFilter extends FileFilter
{
    public String getDescription()
    {
        return "PNG file";
    }
    
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) 
        {
            if(extension.endsWith("png"))
            {
                return true;
            } 
            else 
            {
                return false;
            }
        }

        return false;
    }
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
