package by.golik.webcrawler.controller;

import by.golik.webcrawler.service.SimpleCrawler;
import by.golik.webcrawler.statistics.StatisticsShower;

import java.io.IOException;

/**
 * @author Nikita Golik
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new SimpleCrawler().crawl();
        StatisticsShower.writeAll();
        StatisticsShower.writeTenFirst();
    }
}
