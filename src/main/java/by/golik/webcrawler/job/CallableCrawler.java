package by.golik.webcrawler.job;

import by.golik.webcrawler.statistics.TermsTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Nikita Golik
 */
public class CallableCrawler implements Callable<CallableCrawler> {

    public static final int TIMEOUT = 60000;
    private static final Logger logger = LogManager.getLogger(CallableCrawler.class);
    // url address
    private final URL url;
    // depth
    private final int depth;
    // received set from url addresses
    private final Set<URL> urlSet = new HashSet<>();

    public CallableCrawler(URL url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    /**
     * This method-parser of html page
     * @return - object of CrawlerCallable
     */
    @Override
    public CallableCrawler call() {
        logger.info("Crawling url {} {} {}", depth, url, Thread.currentThread().getName());

        Document doc = null;
        try {
            // get absolute links on the page
            doc = Jsoup.parse(url, TIMEOUT);
        } catch (IOException e) {

            logger.error("Problem accessing url {}", url);
        }
        if (doc != null) {
            //selector like CSS (or jquery) for searching links
            doc.select("a[href]").forEach(link -> {
                //absolute url permissions from attribute:
                        String href = link.attr("abs:href");
                        // if the link is not empty, and if it is not javascript code
                        if (!href.isBlank() && !href.startsWith("#") && !href.startsWith("javascript")) {
                            try {
                                // create a url from the resulting absolute url
                                URL nextUrl = new URL(url, href);
                                // add to the set
                                urlSet.add(nextUrl);
                            } catch (MalformedURLException e) {
                                logger.error("Bad URL: {}", href);
                            }
                        }
                    }
            );
        }

        new TermsTransformer(url).collect(doc);

        return this;
    }

    public Set<URL> getUrlSet() {
        return urlSet;
    }

    public int getDepth() {
        return depth;
    }


}
