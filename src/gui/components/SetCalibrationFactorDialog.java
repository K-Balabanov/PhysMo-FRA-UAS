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
public class SetCalibrationFactorDialog extends JDialog implements ActionListener
{
    private JPanel myPanel = null;
    private JButton yesButton = null;
    
    private double calibrationFactor;
    DoubleTextField t;
    
    SetCalibrationFactorDialog(JFrame frame, boolean modal, String myMessage)
    {
        super(frame, modal);
        
        this.calibrationFactor = 0.00;
        myPanel = new JPanel();
        
        myPanel.add(new JLabel("Distance (m): "));
        
        //t = new DoubleTextField(PhysMo.fps);
        t = new DoubleTextField(1);
        
        myPanel.add(t);
        yesButton = new JButton("Set calibration");
        yesButton.addActionListener(this);
        myPanel.add(yesButton); 
        
        
        
        getContentPane().add(myPanel);
        
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }
    
    public double getCalibration()
    {
        return this.calibrationFactor;
    }
    
        public void actionPerformed(ActionEvent e) {
        if(yesButton == e.getSource()) {
            
        this.calibrationFactor = Double.parseDouble(t.getText());
        System.out.println("The cal factor post dialog is: "+this.calibrationFactor);
        
            
            
            setVisible(false);
        }
        
        }
    }



