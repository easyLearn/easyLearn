package translate;

import java.util.List;

/**
 * This abstract class provides a simple interface for Translators. Implementations can make use of Webservices or
 * other local translation services.
 * @author Tobias Wolf
 *
 */
public abstract class AbstractTranslator {
	
	/**
	 * From which language will be translated. Default = ENGLISH
	 */
	protected TranslationLocale From = TranslationLocale.ENGLISH;
	
	/**
	 * To which language will be translated. Default = GERMAN
	 */
	protected TranslationLocale To = TranslationLocale.GERMAN;
	
	/**
	 * Which translation possiblities / which translations languages should be supported.
	 * Current: AUTO (automatische Erkennung), ENGLISH, GERMAN, FRENCH
	 */
	public enum TranslationLocale{
		AUTO, ENGLISH, GERMAN, FRENCH;
	}

	/**
	 * Translates a single word or term without a context.
	 * For instance: car, cloud but also cloud computing (words with a space)
	 * should be supported.
	 * @param word the word or term which will be translated
	 * @return list of possible translations
	 */
	public abstract List<String> translateSingleWord(String word);
	
	/**
	 * Translates a text with a context. That means that words and terms
	 * can be translated differently depending on the context.
	 * @param text
	 * @return list of possible translations (usually it is only one translation)
	 */
	public abstract List<String> translateText(String text);
	
	
	/**
	 * @return From which language will be translated.
	 */
	public TranslationLocale getFrom() {
		return From;
	}

	public void setFrom(TranslationLocale from) {
		From = from;
	}

	/**
	 * @return To which language will be translated.
	 */
	public TranslationLocale getTo() {
		return To;
	}

	public void setTo(TranslationLocale to) {
		To = to;
	}
	
	
}
