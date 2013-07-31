package gui.translator;

import gui.XCombo;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
	private Combo comboTranslator;
	private XCombo<TranslationLocale> comboFrom;
	private XCombo<TranslationLocale> comboTo;
	private String translatorSelection = "";
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
		
		comboTranslator = new Combo(translatorSelectorArea, SWT.NONE);
		comboTranslator.add(TRANSLATOR_SEL_GOOGLE);
		comboTranslator.add(TRANSLATOR_SEL_DICTCC);
		comboTranslator.select(0);
		translatorSelection = TRANSLATOR_SEL_GOOGLE;
		comboTranslator.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Selected");
				translatorSelection = (String) comboTranslator.getItem(comboTranslator.getSelectionIndex());
			}
		});
		
		createLanguagePanel(translatorSelectorArea);
		
		Button b = new Button(translatorSelectorArea, SWT.PUSH);
		b.setText("Translate");
		b.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onTranslate();
			}
		});
		
		// Uebersetzungsfeld in dem die Uebersetzung angezeigt wird
		result = new Text(translatorArea, SWT.MULTI | SWT.BORDER | SWT.WRAP );
		result.setBackground(new Color(parent.getDisplay(), 240, 240, 240));
		FormData textData = new FormData();
		textData.top = new FormAttachment(translatorSelectorArea);
		textData.left = new FormAttachment( 0 );
		textData.right = new FormAttachment( 100 );
		textData.bottom = new FormAttachment( 100 );
		result.setLayoutData(textData);
		
		return translatorArea;
	}
	// noch zu tun
	private void createLanguagePanel(Composite translatorSelectorArea) {
		comboFrom = new XCombo<TranslationLocale>(translatorSelectorArea, SWT.NONE);
		comboFrom.addItem(TranslationLocale.AUTO);
		comboFrom.addItem(TranslationLocale.ENGLISH);
		comboFrom.addItem(TranslationLocale.GERMAN);
		comboFrom.addItem(TranslationLocale.FRENCH);
		comboTo = new XCombo<TranslationLocale>(translatorSelectorArea, SWT.NONE);
		comboTo.addItem(TranslationLocale.ENGLISH);
		comboTo.addItem(TranslationLocale.GERMAN);
		comboTo.addItem(TranslationLocale.FRENCH);
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
						AbstractTranslator translator = null;
		
						switch (translatorSelection) {
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
		if(result != null) result.setText("Uebersetzung\n\n" + translationResult);
	}

}