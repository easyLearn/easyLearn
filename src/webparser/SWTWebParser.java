package webparser;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
 * Parser der den SWT Browser oeffnet. Sollte im Normalfall nicht verwendet werden (wenn Seite ueber JsoupParser aufrufbar ist).
 * @author Tobias Wolf
 *
 */
public class SWTWebParser extends AbstractWebParser{

	private List<Document> contents = new ArrayList<Document>();
	private Shell shell;
	private Browser browser;
	private Display display;
	private boolean disposeBrowser = true; // Debugvariable (sollte wieder raus): am Ende wieder Browser schließen ?
	private boolean finished = false; // Variable, die anzeigt wann die Ergebnisse vorliegen
	
	@Override
	public Document readUrl(String weburl) {
		Thread thread = Thread.currentThread();
		if(thread.getName().equals("main")) return read(weburl);
		
		  /* Fall nicht im Main Thread -> mit diesem synchronisieren*/
	      asyncRead(weburl);
	      try {Thread.sleep(1500);} catch (InterruptedException e) {}
	    	  
	      Display.getDefault().asyncExec(new Runnable(){ public void run(){ shell.dispose(); } });
	      return contents.get(contents.size() - 1);
	}
	
	@Override
	public List<Document> readUrls(final String... weburls) {
		return null;
	}
	        
	long time;
	
	private void asyncRead(final String url) {
		Runnable r = new Runnable(){ public void run(){ read(url); } }; // READ WEBURLS
	    Display.getDefault().asyncExec(r);
	}
	
	/**
	 * Oeffnet  kurzzeitig einen Webbrowser, laedt und liest die vorhandenen Seiten aus.
	 * Eigentlich geht es einfacher, allerdings gibt es bisher keine andere Loesung um z.B. die Seite
	 * dict.cc auszulesen.
	 */
	public Document read(final String url) {
		time = System.currentTimeMillis(); // Time
		initBrowser();
		
		setUrl(url);
		finished = false;
		
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent arg0) {
				System.out.println("Completed 1 time: " + (System.currentTimeMillis() - time));
				contents.add(Jsoup.parse(browser.getText()));
			}
			
			@Override
			public void changed(ProgressEvent arg0) {
				// Mittelalter
				contents.add(Jsoup.parse(browser.getText()));
				System.out.println("Completed 2 time: " + (System.currentTimeMillis() - time) + " Current = " + arg0.current); 
				
			}
		});
		
		readAndDispatch();

		return contents.get(contents.size() -1);
	}
	
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
		
		String newUrl = URLEncoder.encode(url);
		browser.setUrl(url);
	}

	
}
