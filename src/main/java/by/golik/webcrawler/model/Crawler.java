package by.golik.webcrawler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

/**
 * @author Nikita Golik
 */
//Вам необходимо спроектировать и создать простой веб-сканер. Он должен принять URL-адрес в качестве параметра и создать
// дерево дочерних страниц, связанных с URL-адресом. Ожидается, что ваше приложение предоставляет решение для глубокого
// сканирования, что означает, что оно проходит через несколько уровней иерархии ссылок.
//
//Создайте простую конечную точку API, которая будет принимать URL-адрес в качестве параметра и возвращать json,
// представляющий дерево, описанное выше. Каждый узел должен иметь как минимум следующие поля: URL, заголовок, узлы.

public class Crawler {

    private String url;
    private String title;

    private List<Crawler> nodes;

    public Crawler() {
    }

    public Crawler(String url, String title, List<Crawler> nodes) {
        this.url = url;
        this.title = title;
        this.nodes = nodes;
    }
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public List<Crawler> getNodes() {
        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crawler crawler = (Crawler) o;
        return Objects.equals(url, crawler.url) &&
                Objects.equals(title, crawler.title) &&
                Objects.equals(nodes, crawler.nodes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, title, nodes);
    }

    @Override
    public String toString() {
        return "Crawler{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
