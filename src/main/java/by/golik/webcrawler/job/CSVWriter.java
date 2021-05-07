package by.golik.webcrawler.job;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriter {

    public static void write(List<StatisticsRecord> result, String filePath) throws IOException {

        String[] headers = Stream.of(PropertiesLoader.TERMS.split(","))
                .map(String::strip)
                .toArray(String[]::new);

        try (FileWriter out = new FileWriter(filePath);
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {

            for (StatisticsRecord record : result) {
                List<String> csvResult = new ArrayList<>();
                csvResult.add(record.getUrl().toString());
                List<String> termsStatistic = record.getStatistics().values().stream().map(Object::toString).collect(Collectors.toList());
                csvResult.addAll(termsStatistic);

                printer.printRecord(csvResult);
            }
        }
    }

}
