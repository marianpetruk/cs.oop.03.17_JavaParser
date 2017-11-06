package computermice;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static computermice.Main.*;

class ParseReviews {
    ParseReviews(String url) throws IOException {
        int num = 0;
        String pg = "";

        String selector = "a.paginator-catalog-l-link";
        PageParser pageParser = new PageParser(url, selector);
        Elements links = pageParser.getElements();
        String filename = "Data/" + url.split("/")[4] + ".csv";
        boolean fileExists = new File(filename).exists();

        List<String> pages = new ArrayList<String>();

        for (Element link : links) {
            pages.add(link.text());
        }

        if (pages.size() > 0) {
            num = Integer.parseInt(pages.get(pages.size() - 1));
        }

        List<List<String>> sentiments = new ArrayList<List<String>>();

        for (int i = 0; i < num; i++) {
            pg = url + String.format("page=%d", i + 1);
            new ParseReviewsPage(pg, sentiments);
        }
        System.out.println("start writing csv");


        if (!fileExists) {
            PrintWriter printWriter = new PrintWriter(new File(filename));
            StringBuilder stringBuilder = new StringBuilder();

            for (List<String> str : sentiments) {
                String strForAppend = "";
                strForAppend += str.get(0) + "," + str.get(1) + "\n";
                sum += Integer.parseInt(str.get(0));
                number += 1;
                stringBuilder.append(strForAppend);
            }
            printWriter.write(stringBuilder.toString());
            printWriter.close();
            System.out.println("done writing csv!");
            if (number != 0) average = sum / number;
            if (number > topNum && average > topAverage) {
                bestMouse = url;
                topNum = number;
                topAverage = average;
            }
            System.out.println(number + " reviews from " + url + "\n");
            number = 0;
            average = 0;
        } else {
            for (List<String> str : sentiments) {
                sum += Integer.parseInt(str.get(0));
                number += 1;
            }
            if (number != 0) average = sum / number;
            if (number > topNum && average > topAverage) {
                bestMouse = url;
                topNum = number;
                topAverage = average;
            }
            System.out.println(number + " reviews from " + url + "\n");
            number = 0;
            average = 0;
        }
    }
}