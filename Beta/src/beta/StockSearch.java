package beta;

import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class StockSearch {

    //variables
    private final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private int userChoice;
    private String url;
    private String stockInfo = "";
    private String stockSymbol = "";
    private final String stockArrayName[] = {"Open", "High", "Low", "Mkt cap",
        "P/E ratio", "Div yield", "Prev close",
        "52-wk high", "52-wk low"};
    private double currentStockPrice;

    //scanner
    Scanner scanner = new Scanner(System.in);

    //mathod checks if user input is a stock
    public boolean checkIfStock(String SS) throws IOException {

        boolean isStock = false;

        String searchStock;
        searchStock = SS;

        //searches the google for the stock
        String searchURL = GOOGLE_SEARCH_URL + "?q=" + searchStock + "Stock" + "&num=1";
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        Elements results = doc.select("h3.r > a");
        String stockURL = results.attr("href");

        //copies the url to variable
        url = stockURL;

        //checks if the url contains finance
        if (url.contains("finance")) {
            isStock = true;
        }else{
            System.out.println("NOT A STOCK");
        }
        return isStock;
    }

    public String showStockInfo() throws IOException {

        String fullInfo;

        Document docOne = Jsoup.connect(url).get();

        // get Stock info
        Elements stockInf = docOne.getElementsByClass("iyjjgb");
        Elements stockSym = docOne.getElementsByClass("wx62f");
        Elements stockPrice = docOne.getElementsByClass("gsrt");

        // Get the stock price.
        // 1,000 -> needs to remove , to pass 1000
        String tempPrice = stockPrice.text();
        if (tempPrice.contains(",")) {
            tempPrice = tempPrice.replace(",", "");
        }

        //splits tempPrice and copies it to tempPriceArr
        String tempPriceArr[] = tempPrice.split(" ");

        //copies over the stock info text to variables
        stockInfo = stockInf.text();
        stockSymbol = stockSym.text();

        //splits info to arrays
        String arr[] = stockInfo.split(" ");
        fullInfo = stockSymbol + "\n$" + tempPriceArr[0] + "test";

        //formats the data and appends to full info
        for (int i = 0; i < arr.length; i++) {
            if (i < 7) {
                fullInfo += String.format("%-15s \t %3s", stockArrayName[i], arr[i]) + '\n';
            } else {
                fullInfo += String.format("%-15s %6s", stockArrayName[i], arr[i]) + '\n';
            }
            if (i == 4) {
                fullInfo += "second ";
            }
            if (i == 2) {
                System.out.println(" ");
            }
        }
        return fullInfo;
    }

    // used when selling to get the current stock price
    public void sellStockPrice() throws IOException {

        Document docOne = Jsoup.connect(url).get();

        Elements stockPrice = docOne.getElementsByClass("gsrt");

        String tempPrice = stockPrice.text();
        if (tempPrice.contains(",")) {
            tempPrice = tempPrice.replace(",", "");
        }

        String tempPriceArr[] = tempPrice.split(" ");
        currentStockPrice = Double.parseDouble(tempPriceArr[0]);
    }

    //accesors
    ////////////////////////////////////////////
    public String getStockName() {
        return stockSymbol;
    }

    public String getStockInfo() {
        return stockInfo;
    }

    public double getCurrentStockPrice() {

        return currentStockPrice;
    }
    ////////////////////////////////////////////
}
