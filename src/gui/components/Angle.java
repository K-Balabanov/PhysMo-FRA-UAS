/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import java.math.*;
/**
 * 
 *
 * @author jasonkb
 */
public class Angle 
{
    private double x1=1;
    private double x2=1;
    private double y1=1;
    private double y2=1;
    private double originalMagnification= 1;
    private boolean hasValue = false;
    
    private double[] point1 = new double[]{0.0,0.0};
    private double[] point2 = new double[]{0.0,0.0};;
    
    public Angle()
    {
        this.hasValue = false;
    }
    
    public Angle(double[] focusPoint, double[] rayPoint, double mag)
    {
        this.x1 = focusPoint[0];
        this.y1 = focusPoint[1];
        
        this.x2 = rayPoint[0];
        this.y2 = rayPoint[1];
        
        this.originalMagnification = mag;
        
        point1 = new double[]{x1,y1};
        point2 = new double[]{x2,y2};
        this.hasValue = true;
        
        
        
    }
    
    /**
     * determine which quadrant the angle is in and assist in angle calcs
     * @return quadrant
     */
    public int getQuadrant()
    {
        if(this.point2[0]>= this.point1[0])//either quadrant 1 or 4...
        {
            if(this.point2[1] >= point1[1])//quadrant 1
            {
                return 1;
            }
            else
            {
                return 4;
            }
        }
        
        if(this.point2[0] < point1[0])//either quadrant 2 or 3...
        {
            if(this.point2[1] >= point1[1])//quadrant 2
            {
                return 2;
            }
            else
            {
                return 3;
            }
        }
        
        //default is 1
        return 1;
    }
    
    public double[] getFocusPoint()
    {
        return this.point1;
    }
    
    public void setFocusPoint(double[] point)
    {
        this.point1[0] = point[0];
        this.point1[1] = point[1];
    }
    
    public double getFocusXPoint()
    {
        System.out.println("getting Focus X point "+point1[0]);
        return point1[0];
    }
    
    public double getFocusYPoint()
    {
        System.out.println("getting Focus y point "+point1[1]);
        return point1[1];
    }
    
    public double getRayXPoint()
    {
        System.out.println("getting ray X point "+point2[0]);
        return point2[0];
    }
    
    public double getRayYPoint()
    {
        System.out.println("getting ray X point "+point2[1]);
        return point2[1];
    }
    
    public double[] getRayPoint()
    {
        return this.point2;
    }
    
    public void setRayPoint(double[] point)
    {
        this.point2[0] = point[0];
        this.point2[1] = point[1];
    }
    
    public void setMagnification(double mag)
    {
        this.originalMagnification = mag;
    }
    
    public double getOriginalMagnification()
    {
        return this.originalMagnification;
    }
    
    public void setSet()
    {
        this.hasValue = true;
    }
    public void reset()
    {
        this.hasValue = false;
    }
    
    public boolean isSet()
    {
        return this.hasValue;
    }
    
    /**
     * Returns the degrees from true north of this angle
     * 
     * @return 0.00 if #isSet() is false and the value from true north if available
     */
    public double getValueDegreesT()
    {
        if(isSet())
        {
            
            double value = 0.00;
            
            value = (Math.atan((Math.abs(point2[1]-point1[1]))/(Math.abs(point2[0]-point1[0]))))*(180/(Math.PI));
            System.out.println("deg t value "+value+ " quadrant "+this.getQuadrant());
            
            if(this.getQuadrant()== 1)
            {
                value = 90-value;
            }
            if(this.getQuadrant()== 2)
            {
                value = 270+value;
            }
            if(this.getQuadrant()== 3)
            {
                value = 270-value;
            }
            if(this.getQuadrant()== 4)
            {
                value = 90+value;
            }
            
            System.out.println("deg t north value "+value);
            
            return value; 
        }
        else
        {
            return 0.00;
        }       
    }
    
    /**
     * Get value in radians...
     * @return 
     */
    public double getValueRadians()
    {
        if(isSet())
        {
            
            return (this.getValueDegreesT()*Math.PI)/180; 
        }
        else
        {
            return 0.00;
        }
    }
    
}
