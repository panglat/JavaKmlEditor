/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author admin
 */
public class KML_StyleReplace {
    static boolean processPlacemark(Placemark placemark, StyleMap iconStyleMap, StyleMap lineStringStyleMap) {
        Geometry geometry = placemark.getGeometry();
        if(geometry instanceof Point) {
            if(iconStyleMap != null) {
                placemark.setStyleUrl("#" + iconStyleMap.getId());
            }
            return true;
        } else if(geometry instanceof LineString) {
            if(lineStringStyleMap != null) {
                placemark.setStyleUrl("#" + lineStringStyleMap.getId());
            }
            return true;
        }
        return false;
    }

    static boolean processFolder(Folder folder, StyleMap iconStyleMap, StyleMap lineStringStyleMap, boolean applyToSubfolder) {
        List<Feature> featureList = folder.getFeature();
        Feature feature;
        for(Iterator<Feature> it = featureList.iterator(); it.hasNext();) {
            feature = it.next();
            processFeature(feature, iconStyleMap, lineStringStyleMap, applyToSubfolder);
        }
        return true;
    }

    static boolean processFeature(Feature feature, StyleMap iconStyleMap, StyleMap lineStringStyleMap, boolean applyToSubfolder) {
        if(feature instanceof Placemark) {
            return processPlacemark((Placemark) feature, iconStyleMap, lineStringStyleMap);
        } else if(feature instanceof Folder) {
            if(applyToSubfolder) {
                return processFolder((Folder) feature, iconStyleMap, lineStringStyleMap, applyToSubfolder);
            }
        }
        return false;
    }

    static boolean processFeatureList (List<Feature> featureList, StyleMap iconStyleMap, StyleMap lineStringStyleMap, String description, String name, boolean applyToSubfolder) {
        Feature feature;
        for(Iterator<Feature> it = featureList.iterator(); it.hasNext();) {
            feature = it.next();
            if(featureList.size() == 1) {
                if(description != null) {
                    feature.setDescription(description);
                }
                if(name != null) {
                    feature.setName(name);
                }
            }
            processFeature(feature, iconStyleMap, lineStringStyleMap, applyToSubfolder);
        }
        return true;
    }
}
