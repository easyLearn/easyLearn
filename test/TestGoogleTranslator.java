import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import translate.AbstractTranslator;
import translate.AbstractTranslator.TranslationLocale;
import translate.translators.DictCCTranslator;
import translate.translators.GoogleTranslator;


public class TestGoogleTranslator extends TranslatorTestUtility{

	@Test
	public void testEnDe() {
		AbstractTranslator translator = new GoogleTranslator();
		translator.setFrom(TranslationLocale.ENGLISH);
		translator.setTo(TranslationLocale.GERMAN);
		List<String> translations = translator.translateSingleWord("chair");
		print(translations);
		assertTrue(containsTranslations(translations.toArray(new String[translations.size()]), new String[]{"Stuhl"}));
	}
	
	@Test
	public void testPerformance() {
		final AbstractTranslator translator = new GoogleTranslator();
		translator.setFrom(TranslationLocale.GERMAN);
		translator.setTo(TranslationLocale.ENGLISH);
		new Thread() {public void run() {	translator.translateSingleWord("chair"); }}.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		List<String> translations = translator.translateSingleWord("An seiner Spitze befinden sich empfindliche Tasthaare, welche auch kleinste Unebenheiten wahrnehmen. Dadurch eignet sich der Rüssel auch zum Tasten. Auch bei der Kontaktaufnahme zu Artgenossen in der Herde wird der Rüssel eingesetzt: gegenseitiges Umschlingen der Rüssel als Liebes- und Freundschaftszeichen und beim Spiel. Mit dem Rüssel werden auch Staub und Schmutz auf der Haut verteilt, was zum Schutz vor der starken Sonneneinstrahlung und vor Insekten geschieht. Der Rüssel wird auch dazu benutzt, Gegenstände zu greifen, beispielsweise, um sie zum Mund zu bewegen. Ausgebildete Arbeitselefanten können in Zusammenarbeit mit dem Elefantenführer Gegenstände von erheblichem Gewicht mit Hilfe des Rüssels – mit Unterstützung der Stoßzähne – manipulieren, heben und bewegen. Mit Hilfe des Rüssels kann ein Elefant auch Äste und Pflanzen aus bis zu sieben Meter Höhe erreichen. Ähnlich einem Giraffenhals verdoppelt er damit seine Streckhöhe.");
//		print(translations);
//		assertTrue(containsTranslations(translations.toArray(new String[translations.size()]), new String[]{"Stuhl"}));
	}

}
