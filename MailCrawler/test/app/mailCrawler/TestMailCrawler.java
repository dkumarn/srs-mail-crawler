package app.mailCrawler;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestMailCrawler {

	@Test
	public void testExtracter() throws Exception {
		EmailExtracter extracter = new EmailExtracter();
		Map<String, List<String>> emails = extracter.findMails(2015);
		assertThat(emails.get("201512").size(), is(104));
	}

}
