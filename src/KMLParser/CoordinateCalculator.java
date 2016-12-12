/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


// Movable Type Scripts
// Calculate distance, bearing and more between Latitude/Longitude points
// http://www.movable-type.co.uk/scripts/latlong.html


package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import java.util.ArrayList;
import java.util.List;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 *
 * @author admin
 */
// y = latitude, x = longitude
public class CoordinateCalculator {
    public static final double eartRadius = 6378.137; // eartRadius = 6371 km

    public static double getDistance(Coordinate c1, Coordinate c2) {
        double dLat = c1.getLatitude() - c2.getLatitude();
        double dLon = c1.getLongitude() - c2.getLongitude();
        double distance = dLat * dLat + dLon * dLon;
        distance = Math.sqrt(distance);
        return distance;
    }
/*
    public static Coordinate getCoordinateInterception(Coordinate c1, Coordinate c2, Coordinate p) {
        double dC = getDistance(c1, c2);
        double dPC1_lon = p.getLongitude() - c1.getLongitude();
        double dPC1_lat = p.getLatitude() - c1.getLatitude();
        double dC_lon = c2.getLongitude() - c1.getLongitude();
        double dC_lat = c2.getLatitude() - c1.getLatitude();

        double u = dPC1_lon * dC_lon + dPC1_lat * dC_lat;
        u /= dC * dC;

        if(u >= 0 && u <= 1) {
            // Intersection point is in the line
            double retLon = c1.getLongitude() + u * dC_lon;
            double retLat = c1.getLatitude() + u * dC_lat;
            return new Coordinate(retLon, retLat);
        } else {
            return null;
        }
    }
*/
    public static double getDistanceKmVincenty(Coordinate c1, Coordinate c2) {
        // Prefered Method Vincenty formula

        // instantiate the calculator
        GeodeticCalculator geoCalc = new GeodeticCalculator();

        // select a reference elllipsoid
        Ellipsoid reference = Ellipsoid.WGS84;

        // set c1 coordinates
        GlobalCoordinates gc_c1 = new GlobalCoordinates(c1.getLatitude(), c1.getLongitude());

        // set c2 coordinates
        GlobalCoordinates gc_c2 = new GlobalCoordinates(c2.getLatitude(), c2.getLongitude());

        // calculate the geodetic curve
        GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(reference, gc_c1, gc_c2);

        double ellipseKilometers = geoCurve.getEllipsoidalDistance() / 1000.0;

        return ellipseKilometers;
    }

    public static double getDistanceKm(Coordinate c1, Coordinate c2) {
       // return getDistanceKmVincenty(c1, c2);
       return getDistanceKmHaversine(c1, c2);
    }


