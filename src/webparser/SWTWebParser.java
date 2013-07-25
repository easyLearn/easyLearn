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
	private int readIndex = 0;
	private Shell shell;
	private Browser browser;
	private boolean disposeBrowser = true; // Debugvariable (sollte wieder raus): am Ende wieder Browser schlieﬂen ?
	private boolean finished = false; // Variable, die anzeigt wann die Ergebnisse vorliegen
	
	@Override
	public List<Document> readUrls(final String... weburls) {
		Thread thread = Thread.currentThread();
		if(thread.getName().equals("main")) return read(weburls);
		
		  /* Fall nicht im Main Thread -> mit diesem synchronisieren*/
	      Runnable r = new Runnable(){ public void run(){ read(weburls); } }; // READ WEBURLS
	      Display.getDefault().asyncExec(r);
	      while(!finished) {
	    	  
	    	  try {Thread.sleep(100);} catch (InterruptedException e) {}
	    	  
	      }
	      return contents;
	}
	        
	
	/**
	 * Oeffnet kurzzeitig einen Webbrowser, laedt und liest die vorhandenen Seiten aus.
	 * Eigentlich geht es einfacher, allerdings gibt es bisher keine andere Loesung um z.B. die Seite
	 * dict.cc auszulesen.
	 */
	public List<Document> read(final String... weburls) {
		if(weburls.length <= 0) return new ArrayList<Document>();
		finished = false;
		Display display = Display.getDefault();
		shell = new Shell();
		shell.setSize(600, 300);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		browser = new Browser(shell, SWT.NONE);
		setUrl(weburls[0]);
		readIndex = 0;
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent arg0) {
				contents.add(Jsoup.parse(browser.getText()));
				if(readIndex < weburls.length - 1 ) SWTWebParser.this.setUrl(weburls[++readIndex]);
				else {
					finished = true;
					if(disposeBrowser) shell.dispose();
					
				}
			}
			
			@Override
			public void changed(ProgressEvent arg0) {}
		});
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return contents;
	}

	private void setUrl(String url) {
		
			String newUrl = URLEncoder.encode(url);
			System.out.println(url);
			browser.setUrl(url);
		

	}
}
