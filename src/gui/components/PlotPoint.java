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
public class PlotPoint 
{
    private double x;
    private double y;
    private double originalMultiplicationFactorOnInitialization;
    private boolean isSet = false;
    
    public PlotPoint(int xPoint, int yPoint, double muliplication)
    {
        x = xPoint;
        y = yPoint;
        originalMultiplicationFactorOnInitialization = muliplication;
        this.isSet = true;
    }
    
    public PlotPoint(double xPoint, double yPoint, double muliplication)
    {
        x = xPoint;
        y = yPoint;
        originalMultiplicationFactorOnInitialization = muliplication;
        this.isSet = true;
    }
    
    public void setXPoint(int xPoint)
    {
        this.x = xPoint;
    }
    public void setXPoint(double xPoint)
    {
        this.x = xPoint;
    }
    public void setYPoint(int yPoint)
    {
        this.y = yPoint;
    }
    public void setYPoint(double yPoint)
    {
        this.y = yPoint;
    }
    public void setMultiplication(double m)
    {
        this.originalMultiplicationFactorOnInitialization = m;
    }
    public void setData(int xPoint, int yPoint, double muliplication)
    {
        x = xPoint;
        y = yPoint;
        originalMultiplicationFactorOnInitialization = muliplication;
        this.isSet = true;
    }
    public void setData(double xPoint, double yPoint, double muliplication)
    {
        x = xPoint;
        y = yPoint;
        originalMultiplicationFactorOnInitialization = muliplication;
        this.isSet = true;
    }
    
    public boolean isSet()
    {
        return this.isSet;
    }
    
    public void setSetStatus(boolean status)
    {
        this.isSet = status;
    }
    
    public void reset()
    {
        this.isSet = false;
    }
    
    public double getXPoint()
    {
        return this.x;
    }
    public int getScaledXPoint(double currentMultiplication)
    {
        double scaledX = this.x/this.originalMultiplicationFactorOnInitialization;
        scaledX = scaledX*currentMultiplication;
        return (int)scaledX;
    }
    public double getYPoint()
    {
        return this.y;
    }
    public int getScaledYPoint(double currentMultiplication)
    {
        double scaledY = this.y/this.originalMultiplicationFactorOnInitialization;
        scaledY = scaledY*currentMultiplication;
        return (int)scaledY;
    }
    public double getOriginalMultiplication()
    {
        return this.originalMultiplicationFactorOnInitialization;
    }
}
