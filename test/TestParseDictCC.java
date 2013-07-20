import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import translate.AbstractTranslator;
import translate.AbstractTranslator.TranslationLocale;
import translate.translators.DictCCTranslator;
import webparser.SWTWebParser;

/**
 * Testklasse fuer das Uebersetzen mit der Seite dict.cc
 * Jsoup Doku siehe http://jsoup.org/apidocs/org/jsoup/select/Selector.html
 * @author Tobias Wolf
 *
 */
public class TestParseDictCC {

	private static String word1 = "Mutter";
	private static List<String> contents = new ArrayList<String>();
	private static String website = "http://www.dict.cc/?s=";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		contents = new SWTWebParser().readUrls(website + word1);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		AbstractTranslator translator = new DictCCTranslator();
//		List<String> translations = translator.translateSingleWord("Cloud Computing");
//		for(String t : translations) {
//			System.out.println(t);
//		}
		translator.setFrom(TranslationLocale.GERMAN);
		translator.setTo(TranslationLocale.FRENCH);
		List<String> translations = translator.translateSingleWord("verstecken");
		for(String t : translations) {
			System.out.println(t);
		}
	}

}
