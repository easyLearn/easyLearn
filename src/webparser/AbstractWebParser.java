package webparser;

import java.util.List;

import org.jsoup.nodes.Document;

public abstract class AbstractWebParser {
	
	public abstract List<Document> readUrls(String ... weburls);
}
