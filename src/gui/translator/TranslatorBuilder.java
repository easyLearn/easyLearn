package gui.translator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

public final class TranslatorBuilder {
	
	public static final String TRANSLATOR_SEL_GOOGLE = "Google Translator";
	public static final String TRANSLATOR_SEL_DICTCC = "Dict.cc Translator";
	
	private Combo comboTranslator;
	private String translatorSelection = "";
	private Text result;
	

	public Composite build(Composite tabFolder) {
		Composite translatorArea = new Composite(tabFolder, SWT.NONE);
		translatorArea.setLayout(new FormLayout());
		
		Composite translatorSelectorArea = new Composite(translatorArea, SWT.NONE);
		translatorSelectorArea.setLayout(new RowLayout(SWT.HORIZONTAL));
		translatorSelectorArea.setLayoutData(new FormData());
		
		Label labelTranslatorSelector = new Label(translatorSelectorArea, SWT.NONE);
		labelTranslatorSelector.setText("Auswahl \u00DCbersetzer: ");
		
		comboTranslator = new Combo(translatorSelectorArea, SWT.NONE);
		comboTranslator.add(TRANSLATOR_SEL_GOOGLE);
		comboTranslator.add(TRANSLATOR_SEL_DICTCC);
		comboTranslator.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				translatorSelection = (String) comboTranslator.getItem(comboTranslator.getSelectionIndex());
			}
		});
		
		// Uebersetzungsfeld in dem die Uebersetzung angezeigt wird
		result = new Text(translatorArea, SWT.MULTI | SWT.BORDER | SWT.WRAP );
		result.setBackground(new Color(tabFolder.getDisplay(), 240, 240, 240));
		FormData textData = new FormData();
		textData.top = new FormAttachment(translatorSelectorArea);
		textData.left = new FormAttachment( 0 );
		textData.right = new FormAttachment( 100 );
		textData.bottom = new FormAttachment( 100 );
		result.setLayoutData(textData);
		
		return translatorArea;
	}
	
	public String getTranslatorSelection() {
		return translatorSelection;
	}
	
	public void setTranslationResult(String translationResult) {
		if(result != null) result.setText("Uebersetzung\n\n" + translationResult);
	}
}