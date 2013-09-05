package gui.overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Overlay
{
	
	private int frame_width = 300;
	private int frame_height = 100;
	private int header_height = 25;
	private int header_margin = 20;
	private int separator_height = 1;
	
	
	private JFrame frame;
	private JLabel title_label;
	private JPanel topComp;
	private JPanel closeComp;
	private JPanel separatorComp;
	private JTextArea text;
	
	private String title = "";
	private String content = "";
	
    public static void main(String[] args) {
        new Overlay().create();
        
    }
    
    public Overlay() {
    	title = "Overlay";
    	content = "Hier kommt Text";
    }

    public Overlay(String title, String content) {
    	this.title = title;
    	this.content = content;
    }
    
    public Overlay create() {
    	
        createFrame();

        createTopLevelComponents();
        
        createTopBar();
        
        new OverlayController(this);
        
        return this;
    }
    
    private void createFrame() {
    	frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 170));
        frame.setAlwaysOnTop(true);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.pack();
        frame.setLocation(700, 400);
        frame.setBounds(0, 0, frame_width, frame_height);
    }
    
    private void createTopLevelComponents() {
    	topComp = new JPanel(new GridBagLayout());
        topComp.setBackground(new Color(200, 0, 0, 200));
        frame.add(topComp);
        
        // Close
        closeComp = new JPanel();
        closeComp.setBackground(new Color(0, 0, 0, 0));
        frame.add(closeComp);
        
        // Separator
        
        separatorComp = new JPanel();
        separatorComp.setBackground(new Color(255, 255, 255, 150));
        frame.add(separatorComp);
        
        text =new JTextArea(content);
//        text.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        text.setEditable(false);  
        text.setWrapStyleWord(true);  
        text.setLineWrap(true);
        text.setForeground(Color.WHITE);
        text.setBackground(new Color(0, 0, 0, 0));
//        text.setFocusable(false);
        frame.add(text);
        
        /* Bug Workaround -> sonst wird markiert angegklickter Text bei transparenten Hintergrund verwaschen/ undeutlich 
        	also neu zeichnen uber setzen einer minimal anderen Groesse */
        text.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				repaintTextArea();
			}
		});
        text.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				repaintTextArea();
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				repaintTextArea();
			}
		});
        resize();
        resizeFrameToText();
    }
    
    private void createTopBar() {
    	title_label = new JLabel(); 
        title_label.setForeground(Color.WHITE);
        setTitle(title);
        topComp.add(title_label);
        
        JLabel close = new JLabel("x");
        close.setForeground(Color.WHITE);
        close.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				close();
			}
		});
        closeComp.add(close);
    }
    
    /**
     * Bug Workaround. Ansonsten wird Schrift beim Anklicken z.T. unleserlich dargestellt.
     */
    private int i = 1;
    private void repaintTextArea() {
    	if(text == null) return;
    	Dimension size = text.getSize();
		synchronized(size) {
			text.setSize(size.width , size.height + i);
			switch(i) {
				case 1: i = -1; break;
				case -1: i = 1; break;
			}
		}
    }
    
    public void resize() {
    	frame_width = frame.getSize().width;
    	frame_height = frame.getSize().height;
    	topComp.setBounds(0,0,frame_width - header_margin, header_height);
    	closeComp.setBounds(frame_width - header_margin, 0, header_margin, header_height);
    	separatorComp.setBounds(0,header_height+1, frame_width , separator_height);
    	text.setBounds(5, header_height + separator_height + 2, frame_width -10, frame_height - header_height - separator_height);
    }
    
    private void resizeFrameToText() {
    	int textHeight =  text.getPreferredSize().height;
    	frame_height = textHeight + header_height + separator_height;
    	frame.setSize(frame_width, frame_height);
    	resize();
    }
    
    public void close() {
    	frame.dispose();
    	frame = null;
    } 
    
    public boolean isDisposed() {
    	return (frame == null) ? true : false;
    }
    
    public JFrame getFrame() {
		return frame;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				title_label.setText(title);
				title_label.setToolTipText(title);
			}
		});
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
		text.setText(content);
		resizeFrameToText();
		repaintTextArea();
	}
	
	public void setContent(final String title, final String content) {
		this.title = title;
		setTitle(title);
		setContent(content);
	}
	
	public JTextArea getText() {
		return text;
	}
    
}
