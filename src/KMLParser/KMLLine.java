/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 *
 * @author admin
 */
public class KMLLine {
    protected double slope = 0;
    protected double yIntercept = 0;
    KMLLine(double slope, double yIntercept) { this.slope = slope; this.yIntercept = yIntercept;}
    public double getSlope() { return slope;}
    public void setSlope(double slope) {this.slope = slope; }
    public double getYintercept() { return this.yIntercept; }
    public void setYintercept(double yIntercept) { this.yIntercept = yIntercept; }

    public void getLine(Coordinate c1, Coordinate c2) {
        // y = m.x+b
        //
        //            (y1 - y2)
        // slope (m)= ---------       y-intercept (b) = y - m.x
        //            (x1 - x2)
        this.slope = (c1.getLatitude() - c2.getLatitude()) / (c1.getLongitude() - c2.getLongitude());
        this.yIntercept = c1.getLatitude() - this.slope * c1.getLongitude();
    }


}
