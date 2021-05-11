package by.golik.webcrawler.statistics;

import org.jsoup.nodes.Document;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

public class TermsStaticticParser {
    private URL url;

    public TermsStaticticParser(URL url) {
        this.url = url;
    }

    public void collect(Document document) {
        if (document != null) {
            String text = document.body().text().toLowerCase();
            StatisticsGetter statisticsGetter = new StatisticsGetter(url);
            Map<String, Integer> statistics = statisticsGetter.getStatistics();


            statistics.keySet().forEach(term -> {
                statistics.put(term, (int) Pattern.compile(term.toLowerCase()).matcher(text).results().count());
            });
            StatisticsShower.RECORDS.add(statisticsGetter);

        } else {
            StatisticsShower.RECORDS.add(new StatisticsGetter(url));

        }

    }

}
