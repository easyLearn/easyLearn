package translate.translators;

import java.util.List;

import translate.AbstractTranslator;

public class GoogleTranslator extends AbstractTranslator{

	private final String p1_website = "https://translate.google.de/";
	private final String p2_site = "?hl=de&tab=wT#";
	private final String p3_from = "auto/"; // automatische Erkennung
	private final String p4_to = "en/";
	
	@Override
	public List<String> translateSingleWord(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> translateText(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String mapTranslationLocale(TranslationLocale locale) {
		switch(locale) {
			case ENGLISH: return "en";
			case GERMAN: return "de";
			case FRENCH: return "fr";
			case AUTO: return "auto";
			default: return "";
		}
	}

}