    public static double getDistanceKmHaversine(Coordinate c1, Coordinate c2) {
        double lat1 = Math.toRadians(c1.getLatitude());
        double lon1 = Math.toRadians(c1.getLongitude());
        double lat2 = Math.toRadians(c2.getLatitude());
        double lon2 = Math.toRadians(c2.getLongitude());
        
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
/*
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1) * Math.cos(lat2) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = eartRadius * c;

        d = Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2) * Math.cos(lon2-lon1)) * eartRadius;
        return d;*/

        return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2) * Math.cos(lon2-lon1)) * CoordinateCalculator.eartRadius;
    }

    public static double getBearing(Coordinate c1, Coordinate c2) {
        double lat1 = Math.toRadians(c1.getLatitude());
        double lon1 = Math.toRadians(c1.getLongitude());
        double lat2 = Math.toRadians(c2.getLatitude());
        double lon2 = Math.toRadians(c2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) -
            Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
        double bearing = Math.atan2(y, x);
        bearing = (Math.toDegrees(bearing) + 360) % 360;
        return bearing;
    }

    public static Coordinate getCoordinateInterception(Coordinate c1, Coordinate c2, Coordinate p) {
        double b = getBearing(c1, c2);

        double b1 = ((b - 90) + 360) % 360;
        double b2 = ((b + 90) + 360) % 360;

        Coordinate resultC = intersection(c1, b, p, b1);
        if(resultC == null) {
            resultC = intersection(c1, b, p, b2);
        }
        // check if the pointer p is ortonormal tp c1c2
        if(resultC != null) {
            double lat = resultC.getLatitude();
            double lon = resultC.getLongitude();
            
            if(lat > 90 || lat < -90) return null;
            if(lon > 180 || lon < -180) return null;
        }

        return resultC;
    }

    public static Coordinate intersection(Coordinate c1, double brng1, Coordinate c2, double brng2) {
        double lat1 = Math.toRadians(c1.getLatitude());
        double lon1 = Math.toRadians(c1.getLongitude());
        double lat2 = Math.toRadians(c2.getLatitude());
        double lon2 = Math.toRadians(c2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double brng13 = Math.toRadians(brng1);
        double brng23 = Math.toRadians(brng2);

        double dist12 = 2*Math.asin( Math.sqrt( Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.cos(lat1)*Math.cos(lat2)*Math.sin(dLon/2)*Math.sin(dLon/2) ) );
        if (dist12 == 0) return null;

        // initial/final bearings between points
        double brngA = Math.acos( ( Math.sin(lat2) - Math.sin(lat1)*Math.cos(dist12) ) /
                ( Math.sin(dist12)*Math.cos(lat1) ) );
        if (brngA == Double.NaN) brngA = 0;  // protect against rounding
        double brngB = Math.acos( ( Math.sin(lat1) - Math.sin(lat2)*Math.cos(dist12) ) /
                ( Math.sin(dist12)*Math.cos(lat2) ) );

        double brng12, brng21;
        if (Math.sin(lon2-lon1) > 0) {
            brng12 = brngA;
            brng21 = 2*Math.PI - brngB;
        } else {
            brng12 = 2*Math.PI - brngA;
            brng21 = brngB;
        }
        double alpha1 = (brng13 - brng12 + Math.PI) % (2*Math.PI) - Math.PI;  // angle 2-1-3
        double alpha2 = (brng21 - brng23 + Math.PI) % (2*Math.PI) - Math.PI;  // angle 1-2-3

        if (Math.sin(alpha1)==0 && Math.sin(alpha2)==0) return null;  // infinite intersections
        if (Math.sin(alpha1)*Math.sin(alpha2) < 0) return null;       // ambiguous intersection


        //alpha1 = Math.abs(alpha1);
        //alpha2 = Math.abs(alpha2);
        // ... Ed Williams takes abs of alpha1/alpha2, but seems to break calculation?

        double alpha3 = Math.acos( -Math.cos(alpha1)*Math.cos(alpha2) +
                Math.sin(alpha1)*Math.sin(alpha2)*Math.cos(dist12) );
        double dist13 = Math.atan2( Math.sin(dist12)*Math.sin(alpha1)*Math.sin(alpha2),
                Math.cos(alpha2)+Math.cos(alpha1)*Math.cos(alpha3) );
        double lat3 = Math.asin( Math.sin(lat1)*Math.cos(dist13) +
                Math.cos(lat1)*Math.sin(dist13)*Math.cos(brng13) );
        double dLon13 = Math.atan2( Math.sin(brng13)*Math.sin(dist13)*Math.cos(lat1),
                Math.cos(dist13)-Math.sin(lat1)*Math.sin(lat3) );

        double lon3 = lon1+dLon13;
        lon3 = (lon3+Math.PI) % (2*Math.PI) - Math.PI;  // normalise to -180..180º

        lat3 = Math.toDegrees(lat3);
        lon3 = Math.toDegrees(lon3);

        return new Coordinate(lon3, lat3);
    }

    public static Coordinate destinationPointVincenty (Coordinate c1, double brng, double dist) {
        // Prefered Method Vincenty formula

        // instantiate the calculator
        GeodeticCalculator geoCalc = new GeodeticCalculator();

        // select a reference elllipsoid
        Ellipsoid reference = Ellipsoid.WGS84;

        GeodeticCurve geoCurve = new GeodeticCurve(dist, brng, 0);

        // set c1 coordinates
        GlobalCoordinates gc_c1 = new GlobalCoordinates(c1.getLatitude(), c1.getLongitude());

        GlobalCoordinates gc_p = geoCalc.calculateEndingGlobalCoordinates(reference, gc_c1, brng, dist);

        Coordinate p = new Coordinate(gc_p.getLongitude(), gc_p.getLatitude());

        return p;
    }

    public static Coordinate destinationPoint (Coordinate c1, double brng, double dist) {
        // Prefered Method Vincenty formula
        //return destinationPointVincenty(c1, brng, dist);
        return destinationPointHaversine(c1, brng, dist);
    }

    public static Coordinate destinationPointHaversine (Coordinate c1, double brng, double dist) {
        dist = dist / CoordinateCalculator.eartRadius;  // convert dist to angular distance in radians
        brng = Math.toRadians(brng);  //
        double lat1 = Math.toRadians(c1.getLatitude());
        double lon1 = Math.toRadians(c1.getLongitude());
        
        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) +
                Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
        double lon2 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1),
                Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
        lon2 = (lon2+3*Math.PI)%(2*Math.PI) - Math.PI;  // normalise to -180...+180
        if(lat2 == Double.NaN || lon2 == Double.NaN) return null;
        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);

        return new Coordinate(lon2, lat2);
    }

    public static Coordinate calculateCoordinateInLineStringPos(List<Coordinate> coordinateList, int index, double posDistance) {

        double d, accDistance;
        Coordinate c1, c2;
        if(posDistance >= 0) {
            accDistance = 0;
            for(int i = index; i < coordinateList.size()-1; i++) {
                c1 = coordinateList.get(i);
                c2 = coordinateList.get(i+1);
                d = getDistanceKm(c1, c2);
                if((accDistance + d) < posDistance) {
                    accDistance += d;
                } else {
                    double b = getBearing(c1, c2);
                    return destinationPoint(c1, b, posDistance - accDistance);
                }
            }
            return coordinateList.get(coordinateList.size()-1);
        }
        return null;
    }

    public static List<Placemark> calculatePlacemark(LineString pathLineString, Coordinate startC, double startingKM,
            List<Double> placemarkDistanceList, List<String> placemarkNameList, String styleUrl) {
        List<Coordinate> coordinateList = pathLineString.getCoordinates();

        Coordinate c1 = null, c2 = null, p = null;
        Coordinate minC = null;

        double minDistance = Double.MAX_VALUE;
        int minDistanceIndex = -1;

        double d;

        // Find the nearest vertex to point p
        for(int index = 0; index < coordinateList.size(); index++) {
            c1 = coordinateList.get(index);
            d = getDistanceKm(startC, c1);
            if(d < minDistance) {
                minC = c1;
                minDistanceIndex = index;
                minDistance = d;
            }
        }

        // Find if there is a point in some segment which is nearest to point p, shorter thant the nearest vertex
        for(int index = 0; index < coordinateList.size() - 1; index++) {
            c1 = coordinateList.get(index);
            c2 = coordinateList.get(index+1);
            p = getCoordinateInterception(c1, c2, startC);
            if(p != null) {
                d = getDistanceKm(startC, p);
                if(d < minDistance) {
                    minC = p;
                    minDistanceIndex = index;
                    minDistance = d;
                }
            }
        }

        if(minC == null) return null;

        // Generate a temporal coordinate list containing the shortest point to point p
        List<Coordinate> tmpCoordinateList;
        if (!coordinateList.contains(minC)) {
            tmpCoordinateList = new  ArrayList<Coordinate>();
            if(minDistanceIndex != 0) {
                tmpCoordinateList.addAll(coordinateList.subList(0, minDistanceIndex+1));
            }
            tmpCoordinateList.add(minC);
            tmpCoordinateList.addAll(coordinateList.subList(minDistanceIndex, coordinateList.size()));
        } else {
            tmpCoordinateList = coordinateList;
        }

        ArrayList<Placemark> placemarkList = new ArrayList<Placemark>();
        List<Coordinate> invCoordList = null;
        int invMinDistanceIndex = -1;
        double diffKM;
        double km;
        Placemark dstPlacemark;
        Point dstPoint;
        for(int i = 0; i < placemarkDistanceList.size(); i++) {
            km = placemarkDistanceList.get(i);
            p = null;
            diffKM = km - startingKM;
            if(diffKM >= 0) {
                p = calculateCoordinateInLineStringPos(tmpCoordinateList, minDistanceIndex, diffKM);
            } else {
                if(invCoordList == null) {
                    invCoordList = KMLLineString.inverseCoordinateList(coordinateList);
                    invMinDistanceIndex = invCoordList.indexOf(minC);
                }
                p = calculateCoordinateInLineStringPos(invCoordList, invMinDistanceIndex, -diffKM);
            }
            if(p!= null) {
                dstPoint = new Point();
                ArrayList<Coordinate> tPointCoordinateList = new ArrayList<Coordinate>();
                tPointCoordinateList.add(p);
                dstPoint.setCoordinates(tPointCoordinateList);
                dstPlacemark = new Placemark();
                dstPlacemark.setName(placemarkNameList.get(i));
                dstPlacemark.setDescription(placemarkNameList.get(i));
                dstPlacemark.setStyleUrl(styleUrl);
                dstPlacemark.setGeometry(dstPoint);
                placemarkList.add(dstPlacemark);
            }
        }

        return placemarkList;
    }
}
