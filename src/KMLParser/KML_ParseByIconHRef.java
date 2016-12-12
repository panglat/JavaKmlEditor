/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Pair;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author admin
 */
public class KML_ParseByIconHRef {
    protected static HashSet<String> preProcessStyleSelectorList(List<StyleSelector> srcStyleSelectorList, HashSet<String> iconHRef) {
        StyleSelector styleSelector;
        Style style;
        HashSet<String> styleIDList = new HashSet<String>();

        for(Iterator<StyleSelector> it = srcStyleSelectorList.iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                style = (Style) styleSelector;
                if(iconHRef.contains(style.getIconStyle().getIcon().getHref())) {
                    styleIDList.add(style.getId());
                }
            }
        }
        return styleIDList;
    }

    protected static List<StyleSelector> processStyleSelectorList(List<StyleSelector> srcStyleSelectorList, HashSet<String> styleIDList) {
        StyleSelector styleSelector;
        Style style;
        StyleMap styleMap;
        String styleUrl = null;
        List<StyleSelector> dstStyleSelectorList = new ArrayList<StyleSelector>();


        for(Iterator<StyleSelector> it = srcStyleSelectorList.iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                style = (Style) styleSelector;
                if(styleIDList.contains(style.getId())) {
                    dstStyleSelectorList.add(styleSelector.clone());
                }
            } else if (styleSelector instanceof StyleMap) {
                styleMap = (StyleMap) styleSelector;
                for(Iterator<Pair> pIt = styleMap.getPair().iterator(); pIt.hasNext();) {
                    Pair pair = pIt.next();
                    styleUrl = pair.getStyleUrl();
                    if(styleUrl.startsWith("#")) {
                        styleUrl = styleUrl.substring(1);
                    }
                    if(styleIDList.contains(styleUrl)) {
                        dstStyleSelectorList.add(styleSelector.clone());
                    }
                }
            }
        }

        return dstStyleSelectorList;
    }

    protected static HashSet<String> postProcessStyleSelectorList(List<StyleSelector> srcStyleSelectorList) {
        StyleSelector styleSelector;
        HashSet<String> styleMapIDList = new HashSet<String>();
        for(Iterator<StyleSelector> it = srcStyleSelectorList.iterator(); it.hasNext();) {
            styleSelector = it.next();
            if (styleSelector instanceof StyleMap) {
                styleMapIDList.add(((StyleMap)styleSelector).getId());
            }
        }
        return styleMapIDList;
    }

    protected static Document processDocument(Document srcDocument, HashSet<String> iconHRef, HashSet<String> srcStyleIDList) {
        boolean ret = false;
        HashSet<String> styleIDList = preProcessStyleSelectorList(srcDocument.getStyleSelector(), iconHRef);
        if(!styleIDList.isEmpty()) {
            List<StyleSelector> dstStyleSelectorList = processStyleSelectorList(srcDocument.getStyleSelector(), styleIDList);
            HashSet<String> styleMapIDList = postProcessStyleSelectorList(dstStyleSelectorList);
            List<Feature> srcFeatureList = srcDocument.getFeature();
            Feature feature;
            Feature dstFeature;
            ArrayList<Feature> dstFeatureList = new ArrayList<Feature>();
            for(Iterator<Feature> it=srcFeatureList.iterator(); it.hasNext();) {
                feature = it.next();
                dstFeature = processFeature(feature, iconHRef, styleMapIDList);
                if(dstFeature != null) {
                    dstFeatureList.add(dstFeature);
                    ret = true;
                }
            }
            if(ret == true) {
                Document dstDocument = srcDocument.clone();
                dstDocument.setStyleSelector(dstStyleSelectorList);
                dstDocument.setFeature(dstFeatureList);

                return dstDocument;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
    
    protected static Folder processFolder(Folder srcFolder, HashSet<String> iconHRef, HashSet<String> srcStyleMapIDList) {
        boolean ret = false;
        List<Feature> srcFeatureList = srcFolder.getFeature();
        ArrayList<Feature> dstFeatureList = new ArrayList<Feature>();
        Feature feature;
        Feature dstFeature;
        for(Iterator<Feature> it=srcFeatureList.iterator(); it.hasNext();) {
            feature = it.next();
            dstFeature = processFeature(feature, iconHRef, srcStyleMapIDList);
            if(dstFeature != null) {
                dstFeatureList.add(dstFeature);
                ret = true;
            }
        }

        if(ret == true) {
            Folder dstFolder = srcFolder.clone();
            dstFolder.setFeature(dstFeatureList);

            return dstFolder;
        } else {
            return null;
        }
    }

    protected static Placemark processPlacemark(Placemark srcPlacemark, HashSet<String> srcStyleMapIDList) {
        String styleUrl = srcPlacemark.getStyleUrl();
        if(styleUrl.startsWith("#")) {
            styleUrl = styleUrl.substring(1);
        }

        if(srcStyleMapIDList.contains(styleUrl)) {
            return srcPlacemark.clone();
        } else {
            return null;
        }
    }

    protected static Feature processFeature(Feature srcFeature, HashSet<String> iconHRef, HashSet<String> srcStyleMapIDList) {
        if(srcFeature instanceof Document) {
            return processDocument((Document) srcFeature, iconHRef, srcStyleMapIDList);
        } else if (srcFeature instanceof Folder) {
            return processFolder((Folder)srcFeature, iconHRef, srcStyleMapIDList);
        } else if (srcFeature instanceof Placemark) {
            return processPlacemark((Placemark) srcFeature, srcStyleMapIDList);
        } else {
            return null;
        }
    }

    public static Kml parseKML(Kml sourceKml, HashSet<String> iconHRef) {
        Feature dstFeature = processFeature(sourceKml.getFeature(), iconHRef, null);
        if(dstFeature != null) {
            Kml destKml = new Kml();
            destKml.setFeature(dstFeature);
            return destKml;
        }
        return null;
    }
}
