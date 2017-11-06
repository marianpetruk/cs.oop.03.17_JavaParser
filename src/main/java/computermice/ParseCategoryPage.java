package computermice;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static computermice.Main.*;


class ParseCategoryPage {
    ParseCategoryPage(String url) throws IOException {

        String selector = "div.g-i-tile-i-title";
        PageParser pageParser = new PageParser(url, selector);
        Elements titles = pageParser.getElements();

        for (Element title : titles) {
            Element link = title.getElementsByTag("a").first();
            numberOfMice += 1;
            new ParseReviews(link.attr("href") + "comments/");
        }
    }
}