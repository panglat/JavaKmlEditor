/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

class KMLtreeNode {
    protected String tittle;
    protected Feature feature;
    protected ImageIcon imageIcon;

    public KMLtreeNode(String tittle, Feature feature, ImageIcon imageIcon) {
        this.tittle = tittle;
        this.feature = feature;
        this.imageIcon = imageIcon;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
    public String getTittle() {
        return this.tittle;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;

    }
    public Feature getFeature() {
        return this.feature;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }
    public ImageIcon getImageIcon() {
        return this.imageIcon;
    }

    @Override
    public String toString() {
        return this.tittle;
    }
}

/**
 *
 * @author admin
 */
public class KML_Tree {
    static protected DefaultMutableTreeNode processDocument(Document document) {
        boolean ret = false;
        DefaultMutableTreeNode node = null;
        Feature feature = null;
        DefaultMutableTreeNode documentNode = new DefaultMutableTreeNode(new KMLtreeNode(document.getName(), document, null));

        List<Feature> featureList = document.getFeature();
        for(Iterator<Feature> it=featureList.iterator(); it.hasNext();) {
            feature = it.next();
            node = processFeature(feature);
            if(node != null) {
                documentNode.add(node);
                ret = true;
            }
        }
        if(ret == true) {
            return documentNode;
        } else {
            return null;
        }
    }

    static protected DefaultMutableTreeNode processFolder(Folder folder) {
        boolean ret = false;
        DefaultMutableTreeNode node = null;
        Feature feature = null;
        DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(new KMLtreeNode(folder.getName(), folder, null));

        List<Feature> featureList = folder.getFeature();
        for(Iterator<Feature> it=featureList.iterator(); it.hasNext();) {
            feature = it.next();
            node = processFeature(feature);
            if(node != null) {
                folderNode.add(node);
                ret = true;
            }
        }
        if(ret == true) {
            return folderNode;
        } else {
            return null;
        }
    }

    static protected DefaultMutableTreeNode processPlacemark(Placemark placemark) {
        return new DefaultMutableTreeNode(new KMLtreeNode(placemark.getName(), placemark, null));
    }

    static protected DefaultMutableTreeNode processFeature(Feature srcFeature) {
        if(srcFeature instanceof Document) {
            return processDocument((Document) srcFeature);
        } else if (srcFeature instanceof Folder) {
            return processFolder((Folder)srcFeature);
        } else if (srcFeature instanceof Placemark) {
            return processPlacemark((Placemark) srcFeature);
        } else {
            return null;
        }
    }

    static public DefaultMutableTreeNode processKML(Kml kml) {
        return processFeature(kml.getFeature());
    }
}
