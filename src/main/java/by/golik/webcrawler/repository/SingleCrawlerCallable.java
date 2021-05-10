package by.golik.webcrawler.repository;

import by.golik.webcrawler.job.Statistics;
import by.golik.webcrawler.job.TermsStaticticParser;
import by.golik.webcrawler.model.SingleCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Nikita Golik
 */
public class SingleCrawlerCallable  implements Callable<SingleCrawler> {
    private final static Logger LOGGER = LoggerFactory.getLogger(SingleCrawlerCallable.class);

    private int timeoutInMillis;
    private URL url;

    public SingleCrawlerCallable(URL url, int timeoutInMillis) {
        this.url = url;
        this.timeoutInMillis = timeoutInMillis;
    }

    @Override
    public SingleCrawler call() throws Exception {
        LOGGER.info("Crawling url {}", url.toString());

        Document document = null;
        try {
            //Используйте статический Jsoup.parse(String html)метод или, Jsoup.parse(String html, String baseUri)если
            // страница пришла из Интернета, и вы хотите получить абсолютные URL-адреса
            document = Jsoup.parse(url, timeoutInMillis);
        } catch (IOException e) {
            LOGGER.warn("Problem accessing url {}", String.valueOf(url));
            throw new IOException("Problem accessing url " + String.valueOf(url));
        }
        //селектор на поиск названий
        Elements titles = document.select("title");
        //селектор на поиск юрл адресов
        Elements links = document.select("a[href]");
        // список юрл адресов
        List<URL> linkList = new ArrayList<>();

        // проходим по всему списку юрл адресов
        for (Element link : links) {

            // abs:префикс атрибута для разрешения абсолютного URL-адреса из атрибута:
            String linkString = link.attr("abs:href");
            try {
                    //создаем юрл из полученного адреса
                    URL linkURL = new URL(linkString);
                    //todo
//                if(linkList.size() < 100)
                    // добавляем в список юрлов
                    linkList.add(linkURL);
            } catch (MalformedURLException e) {
                LOGGER.info("skipping url: {}", linkString);
                continue;
            }

        }
        //выводим результаты
        new TermsStaticticParser(url).collect(document);
        // возвращаем объект синглКраулера
        return new SingleCrawler(url, titles == null || titles.size() == 0 ? "" : titles.get(0).text(), linkList);
    }

}
