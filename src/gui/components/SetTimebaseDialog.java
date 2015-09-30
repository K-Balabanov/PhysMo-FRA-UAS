/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;




import javax.swing.JDialog; 
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import main.PhysMo;
/**
 * 
 *
 * @author jasonkb
 */
public class SetTimebaseDialog extends JDialog implements ActionListener
{
    private JPanel myPanel = null;
    private JButton yesButton = null;
    
    private double fps;
    DoubleTextField t;
    
    SetTimebaseDialog(JFrame frame, boolean modal, String myMessage)
    {
        super(frame, modal);
        
        this.fps = 0.00;
        myPanel = new JPanel();
        
        myPanel.add(new JLabel("FPS: "));
        
        t = new DoubleTextField(PhysMo.fps);
        
        myPanel.add(t);
        yesButton = new JButton("Set new timebase");
        yesButton.addActionListener(this);
        myPanel.add(yesButton); 
        
        
        
        getContentPane().add(myPanel);
        
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }
    
        public void actionPerformed(ActionEvent e) {
        if(yesButton == e.getSource()) {
            
        fps = Double.parseDouble(t.getText());
        PhysMo.fps = fps;
            
            
            setVisible(false);
        }
        
        }
    }



