package webparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupWebParser extends AbstractWebParser {

	@Override
	public List<Document> readUrls(String... weburls) {
		List<Document> result = new ArrayList<Document>();
		for(String weburl : weburls) {
			try {
				Document doc = Jsoup.connect(weburl).get();
				result.add(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

}
