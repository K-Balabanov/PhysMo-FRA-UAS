/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;


import java.awt.Dimension;
import java.awt.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * 
 *
 * @author jasonkb
 */
public class PhysMoResizableIcon implements ResizableIcon
{
    protected ImageIcon delegate;

        public PhysMoResizableIcon(ImageIcon delegate) {
                this.delegate = delegate;
        }

        @Override
        public int getIconHeight() {
                return delegate.getIconHeight();
        }

        @Override
        public int getIconWidth() {
                return delegate.getIconHeight();
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
                delegate.paintIcon(c, g, x, y);
        }

        /*
         * (non-Javadoc)
         * 
         * @see 
org.jvnet.flamingo.common.ResizableIcon#revertToOriginalDimension()
         */
        public void revertToOriginalDimension() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see 
org.jvnet.flamingo.common.ResizableIcon#setDimension(java.awt.Dimension)
         */
        public void setDimension(Dimension dim) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.jvnet.flamingo.common.ResizableIcon#setHeight(int)
         */
        public void setHeight(int height) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.jvnet.flamingo.common.ResizableIcon#setWidth(int)
         */
        public void setWidth(int width) {
        }
}
