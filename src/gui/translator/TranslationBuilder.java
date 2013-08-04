package gui.translator;

import gui.XCombo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import translate.AbstractTranslator.TranslationLocale;

public final class TranslationBuilder {
	
	public static final String TRANSLATOR_SEL_GOOGLE = "Google Translator";
	public static final String TRANSLATOR_SEL_DICTCC = "Dict.cc Translator";
	
	private TranslationController controller;
	
	private Shell shell;
	private Composite parent;
	private XCombo<String> comboTranslator;
	private XCombo<TranslationLocale> comboFrom;
	private XCombo<TranslationLocale> comboTo;
	private Button translationButton;
	private Link source;
	private FormData sourceData;
	private Text resultText;

	public TranslationBuilder(Composite parent) {
		this.parent = parent;
		shell = parent.getShell();
		controller = new TranslationController(this);
	}
	
	public Composite build() {
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
		
		translationButton = new Button(translatorSelectorArea, SWT.PUSH);
		translationButton.setText("Translate");
		
		/* Ergebnisse der Uebersetzung werden dort angezeigt */
		
		createResultArea(translatorArea, translatorSelectorArea);
		
		/* wenn Build finished -> Listener und andere Initialisierungen ueber Controller */
		
		controller.buildFinished();
		
		return translatorArea;
	}
	
	private void createResultArea(Composite translatorArea, Composite attachTo) {
		source = new Link(translatorArea, SWT.NONE);
		sourceData = new FormData();
		sourceData.top = new FormAttachment(attachTo);
		sourceData.left = new FormAttachment( 0 );
		sourceData.right = new FormAttachment( 100 );
		source.setLayoutData(sourceData);
		// Uebersetzungsfeld in dem die Uebersetzung angezeigt wird
		resultText = new Text(translatorArea, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY );
		
		resultText.setBackground(new Color(parent.getDisplay(), 240, 240, 240));
		FormData textData = new FormData();
		textData.top = new FormAttachment(source);
		textData.left = new FormAttachment( 0 );
		textData.right = new FormAttachment( 100 );
		textData.bottom = new FormAttachment( 100 );
		resultText.setLayoutData(textData);
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

	public XCombo<String> getComboTranslator() {
		return comboTranslator;
	}

	public XCombo<TranslationLocale> getComboFrom() {
		return comboFrom;
	}

	public XCombo<TranslationLocale> getComboTo() {
		return comboTo;
	}

	public Shell getShell() {
		return shell;
	}

	public Link getSource() {
		return source;
	}

	public FormData getSourceData() {
		return sourceData;
	}

	public Composite getParent() {
		return parent;
	}

	public Text getResultText() {
		return resultText;
	}

	public Button getTranslationButton() {
		return translationButton;
	}
}