import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import translate.translators.DictCCTranslator;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;


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
				switch(arg0.getKeyCode()) {
				case  NativeKeyEvent.VK_F8 : {
					try {
						System.out.println("KeyStroke");
						copyMarkedText();
						String text = getClipboardData();
						System.out.println("Text = " + text);
						if(text != null) {
							System.out.println("Anfang");
							List<String> translations = new DictCCTranslator().translateSingleWord(text);
							System.out.println("Ende");
							if(translations != null) {
								String translation = "";
								for(String s : translations) {
									translation += s + "\n";
								}
								System.out.println("Anzeigen ");
								JOptionPane.showMessageDialog(null, translation, "Übersetzung", 1);
								
							}
						}
						robot.keyPress(27); // esc
						robot.keyRelease(27);
					} catch (Exception e) {
						e.printStackTrace();
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
		
		Browser browser = new Browser(shlEasyTranslator, SWT.NONE);
		browser.setUrl("www.google.de");
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
