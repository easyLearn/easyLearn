package webparser;

import java.io.IOException;
import java.net.MalformedURLException;
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
				System.out.println("Weburl = " + weburl);
				Document doc = Jsoup.connect(weburl)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").timeout(5000)
					      .get();
				
//				System.out.println(doc.html()); // Mittelalter
				result.add(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	@Override
	public Document readUrl(String weburl) {
		// TODO Auto-generated method stub
		return null;
	}
}
