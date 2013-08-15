package gui.translator;

import gui.overlay.Overlay;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import translate.AbstractTranslator;

public class TranslationController {

	private Robot robot;
	private AbstractTranslator translator;
	private TranslationBuilder gui;
	private Overlay overlay;
	private Map<String, Class<? extends AbstractTranslator>> translatorMap = new HashMap<>();
	
	public TranslationController(TranslationBuilder gui) {
		this.gui = gui;
		try {robot = new Robot();} catch (Exception e) {}
	}
	
	/**
	 * Wird nach dem Build der GUI Komponente ausgefuehrt
	 */
	public void buildFinished() {
		
		/* Wenn die Events getriggered werden, wird die Uebersetzungsmethode ausgefuehrt */
		
		addOnTranslateListener();
		
		gui.getTranslationButton().addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onTranslate();
			}
		});
		
		/* Wenn Resulttext - Feld ausgewaehlt wird die Quelle angezeigt */
		
		gui.getResultText().addListener(SWT.MouseUp, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	        	if(translator != null && translator.getSource() != null) setVisibilityOfSource(true);
	        }
	    });
		
		/* Browser der Uebersetzungs - Quelle oeffnen */
		
		gui.getSource().addListener (SWT.Selection, new Listener () {
	        private Shell s;
			public void handleEvent(Event event) {
	        	s = new Shell();
	        	Point location = gui.getShell().getLocation();
	        	Point newLocation = new Point(location.x + gui.getShell().getSize().x, location.y);
	        	s.setLocation(newLocation);
	        	s.setLayout(new FillLayout());
	        	s.setSize(new Point(600, 400));
	    		Browser b = new Browser(s, SWT.NONE); b.setUrl(event.text);
	    		s.open();
	    		s.layout();
	        }
	    });
		
		addTranslators();
		
		/* Quelle ist anfangs nicht sichtbar */
		
		setVisibilityOfSource(false);
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
	 * Fuegt die annotierten Uebersetzer der Combo hinzu.
	 */
	private void addTranslators() {
		for(Annotation a : gui.getClass().getAnnotations()) {
			if(a instanceof Translators) {
				Translators translators = (Translators) a;
				for(Translator t : translators.value()) {
					gui.getComboTranslator().addItem(t.name());
					translatorMap.put(t.name(), t.translator());
				}
				gui.getComboTranslator().select(0);
			}
		}
	}
	
	/**
	 * Erstellt einen Uebersetzer, wenn einer in der Combo selektiert ist.
	 * @return
	 */
	private AbstractTranslator createSelectedTranslator() {
		String name = gui.getComboTranslator().getSelectedItem();
		Class<? extends AbstractTranslator> c = translatorMap.get(name);
		if(c != null)
			try {
				return c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
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
						translator = createSelectedTranslator();
						if(translator == null) return;
						translator.setFrom(gui.getComboFrom().getSelectedItem());
						translator.setTo(gui.getComboTo().getSelectedItem());
						List<String> translations = translator.translateSingleWord(text);
						if(translations != null) {
							String translation = "";
							for(String s : translations) {
								translation += s + "\n";
							}
							final String t = translation;
							Runnable setTranslation = new Runnable(){ public void run(){ 
								gui.getShell().forceActive(); /* Fenster geht in Vordergrund */ 
								setTranslationResult(t);
								gui.getSource().setText(translator.getSource() != null || translator.getSource().equals("") 
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
	
	private void setVisibilityOfSource(boolean visible) {
		if(visible) {
			gui.getSource().setVisible(true);
        	gui.getSourceData().height = -1;
        	
		} else {
			gui.getSource().setVisible(false);
			gui.getSourceData().height = 0;
		}
		gui.getParent().layout(true, true);
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
		// TODO: delay um den Text wirklich sicher in der Zwischenablage
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}
	
	private String getClipboardData() throws Exception{
		Clipboard systemClipboard;
	    systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    return (String) systemClipboard.getData(DataFlavor.stringFlavor);
	}
	
	private void setTranslationResult(final String translationResult) {
		if(gui.getResultText() != null) gui.getResultText().setText(translationResult);
		
		// TODO: noch anpassen
		if(overlay == null || overlay.isDisposed()) 
			overlay = new Overlay("", translationResult).create();
		else overlay.setContent(translationResult);
		
	}

	public AbstractTranslator getTranslator() {
		return translator;
	}
}
