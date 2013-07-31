import java.util.List;


public abstract class TranslatorTestUtility {

	protected boolean containsTranslations(String[] actual, String[] expected) {
		for(String e : expected) {
			boolean match = false;
			for(String a : actual) {
				if(a.trim().equals(e)) {
					match = true;
					break;
				}
			}
			if(!match) return false;
		}
		return true;
	}
	
	protected void print(List<String> tr) {
		for(String t : tr) System.out.println(t);
	}
}
