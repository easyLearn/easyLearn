package translate.translators;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import translate.AbstractTranslator;
import webparser.AbstractWebParser;
import webparser.JsoupWebParser;
import webparser.SWTWebParser;

public class GoogleTranslator extends AbstractTranslator{

	private final SWTWebParser parser;
	private final String p1_website = "https://translate.google.de/";
	private final String p2_site = "?hl=de&tab=wT#";
	
	public GoogleTranslator() {
		parser = new SWTWebParser(5000); // Timeout von 5 s
	}
	
	@Override
	public List<String> translateSingleWord(String word) {
		// TODO Auto-generated method stub
		return translateText(word);
	}

	@Override
	public List<String> translateText(String text) {
		String translation = "";
		text = text.trim();
		List<String> result = new ArrayList<String>();
		String from = mapTranslationLocale(From);
		String to = mapTranslationLocale(To);
		String url = p1_website + p2_site + from + "/" + to + "/" + text; // URL fuer das zu uebersetzende Wort
		System.out.println("Google Url = " + url);
		translation = getTranslation(parser.readUrl(url));
		
		int tries = 0;
		while ("".equals(translation) && ++tries <= 10) // nicht mehr als 10 Versuche
			translation = getTranslation(parser.readUntilNextProgress());
		
		parser.dispose(); // sollte nach jedem readUrl gemacht werden, ansonsten bleibt Browser auf
		
		if(translation.length() > 0) result.add(translation);
		
		return result;
	}
	
	private String getTranslation(Document doc) {
		String t = "";
		if(doc == null ) return t; 
		
		String pattern = "span#result_box"; // Uebersetzungsfeld
		
		for(Element e : doc.select(pattern)) { // es gibt nur 1 solches Element
			for(Element word : e.children()) {
				t += word.text() + " ";
			}
		}
	
		return t;
		
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
