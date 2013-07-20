package webparser;

import java.util.List;

public abstract class AbstractWebParser {
	
	public abstract List<String> readUrls(String ... weburls);
}
