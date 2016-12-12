/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author admin
 */
public class KML_LineStringRenderer extends JLabel implements ListCellRenderer {
    final static Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
    protected KML_StyleManager kmlStyleManager;

    public KML_LineStringRenderer(KML_StyleManager kmlStyleManager) {
        this.kmlStyleManager = kmlStyleManager;
        this.setOpaque(true);
        this.setIconTextGap(12);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String colorString = (String) value;
        Color color = kmlStyleManager.getColorFromStyle(colorString);

        ImageIcon imageIcon = kmlStyleManager.getImageIcon(color);
        String tittle = "Path";
        this.setText(tittle);
        this.setIcon(imageIcon);
        if(isSelected) {
            this.setBackground(HIGHLIGHT_COLOR);
            this.setForeground(Color.white);
        } else {
            this.setBackground(Color.white);
            this.setForeground(Color.black);
        }

        return this;
    }

}
