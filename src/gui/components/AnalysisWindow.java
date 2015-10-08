/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

//import com.sun.xml.internal.ws.util.StringUtils;
import javax.swing.*;
import java.awt.event.*;
import main.PhysMo;

import java.util.*;

import gui.components.*;
import gui.images.Distributor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.*;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.WorkbookUtil;
import tools.CannyEdgeDetector;

/**
 * 
 *
 * @author jasonkb
 */
public class AnalysisWindow extends JFrame implements PropertyChangeListener, ChangeListener, ItemListener, KeyListener, ActionListener
{
    
    Distributor d = new Distributor();
    
    AnalysisWindowMenuBar menu;
    
    
    DataTabbedPane tabPane;
    JPanel westPanel;
    PhysMoPreview p;
    JPanel centerPanel;
    SplitButton sb;
    JButton grabData;
    JButton exportData;
    DataTable table;
    JPanel tablePanel;
    
    
    
    JToolBar analysisBar;
    JComboBox analysisMode;
    AnalysisPanel analysisPanel;
    JSpinner magnification;
    JCheckBox filters;
    JButton newTrace;
    JComboBox currentTrace;
    JButton saveImage;
    
    JScrollPane sp;
    JScrollPane tableSp;
    
    JToolBar bottomBar;
    JLabel information;
    
    int x = 0;
    int y = 0;
        
    JCheckBox anglesVisibleCheck;
    JCheckBox plotsVisibleCheck;
    JCheckBox showEdges;
    
    // txt-file writing
    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    Date currdate;
    File exportedData;
    FileWriter fw;
    String exportedDataName;
    
    
    public AnalysisWindow()
    {
        
        super("PhysMo v3.0 - FRA-UAS - "+PhysMo.videoName);

        
        
        information = new JLabel("X = "+x+ "    Y = "+y);
        analysisPanel = new AnalysisPanel(information);
        //analysisPanel.add(new KeyListener(this));
        analysisPanel.addPropertyChangeListener(this);
        
        menu = new AnalysisWindowMenuBar();
        this.setJMenuBar(menu.getJMenuBar());
        menu.getJMenuBar().addPropertyChangeListener(this);
        
        
        this.setIconImage(d.getFrameIcon());
        this.setSize(new Dimension(1280,1024));
        this.setVisible(true);
        
        westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        p = new PhysMoPreview();
        p.addPropertyChangeListener(this);
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new CustomKeyEventDisptatcher(p.p, p));
        
        analysisPanel.maxFrame = p.getMaxPosition();
        analysisPanel.initPlottingPoints(p.getMaxPosition());
        tabPane = new DataTabbedPane(p.getMaxPosition());
        tabPane.getJTabbedPane().addChangeListener(this);
        //analysisPanel.tp = tabPane;
        
        westPanel.add(p, BorderLayout.NORTH);
        
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        
        grabData = new JButton("Grab data...");
        grabData.addActionListener(this);
        exportData = new JButton("Export data as .txt file", new ImageIcon(d.getFloppyDisk()));
        exportData.addActionListener(this);
        
        
        //tablePanel.add(grabData, BorderLayout.NORTH);
        tablePanel.add(tabPane.getJTabbedPane(), BorderLayout.CENTER);
        tablePanel.add(exportData, BorderLayout.SOUTH);
        westPanel.add(tablePanel, BorderLayout.CENTER);
             
        
        
        
        
        
        
        this.getContentPane().add(westPanel, BorderLayout.WEST);
        System.out.println("Sizes of preview "+p.getWidth()+" ,"+p.getHeight());
        
        
        //create the magnified window of physmo
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        analysisBar = new JToolBar();
        analysisBar.setOrientation(JToolBar.HORIZONTAL);
        analysisBar.setFloatable(false);
        analysisBar.setRollover(true);
        analysisBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        
        Map<Object, Icon> icons = new HashMap<Object, Icon>();
		icons.put("Calibrate ",new ImageIcon(d.getRuler()));
		icons.put("Find angle ",new ImageIcon(d.getProtractor()));
		icons.put("Plot ",new ImageIcon(d.getTarget()));
                icons.put("Set origin ", new ImageIcon(d.getMarker()));
        
