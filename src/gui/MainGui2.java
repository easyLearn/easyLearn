package gui;

import gui.translator.TranslationBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;


public class MainGui2 {

	protected Shell shell;
	private TranslationBuilder translatorBuilder;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		MainGui2 window = new MainGui2();
		registerNativeHook();
		window.open();
		unregisterNativeHook();
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
	
	private static void registerNativeHook() {
		try {
            GlobalScreen.registerNativeHook();
	    }
	    catch (NativeHookException ex) {
	            System.err.println("There was a problem registering the native hook.");
	            System.err.println(ex.getMessage());
	            ex.printStackTrace();
	
	            System.exit(1);
	    }
	}

	private static void unregisterNativeHook() {
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
		shell.setSize(403, 265);
		shell.setText("Easy Translator");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Menu menuBar = new Menu(shell, SWT.BAR);
	    MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");
		shell.setMenuBar(menuBar);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		TabItem tabTranslator = new TabItem(tabFolder, SWT.NONE);
		tabTranslator.setText("Translator");
		
		/* Translator Bereich */
		
		translatorBuilder = new TranslationBuilder(tabFolder);
		Composite translatorArea = translatorBuilder.build();
		tabTranslator.setControl(translatorArea);	
		
		/* Content Extractor Bereich */
		
		TabItem tbtmContentExtractor = new TabItem(tabFolder, SWT.NONE);
		tbtmContentExtractor.setText("Content Extractor");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmContentExtractor.setControl(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
	}

}
