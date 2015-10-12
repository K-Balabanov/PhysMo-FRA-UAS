/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import gui.components.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author kris
 */
public class aboutDialog extends JDialog {
  public aboutDialog(JFrame parent) {
    super(parent, "About PhysMo v3.0 - FRA-UAS", true);
    //this.getContentPane().setBackground(Color.WHITE);
    URL urlImage = getClass().getResource("fh_frankfurt_logo.jpg");
    Image img = Toolkit.getDefaultToolkit().getImage(urlImage);
    JLabel label = new JLabel(new ImageIcon(img));
    JPanel p1 = new JPanel();
    p1.add(label);
    getContentPane().add(p1, "North");
    Box b = Box.createVerticalBox();
    b.add(Box.createGlue());
    b.add(new JLabel("  Developed by Kristiyan Balabanov, an IT-Engineering student at the"));
    b.add(new JLabel("  Frankfurt University of Applied Sciences. This would not have been"));
    b.add(new JLabel("  possible without the kind supervision of Prof. Dr. Faouzi Attallah,"));
    b.add(new JLabel("  Department of Computer Science and Engineering at the FRA-UAS,"));
    b.add(new JLabel("  and the work of Dr. Jason Barraclough, developer of the original"));
    b.add(new JLabel("  PhysMo software."));
    b.add(new JLabel("  Neither I, nor the FRA-UAS claim any rights to this product. I have"));
    b.add(new JLabel("  used exquisitely open source resources in order to create it, and "));
    b.add(new JLabel("  strongly support its further distribution as such."));
    b.add(new JLabel(" "));
    b.add(new JLabel("  Long live free software! :)       (15.10.2015,   Frankfurt am Main)"));
    b.add(Box.createGlue());
    getContentPane().add(b, "Center");
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screen.width- 500)/2 + 100;
    int y = (screen.height - 350)/2 - 100;
    JPanel p2 = new JPanel();
    JButton ok = new JButton("Ok");
    p2.add(ok);
    getContentPane().add(p2, "South");

    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        setVisible(false);
      }
    });
    this.setBounds(x, y, 500, 350);
  } 
}
