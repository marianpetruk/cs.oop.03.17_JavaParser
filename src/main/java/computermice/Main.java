package computermice;

import java.io.IOException;


public class Main {
    static int numberOfMice = 0;
    static int average = 0;
    static int sum = 0;
    static int number = 0;
    static int topNum = 0;
    static int topAverage = 0;
    static String bestMouse = "";


    public static void main(String[] args) throws IOException {
        System.out.println("Start (please wait, a lot of data to parse)");
        String url = "https://hard.rozetka.com.ua/ua/mouses/c80172/";
        new ParseCategory(url);
        System.out.println(numberOfMice + "mice are available.");
        System.out.println("\nThis is the most popular mouse (based on ratings) among all that are available on rozetka.com.ua " + bestMouse.replace("/comments/", ""));
        System.exit(1);
    }
}
