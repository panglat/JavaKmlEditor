/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author admin
 */
public class KMLFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(f);
        if(extension != null) {
            if(extension.equals(Utils.kml)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "KML Files";
    }
}
