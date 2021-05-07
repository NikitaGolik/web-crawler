package by.golik.webcrawler.service.util;

import java.net.URL;

/**
 * @author Nikita Golik
 */
public class CrawlerUtil {
    public final static boolean isValidURL(URL url) {
        return url != null
                && ( url.getProtocol().startsWith("http") || url.getProtocol().startsWith("https") )
                && !url.getProtocol().endsWith("#");
    }
}