        analysisMode = new JComboBox(new Object[]{"Calibrate ", "Find angle ", "Plot ","Set origin "});
        analysisMode.setRenderer(new IconListRenderer(icons));
        analysisMode.setFocusable(false);
        analysisMode.addItemListener(this);
        
        
        //analysisMode.setToolTipText("Mode of analysis... Angle or point plotting");
        JLabel mode = new JLabel("Analysis Mode:   ");
        
        
        analysisBar.add(mode);
        analysisBar.add(analysisMode);
        
        
        String[] mags = new String[]{"1x","2x","3x","4x","5x","6x","7x","8x","9x","10x"};
        magnification = new JSpinner(new SpinnerListModel(mags));
        magnification.setPreferredSize(new Dimension(70,50));
        
        JFormattedTextField tf =
    ((JSpinner.DefaultEditor)magnification.getEditor()).getTextField();
tf.setEditable(false);
tf.setBackground(Color.white);

        
        magnification.addChangeListener(this);
        
        //analysisBar.add(new JLabel("    "));
        analysisBar.addSeparator();
        //analysisBar.add(new JLabel("    "));
        
        
        analysisBar.add(new JLabel("  Magnification: "));
        analysisBar.add(magnification);
        
        analysisBar.addSeparator();
        
        newTrace = new JButton("New Trace");
        newTrace.setFocusable(false);
        newTrace.addActionListener(this);
        
        String[] traceString = new String[]{"Default Trace"};
        currentTrace = new JComboBox(traceString);
        currentTrace.setFocusable(false);
        currentTrace.addItemListener(this);
        
        analysisBar.add(newTrace);
        analysisBar.add(new JLabel("Current Trace: "));
        analysisBar.add(currentTrace);
        this.newTrace.setEnabled(false);
        this.currentTrace.setEnabled(false);
        
        
        centerPanel.add(analysisBar, BorderLayout.NORTH);
        
        
        sp = new JScrollPane(analysisPanel);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        centerPanel.add(sp, BorderLayout.CENTER);
        
        
        
        bottomBar = new JToolBar();
        bottomBar.setOrientation(JToolBar.HORIZONTAL);
        bottomBar.setFloatable(false);
        bottomBar.setRollover(true);
        information.setText("X = "+x+ "    Y = "+y);
        
        plotsVisibleCheck = new JCheckBox("Plots visible");
        anglesVisibleCheck = new JCheckBox("Angles visible");
        filters = new JCheckBox("Show edges");
        
        
        
        plotsVisibleCheck.setSelected(true);
        anglesVisibleCheck.setSelected(true);
        filters.setSelected(false);
        
        plotsVisibleCheck.setFocusable(false);
        anglesVisibleCheck.setFocusable(false);
        filters.setFocusable(false);
        
