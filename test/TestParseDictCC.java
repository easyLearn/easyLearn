
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
public class TestParseDictCC extends TranslatorTestUtility{
	
	@Test
	public void testEnDe() {
		AbstractTranslator translator = new DictCCTranslator();
		translator.setFrom(TranslationLocale.ENGLISH);
		translator.setTo(TranslationLocale.GERMAN);
		List<String> translations = translator.translateSingleWord("chair");
		print(translations);
		assertTrue(containsTranslations(translations.toArray(new String[translations.size()]), new String[]{"Stuhl", "Sitz"}));
	}
	
	@Test
	public void testDeEn() {
		AbstractTranslator translator = new DictCCTranslator();
		translator.setFrom(TranslationLocale.GERMAN);
		translator.setTo(TranslationLocale.ENGLISH);
		List<String> translations = translator.translateSingleWord("erwarten");
		assertTrue(containsTranslations(translations.toArray(new String[translations.size()]), new String[]{"expect", "anticipate", "await", "estimate"}));
	}
	
	@Test
	public void testFrEn() {
		AbstractTranslator translator = new DictCCTranslator();
		translator.setFrom(TranslationLocale.FRENCH);
		translator.setTo(TranslationLocale.ENGLISH);
		List<String> translations = translator.translateSingleWord("sacré");
		assertTrue(containsTranslations(translations.toArray(new String[translations.size()]), new String[]{"holy", "sacred"}));
	}
	
	

}
