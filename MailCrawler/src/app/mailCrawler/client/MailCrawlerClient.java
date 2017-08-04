package app.mailCrawler.client;

import java.util.List;
import java.util.Map;

import app.mailCrawler.EmailExtracter;

public class MailCrawlerClient {

	public static void main(String[] args) {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
		
		EmailExtracter extracter = new EmailExtracter();
		try {
			Map<String,List<String>> emails = extracter.findMails(2015);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
