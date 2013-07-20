package webparser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTWebParser extends AbstractWebParser{

	private List<String> contents = new ArrayList<String>();
	private int readIndex = 0;
	
	/**
	 * Oeffnet kurzzeitig einen Webbrowser, laedt und liest die vorhandenen Seiten aus.
	 * Eigentlich geht es einfacher, allerdings gibt es bisher keine andere Loesung um z.B. die Seite
	 * dict.cc auszulesen.
	 */
	@Override
	public List<String> readUrls(final String... weburls) {
		if(weburls.length <= 0) return new ArrayList<String>();
		Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setSize(600, 300);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		final Browser browser = new Browser(shell, SWT.NONE);
		browser.setUrl(weburls[0]);
		readIndex = 0;
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent arg0) {
				contents.add(browser.getText());
				if(readIndex < weburls.length - 1 ) browser.setUrl(weburls[++readIndex]);
				else shell.dispose();
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

}
