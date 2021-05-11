package by.golik.webcrawler.controller;

import by.golik.webcrawler.service.SimpleCrawler;
import by.golik.webcrawler.statistics.CrawlStatShower;

import java.io.IOException;

/**
 * Class-runner for application
 * @author Nikita Golik
 */
public class Runner {

    public static void main(String[] args) throws IOException, InterruptedException {
        new SimpleCrawler().crawl();
        CrawlStatShower.writeAll();
        CrawlStatShower.writeTenFirst();
    }
}
