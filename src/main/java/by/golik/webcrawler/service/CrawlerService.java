package by.golik.webcrawler.service;

import by.golik.webcrawler.exception.CrawlerException;
import by.golik.webcrawler.model.Crawler;

import java.io.IOException;
import java.net.URL;

/**
 * @author Nikita Golik
 */
public interface CrawlerService {
    Crawler crawlURL(URL rootUrl, int maxDepth) throws IOException, CrawlerException;
}
