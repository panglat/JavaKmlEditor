/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author admin
 */
public class KML_ProblemsResolver {

    static void scaleProblem(Kml kml) {
        Feature feature = kml.getFeature();
        if(feature instanceof Document) {
            Document document = (Document) feature;
            List<StyleSelector> listStyleSelector = document.getStyleSelector();
            StyleSelector styleSelector;
            Style style;
            IconStyle iconStyle;
            LineStyle lineStyle;
            double scale, width;
            for(Iterator<StyleSelector> it=listStyleSelector.iterator(); it.hasNext();) {
                styleSelector = it.next();
                if(styleSelector instanceof Style) {
                    style = (Style) styleSelector;
                    iconStyle = style.getIconStyle();
                    if(iconStyle != null) {
                        scale = iconStyle.getScale();
                        if(scale == 0.0) {
                            iconStyle.setScale(1.0);
                        }
                    }
                    lineStyle = style.getLineStyle();
                    if(lineStyle != null) {
                        width = lineStyle.getWidth();
                        if(width == 0.0) {
                            lineStyle.setWidth(2.0);
                        }
                    }
                }
            }
        }
    }
}
