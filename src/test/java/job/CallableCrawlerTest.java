package job;

import by.golik.webcrawler.job.CallableCrawler;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Nikita Golik
 */
public class CallableCrawlerTest {

    @Test
    public void testModelCreation() throws MalformedURLException {

        URL actualURL = new URL("https://en.wiskipedia.org/wiki/Elon_Musk");
        int currentDepth = 10;
        CallableCrawler callableCrawler = new CallableCrawler(actualURL, currentDepth);

        Assert.assertNotNull(callableCrawler);
        Assert.assertEquals(currentDepth, callableCrawler.getDepth());
    }

}
