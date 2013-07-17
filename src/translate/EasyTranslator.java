package translate;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.javanetworkframework.rb.util.AbstractWebTranslator;

public class EasyTranslator {

	private static final String from = "en";
	private static final String to = "de";
	
	/**
	 * @param args
	 */
	public static String translate(String text) {

		Locale srcLoc = new Locale(from); // French source

		Locale dstLoc = new Locale(to); // Translate to Russian

		AbstractWebTranslator res = (AbstractWebTranslator)

//		ResourceBundle.getBundle("com.javanetworkframework.rb.com.worldlingo.WorldLingoRB", dstLoc);
		ResourceBundle.getBundle("com.javanetworkframework.rb.com.freetranslation.FreeTranslationTranslatorRB", dstLoc);

//		ResourceBundle.getBundle("com.javanetworkframework.rb.com.altavista.AltaVistaTranslatorRB", dstLoc);
		
//		ResourceBundle.getBundle("com.javanetworkframework.rb.com.google.GoogleTranslatorRB", dstLoc);
		
		String result = res.getString(text, srcLoc);
		
//		JOptionPane.showMessageDialog(null, result, "FreeTranslationTranslatorTester", 1);
		
		return result;
	}

}
