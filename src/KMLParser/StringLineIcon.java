/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KMLParser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author admin
 */
public class StringLineIcon extends BaseIcon{
    protected BasicStroke stroke;
    protected Color color;
    protected int intStroke;

    public StringLineIcon(int width, int height, int stroke, Color color) {
        super(width, height);
        this.stroke = new BasicStroke(stroke);
        this.color = color;
        this.intStroke = stroke;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(x ,y , width, height);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y + (height/2 - (intStroke/2 + 1)), width, intStroke + 2);
        
        g2d.setColor(color);
        g2d.setStroke(stroke);
        g2d.drawLine(x, y + (height/2), x + width, y + (height/2));

        g2d.dispose();
    }

}
