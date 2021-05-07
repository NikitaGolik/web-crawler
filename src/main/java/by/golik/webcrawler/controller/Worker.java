package by.golik.webcrawler.controller;

import by.golik.webcrawler.repository.CrawlerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Nikita Golik
 */
//public class Worker implements Callable {
//
//    public final CrawlerRepository crawlerRepository;
//
//    @Autowired
//    public Worker(CrawlerRepository crawlerRepository) {
//        this.crawlerRepository = crawlerRepository;
//    }
//
//    public static final int TIMEOUT = 60000;
//    private static final Logger logger = LogManager.getLogger(Worker.class);
//    private URL url;
//    private int depth;
//    private final Set<URL> urlList = new HashSet<>();
//
//    public Worker(URL url, int depth) {
//        this.url = url;
//        this.depth = depth;
//    }
//
//
//    /**
//     * метод который непосредственно делает всю работу
//     * @return
//     */
//    @Override
//    public Object call() {
//        logger.info("URL received " + url +"----" + Thread.currentThread().getName());
//
//        Document doc = null;
//        try {
//            //Это основной способ получения объекта Document, Это представление нашей html страницы
//            doc = Jsoup.parse(url, TIMEOUT);
//        } catch (IOException e) {
//            logger.error("Error while parsing: " + url);
//        }
//        if(doc != null) {
//            // передаем селектор. так как линки в html начинаются с href
//            doc.select("a[href]").forEach(link -> {
//                //получение значения атрибута href
//                String new_link =link.attr("abs:href");
//                //если линк не пустой и не начинается с решетки и не начинается с javascript
//                if(!new_link.isBlank() && !new_link.startsWith("#") && !new_link.startsWith("javascript")) {
//                try {
//                    //тогда мы создаем новый юрл
//                    URL nextURL = new URL(url, new_link);
//                    // и добавляем его в список юрлов
//                    urlList.add(nextURL);
//                } catch (MalformedURLException e) {
//                    logger.error("URL with error: " + url);
//                }
//            }
//        });
//        }
//        return this;
//    }
//
//    public Set<URL> getUrlList() {
//        return urlList;
//    }
//
//    public int getDepth() {
//        return depth;
//    }
// }
