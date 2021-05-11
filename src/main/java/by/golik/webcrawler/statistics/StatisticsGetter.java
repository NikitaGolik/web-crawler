package by.golik.webcrawler.statistics;

import by.golik.webcrawler.util.PropertiesLoader;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticsGetter {
    private final URL url;
    private final Map<String, Integer> statistics;


    /**
     * Constructor
     * @param url - url address
     */
    public StatisticsGetter(URL url) {
        this.url = url;
        this.statistics = Stream.of(PropertiesLoader.TERMS.split(","))
                .collect(Collectors.toMap(String::strip, x -> 0, (e1, e2) -> e1, LinkedHashMap::new));

    }


    public String getShortStatistics() {
        return url.toString() + " " + statistics.values().stream()
                .map(Object::toString).collect(Collectors.joining(" "));
    }


    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    /**
     * This method counts sum of mentions
     * @return sum of mentions
     */
    public Integer getTotalHits() {
        return statistics.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public URL getUrl() {
        return url;
    }
}
