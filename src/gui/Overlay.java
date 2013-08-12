package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Overlay
{
	private JFrame frame;
	
    public static void main(String[] args) {
        new Overlay().create();
    }
    
    public void create() {
    	frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 30));
        frame.setAlwaysOnTop(true);
        frame.setLayout(new GridLayout());
        JLabel l1 = new JLabel("OVERLAY");
        frame.add(l1);
        frame.setVisible(true);
        frame.pack();
        frame.setLocation(700, 400);
        frame.setBounds(0, 0, 200, 100);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        addListener();
    }
    
    public void close() {
    	frame.dispose();
    }
    
    /* # # # # # # #  */
    /* DRAG DETECTION */
    
    private Point start_drag;
    private Point start_loc;
    
    private void addListener() {
    	frame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        frame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
    }
    
    private void formMousePressed(java.awt.event.MouseEvent evt) {
        start_drag = getScreenLocation(evt);
        start_loc = frame.getLocation();
    }
    
    private void formMouseDragged(java.awt.event.MouseEvent evt) {
        Point current = getScreenLocation(evt);
        Point offset = new Point((int) current.getX() - (int) start_drag.getX(), (int) current.getY() - (int) start_drag.getY());
		Point new_location = new Point((int) (start_loc.getX() + offset.getX()), (int) (start_loc.getY() + offset.getY()));
	    frame.setLocation(new_location);
	}
    
	private Point getScreenLocation(MouseEvent e) {
	    Point cursor = e.getPoint();
	    Point target_location = frame.getLocationOnScreen();
	    return new Point((int) (target_location.getX() + cursor.getX()), (int) (target_location.getY() + cursor.getY()));
	}
}
