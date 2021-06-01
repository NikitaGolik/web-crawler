package util;

import by.golik.webcrawler.util.PropertiesLoader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Nikita Golik
 */
public class PropertiesLoaderTest {

    private final String terms = "Tesla, Musk, Gigafactory, Elon Mask";
    private final String startUrl = "https://en.wikipedia.org/wiki/Elon_Musk";
    private final int maxDepth = 2;
    private final int maxUrls = 10;

    private final String wrongTerms = "Tessla, Musak, Gigafactowry, Eleon Mask";
    private final String wrongStartUrl = "https://en.wiskipedia.org/wiki/Elon_Musk";
    private final int wrongMaxDepth = 12;
    private final int wrongMaxUrls = 3;

    @Test
    public void checkProperties() {
        assertEquals((int) PropertiesLoader.MAX_DEPTH, maxDepth);
        assertEquals((int) PropertiesLoader.MAX_URL, maxUrls);
        assertEquals(PropertiesLoader.TERMS, terms);
        assertEquals(PropertiesLoader.START_URL, startUrl);
    }

    @Test
    public void checkWrongProperties() {
        assertNotEquals((int) PropertiesLoader.MAX_DEPTH, wrongMaxDepth);
        assertNotEquals((int) PropertiesLoader.MAX_URL, wrongMaxUrls);
        assertNotEquals(PropertiesLoader.TERMS, wrongTerms);
        assertNotEquals(PropertiesLoader.START_URL, wrongStartUrl);
    }
}
