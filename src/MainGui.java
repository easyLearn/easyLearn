import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JOptionPane;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import translate.EasyTranslator;
import translate.TranslateKeyListener;


public class MainGui {

	protected Shell shlEasyTranslator;
	private static Robot robot;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			robot = new Robot();
			MainGui window = new MainGui();
<<<<<<< HEAD
			window.registerListener(); // Test..
=======
			window.registerListener(); // Test
>>>>>>> branch 'master' of https://github.com/easyLearn/easyLearn.git
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
		shlEasyTranslator.setSize(233, 38);
		shlEasyTranslator.setText("Easy Translator");

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
