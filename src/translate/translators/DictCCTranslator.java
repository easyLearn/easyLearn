package translate.translators;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import translate.AbstractTranslator;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import webparser.AbstractWebParser;
import webparser.JsoupWebParser;
import webparser.SWTWebParser;

/**
 * Speziell angepasster Translator fuer die Seite dict.cc
 * @author Tobias Wolf
 *
 */
public class DictCCTranslator extends AbstractTranslator{

	private AbstractWebParser webparser;
	private String website = ".dict.cc/?s="; // Website des Uebersetzerservices
	
	public DictCCTranslator() {
		webparser = new JsoupWebParser();
	}
	
	/**
	 * TODO: Noch nicht optimal: es werden Woerter nicht angezeigt, da sie sich etwas unterscheiden in Schreibweise.
	 * z.B. von deutsch-fr: verstecken -> wird nur eine Uebersetzung angezeigt, weil z.B. sich verstecken dasteht.
	 * IDEE: Begriffe absteigend nach Uebereinstimmung mit Eingabewort sortieren
	 */
	@Override
	public List<String> translateSingleWord(String word) {
		word = word.trim();
		/* Die Website des zu uebersetzenden Wortes einlesen */
	
		List<Document> contents = null;
		try {
			contents = webparser.readUrls("http://" + sitePrefix(From, To) + website + URLEncoder.encode(word, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		/* Die Ergebnisliste */
		
		List<String> result = new ArrayList<String>();
		
		/* Seite nach Uebersetzungen durchsuchen */
		
		for(Document doc : contents) {
			
			/* FromTo Pattern:  Muster fuer die Uebersetzung*/
			
			String FromTo = "a[href*=/"+ dictLinkAttr(From, To) + "]";
			
			/* ToFrom Pattern: Muster des Ausgangswortes */
			
			String ToFrom = "a[href*=/"+ dictLinkAttr(To, From) + "]";
			
			/* tr ist ein Zeilenelement, das sowohl Uebersetzung als auch Ausgangswort beinhaltet 
			 * tr beinhaltet dabei Unterelemente die den Pattern FromTo */
			for(Element e : doc.select("tr:has("+FromTo+")")) {
				
				String ausgangswort = "";
				for(Element wordPart : e.select(FromTo + ":matches(\\w)" + ":not(:matches([\\[\\]{}<>]))")) { //matches ein Wort mit Buchstabe oder Ziffer ohne Klammerungen
					ausgangswort += wordPart.text() + " ";
				}
				ausgangswort = ausgangswort.trim(); // rechtes leerzeichen entfernen

				/* Abfrage wird benoetigt damit nur wirklich genau das Ausganswort 
				 * beruecksichtigt wird, und nicht noch zusammengesetzte Woerter */
				
				if(new JaroWinkler().getSimilarity(ausgangswort.toLowerCase(), word.toLowerCase()) > 0.94) {
					String translation = "";
					for(Element wordPart : e.select(ToFrom + ":not(:matches([\\[\\]{}<>]))")) { // keine Klammern oder so (dort stehen Zusatinfos, die wir hier nicht brauchen)
						translation += wordPart.text() + " ";
					}
					result.add(translation);
				}
			}
		}
		return result;
	}

	/**
	 * NOT SUPPORTED
	 * @return null
	 */
	@Override
	public List<String> translateText(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String sitePrefix(TranslationLocale from, TranslationLocale to) {

		String FROM = mapSitePrefix(from);
		String TO = mapSitePrefix(to);
		if(FROM.length() > 0 && TO.length() > 0) {
			return FROM + TO;
		} else return "www";
	}
	
	private String mapSitePrefix(TranslationLocale locale) {
		switch(locale) {
			case ENGLISH : return "en";
			case GERMAN : return "de";
			case FRENCH : return "fr";
			default: return "";
		}
	}
	
	/**
	 * Erstellt das charakteristische Linkattribut von dict.cc, um die entsprechenden Uebersetzungen
	 * zu finden.
	 * @param from
	 * @param to
	 * @return
	 */
	private String dictLinkAttr(TranslationLocale from, TranslationLocale to) {
		// es wird alles deutsch geschrieben
		if(from == TranslationLocale.GERMAN || to == TranslationLocale.GERMAN) {
			return mapGermanLocale(from) + "-" + mapGermanLocale(to);
		} else {
			if(from == TranslationLocale.ENGLISH || to == TranslationLocale.ENGLISH) {
				return mapEnglishLocale(from) + "-" + mapEnglishLocale(to);
			}
		}
		
		return "";
	}
	
	private String mapGermanLocale(TranslationLocale locale) {
		switch(locale) {
			case ENGLISH : return "englisch";
			case GERMAN : return "deutsch";
			case FRENCH : return "franzoesisch";
			default: return "";
		}
	}
	
	private String mapEnglishLocale(TranslationLocale locale) {
		switch(locale) {
			case ENGLISH : return "english";
			case GERMAN : return "german";
			case FRENCH : return "french";
			default: return "";
		}
	}
}
