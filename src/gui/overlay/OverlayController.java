package gui.overlay;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class OverlayController {

	private Overlay gui;
	
	private Integer DRAG_MODE = DRAG_MODE_NONE;
	private static final int DRAG_MODE_NONE = -1;
	private static final int DRAG_MODE_MOVE = 0;
	private static final int DRAG_MODE_SIZE_LEFT = 1;
	private static final int DRAG_MODE_SIZE_RIGHT = 2;
	private Point start_drag;
    private Point start_loc;
    int w;
    int h;
    
	
	public OverlayController(Overlay gui) {
		this.gui = gui;
		addListener();
	}
	
	private void addListener() {
		gui.getFrame().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                getLocation(evt);
                System.out.println("Drag: " + DRAG_MODE);
            }
        });
		gui.getFrame().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                onDrag(evt);
            }
        });
	}
	
	private void getLocation(java.awt.event.MouseEvent evt) {
        start_drag = getScreenLocation(evt);
        start_loc = gui.getFrame().getLocation();
        w = gui.getFrame().getSize().width;
		h = gui.getFrame().getSize().height;
        determineDragMode();
    }
    
	private void determineDragMode() {
		int diff_x = start_drag.x - start_loc.x;
        int diff_y = start_drag.y - start_loc.y;
        if(diff_x < 5 ) { DRAG_MODE = DRAG_MODE_SIZE_LEFT; return; }
        if(diff_x > w - 5) { DRAG_MODE = DRAG_MODE_SIZE_RIGHT; return; }
        
        DRAG_MODE = DRAG_MODE_MOVE; // ansonsten
	}
	
    private void onDrag(java.awt.event.MouseEvent evt) {
        Point current = getScreenLocation(evt);
        Point offset = new Point((int) current.getX() - (int) start_drag.getX(), (int) current.getY() - (int) start_drag.getY());   
        switch(DRAG_MODE) {
        	case DRAG_MODE_MOVE: {
        		Point new_location = new Point((int) (start_loc.getX() + offset.getX()), (int) (start_loc.getY() + offset.getY()));
        		gui.getFrame().setLocation(new_location);
        		break;
        	}
        	case DRAG_MODE_SIZE_LEFT: {
        		gui.getFrame().setBounds(current.x, gui.getFrame().getLocation().y, w - offset.x, h);
        		gui.resize();
        		break;
        	}
        	case DRAG_MODE_SIZE_RIGHT: {
        		gui.getFrame().setSize(w+offset.x, h);
        		gui.resize();
        		break;
        	}
        	default: break;
        }
        
	}
    
	private Point getScreenLocation(MouseEvent e) {
	    Point cursor = e.getPoint();
	    Point target_location = gui.getFrame().getLocationOnScreen();
	    return new Point((int) (target_location.getX() + cursor.getX()), (int) (target_location.getY() + cursor.getY()));
	}
}
