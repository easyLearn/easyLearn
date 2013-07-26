package gui;

import gui.translator.TranslatorBuilder;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import translate.AbstractTranslator;
import translate.translators.DictCCTranslator;
import translate.translators.GoogleTranslator;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;


public class MainGui2 {

	protected Shell shell;
	private static Robot robot;
	private TranslatorBuilder translatorBuilder;

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
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void registerListener() {
		try {
			System.out.println("Register Listener");
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
						copyMarkedText();
						String text = getClipboardData();
						
						if(text != null) {
							
							AbstractTranslator translator = null;
//							System.out.println("Selection = " + selectionTranslator);
							switch (translatorBuilder.getTranslatorSelection()) {
								case TranslatorBuilder.TRANSLATOR_SEL_GOOGLE : translator = new GoogleTranslator(); break;
								case TranslatorBuilder.TRANSLATOR_SEL_DICTCC : translator = new DictCCTranslator(); break;
								default : ;
							}
							if(translator == null) return;
							List<String> translations = translator.translateSingleWord(text);
							if(translations != null) {
								String translation = "";
								for(String s : translations) {
									translation += s + "\n";
								}
								final String t = translation;
								Runnable setTranslation = new Runnable(){ public void run(){ 
									shell.forceActive(); /* Fenster geht in Vordergrund */ 
									translatorBuilder.setTranslationResult(t);
								} };
							    Display.getDefault().asyncExec(setTranslation);
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
	 * TODO: GUI-Elemente in einzelne create Methoden auslagern. Evtl. Builder einsetzen.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(361, 265);
		shell.setText("Easy Translator");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		TabItem tabTranslator = new TabItem(tabFolder, SWT.NONE);
		tabTranslator.setText("Translator");
		
		/* Translator Bereich */
		
		translatorBuilder = new TranslatorBuilder();
		Composite translatorArea = translatorBuilder.build(tabFolder);
		tabTranslator.setControl(translatorArea);	
		
		/* Content Extractor Bereich */
		
		TabItem tbtmContentExtractor = new TabItem(tabFolder, SWT.NONE);
		tbtmContentExtractor.setText("Content Extractor");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmContentExtractor.setControl(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
	}
	
	/**
	 * Simuliert Strg + C
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
	    System.out.println("Clipboard = " + systemClipboard.getData(DataFlavor.stringFlavor));
	    return (String) systemClipboard.getData(DataFlavor.stringFlavor);
//	    for(DataFlavor dataFlavor : transferData.getTransferDataFlavors()){ 
//	      Object content = transferData.getTransferData( dataFlavor ); 
//	      if ( content instanceof String ) 
//	      { 
//	    	String s = (String) content;
////	        System.out.println( s );
//	        if(s.length() < 30 ) return (String) content;
//	      }
//	    }
//	  
	}

}
