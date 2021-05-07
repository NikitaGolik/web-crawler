package by.golik.webcrawler.controller;

import by.golik.webcrawler.exception.CrawlerException;
import by.golik.webcrawler.job.Statistics;
import by.golik.webcrawler.model.Crawler;
import by.golik.webcrawler.service.impl.CrawlerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * @author Nikita Golik
 */
@RestController
@EnableCaching
public class CrawlerController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CrawlerController.class);

    @Autowired
    private CrawlerServiceImpl crawlerService;

    @Value("${maxDepth}")
    private int MAX_DEPTH;


    /**
     * Web Crawler GET Endpoint
     * @param url - URL to crawl
     * @param maxDepth - Optional Parameter
     * @return
     * @throws IOException
     */
    @RequestMapping("/web-crawler")
    @ResponseBody
    public HttpEntity<Crawler> crawl(@RequestParam(value = "url") URL url,
                                     @RequestParam("maxDepth") Optional<Integer> maxDepth) throws IOException, CrawlerException {

        LOGGER.info("[CRAWL-START] URL = {}", url);

        if (maxDepth.isPresent())
            LOGGER.info("maxDepth = {}", maxDepth.get());

        Object crawler = crawlerService.crawl(url, maxDepth.isPresent() ? maxDepth.get() : MAX_DEPTH);

        LOGGER.info("[CRAWL-COMPLETED] URL = {} ", url);
        return crawlResult(url, maxDepth);

    }

    /**
     * Result for Crawl
     *
     */
    @Cacheable("webcrawlResult" )
    public ResponseEntity crawlResult(URL url, Optional<Integer> maxDepth) throws IOException {
        Object crawler = crawlerService.crawl(url, maxDepth.isPresent() ? maxDepth.get() : MAX_DEPTH);
        Statistics.writeAll();
        Statistics.writeTenFirst();
        return new ResponseEntity<>(crawler, HttpStatus.OK);
    }
}
