package app.mailCrawler;
import java.net.URL;
import java.util.List;


public interface UrlReader {
	
	public List<String> read(URL url); 

}
