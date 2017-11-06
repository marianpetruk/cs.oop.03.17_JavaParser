package computermice;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class ParseReviewsPage extends ArrayList<ArrayList<String>> {
    ParseReviewsPage(String url, List<List<String>> sentiments) throws IOException {

        String selector = "article.pp-review-i";
        PageParser pageParser = new PageParser(url, selector);
        Elements reviews = pageParser.getElements();
        Elements texts;

        for (Element review : reviews) {
            Elements star = review.select("span.g-rating-stars-i");
            Elements text = review.select("div.pp-review-text");

            if (!star.attr("content").equals("")) {
                texts = text.select("div.pp-review-text-i");
                List<String> content = new ArrayList<String>();
                content.add(star.attr("content"));
                content.add(texts.first().text());
                sentiments.add(content);
            }
        }
    }
}