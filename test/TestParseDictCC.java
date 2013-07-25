
import java.util.List;


import org.junit.Test;

import translate.AbstractTranslator;
import translate.AbstractTranslator.TranslationLocale;
import translate.translators.DictCCTranslator;


/**
 * Testklasse fuer das Uebersetzen mit der Seite dict.cc
 * Jsoup Doku siehe http://jsoup.org/apidocs/org/jsoup/select/Selector.html
 * @author Tobias Wolf
 *
 */
public class TestParseDictCC {
	
	@Test
	public void test() {
		AbstractTranslator translator = new DictCCTranslator();
		translator.setFrom(TranslationLocale.FRENCH);
		translator.setTo(TranslationLocale.ENGLISH);
		List<String> translations = translator.translateSingleWord("sacré");
		for(String t : translations) {
			System.out.println(t);
		}
	}

}
