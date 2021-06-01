package by.golik.webcrawler.statistics;

import org.jsoup.nodes.Document;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Nikita Golik
 * Class which works with URL addresses to get all mentions of key words
 */
public class TermsTransformer {
    private final URL url;

    public TermsTransformer(URL url) {
        this.url = url;
    }

    /**
     * This method gets absolute links on the page and finds all mentions on the web site
     * @param document - absolute links on the page
     */
    public void collect(Document document) {
        if (document != null) {
            String text = document.body().text().toLowerCase();
            CrawlStatGetter crawlStatGetter = new CrawlStatGetter(url);
            Map<String, Integer> statistics = crawlStatGetter.getStat();

            statistics.keySet().forEach(term -> {
                statistics.put(term, (int) Pattern.compile(term.toLowerCase()).matcher(text).results().count());
            });
            CrawlStatShower.RECORDS.add(crawlStatGetter);

        } else {
            CrawlStatShower.RECORDS.add(new CrawlStatGetter(url));
        }

    }

}
