package gui.translator;

import gui.XCombo;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import translate.AbstractTranslator;
import translate.AbstractTranslator.TranslationLocale;
import translate.translators.DictCCTranslator;
import translate.translators.GoogleTranslator;

public final class TranslatorBuilder {
	
	public static final String TRANSLATOR_SEL_GOOGLE = "Google Translator";
	public static final String TRANSLATOR_SEL_DICTCC = "Dict.cc Translator";
	
	private Composite parent;
	private XCombo<String> comboTranslator;
	private XCombo<TranslationLocale> comboFrom;
	private XCombo<TranslationLocale> comboTo;
	private AbstractTranslator translator;
	private Link source;
	private FormData sourceData;
	private Text result;
	private Shell shell;
	private Robot robot;

	public TranslatorBuilder(Composite parent) {
		this.parent = parent;
		shell = parent.getShell();
		try {robot = new Robot();} catch (Exception e) {}
	}
	
	public Composite build() {
		addOnTranslateListener();
		Composite translatorArea = new Composite(parent, SWT.NONE);
		translatorArea.setLayout(new FormLayout());
		
		Composite translatorSelectorArea = new Composite(translatorArea, SWT.NONE);
		translatorSelectorArea.setLayout(new RowLayout(SWT.HORIZONTAL));
		translatorSelectorArea.setLayoutData(new FormData());
		
		Label labelTranslatorSelector = new Label(translatorSelectorArea, SWT.NONE);
		labelTranslatorSelector.setText("\u00DCbersetzer: ");
		
		comboTranslator = new XCombo<String>(translatorSelectorArea, SWT.NONE);
		comboTranslator.addItem(TRANSLATOR_SEL_GOOGLE);
		comboTranslator.addItem(TRANSLATOR_SEL_DICTCC);
		comboTranslator.select(0);
		
		createLanguagePanel(translatorSelectorArea);
		
		Button b = new Button(translatorSelectorArea, SWT.PUSH);
		b.setText("Translate");
		b.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onTranslate();
			}
		});
		
		/* Ergebnisse der Uebersetzung werden dort angezeigt */
		
		createResultArea(translatorArea, translatorSelectorArea);

		return translatorArea;
	}
	
	private void createResultArea(Composite translatorArea, Composite attachTo) {
		source = new Link(translatorArea, SWT.NONE);
		sourceData = new FormData();
		sourceData.top = new FormAttachment(attachTo);
		sourceData.left = new FormAttachment( 0 );
		sourceData.right = new FormAttachment( 100 );
		source.setLayoutData(sourceData);
		source.addListener (SWT.Selection, new Listener () {
	        private Shell s;
			public void handleEvent(Event event) {
	        	s = new Shell();
	        	Point location = shell.getLocation();
	        	Point newLocation = new Point(location.x + shell.getSize().x, location.y);
	        	s.setLocation(newLocation);
	        	s.setLayout(new FillLayout());
	        	s.setSize(new Point(600, 400));
	    		Browser b = new Browser(s, SWT.NONE); b.setUrl(event.text);
	    		s.open();
	    		s.layout();
	        }
	    });
		setVisibilityOfSource(false);
		// Uebersetzungsfeld in dem die Uebersetzung angezeigt wird
		result = new Text(translatorArea, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY );
		
		result.setBackground(new Color(parent.getDisplay(), 240, 240, 240));
		FormData textData = new FormData();
		textData.top = new FormAttachment(source);
		textData.left = new FormAttachment( 0 );
		textData.right = new FormAttachment( 100 );
		textData.bottom = new FormAttachment( 100 );
		result.setLayoutData(textData);
		result.addListener(SWT.MouseUp, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	        	if(translator != null && translator.getSource() != null) setVisibilityOfSource(true);
	        }
	    });
	}
	
	private void setVisibilityOfSource(boolean visible) {
		if(visible) {
			source.setVisible(true);
        	sourceData.height = -1;
        	
		} else {
			source.setVisible(false);
			sourceData.height = 0;
		}
		parent.layout(true, true);
	}
	
	private void createLanguagePanel(Composite translatorSelectorArea) {
		comboFrom = new XCombo<TranslationLocale>(translatorSelectorArea, SWT.NONE);
		comboFrom.addItem(TranslationLocale.AUTO);
		comboFrom.addItem(TranslationLocale.ENGLISH);
		comboFrom.addItem(TranslationLocale.GERMAN);
		comboFrom.addItem(TranslationLocale.FRENCH);
		comboFrom.select(0);
		comboTo = new XCombo<TranslationLocale>(translatorSelectorArea, SWT.NONE);
		comboTo.addItem(TranslationLocale.ENGLISH);
		comboTo.addItem(TranslationLocale.GERMAN);
		comboTo.addItem(TranslationLocale.FRENCH);
		comboTo.select(0);
	}
	
	private void addOnTranslateListener() {
		if(GlobalScreen.isNativeHookRegistered()) { /* Registrierung des NativeHook siehe MainGui  */ 
			GlobalScreen.getInstance().addNativeKeyListener(new NativeKeyListener() {
				
				@Override
				public void nativeKeyTyped(NativeKeyEvent arg0) {
				}
				
				@Override
				public void nativeKeyReleased(NativeKeyEvent arg0) {
				}
				
				@Override
				public void nativeKeyPressed(NativeKeyEvent arg0) {
					switch(arg0.getKeyCode()) {
						case  NativeKeyEvent.VK_F8 : onTranslate(); break;
					}
				}
			});
		}
	}
	
	/**
	 * Der Uebersetzungsprozess wird gestartet. Zunaechst wird ueber den Copy Befehl (Strg + C) ein markierte Text in die
	 * Zwischenablage kopiert. Dann wird dieser Text dem Uebersetzer uebergeben. Dieser uebersetzt dann den Text.
	 */
	private void onTranslate() {
		new Thread() {
			public void run() {
				try {
					copyMarkedText();
					String text = getClipboardData();
					
					if(text != null) {
						translator = null;
		
						switch (comboTranslator.getSelectedItem()) {
							case TranslatorBuilder.TRANSLATOR_SEL_GOOGLE : translator = new GoogleTranslator(); break;
							case TranslatorBuilder.TRANSLATOR_SEL_DICTCC : translator = new DictCCTranslator(); break;
							default : ;
						}
						if(translator == null) return;
						translator.setFrom(comboFrom.getSelectedItem());
						translator.setTo(comboTo.getSelectedItem());
						List<String> translations = translator.translateSingleWord(text);
						if(translations != null) {
							String translation = "";
							for(String s : translations) {
								translation += s + "\n";
							}
							final String t = translation;
							Runnable setTranslation = new Runnable(){ public void run(){ 
								shell.forceActive(); /* Fenster geht in Vordergrund */ 
								setTranslationResult(t);
								source.setText(translator.getSource() != null || translator.getSource().equals("") 
										? "Quelle: <a>" + translator.getSource() + "</a>": "keine Angabe");
								setVisibilityOfSource(false);
							} };
						    Display.getDefault().asyncExec(setTranslation);
						}
					}
					if(robot == null) return;
					robot.keyPress(27); // escape druecken, um Markierungsbugs zu vermeiden
					robot.keyRelease(27);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * Simuliert Strg + C
	 */
	private void copyMarkedText() {
		if(robot == null) return;
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
	    return (String) systemClipboard.getData(DataFlavor.stringFlavor);
	}
	
	private void setTranslationResult(String translationResult) {
		if(result != null) result.setText(translationResult);
	}

}