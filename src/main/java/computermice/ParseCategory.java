package computermice;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ParseCategory {
    ParseCategory(String url) throws IOException {
        // creating a folder for our csv data
        File file = new File("Data");
        file.mkdir();

        // getting links to all pages of the chosen (in url) category

        String selector = ".paginator-catalog-l-link";
        PageParser pageParser = new PageParser(url, selector);
        Elements links = pageParser.getElements();

        List<String> pages = new ArrayList<String>();

        for (Element link : links) {
            pages.add(link.text());
        }

        int num = Integer.parseInt(pages.get(pages.size() - 1));

        for (int i = 0; i < num; i++) {
            String page = url + String.format("page=%d", i + 1);
            new ParseCategoryPage(page);
        }
    }
}