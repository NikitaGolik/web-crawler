package by.golik.webcrawler.service;

import by.golik.webcrawler.job.CrawlerCallable;
import by.golik.webcrawler.util.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimpleCrawler {
    private static final Logger logger = LogManager.getLogger(SimpleCrawler.class);

    // maximum depth, stored in the property
    private final int maxDepth;
    // max number of addresses
    private final int maxCountOfURLs;
    // final set of urls
    private final Set<URL> finalSetOfURLs = new HashSet<>();
    // result in the form of a list of urls received from the call stream
    private final List<Future<CrawlerCallable>> futures = new ArrayList<>();
    //Creates a work-stealing thread pool using the number of Runtime#availableProcessors available processors}
    // as its target parallelism level.
    private final ExecutorService executorService = Executors.newWorkStealingPool();
    // start page
    private final URL startURL;

    /**
     * Constructor
     * @throws MalformedURLException - Thrown to indicate that a malformed URL has occurred
     */
    public SimpleCrawler() throws MalformedURLException {
        this.maxDepth = PropertiesLoader.MAX_DEPTH;
        this.maxCountOfURLs = PropertiesLoader.MAX_URL;
        this.startURL = new URL(PropertiesLoader.START_URL);
    }

    /**
     * Start process for crawling pages
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied,
     * and the thread is interrupted, either before or during the activity.
     */
    public void crawl() throws InterruptedException {
        // add start address, start depth 0
        addNewURL(startURL, 0);
        while (checkPages()) {
            logger.info("Found {} urls", finalSetOfURLs.size());
            System.out.println();
        }
    }

    /**
     * This method checks pages for unfinished tasks
     * @return - true, if list of tasks is empty
     * @throws InterruptedException - Thrown when a thread is waiting, sleeping, or otherwise occupied,
     *  and the thread is interrupted, either before or during the activity.
     */
    private boolean checkPages() throws InterruptedException {
        Thread.sleep(1000);
        Set<CrawlerCallable> pageSet = new HashSet<>();
        // create an iterator over the result list from the stream
        Iterator<Future<CrawlerCallable>> iterator = futures.iterator();

        while (iterator.hasNext()) {
            // iterate over the entire list
            Future<CrawlerCallable> future = iterator.next();
            // returns true if this task is completed.
            if (future.isDone()) {
                // if this task is complete this method removes the current item in the collection.
                iterator.remove();
                try {
                    // adding outstanding tasks to set
                    pageSet.add(future.get());
                } catch (ExecutionException | InterruptedException e) {
                    logger.error("[ERROR] Can not process the URL {}", String.valueOf(future));
                }
            }
        }
        // adding unfinished tasks to parser
        for (CrawlerCallable crawlerCallable : pageSet) {
            addChildURL(crawlerCallable);
        }
        // false - if futures empty
        return !futures.isEmpty();
    }

    /**
     * This method checks pages for links and goes deep
     * @param crawlerCallable - object, which do parsing html web page in callable thread
     */
    private void addChildURL(CrawlerCallable crawlerCallable) {
        // go deep into the pages, check if there are links on the new pages
        for (URL url : crawlerCallable.getUrlSet()) {
            if (url.toString().contains("#")) {
                try {
                    // trying to get a fragment of url
                    url = new URL(StringUtils.substringBefore(url.toString(), "#"));
                } catch (MalformedURLException e) {
                    logger.info("skipping url: {}", url);
                }
            }
            // add the address and go deeper
            addNewURL(url, crawlerCallable.getDepth() + 1);
        }
    }

    /**
     * This method finds links on web-site and take result to new list of links
     * @param url - found link on the web-site
     * @param depth - follow links found to dive deeper
     */
    private void addNewURL(URL url, int depth) {
        if (checkLinks(url, depth)) {
            // add the address to the set after verification
            finalSetOfURLs.add(url);
            // we call the parser of the new received page
            CrawlerCallable crawlerCallable = new CrawlerCallable(url, depth);
            // getting the result from the thread
            Future<CrawlerCallable> future = executorService.submit(crawlerCallable);
            // add the result to the list
            futures.add(future);
        }
    }

    /**
     * This method checks getting links for unique and compliance with conditions
     * @param url - found link on the web-site
     * @param depth - follow links found to dive deeper
     * @return - true - if conditions are valid, another - false
     */
    private boolean checkLinks(URL url, int depth) {
        // if you have already been on the page - false
        if (finalSetOfURLs.contains(url)) {
            return false;
        }
        // we check that these are not links to documents and pictures
        String[] extn = {".txt", ".doc", ".pdf", ".jpg", ".gif", ".png"};
        if (Arrays.stream(extn).anyMatch(entry -> url.toString().endsWith(entry))) {
            return false;
        }
        // parse only to the specified depth
        if (depth > maxDepth) {
            return false;
        }
        // we get no more than the specified number of addresses
        return finalSetOfURLs.size() < maxCountOfURLs;
    }
}


