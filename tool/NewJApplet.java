package sheet.music.maker.tool;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JApplet;
import javax.swing.Timer;

/**
 *
 * @author Ryan
 */
public class NewJApplet extends JApplet implements ActionListener, MouseListener, KeyListener, MouseMotionListener {

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    MainGUI app;
    Timer t = new Timer(1,this);
    Staff staff;
    
    public void init() {
        app = new MainGUI();
        staff = new Staff();

        staff.addMouseListener(this);
        staff.addKeyListener(this);
        staff.addMouseMotionListener(this);
    }

   public void start() {
       t.start();
       add(staff,BorderLayout.CENTER);
   }
    
    public void paint(Graphics g) {
        app.paint(g);
    }
    
    // TODO overwrite start(), stop() and destroy() methods

    @Override
    public void actionPerformed(ActionEvent e) {
        app.actionPerformed(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        app.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        app.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        app.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        app.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        app.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        app.keyReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        app.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        app.mouseMoved(e);
    }
}
