package app.mailCrawler;

import java.util.logging.Logger;
import java.util.regex.*;
import java.net.*;
import java.util.*;

public class EmailExtracter {

	private static final Logger LOGGER = Logger.getLogger(EmailExtracter.class
			.getName());

	private HtmlUrlReader htmlReader;

	public EmailExtracter() {
		this.htmlReader = new HtmlUrlReader();
	}

	public Map<String, List<String>> findMails(int year) throws Exception {
		URL url = new URL(AppConstants.BASE_URL);
		final List<String> rows = htmlReader.read(url);
		final List<String> months = new ArrayList<>();
		
		// find all monthly links for the given year
		rows.stream().parallel().forEach(row -> {
			findMonthLinks(year, row, months);
		});

		rows.clear();

		//find all email links monthly
		Map<String, List<String>> mailsByMonth = new HashMap<String, List<String>>();
		processMonthlyLinks(rows, months, mailsByMonth);

		for (Map.Entry<String, List<String>> monthlyData : mailsByMonth
				.entrySet()) {
			LOGGER.info(String.format("Month[%s] has [%s] mails.",
					monthlyData.getKey(), monthlyData.getValue().size()));
		}

		rows.clear();
		//finding email data for each email link.
		Map<String, List<String>> mails = new HashMap<String, List<String>>();
		processEmailLinks(mailsByMonth, mails);
		return mails;

	}

	private void processMonthlyLinks(List<String> rows, List<String> months,
			Map<String, List<String>> mailsByMonth) {
		months.stream()
				.forEach(
						month -> {

							try {
								rows.addAll(htmlReader.read(new URL(
										String.format(AppConstants.MONTHLY_URL,
												month))));
								List<String> mailMonthlyPageUrls = getEmailByMonthPages(
										rows, month);
								mailsByMonth.put(month, mailMonthlyPageUrls);
							} catch (Exception e) {
								e.printStackTrace();
							}

						});
	}

	private void processEmailLinks(Map<String, List<String>> mailsByMonth,
			Map<String, List<String>> mails) {
		mailsByMonth
				.entrySet()
				.stream()
				.parallel()
				.forEach(
						monthlyData -> {
							if (!mails.containsKey(monthlyData.getKey())) {
								mails.put(monthlyData.getKey(),
										new ArrayList<String>());
							}
							StringBuilder mailContent = new StringBuilder();
							monthlyData
									.getValue()
									.stream()
									.parallel()
									.forEach(
											mailLink -> {
												try {
													mailContent.append(htmlReader.read(new URL(
															String.format(
																	AppConstants.MAIL_URL,
																	monthlyData
																			.getKey(),
																	mailLink))));
												} catch (Exception e) {
													e.printStackTrace();
												}
											});
							mails.get(monthlyData.getKey()).add(
									mailContent.toString());

						});

	}

	private void findMonthLinks(int year, String row, List<String> links) {
		if (row.contains("<a href=\"" + year)) {
			links.add(row.substring(row.indexOf("id=") + 4,
					row.indexOf("id=") + 10));
		}
	}

	private List<String> getEmailByMonthPages(List<String> rows, String month)
			throws Exception {
		int count = 0;
		List<String> mailMonthlyPages = new ArrayList<String>();
		for (String row : rows) {
			if (row.contains("<a href=\"/mod_mbox/maven-users/" + month
					+ ".mbox/date?")) {
				count++;
			}
		}
		if (count == 0)
			count = 1;
		for (int i = 0; i < count; i++) {
			mailMonthlyPages.addAll(htmlReader.read(new URL(String.format(
					AppConstants.MONTHLY_URL_PAGE, month, i))));
		}
		List<String> emailLinks = getMail(mailMonthlyPages);

		return emailLinks;

	}

	private List<String> getMail(List<String> rows) {

		List<String> mailIds = new ArrayList<>();
		for (String row : rows) {
			if (row.contains("class=\"subject\"><a href=")) {
				mailIds.add(findMailLink(row));
			}
		}
		return mailIds;
	}

	private String findMailLink(String row) {
		Pattern p = Pattern.compile("href=\"(.*?)\"", Pattern.DOTALL);
		Matcher m = p.matcher(row);
		String link = "";

		while (m.find()) {
			link = m.group(1);
		}
		return link.replace("%3c", "").replace("%3e", "");
	}
}
