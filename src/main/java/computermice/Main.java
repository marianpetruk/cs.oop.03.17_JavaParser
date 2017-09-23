package computermice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Main {
    static int numberOfMice = 0;
    static int average = 0;
    static int sum = 0;
    static int number = 0;
    static int topNum = 0;
    static int topAverage = 0;
    static String bestMouse = "";

    public static void parse_category(String url) throws IOException {
        // creating a folder for our csv data
        File file = new File("Data");
        file.mkdir();

        // getting links to all pages of the chosen (in url) category
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.getElementsByClass("paginator-catalog-l-link");

        List<String> pages = new ArrayList<String>();

        for (Element link : links) {
            pages.add(link.text());
        }

        int num = Integer.parseInt(pages.get(pages.size() - 1));

        for (int i = 0; i < num; i++) {
            String page = url + String.format("page=%d", i + 1);
            parse_category_page(page);
        }


    }

    public static void parse_category_page(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements tiles = doc.getElementsByClass("g-i-tile-i-title");
        for (Element tile : tiles) {
            Element link = tile.getElementsByTag("a").first();
            numberOfMice += 1;
            parse_reviews(link.attr("href") + "comments/");
        }
    }

    private static void parse_reviews(String url) throws IOException {
        int num = 0;
        String pg = "";
        Document doc = Jsoup.connect(url).get();
        String filename = "Data/" + url.split("/")[4] + ".csv";
        boolean fileExists = new File(filename).exists();

        Elements links = doc.getElementsByClass("paginator-catalog-l-link");

        List<String> pages = new ArrayList<String>();

        for (Element link : links) {
            pages.add(link.text());
        }

        if (pages.size() > 0) {
            num = Integer.parseInt(pages.get(pages.size() - 1));
        }

        ArrayList<ArrayList<ArrayList<String>>> sentiments = new ArrayList<ArrayList<ArrayList<String>>>();

        for (int i = 0; i < num; i++) {
            pg = url + String.format("page=%d", i + 1);
            sentiments.add(parse_reviews_page(pg));
        }
        System.out.println("start writing csv");


        if (!fileExists) {
            PrintWriter printWriter = new PrintWriter(new File(filename));
            StringBuilder stringBuilder = new StringBuilder();

            for (ArrayList<ArrayList<String>> str : sentiments) {
                String strForAppend = "";
                for (int i = 0; i < str.size(); i++) {
                    strForAppend += str.get(i).get(0) + "," + str.get(i).get(1) + "\n";
                    sum += Integer.parseInt(str.get(i).get(0));
                    number += 1;
                }
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
            for (ArrayList<ArrayList<String>> str : sentiments) {
                for (int i = 0; i < str.size(); i++) {
                    sum += Integer.parseInt(str.get(i).get(0));
                    number += 1;
                }
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


    private static ArrayList<ArrayList<String>> parse_reviews_page(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements reviews = doc.select("article.pp-review-i");
        ArrayList<ArrayList<String>> sentiments = new ArrayList<ArrayList<String>>();
        Elements texts;

        for (Element review : reviews) {
            Elements star = review.select("span.g-rating-stars-i");
            Elements text = review.select("div.pp-review-text");

            if (!star.attr("content").equals("")) {
                texts = text.select("div.pp-review-text-i");
                ArrayList<String> content = new ArrayList<String>();
                content.add(star.attr("content"));
                content.add(texts.first().text());
                sentiments.add(content);
            }
        }
        return sentiments;
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Start (please wait, a lot of data to parse)");
        String url = "https://hard.rozetka.com.ua/ua/mouses/c80172/";
        parse_category(url);
        System.out.println(numberOfMice + "mice are available.");
        System.out.println("\nThis is the most popular mouse (based on ratings) among all that are available on rozetka.com.ua " + bestMouse.replace("/comments/", ""));
        System.exit(1);
    }
}
