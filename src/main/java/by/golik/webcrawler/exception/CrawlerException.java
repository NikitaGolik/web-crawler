package by.golik.webcrawler.exception;

/**
 * @author Nikita Golik
 */
public class CrawlerException extends Exception {
    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrawlerException(Throwable cause) {
        super(cause);
    }
}
