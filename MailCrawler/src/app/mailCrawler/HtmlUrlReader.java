package app.mailCrawler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HtmlUrlReader implements UrlReader {
	@Override
	public List<String> read(URL url) {
		List<String> rows = new ArrayList<>();
		try {
			BufferedReader bufferreader = new BufferedReader(
					new InputStreamReader(url.openStream()));
			String someString;
			while ((someString = bufferreader.readLine()) != null)
				rows.add(someString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

}
