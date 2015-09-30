/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.image.BufferedImage;

/**
 * 
 *
 * @author jasonkb
 */
public interface AnalysisFilter 
{
    
    public BufferedImage getSourceImage();
    public void setSourceImage(BufferedImage i);
    
}
