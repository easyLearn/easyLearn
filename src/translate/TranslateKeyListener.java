package translate;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class TranslateKeyListener implements NativeKeyListener{

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		switch(arg0.getKeyCode()) {
			case  NativeKeyEvent.VK_F8 : {
				try {
					String text = getClipboardData();
					if(text != null) EasyTranslator.translate(text);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private String getClipboardData() throws Exception{
		Clipboard systemClipboard;
	    systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	    Transferable transferData = systemClipboard.getContents( null ); 
	    for(DataFlavor dataFlavor : transferData.getTransferDataFlavors()){ 
	      Object content = transferData.getTransferData( dataFlavor ); 
	      if ( content instanceof String ) 
	      { 
	    	String s = (String) content;
	        System.out.println( s );
	        if(s.length() < 30 ) return (String) content;
	      }
	    }
	  
	    return null;
	}

}
