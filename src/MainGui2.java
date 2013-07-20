import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import translate.EasyTranslator;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;


public class MainGui2 {

	protected Shell shlEasyTranslator;
	private static Robot robot;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			robot = new Robot();
			MainGui2 window = new MainGui2();
			window.registerListener(); // Test
			window.open();
			window.unregisterListener();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		
		Display display = Display.getDefault();
		createContents();
		shlEasyTranslator.open();
		shlEasyTranslator.layout();
		while (!shlEasyTranslator.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void registerListener() {
		try {
            GlobalScreen.registerNativeHook();
	    }
	    catch (NativeHookException ex) {
	            System.err.println("There was a problem registering the native hook.");
	            System.err.println(ex.getMessage());
	            ex.printStackTrace();
	
	            System.exit(1);
	    }

		GlobalScreen.getInstance().addNativeKeyListener(new NativeKeyListener() {
			
			@Override
			public void nativeKeyTyped(NativeKeyEvent arg0) {
				
				
			}
			
			@Override
			public void nativeKeyReleased(NativeKeyEvent arg0) {}
			
			@Override
			public void nativeKeyPressed(NativeKeyEvent arg0) {
				System.out.println("Key = " + arg0.getKeyCode());
				switch(arg0.getKeyCode()) {
				case  NativeKeyEvent.VK_F8 : {
					try {
						copyMarkedText();
						String text = getClipboardData();
						if(text != null) {
							String translation = EasyTranslator.translate(text);
							if(translation != null) {
								JOptionPane.showMessageDialog(null, translation, "Übersetzung", 1);
								
							}
						}
						robot.keyPress(27); // esc
						robot.keyRelease(27);
					} catch (Exception e) {
					}
				}
			}
			}
		});
	}

	
	private void unregisterListener() {
		//Clean up the native hook.
        GlobalScreen.unregisterNativeHook();
        System.runFinalization();
        System.exit(0);
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlEasyTranslator = new Shell();
		shlEasyTranslator.setSize(326, 205);
		shlEasyTranslator.setText("Easy Translator");
		shlEasyTranslator.setLayout(new FillLayout(SWT.HORIZONTAL));
		final Browser b = new Browser(shlEasyTranslator, SWT.NONE);
		b.setUrl("http://www.dict.cc/?s=" + "Hallo");
		b.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Text = "  + b.getText());
			}
			
			@Override
			public void changed(ProgressEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

	}
	/**
	 * Strg + C
	 */
	private void copyMarkedText() {
		robot.keyPress(17); // 17 und 67
		robot.keyPress(67);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.keyRelease(67);
		robot.keyRelease(17);
	}
	
	private String getClipboardData() throws Exception{
		Clipboard systemClipboard;
	    systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	    Transferable transferData = systemClipboard.getContents( null ); 
	    for(DataFlavor dataFlavor : transferData.getTransferDataFlavors()){ 
	      Object content = transferData.getTransferData( dataFlavor ); 
	      if ( content instanceof String ) 
	      { 
	    	String s = (String) content;
	        System.out.println( s );
	        if(s.length() < 30 ) return (String) content;
	      }
	    }
	  
	    return null;
	}

}
