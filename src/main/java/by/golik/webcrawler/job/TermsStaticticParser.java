package by.golik.webcrawler.job;

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
            StatisticsRecord statisticsRecord = new StatisticsRecord(url);
            Map<String, Integer> statistics = statisticsRecord.getStatistics();

            statistics.keySet().stream().forEach(term -> {
                statistics.put(term, (int) Pattern.compile(term.toLowerCase()).matcher(text).results().count());
            });
            Statistics.RECORDS.add(statisticsRecord);
        } else {
            Statistics.RECORDS.add(new StatisticsRecord(url));
        }

    }

}
