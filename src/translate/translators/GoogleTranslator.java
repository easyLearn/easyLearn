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

	private final AbstractWebParser parser;
	private final String p1_website = "https://translate.google.de/";
	private final String p2_site = "?hl=de&tab=wT#";
	private final String p3_from = "auto/"; // automatische Erkennung
	private final String p4_to = "en/";
	
	public GoogleTranslator() {
		parser = new SWTWebParser();
	}
	
	@Override
	public List<String> translateSingleWord(String word) {
		// TODO Auto-generated method stub
		return translateText(word);
	}

	@Override
	public List<String> translateText(String text) {
		text = text.trim();
		List<String> result = new ArrayList<String>();
		String from = mapTranslationLocale(From);
		String to = mapTranslationLocale(To);
		Document doc = parser.readUrl(p1_website + p2_site + from + "/" + to + "/" + text);
		if(doc == null ) return result; 
		
		
			String pattern = "span#result_box"; // Uebersetzungsfeld
		
			String translation = "";
			for(Element e : doc.select(pattern)) { // es gibt nur 1 solches Element
//				System.out.println("Elemente = " + e.parent().html());
//				System.out.println(doc.html());
				for(Element word : e.children()) {
					translation += word.text() + " ";
				}
			}
		
			if(translation.length() > 0) result.add(translation);
		
		return result;
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
