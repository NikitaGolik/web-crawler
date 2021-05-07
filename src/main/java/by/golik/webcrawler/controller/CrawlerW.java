package by.golik.webcrawler.controller;

import by.golik.webcrawler.job.PropertiesLoader;
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

/**
 * @author Nikita Golik
 */
//public class CrawlerW {
//
//    private static final Logger logger = LogManager.getLogger(CrawlerW.class);
//
//    private final int maxDepth;
//    private final int maxUrls;
//    private final Set<URL> masterList = new HashSet<>();
//    private final List<Future<Worker>> futures = new ArrayList<>();
//    private final ExecutorService executorService = Executors.newWorkStealingPool();
//    private final URL start;
//
//    public CrawlerW() throws MalformedURLException {
//        this.maxDepth = PropertiesLoader.MAX_DEPTH;;
//        this.maxUrls = PropertiesLoader.MAX_URL;
//        this.start = new URL(PropertiesLoader.START_URL);
//    }
//
//    public void start() throws InterruptedException {
//        submitNewUrl(start, 0);
//        while (isReadyPages());
//        logger.info("Found " + masterList.size() + " links");
//        System.out.println();
//    }
//
//    /**
//     * В этом методе идет проверка страницы
//     * @return
//     * @throws InterruptedException
//     */
//    private boolean isReadyPages() throws InterruptedException {
//        //останавливаем поток
//        Thread.sleep(1000);
//        Set<Worker> pageSet = new HashSet<>();
//        Iterator<Future<Worker>> iterator = futures.iterator();
//
//        while (iterator.hasNext()) {
//            Future<Worker> future = iterator.next();
//            if(future.isDone()) {
//                iterator.remove();
//                try {
//                    pageSet.add(future.get());
//                } catch (InterruptedException e) {
//
//                } catch (ExecutionException e) {
//
//                }
//            }
//        }
//        for(Worker worker : pageSet) {
//            addNewUrls(worker);
//        }
//        return !futures.isEmpty();
//    }
//
//    /**
//     * В этом методе мы находим все линки, которые есть на странице
//     * @param worker
//     */
//    private void addNewUrls(Worker worker) {
//
//        for(URL url : worker.getUrlList()) {
//            // если в юрл линке есть решетка
//            if(url.toString().contains("#")) {
//                try {
//                    // извлекаем подстроку из линка, сепаратор решетка
//                    url = new URL(StringUtils.substringBefore(url.toString(), "#"));
//                } catch (MalformedURLException e) {
//
//                }
//            }
//
//            submitNewUrl(url, worker.getDepth() + 1);
//        }
//
//    }
//
//    /**
//     * В этом методе адреса, которые нам подходят, добавляются в задание на будущее
//     * @param url
//     * @param depth
//     */
//    private void submitNewUrl(URL url, int depth) {
//        // проверяем на критерии, указанные в методе toVisit
//        if(toVisit(url, depth)) {
//            masterList.add(url);
//            Worker worker = new Worker(url, depth);
//            Future<Worker> future = executorService.submit(worker);
//            futures.add(future);
//        }
//    }
//
//    /**
//     * В этом методе я проверяю линки
//     * @param url
//     * @param depth
//     * @return
//     */
//    private boolean toVisit(URL url, int depth) {
//        // если такой адрес у меня уже есть, идем дальше и не проверяем
//        if(masterList.contains(url)) {
//            return false;
//        }
//
//        // если адрес оканчивается на следующие значения, идем дальше и не проверяем
//        String[] extn = {".txt", ".doc", ".pdf", ".jpg", ".gif", ".png"};
//        if(Arrays.stream(extn).anyMatch(entry -> url.toString().endsWith(entry))) {
//            return false;
//        }
//        //если глубина больше установленной, дальше не идем
//        if (depth > maxDepth) {
//            return false;
//        }
//        //не проходим больше чем на указанное количество адресов
//        return masterList.size() < maxUrls;
//    }
//
//}
