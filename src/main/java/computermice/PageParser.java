package computermice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

class PageParser {
    private Elements elements;

    PageParser(String url, String selector) throws IOException {
        Document doc = Jsoup.connect(url).get();
        elements = doc.select(selector);
    }

    Elements getElements() {
        return elements;
    }
}