        plotsVisibleCheck.addItemListener(this);
        plotsVisibleCheck.setToolTipText("Whether plot-points from traces are visible...");
        anglesVisibleCheck.addItemListener(this);
        anglesVisibleCheck.setToolTipText("Whether angles are visible...");
        filters.addItemListener(this);
        filters.setToolTipText("Filter above image to enhance the edges...");
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout());
        
        
        saveImage = new JButton("Take Snapshot", new ImageIcon(d.getSnapshot()));
        saveImage.addActionListener(this);
        saveImage.setToolTipText("Save above frame with markings to file...");
        
        checkBoxPanel.add(plotsVisibleCheck);
        checkBoxPanel.add(anglesVisibleCheck);
        checkBoxPanel.add(filters);
        checkBoxPanel.add(saveImage);
        //bottomBar.addSeparator();
        
        bottomPanel.add(information, BorderLayout.SOUTH);
        bottomPanel.add(checkBoxPanel, BorderLayout.NORTH);
        bottomBar.add(bottomPanel);
        
        
        centerPanel.add(bottomBar, BorderLayout.SOUTH);
        
        
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
                
        
        
    }
    
    public void stateChanged(ChangeEvent e)
    {
        if(e.getSource().getClass() == JSpinner.class)
        {
            JSpinner spinner = (JSpinner)e.getSource();
            String size= (String)(spinner.getModel().getValue());
            size = size.substring(0, size.length()-1);
            int magFac = Integer.parseInt(size);
            analysisPanel.changeScale(magFac);
            information.setText("X = "+x+ "    Y = "+y);
            information.repaint();
       
            ((JComponent)sp.getRootPane()).revalidate();
            sp.setSize(analysisPanel.getPreferredDimension());
            sp.revalidate();
        }
        if(e.getSource() == this.tabPane.getJTabbedPane())
        {
            System.out.println("tabbed pane change");
            int index = this.tabPane.getJTabbedPane().getSelectedIndex();
            if(index != this.currentTrace.getSelectedIndex())
            {
                if(index < currentTrace.getItemCount())
                {
                    this.currentTrace.setSelectedIndex(index);
                }
            }
        }
       
    }
    
    public void propertyChange(PropertyChangeEvent e)
    {
        System.out.println("Property change "+e.getPropertyName());
        if(e.getPropertyName().equals("position") && e.getSource() == p)
        {
            analysisPanel.updateView(p.getPosition());
            if(PhysMo.isVisible)
            {
                tabPane.scrollToCurrent(p.getPosition()-1, currentTrace.getSelectedIndex());
            }
        }
        
        if(e.getPropertyName().equals("timebaseUpdate"))
        {
            this.repaint();
        }
        
        if(e.getPropertyName().equals("closeWindow"))
        {
            WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

        }
        
        if(e.getPropertyName().equals("excelReport"))
        {
            this.exportData.doClick();

        }
        
        if(e.getPropertyName().equals("Table Update x"))
        {
            int dataToUpdate = Integer.parseInt(e.getNewValue().toString());
            
            System.out.println("Change event for table! index is: "+this.currentTrace.getSelectedIndex());
            
            tabPane.updateCell(this.currentTrace.getSelectedIndex(), this.p.getPosition()-1, 2, dataToUpdate);//mouseX
            
            //tabPane.getJTabbedPane().repaint();
            
        }
        
        if(e.getPropertyName().equals("Table Update y"))
        {
            int dataToUpdate = Integer.parseInt(e.getNewValue().toString());
            
            
            tabPane.updateCell(this.currentTrace.getSelectedIndex(), this.p.getPosition()-1, 3, dataToUpdate);//mouseY
            tabPane.getJTabbedPane().repaint();
            System.out.println("Change event for table!");
        }
        
        System.out.println("Change event");
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        //add filters!!!
        if(e.getSource() == this.currentTrace)
        {
            int t = this.currentTrace.getSelectedIndex();
            this.analysisPanel.swapCurrentTrace(t);
            //this.tabPane.newTrace(this.currentTrace.)
            this.tabPane.getJTabbedPane().setSelectedIndex(t);
            this.analysisPanel.repaint();
            this.tabPane.getJTabbedPane().repaint();
        }
        
        if(e.getSource() == filters)
        {
            
            if(!filters.isSelected())
            {
                analysisPanel.setFiltered(false);
                analysisPanel.updateView(this.p.getPosition());
                //this.analysisPanel.repaint();
            }
            if(filters.isSelected())
            {
                analysisPanel.setImageFilter(new CannyEdgeDetector());
                analysisPanel.setFiltered(true);
                analysisPanel.updateView(this.p.getPosition());
                //this.analysisPanel.repaint();
            }
        }
        
        if(e.getSource() == plotsVisibleCheck)
        {
            this.analysisPanel.plotsVisible = !this.analysisPanel.plotsVisible;
            this.analysisPanel.repaint();
        }
        if(e.getSource() == anglesVisibleCheck)
        {
            this.analysisPanel.anglesVisible = !this.analysisPanel.anglesVisible;
            this.analysisPanel.repaint();
        }
        
        if(e.getSource() == analysisMode)
        {
            if(((JComboBox)e.getSource()).getSelectedIndex() == 0)
            {
                //calibrate
                this.analysisPanel.setMode(AnalysisPanel.CALIBRATION_MODE);
                this.newTrace.setEnabled(false);
                this.currentTrace.setEnabled(false);
            }
            if(((JComboBox)e.getSource()).getSelectedIndex() == 1)
            {
                //calibrate
                this.analysisPanel.setMode(AnalysisPanel.PROTRACTOR_MODE);
                this.newTrace.setEnabled(false);
                this.currentTrace.setEnabled(false);
            }
            if(((JComboBox)e.getSource()).getSelectedIndex() == 2)
            {
                //calibrate
                this.analysisPanel.setMode(AnalysisPanel.PLOTTING_MODE);
                this.newTrace.setEnabled(true);
                this.currentTrace.setEnabled(true);
            }
            if(((JComboBox)e.getSource()).getSelectedIndex() == 3)
            {
                //calibrate
                this.analysisPanel.setMode(AnalysisPanel.MARKER_MODE);
                this.newTrace.setEnabled(true);
                this.currentTrace.setEnabled(true);
            }
            analysisPanel.repaint();
        }
    }
    
    public void keyReleased(KeyEvent e)
    {
        
    }
    
    public void keyPressed(KeyEvent e)
    {
        
    }
    
    public void keyTyped(KeyEvent e)
    {
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == saveImage)
        {
            try 
            {
                BufferedImage i = new BufferedImage(analysisPanel.getWidth(), analysisPanel.getHeight(),BufferedImage.TYPE_INT_RGB);
                Graphics g = i.getGraphics();
                analysisPanel.paint(g);
                
                
                JFileChooser fc = new JFileChooser(new File(PhysMo.workingDirectory));
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new PNGFileFilter());
            fc.setFileView(new PNGFileView());
            
            int retval = fc.showSaveDialog(null);
            if (retval == JFileChooser.APPROVE_OPTION) 
            {
                //... The user selected a file, get it, use it.
                File file = fc.getSelectedFile();
                String fileName = file.getAbsolutePath();
                if(fileName.endsWith(".png"))
                {
                    
                }
                else
                {
                    fileName = fileName + ".png";
                    file = new File(fileName);
                }
                ImageIO.write(i, "png", file);
            }
                
                
                
                
                
                
            }
            catch (IOException ioe) 
            {
                
            }
            
            
        }
        if(e.getSource() == newTrace)
        {
            String traceName = JOptionPane.showInputDialog(null, "New Trace Name", "Type in trace name...", JOptionPane.QUESTION_MESSAGE);
            this.analysisPanel.createNewTrace();
            this.analysisPanel.getCurrentTrace().setTraceName(traceName);
            this.analysisPanel.repaint();
            
            this.tabPane.newTrace(traceName);
            
            String[] traceNames = new String[0];
            this.currentTrace.removeAllItems();
            for(int i=0; i<= (this.analysisPanel.traces.length-1);)
            {
                String[] t = new String[traceNames.length+1];
                t[i] = this.analysisPanel.traces[i].getTraceName();
                traceNames = t;
                this.currentTrace.addItem(traceNames[i]);
                i++;
            }
            
            
            for(int i=0; i<=traceNames.length-1;)
            {
                
                i++;
            }
            this.currentTrace.setSelectedIndex(traceNames.length-1);
            
        }
        
       /* if(e.getSource() == grabData)
        {
            String[] headers = new String[]{"Frame","Time(s)","X","Y","X(m)","Y(m)","Comment"};
            Object[][] data = new Object[p.getMaxPosition()][8];
            for(int i = 0; i<=(p.getMaxPosition()-1);)
            {
                data[i][0] = i+1;
                data[i][1] = (1/(PhysMo.fps))*(i+1);
            
            
                i++;
            }
            table = new DataTable(data, headers);
        
        
            tablePanel.remove(tableSp);
            tableSp = new JScrollPane(table);
            tableSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            tableSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            tablePanel.add(tableSp, BorderLayout.CENTER);*/
        
        if(e.getSource() == exportData)
        {
            // txt-file writing
            Calendar cal = Calendar.getInstance();
            exportedDataName = dateFormat.format(cal.getTime())+".txt";
            exportedData = new File("/home/Student/Desktop",exportedDataName);
            Formatter fmt;
            try {
                fmt = new Formatter(exportedData);
            
            int sheets = tabPane.traces.length;
                
                for(int i = 0; i<=(sheets-1);)
                {
                    System.out.println("Creating a sheet");
                    
                    //fw = new FileWriter(exportedData,true);
                    
                    int rowCount = this.p.getMaxPosition();
                    double startTime = 0.;//Double.parseDouble(tabPane.traces[i].getValueAt(0, 1).toString());
                    int frameNum = 1;
                    for(int j = 0; j<=rowCount-1;)
                    {
                        System.out.println("Creating a row");
                        if(j == 0)//headings
                        {
                            fmt.format("%15s %15s %15s\n", "Position" , "Time[s]", "Distance[m]");
                            fmt.flush();
                        }
                        else
                        {
                            if(Double.parseDouble(tabPane.traces[i].getValueAt(j, 2).toString()) != 0.)
                            {
                                if(startTime == 0. && j > 1) startTime = Double.parseDouble(tabPane.traces[i].getValueAt(j, 1).toString()) - 0.033;
                                try
                                {
                                    double time;
                                    double distance;
                                    try
                                    {
                                        time = Double.parseDouble(tabPane.traces[i].getValueAt(j, 1).toString()) - startTime;
                                    }
                                    catch(NullPointerException npe)
                                    {
                                        time = 0.;
                                    }
                                    try
                                    {
                                        distance = Double.parseDouble(tabPane.traces[i].getValueAt(j, 2).toString());
                                    }
                                    catch(NullPointerException npe)
                                    {
                                        distance = 0.;
                                    }
                                    //System.out.println(time);
                                    //System.out.println(distance);
                                    fmt.format("%15d %15.3f %15.3f\n", frameNum , time, distance);
                                    fmt.flush();
                                }
                                catch(NullPointerException npe)
                                {
                                
                                }
                                frameNum++;
                            }
                        }
                        
                        
                        j++;
                    }
                    
                    i++;
                }  
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AnalysisWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /*//create a workbook and drop it in the file we are working in and make a series of videos?
            JFileChooser fc = new JFileChooser(new File(PhysMo.workingDirectory));
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new ExcelFileFilter());
            fc.setFileView(new ExcelFileView());
            
            int retval = fc.showSaveDialog(null);
            if (retval == JFileChooser.APPROVE_OPTION) 
            {
                //... The user selected a file, get it, use it.
                File file = fc.getSelectedFile();
                String fileName = file.getAbsolutePath();
                if(fileName.endsWith(".xls"))
                {
                    
                }
                else
                {
                    fileName = fileName + ".xls";
                    file = new File(fileName);
                }
            
            HSSFWorkbook wb = new HSSFWorkbook();
            try
            {
                FileOutputStream fileOut = new FileOutputStream(file);
                
                CreationHelper createHelper = wb.getCreationHelper();
                
                //create the worksheets and name them safely...
                int sheets = tabPane.traces.length;
                
                for(int i = 0; i<=(sheets-1);)
                {
                    System.out.println("Creating a sheet");
                    HSSFSheet s = wb.createSheet(WorkbookUtil.createSafeSheetName(tabPane.getJTabbedPane().getTitleAt(i)));
                    
                    //now populate the sheet!!!
                    //first rows.  we need to go row by row... so start with row 0
                    int rowCount = this.p.getMaxPosition();
                    double startTime = Double.parseDouble(tabPane.traces[i].getValueAt(0, 1).toString());
                    int frameNum = 1;
                    for(int j = 0; j<=rowCount-1;)
                    {
                        System.out.println("Creating a row");
                        HSSFRow row = s.createRow(j);
                        if(j == 0)//headings
                        {
                            row.createCell(0).setCellValue(createHelper.createRichTextString("Position"));
                            row.createCell(1).setCellValue(createHelper.createRichTextString("Time [s]"));
                            row.createCell(2).setCellValue(createHelper.createRichTextString("Distance [m]"));                           
                        }
                        else
                        {
                            if(Double.parseDouble(tabPane.traces[i].getValueAt(j, 5).toString()) != 0.)
                            {
                                try
                                {
                                    row.createCell(0).setCellValue(frameNum);
                                }
                                catch(NullPointerException npe)
                                {
                                
                                }
                            
                                try
                                {
                                   row.createCell(1).setCellValue(Double.parseDouble(tabPane.traces[i].getValueAt(j, 1).toString()) - startTime);
                                }
                                catch(NullPointerException npe)
                                {
                                   row.createCell(1).setCellValue(0);
                                }
                                
                              try 
                                {
                                    row.createCell(2).setCellValue(Double.parseDouble(tabPane.traces[i].getValueAt(j, 5).toString()));
                                }
                                catch(NullPointerException npe)
                                {
                                    row.createCell(2).setCellValue(0);
                                }  
                            }
                            frameNum++;
                        }
                        
                        
                        j++;
                    }
                    
                    i++;
                }
                
                
                
                
                wb.write(fileOut);
                fileOut.close();
                
            }
            catch(FileNotFoundException err)
            {
                
            }
            catch(IOException io)
            {
                
            }
            }*/
    
    
            
        }
        
        }
        
        
        
        
}
    
    

