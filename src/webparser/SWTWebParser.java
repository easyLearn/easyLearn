package webparser;

import java.util.List;
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

/**
 * Hinweis: SWTWebParser sollte in einem neuen Thread verwendet werden.
 * TODO: evtl. Threadaufruf irgendwie in Klasse kapseln.
 * Parser der den SWT Browser oeffnet. Sollte im Normalfall nicht verwendet werden (wenn Seite ueber JsoupParser aufrufbar ist).
 * Wenn eine URL gelesen wird, dann beginnt eine Zeitmessung. Wenn die Zeit ueber den Timeout liegt, wird nicht weiter auf ein Ergebnis
 * des Browser gewartet und dieses sofort zurueckgegeben (siehe readUntilNextProgress und readUrl)
 * @author Tobias Wolf
 *
 */
public class SWTWebParser extends AbstractWebParser{

	private Document content;
	private Shell shell;
	private Browser browser;
	private Display display;
	private boolean completed = false; // Laden der Seite vollstaendig (muss trotzdem nicht heissen, dass sie komplett geladen wurde)
	private boolean progress = false; // ein Fortschritt wurde gemacht beim Laden
	private long timeout = 10000; // 10s default
	private long timeBegin;
	private String lastHtml = "";
	
	public SWTWebParser(long timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public Document readUrl(final String weburl) {
		timeBegin = System.currentTimeMillis();
		
		Display d = checkForMainGuiThread();
		if(d == null ) { // wenn es noch kein Display gibt also keinen Gui Thread dann muss nicht mit diesem synchronisiert werden.
			new Thread() {public void run() {	read(weburl); }}.start();
		}
		else {
			asyncRead(weburl);		
		}
			
		waitForCompletion(); // warten bis URL fertig geladen
	      
	    return content; // letzte zurueckgeben
	}
	
	private Display checkForMainGuiThread() {
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		for(Thread t : threadSet) {
			Display d = Display.findDisplay(t);
			if(d != null && "main".equals(t.getName())) return d;
		}
		return null;
	}
	
	/**
	 * notSupported: returns null
	 */
	@Override
	public List<Document> readUrls(final String... weburls) {
		return null;
	}
	
	/**
	 * Warten bis zum naechsten Fortschritt oder wenn Timeout erreicht wurde.
	 * @return neuer Inhalt
	 */
	public Document readUntilNextProgress() {
		while(!progress && System.currentTimeMillis() - timeBegin < timeout) {
			Display.getDefault().asyncExec(new Runnable(){ public void run(){ setProgress(browser.getText()); } });
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
		
		completed = false;
		progress = false;
		return content;
	}
	        
	private void waitForCompletion() {
		while(!completed && System.currentTimeMillis() - timeBegin < timeout) {
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
		completed = false;
		progress = false;
	}
	
	private void asyncRead(final String url) {
		Display.getDefault().asyncExec(new Runnable(){ public void run(){ read(url); } });
	}
	
	/**
	 * Falls sich der Inhalt der Seite geaendert hat wird dieser als neuer Inhalt gesetzt und der Fortschritt
	 * ueber die Variable progress (true) vermerkt.
	 * @param text
	 */
	private void setProgress(String text) {
		if(!lastHtml.equals(text)) { // Inhalt hat sich geaendert ?
			lastHtml = text;
			content = Jsoup.parse(text);
			progress = true;
		}
	}
	
	/**
	 * Oeffnet  kurzzeitig einen Webbrowser, laedt und liest die vorhandenen Seiten aus.
	 * Eigentlich geht es einfacher, allerdings gibt es bisher keine andere Loesung um z.B. die Seite
	 * dict.cc auszulesen.
	 */
	public Document read(final String url) {
		initBrowser();
		setUrl(url);
		
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent arg0) {
				setProgress(browser.getText());
				completed = true;
			}
			
			@Override
			public void changed(ProgressEvent arg0) {
				setProgress(browser.getText());
			}
		});
		
		readAndDispatch();

		return content;
	}
	
	/**
	 * Initialisiert einen SWT Browser in einer Shell.
	 */
	private void initBrowser() {
		display = Display.getDefault();
		shell = new Shell();
		shell.setSize(600, 300);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		browser = new Browser(shell, SWT.NONE);
	}
	
	private void readAndDispatch() {
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void setUrl(String url) {
		browser.setUrl(url);
	}
	
	public void dispose() {
		Display.getDefault().asyncExec(new Runnable(){ public void run(){ shell.dispose(); } });
	}

	
}
