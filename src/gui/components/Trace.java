/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

/**
 * 
 *
 * @author jasonkb
 */
public class Trace 
{
    private String traceName = "Default Trace";
    private int traceIndex;
    private PlotPoint[] plotPoints;
    
    public Trace(int numberOfFrames)
    {
        plotPoints = new PlotPoint[numberOfFrames];
    }
    
    public void setPlotPoint(int plotPointFrameIndex, PlotPoint p)
    {
        this.plotPoints[plotPointFrameIndex] = p;
    }
    
    public void setPlotPoint(int plotPointFrameIndex, int x, int y, double multiplication)
    {
        this.plotPoints[plotPointFrameIndex] = new PlotPoint(x,y,multiplication);
    }
    
    public PlotPoint getPlotPoint(int plotPointFrameIndex)
    {
        return this.plotPoints[plotPointFrameIndex];
    }
    
    public void removePlotPoint(int plotPointFrameIndex)
    {
        this.plotPoints[plotPointFrameIndex].reset();
    }
    public int getLength()
    {
        return this.plotPoints.length;
    }
    
    public void setTraceName(String name)
    {
        this.traceName = name;
    }
    public String getTraceName()
    {
        return this.traceName;
    }
}
