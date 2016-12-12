/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 *
 * @author admin
 */
public abstract class BaseIcon implements Icon {
    protected int width;
    protected int height;

    public BaseIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract void paintIcon(Component c, Graphics g, int x, int y);

    public int getIconWidth() {
        return this.width;
    }

    public int getIconHeight() {
        return this.height;
    }

    public Image toImage() {
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          GraphicsDevice gd = ge.getDefaultScreenDevice();
          GraphicsConfiguration gc = gd.getDefaultConfiguration();
          BufferedImage image = gc.createCompatibleImage(this.width, this.height);
          Graphics2D g = image.createGraphics();
          this.paintIcon(null, g, 0, 0);
          g.dispose();
          return image;
      }

    public ImageIcon toImageIcon() {
        return new ImageIcon(this.toImage());
    }
}
