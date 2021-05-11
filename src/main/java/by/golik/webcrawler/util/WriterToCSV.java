package by.golik.webcrawler.util;

import by.golik.webcrawler.statistics.CrawlStatGetter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Nikita Golik
 * Class-writer statistic to the file
 */
public class WriterToCSV {

    public static void write(List<CrawlStatGetter> result, String filePath) throws IOException {

        // get terms
        String[] headers = Stream.of(PropertiesLoader.TERMS.split(","))
                .map(String::strip)
                .toArray(String[]::new);

        try (FileWriter out = new FileWriter(filePath);
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
            //add result mentions
            for (CrawlStatGetter record : result) {
                List<String> csvResult = new ArrayList<>();
                csvResult.add(record.getUrl().toString());
                List<String> termsStatistic = record.getStat().values().stream().map(Object::toString).collect(Collectors.toList());
                csvResult.addAll(termsStatistic);

                printer.printRecord(csvResult);
            }
        }
    }

}
