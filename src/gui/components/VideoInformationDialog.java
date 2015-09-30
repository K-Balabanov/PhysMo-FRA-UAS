/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;



import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog; 
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import main.PhysMo;

/**
 * 
 *
 * @author jasonkb
 */
public class VideoInformationDialog extends JDialog implements ActionListener
{
    private JPanel myPanel = null;
    private JButton yesButton = null;
    JTextArea t;
    
    public VideoInformationDialog(JFrame frame, boolean modal)
    {
        super(frame,"Video Details", modal);
        
        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
        
        myPanel.add(new JLabel("FFMPEG Output: "), BorderLayout.NORTH);
        
        t = new JTextArea();
        t.setWrapStyleWord(true);
        
        String[] items = new String[2];
            items = (PhysMo.videoInformation.split(PhysMo.videoPath));
        String s = items[1];
        items = s.split("At least one output file must be specified");
        
        
        t.setText(items[0]);
        t.setLineWrap(true);
        
        
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new Dimension(400,300));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        myPanel.add(sp, BorderLayout.CENTER);
        yesButton = new JButton("OK");
        yesButton.addActionListener(this);
        myPanel.add(yesButton, BorderLayout.SOUTH); 
        
        
        
        getContentPane().add(myPanel);
        
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }
    
    
    public void actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }
}
