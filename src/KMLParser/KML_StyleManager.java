/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author admin
 */
public class KML_StyleManager {
    protected ImageIcon missingIcon;
    protected HashMap<String, ImageIcon> styleURL_Icon;
    protected HashMap<Color, ImageIcon> styleColor_Icon;
    protected HashSet<String> iconHRefList;
    protected HashSet<String> lineStringColorList;

    protected Document document;
    int iconWidth, iconHeight;

    public KML_StyleManager(int iconWidth, int iconHeight) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        missingIcon = new MissingIcon(iconWidth, iconHeight, 4).toImageIcon();
        styleURL_Icon = new HashMap<String, ImageIcon>();
        styleColor_Icon = new HashMap<Color, ImageIcon>();
    }

    protected final void createIconHRefList() {
        StyleSelector styleSelector;
        this.iconHRefList = new HashSet<String>();
        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                Style style = (Style) styleSelector;
                IconStyle iconStyle = style.getIconStyle();
                if(iconStyle != null) {
                    this.iconHRefList.add(iconStyle.getIcon().getHref());
                }
            }
        }
    }
    
    protected final void createLineStringColorList() {
        StyleSelector styleSelector;
        this.lineStringColorList = new HashSet<String>();
        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                Style style = (Style) styleSelector;
                LineStyle lineStyle = style.getLineStyle();
                if(lineStyle != null) {
                    this.lineStringColorList.add(lineStyle.getColor());
                }
            }
        }
    }

    protected StyleMap getStyleMap(String styleMapID) {
        StyleSelector styleSelector;
        StyleMap styleMap;
        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof StyleMap) {
                styleMap = (StyleMap) styleSelector;
                if(styleMap.getId().equals(styleMapID)) {
                    return styleMap;
                }
            }
        }        
        return null;
    }

    public StyleMap getFirstStyleMapByIconHRef(String iconHRef) {
        Style style = null;
        StyleMap styleMap;
        while((style = this.getStyleFromIconHRef(iconHRef, style)) != null) {
            styleMap = this.getFirtStyleMap(style);
            if(styleMap != null) {
                return styleMap;
            }
        }
        return null;
    }

    public StyleMap getFirstStyleMapByLineStringColor(String colorString) {
        Style style = null;
        StyleMap styleMap;
        while((style = this.getStyleFromLineStringColor(colorString, style)) != null) {
            styleMap = this.getFirtStyleMap(style);
            if(styleMap != null) {
                return styleMap;
            }
        }
        return null;
    }

    protected StyleMap getFirtStyleMap(Style style) {
        StyleSelector styleSelector;
        StyleMap styleMap;
        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof StyleMap) {
                styleMap = (StyleMap) styleSelector;
                String styleUrl = styleMap.getPair().get(0).getStyleUrl();

                if(styleUrl.startsWith("#")) {
                    styleUrl = styleUrl.substring(1);
                }
                String styleID = style.getId();
                if(styleUrl.equals(styleID)) {
                    return styleMap;
                }
            }
        }
        return null;
    }


    protected Style getStyle(String styleID) {
        StyleSelector styleSelector;
        Style style;
        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                style = (Style) styleSelector;
                if(style.getId().equals(styleID)) {
                    return style;
                }
            }
        }
        return null;
    }

    protected Style getStyleFromIconHRef(String hRef, Style afterStyle) {
        StyleSelector styleSelector;
        Style style;
        boolean afterStyleFound;
        
        if(afterStyle != null) {
            afterStyleFound = false;
        } else {
            afterStyleFound = true;
        }

        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                style = (Style) styleSelector;
                if(afterStyleFound) {
                    IconStyle iconStyle = style.getIconStyle();
                    if(iconStyle != null) {
                         if(iconStyle.getIcon().getHref().equals(hRef)) {
                             return style;
                         }
                    }
                } else {
                    if(style.equals(afterStyle)) {
                        afterStyleFound = true;
                    }
                }
            }
        }
        return null;
    }

    protected Style getStyleFromLineStringColor(String colorString, Style afterStyle) {
        StyleSelector styleSelector;
        Style style;
        boolean afterStyleFound;

        if(afterStyle != null) {
            afterStyleFound = false;
        } else {
            afterStyleFound = true;
        }

        for(Iterator<StyleSelector> it = this.document.getStyleSelector().iterator(); it.hasNext();) {
            styleSelector = it.next();
            if(styleSelector instanceof Style) {
                style = (Style) styleSelector;
                if(afterStyleFound) {
                    LineStyle lineStyle = style.getLineStyle();
                    if(lineStyle != null) {
                         if(lineStyle.getColor().equals(colorString)) {
                             return style;
                         }
                    }
                } else {
                    if(style.equals(afterStyle)) {
                        afterStyleFound = true;
                    }
                }
            }
        }
        return null;
    }

    static protected URL getURL(String location) {
        URL url = null;
        try {
            url = new URL(location);
        }
        //this.IconHrefJList.setl   .add();
        catch (MalformedURLException ex) {
            try {
                url = new File(location).toURI().toURL();
                // Logger.getLogger(ParseByIconJPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex1) {
                Logger.getLogger(ParseByIconJPanel.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return url;
    }

    protected ImageIcon loadIcon(String location) {
        URL url = getURL(location);
        ImageIcon imageIcon = null;
        if(url != null) {
            imageIcon = new ImageIcon(url);
            if(imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                imageIcon = this.missingIcon;
            } else {
                Image srcImg = imageIcon.getImage();
                Image dstImg = srcImg.getScaledInstance(this.iconWidth, this.iconHeight, Image.SCALE_SMOOTH);
                imageIcon.setImage(dstImg);
            }
        }

        if(imageIcon == null) {
            imageIcon = missingIcon;
        }

        return imageIcon;
    }

    public void setDocument(Document document) {
        this.document = document;
        createIconHRefList();
        createLineStringColorList();
    }

    public ImageIcon getImageIcon(String iconHRef) {
        ImageIcon imageIcon = styleURL_Icon.get(iconHRef);
        if(imageIcon == null) {
            imageIcon = loadIcon(iconHRef);
            styleURL_Icon.put(iconHRef, imageIcon);
        }
        return imageIcon;
    }

    public ImageIcon getImageIcon(Color color) {
        ImageIcon imageIcon = styleColor_Icon.get(color);
        if(imageIcon == null) {
            imageIcon = new StringLineIcon(30, 30, 4, color).toImageIcon();
            styleColor_Icon.put(color, imageIcon);
        }
        return imageIcon;
    }

    public Style getStyle(Placemark placemark) {
        String styleUrl = placemark.getStyleUrl();

        if(styleUrl.startsWith("#")) {
            styleUrl = styleUrl.substring(1);
        }
        StyleMap styleMap = getStyleMap(styleUrl);
        if(styleMap != null) {
            styleUrl = styleMap.getPair().get(0).getStyleUrl();
            if(styleUrl.startsWith("#")) {
                styleUrl = styleUrl.substring(1);
            }
            return getStyle(styleUrl);
        }

        return null;
    }

    public ImageIcon getImageIcon(Placemark placemark) {
        String iconHRef = "";
        Style style = getStyle(placemark);
        if(style != null) {
            Geometry geometry = placemark.getGeometry();
            if(geometry instanceof Point) {
                if(style.getIconStyle() != null) {
                    iconHRef = style.getIconStyle().getIcon().getHref();
                } else {
                    return getImageIcon("");
                }
            } else if (geometry instanceof LineString) {
                if(style.getLineStyle() != null) {
                    String colorStyle = style.getLineStyle().getColor();
                    return getImageIcon(getColorFromStyle(style.getLineStyle().getColor()));
                } else {
                    return getImageIcon("");
                }
            }
        }
        return getImageIcon(iconHRef);
    }

    public static Color getColorFromStyle(String strColor) {
        int intRGB = Integer.parseInt(strColor.substring(2, 8), 16);
        //return new Color((intRGB&0xff0000)>>16, (intRGB&0xff00)>>8, (intRGB&0xff));
        return new Color(intRGB&0xff, (intRGB&0xff00)>>8, ((intRGB&0xff0000)>>16));
    }

    public HashSet<String> getIconHRefList() {
        return this.iconHRefList;
    }

    public HashSet<String> getLineStringColorList() {
        return this.lineStringColorList;
    }
}
