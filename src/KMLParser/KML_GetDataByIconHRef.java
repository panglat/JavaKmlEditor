/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author admin
 */
public class KML_GetDataByIconHRef {
    protected static void getDataStyle(Style srcStyle, HashSet<String> iconHRef) {
        iconHRef.add(srcStyle.getIconStyle().getIcon().getHref());
    }

    protected static void getDataStyleSelectorList(List<StyleSelector> srcStyleSelectorList, HashSet<String> iconHRef) {
        StyleSelector styleSelector;

        for(Iterator<StyleSelector> it = srcStyleSelectorList.iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                getDataStyle((Style)styleSelector, iconHRef);
            }
        }

    }

    protected static void getDataDocument(Document document, HashSet<String> iconHRef) {
        getDataStyleSelectorList(document.getStyleSelector(), iconHRef);
    }

    protected static void getDataFeature (Feature feature, HashSet<String> iconHRef) {
        if(feature instanceof Document) {
            getDataDocument((Document) feature, iconHRef);
        }
    }

    public static HashSet<String> getDataKml(Kml kml) {
        HashSet<String> iconHRef = new HashSet<String>();

        getDataFeature(kml.getFeature(), iconHRef);

        return iconHRef;
    }
}
