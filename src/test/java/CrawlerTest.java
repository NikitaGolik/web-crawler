import by.golik.webcrawler.service.SimpleCrawler;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;


/**
 * @author Nikita Golik
 */
public class CrawlerTest {

    private final URL startURL = new URL("https://en.wikipedia.org/wiki/Elon_Musk");

    private SimpleCrawler simpleCrawler;

    public CrawlerTest() throws MalformedURLException {
    }

    @Before
    public void setUp() throws MalformedURLException {
        simpleCrawler = new SimpleCrawler();
    }

    @Test
    public void checkTest() {
        assertTrue(simpleCrawler.checkLinks(startURL, 1));
    }

}
