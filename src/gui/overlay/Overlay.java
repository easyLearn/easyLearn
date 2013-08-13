package gui.overlay;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Overlay
{
	
	private int frame_width = 300;
	private int frame_height = 100;
	private int header_height = 25;
	private int header_margin = 20;
	private int separator_height = 1;
	
	
	private JFrame frame;
	private JPanel topComp;
	private JPanel closeComp;
	private JPanel separatorComp;
	private JPanel botComp;
	
    public static void main(String[] args) {
        new Overlay().create();
        
    }
    
    public void create() {
    	
        createFrame();

        createTopLevelComponents();
        
        JLabel l1 = new JLabel("Overlay");
        l1.setForeground(Color.WHITE);
        l1.setToolTipText("Das ist ein Overlay");
//        l1.setFont(new Font("Arial", Font.PLAIN, 14));
        topComp.add(l1);
        
        JLabel close = new JLabel("x");
        close.setForeground(Color.WHITE);
        close.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				close();
				
			}
		});
        closeComp.add(close);
        
        new OverlayController(this);
//        addListener();
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
        frame.setResizable(true);
    }
    
    private void createTopLevelComponents() {
    	topComp = new JPanel(new FlowLayout());
        topComp.setBackground(new Color(200, 0, 0, 250));
        frame.add(topComp);
        
        // Close
        closeComp = new JPanel();
        closeComp.setBackground(new Color(0, 0, 0, 0));
        frame.add(closeComp);
        
        separatorComp = new JPanel();
        separatorComp.setBackground(new Color(255, 255, 255, 150));
        frame.add(separatorComp);
        
        botComp = new JPanel(new FlowLayout());
        botComp.setBackground(new Color(200, 0, 0, 0));
        frame.add(botComp);
        
        resize();
    }
    
    public void resize() {
    	frame_width = frame.getSize().width;
    	frame_height = frame.getSize().height;
    	topComp.setBounds(0,0,frame_width - header_margin, header_height);
    	closeComp.setBounds(frame_width - header_margin, 0, header_margin, header_height);
    	separatorComp.setBounds(0,header_height+1, frame_width , separator_height);
    	botComp.setBounds(0, header_height+1 + separator_height, frame_width, frame_height - header_height - separator_height);
    }
    
    public void close() {
    	frame.dispose();
    } 
    
    public JFrame getFrame() {
		return frame;
	}
}
