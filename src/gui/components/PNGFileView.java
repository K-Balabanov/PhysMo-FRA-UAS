/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;


import gui.images.Distributor;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * 
 *
 * @author jasonkb
 */
public class PNGFileView extends FileView
{
    Distributor d = new Distributor();
    
    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = getExtension(f);
        String type = null;

        if (extension != null) 
        {
            if(extension.endsWith("png"))
            {
                type = "PNG file";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = getExtension(f);
        Icon icon = null;

        if (extension != null)
        {
            if(extension.endsWith("png"))
            {
                return new ImageIcon(d.getPhotoFileIcon());
            }
        }
        return icon;
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
