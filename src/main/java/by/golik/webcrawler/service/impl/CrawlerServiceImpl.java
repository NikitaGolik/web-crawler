package by.golik.webcrawler.service.impl;

import by.golik.webcrawler.exception.CrawlerException;
import by.golik.webcrawler.model.Crawler;
import by.golik.webcrawler.model.SingleCrawler;
import by.golik.webcrawler.repository.SingleCrawlerCallable;
import by.golik.webcrawler.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static by.golik.webcrawler.service.util.CrawlerUtil.isValidURL;
import static java.util.Collections.singletonList;

/**
 * @author Nikita Golik
 */
@Service
@EnableCaching
public class CrawlerServiceImpl implements CrawlerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CrawlerServiceImpl.class);
    // Creates a new {@link Set} backed by a ConcurrentHashMap
    //      from the given type to {@code Boolean.TRUE}.
    private Set<URL> urlSet = ConcurrentHashMap.newKeySet();

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Value("${CRAWLER_TIMEOUT_MILLIS}")
    private int timeoutInMillis;


    /**
     * crawl
     * @param url
     * @param maxDepth
     * @return
     */
    public Object crawl(URL url, int maxDepth){
        Crawler crawlResult = null;
        try {
            return crawlURL(url, maxDepth);
        }catch (Exception e){
            LOGGER.error("Error processing URL: {}", url);
        }

        return crawlResult;
    }

    /**
     * crawlURL
     * @param url
     * @param maxDepth
     * @return
     * @throws IOException
     */
    @Override
    public Crawler crawlURL(URL url, int maxDepth) throws IOException, CrawlerException {
        LOGGER.warn("Starting to crawl: {}", url);
        //проверяем на валидность
        if (!isValidURL(url)) {
            LOGGER.warn("Invalid URL: {}", url);
            throw new CrawlerException("Url must be valid");
        }
        //Returns an immutable list containing only the specified object.
        //  The returned list is serializable.
        Crawler crawler = crawlURL(singletonList(url), maxDepth);
        //очистка сета
        urlSet.clear();
        //возвращаем паука
        return crawler;
    }

    /**
     * Crawl URL
     * @param urls
     * @param depth
     * @return
     * @throws IOException
     */
    private Crawler crawlURL(List<URL> urls, int depth) throws IOException {
        //проверка на глубину
        if (depth <= 0) {
            return null;
        }
        // список пауков из потока
        List<Future<SingleCrawler>> singleCrawlerFutures = new ArrayList<>();
        // пробегаемся по всему списку юрлов
        for (URL url : urls) {
            // проверка юрла на валидность и на уникальность
            if (!isValidURL(url) || urlSet.contains(url)) {
                LOGGER.info("Not a valid URL: {}. Skipping...", String.valueOf(url));
                continue;
            }
            //добавляем в сет
            urlSet.add(url);
            // получаем объект из потока
            SingleCrawlerCallable singleCrawlerCallable = new SingleCrawlerCallable(url, timeoutInMillis);
            //добавляем новый поток для каждого юрла
            Future<SingleCrawler> singleCrawlerFuture = executorService.submit(singleCrawlerCallable);
            //добавляем в список
            singleCrawlerFutures.add(singleCrawlerFuture);
        }

        //пробегаем по всему списку фючер
        for (Future<SingleCrawler> singleCrawlerFuture : singleCrawlerFutures) {
            SingleCrawler singleCrawler = null;
            try {
                //получаем объект паука
                singleCrawler = singleCrawlerFuture.get();
            } catch (ExecutionException | InterruptedException  e) {
                LOGGER.error("[ERROR] Can not process the URL {}", String.valueOf(singleCrawler.getUrl()));
            }
            //создаем паку на основе симпл паука
            Crawler crawler = new Crawler(singleCrawler.getUrl().toString(), singleCrawler.getTitle(), new ArrayList<>());
            //возвращаем линки поулченные после перехода со страниц
            return crawlChildURLs(crawler, singleCrawler.getLinks(), depth - 1);
        }

        return null;
    }

    /**
     * Crawl Children
     * @param crawler
     * @param linkUrls
     * @param depth
     * @return
     * @throws IOException
     */
    private Crawler crawlChildURLs(Crawler crawler, List<URL> linkUrls, int depth) throws IOException {

        // проходим по спику юрлов
        for (URL linkUrl : linkUrls) {
                //отсекаем невалидные
            if (!isValidURL(linkUrl) || urlSet.contains(linkUrl)) {
                LOGGER.warn("URL is not valid:  {}", String.valueOf(linkUrl));
                continue;
            }
            // добавляем юрл в сет, так как сет содержит в себе только уникальные ссылки, а нас интереусют именно
            // уникальные ссылки
            urlSet.add(linkUrl);
            //количество юрлов
                //создаем объект класса callable, так как задачу будем выполнять в потоках
                SingleCrawlerCallable singleCrawlerCallable = new SingleCrawlerCallable(linkUrl, timeoutInMillis);
                //добавляем новый поток для каждого юрла
                Future<SingleCrawler> singleCrawlerFuture = executorService.submit(singleCrawlerCallable);
                try {
                    //получаем объект из потока
                    SingleCrawler singleCrawler = singleCrawlerFuture.get();
                    // создаем объект на основе полученного из потока
                    Crawler crawlerChild = new Crawler(singleCrawler.getUrl().toString(),
                            singleCrawler.getTitle(), new ArrayList<>());
                    //получаем все ссылки из полученного объекта
                    Crawler c = crawlURL(singleCrawler.getLinks(), depth);

                    if (c != null)
                        //добавляем ссылки
                        crawlerChild.getNodes().add(c);
                    // добавляем ссылки
                    crawler.getNodes().add(crawlerChild);
                } catch (ExecutionException | InterruptedException io) {
                    LOGGER.error("[ERROR] Can not process the URL {}", String.valueOf(linkUrl));
                    continue;
                }

        }
        return crawler;
    }

}
