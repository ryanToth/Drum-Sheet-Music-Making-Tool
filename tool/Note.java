package sheet.music.maker.tool;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Ryan
 */

public class Note {
    
    double value = 0;
    double x, y;
    double shift = 0;
    int type;
    boolean connected = false;
    boolean dottedConnected = false;
    double noteSeparation;
    
    public Note(int timeSig , double x, double y, int type, double seperation) {
        this.x = x;
        this.y = y;       
        this.type = type;
        noteSeparation = seperation;
    }
    
    public void paint(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        
        g2.setStroke(new BasicStroke(2));
        
        if (type != 1)
            if (value < 2)
                g2.fill(new Ellipse2D.Double(x-15 - shift,y-8,30,16));
            else
                g2.draw(new Ellipse2D.Double(x-15 - shift,y-8,30,16));
        else {
            g2.setStroke(new BasicStroke(2));
            g2.drawLine((int)x-10 - (int)shift, (int)y-10, (int)x+10 - (int)shift, (int)y+10);
            g2.drawLine((int)x-10 - (int)shift, (int)y+10, (int)x+10 - (int)shift, (int)y-10);
        }
        
        if (value == 0.75 || value == 1.5 || (value >= 3 && value != 4)) g2.fill(new Ellipse2D.Double(x+15 - shift,y+8,7,7));
        
        g2.setStroke(new BasicStroke(2));
        
        if (type == 1) {
            
            g2.drawLine((int)x+10 - (int)shift, (int)y-10, (int)x+10 - (int)shift, (int)y-45);
            
            if ((value == 0.5 || value == 0.75) && !connected)
                g2.drawLine((int)x+10 - (int)shift, (int)y-45, (int)x+30 - (int)shift, (int)y-45);
            else if (value == 0.5 && connected)
                g2.drawLine((int)x+10 - (int)shift, (int)y-45, (int)(x+10-noteSeparation*2) - (int)shift, (int)y-45);
            else if (value == 0.25 && !connected && !dottedConnected) {
                g2.drawLine((int)x+10 - (int)shift, (int)y-45, (int)x+30 - (int)shift, (int)y-45);
                g2.drawLine((int)x+10 - (int)shift, (int)y-35, (int)x+30 - (int)shift, (int)y-35);
            }
            else if (value == 0.25 && connected && !dottedConnected) {
                g2.drawLine((int)x+10 - (int)shift, (int)y-45, (int)(x+10-noteSeparation) - (int)shift, (int)y-45);
                g2.drawLine((int)x+10 - (int)shift, (int)y-35, (int)(x+10-noteSeparation) - (int)shift, (int)y-35);
            }
            else if (value == 0.25 && dottedConnected) {
                g2.drawLine((int)x+10 - (int)shift, (int)y-45, (int)(x+30-noteSeparation*3) - (int)shift, (int)y-45);
                if (!connected)
                    g2.drawLine((int)x+10 - (int)shift, (int)y-35, (int)(x-10) - (int)shift, (int)y-35);
                else
                    g2.drawLine((int)x+10 - (int)shift, (int)y-35, (int)(x+10-noteSeparation) - (int)shift, (int)y-35); 
            }
        }
        else if (type == 2 && value != 4) {
            
            g2.drawLine((int)x+15 - (int)shift, (int)y, (int)x+15 - (int)shift, (int)y-40);
            
            if (dottedConnected) g2.drawLine((int)x+15 - (int)shift, (int)y-40, (int)(x+15-noteSeparation*3) - (int)shift, (int)y-40);
            
            if ((value == 0.5 || value == 0.75) && !connected && !dottedConnected)
                g2.drawLine((int)x+15 - (int)shift, (int)y-40, (int)x+30 - (int)shift, (int)y-40);
            else if ((value == 0.5 || value == 0.75) && connected)
                g2.drawLine((int)x+15 - (int)shift, (int)y-40, (int)(x+15-noteSeparation*2) - (int)shift, (int)y-40);
            else if (value == 0.25 && !connected && !dottedConnected) {
                g2.drawLine((int)x+15 - (int)shift, (int)y-30, (int)x+30 - (int)shift, (int)y-30);
                g2.drawLine((int)x+15 - (int)shift, (int)y-40, (int)x+30 - (int)shift, (int)y-40);
            }
            else if (value == 0.25 && connected && !dottedConnected) {
                g2.drawLine((int)x+15 - (int)shift, (int)y-40, (int)(x+15-noteSeparation) - (int)shift, (int)y-40);
                g2.drawLine((int)x+15 - (int)shift, (int)y-30, (int)(x+15-noteSeparation)- (int)shift, (int)y-30);
            }
            else if (value == 0.25 && dottedConnected) {
                if (!connected)
                    g2.drawLine((int)x+15 - (int)shift, (int)y-30, (int)(x) - (int)shift, (int)y-30);
                else
                    g2.drawLine((int)x+15 - (int)shift, (int)y-30, (int)(x+15-noteSeparation) - (int)shift, (int)y-30);
            }
        }
        else if (type == 3 && value != 4) {
            
            g2.drawLine((int)x-15 - (int)shift, (int)y, (int)x-15 - (int)shift, (int)y+40);
            
            if ((value == 0.5 || value == 0.75) && !connected)
                g2.drawLine((int)x-15 - (int)shift, (int)y+40, (int)x - (int)shift, (int)y+40);
            
            else if (value == 0.5 && connected)
                g2.drawLine((int)x-15 - (int)shift, (int)y+40, (int)(x-15-noteSeparation*2) - (int)shift, (int)y+40);
            
            else if (value == 0.25 && !connected && !dottedConnected) {
                g2.drawLine((int)x-15 - (int)shift, (int)y+30, (int)x - (int)shift, (int)y+30);
                g2.drawLine((int)x-15 - (int)shift, (int)y+40, (int)x - (int)shift, (int)y+40);
            }
            else if (value == 0.25 && connected && !dottedConnected) {
                g2.drawLine((int)x-15 - (int)shift, (int)y+40, (int)(x-15-noteSeparation) - (int)shift, (int)y+40);
                g2.drawLine((int)x-15 - (int)shift, (int)y+30, (int)(x-15-noteSeparation) - (int)shift, (int)y+30);
            }
            else if (value == 0.25 && dottedConnected) {
                g2.drawLine((int)x-15 - (int)shift, (int)y+40, (int)(x-15-noteSeparation*3) - (int)shift, (int)y+40);
                if (!connected)
                    g2.drawLine((int)x-15 - (int)shift, (int)y+30, (int)(x-30) - (int)shift, (int)y+30);
                else
                    g2.drawLine((int)x-15 - (int)shift, (int)y+30, (int)(x-15-noteSeparation) - (int)shift, (int)y+30);
            }
        }
    }
    
    public void skipSpace() {
        
    }
    
}
