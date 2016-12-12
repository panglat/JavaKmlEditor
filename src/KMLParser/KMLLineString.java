/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class KMLLineString {
    static List<Coordinate> inverseCoordinateList(List<Coordinate> coordinateList) {
        if(coordinateList == null) return null;

        List<Coordinate> invList = new ArrayList<Coordinate>();
        for(int i = coordinateList.size()-1; i >= 0; i--) {
            invList.add(coordinateList.get(i));
        }
        return invList;
    }
}
