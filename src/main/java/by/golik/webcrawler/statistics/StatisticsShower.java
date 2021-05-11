package by.golik.webcrawler.statistics;


import by.golik.webcrawler.util.WriterToCSV;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static by.golik.webcrawler.util.WriterToCSV.write;


public class StatisticsShower {
    public static final List<StatisticsGetter> RECORDS = new CopyOnWriteArrayList<>();

    /**
     * This method finds TOP-10 by the number of occurrences
     * @return - list of top-10
     */
    public static List<StatisticsGetter> getTenFirst() {

        // sorting - by the number of occurrences
        return RECORDS.stream()
                .sorted(Comparator.comparingInt(StatisticsGetter::getTotalHits)
                        .reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * This method writes to file and console TOP-10 by the number of occurrences
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public static void writeTenFirst() throws IOException {
        // on console
        getTenFirst().stream().map(StatisticsGetter::getShortStatistics).forEach(System.out::println);
        // to file
        write(getTenFirst(), "top10.csv");
    }

    /**
     * Method for getting all resulting values
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public static void writeAll() throws IOException {
        WriterToCSV.write(RECORDS, "result.csv");
    }

}