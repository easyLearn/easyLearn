import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class Test {

	private static Shell shlEasyTranslator;
	private static Browser b = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
	
	    
		

	}

	private static void createContents() {
		shlEasyTranslator = new Shell();
		shlEasyTranslator.setSize(326, 205);
		shlEasyTranslator.setText("Easy Translator");
		shlEasyTranslator.setLayout(new FillLayout(SWT.HORIZONTAL));
		b = new Browser(shlEasyTranslator, SWT.NONE);
		
		b.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Text = "  + b.getText());
				shlEasyTranslator.dispose();
				
			}
			
			@Override
			public void changed(ProgressEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
