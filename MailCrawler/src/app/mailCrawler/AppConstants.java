package app.mailCrawler;

public interface AppConstants {
	String BASE_URL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
	String MONTHLY_URL = "http://mail-archives.apache.org/mod_mbox/maven-users/%s.mbox/date";
	String MONTHLY_URL_PAGE = "http://mail-archives.apache.org/mod_mbox/maven-users/%s.mbox/date?%s";
	String MAIL_URL = "http://mail-archives.apache.org/mod_mbox/maven-users/%s.mbox/<%s>";
}
