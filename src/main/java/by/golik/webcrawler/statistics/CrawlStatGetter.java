package by.golik.webcrawler.statistics;

import by.golik.webcrawler.util.PropertiesLoader;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Nikita Golik
 * Class counts all mentions of key words and saves it
 */
public class CrawlStatGetter {
    private final URL url;
    /**
     * statistics about mentions on the site
     */
    private final Map<String, Integer> stat;


    /**
     * Constructor
     * @param url - url address
     */
    public CrawlStatGetter(URL url) {
        this.url = url;
        // get terms from property file, which splitted by ","
        this.stat = Stream.of(PropertiesLoader.TERMS.split(","))
                .collect(Collectors.toMap(String::strip, x -> 0, (e1, e2) -> e1, LinkedHashMap::new));

    }

    /**
     * Method provides statistics about mentions
     * @return String value of URL address
     */
    public String getShortStatistics() {
        return url.toString() + " " + stat.values().stream()
                .map(Object::toString).collect(Collectors.joining(" "));
    }

    public Map<String, Integer> getStat() {
        return stat;
    }

    /**
     * This method counts sum of mentions
     * @return sum of mentions
     */
    public Integer getTotalHits() {
        return stat.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public URL getUrl() {
        return url;
    }
}
