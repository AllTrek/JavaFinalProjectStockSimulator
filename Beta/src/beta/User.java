package beta;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.ArrayList;

public class User {

    private double balance;

    final private StockSearch search = new StockSearch();
    private Scanner keboard = new Scanner(System.in);

    private ArrayList<String> stockList = new ArrayList<>();
    private ArrayList<Integer> shares = new ArrayList<>();

    //decimal format for money
    DecimalFormat df = new DecimalFormat("#.##");

    public boolean buyStock(String stockName, String stockInfo, int amountOfStock) throws IOException {
        boolean purchaseSuccess = false;
        int stockToBuy;

        String[] company = stockName.split(" ");

        double price = this.getCurrentStockPrice(company[1]);

        stockToBuy = amountOfStock;

        //Check if they have enough money
        if ((balance - (price * stockToBuy) >= 0)) {
            //if user already has stock for company
            if (stockList.contains(company[1])) {
                //temporary stock to add the new bought stock of same company
                int tempStock = shares.get(stockList.indexOf(company[1]));
                shares.set(stockList.indexOf(company[1]), (tempStock + stockToBuy));
            } //if user does not have stock for company
            else {
                stockList.add(company[1]);
                shares.add(stockToBuy);
            }
            //always happens
            this.balance -= (price * stockToBuy);
            purchaseSuccess = true;
        } else {
            purchaseSuccess = false;
        }
        //returns weather purchace was successful
        return purchaseSuccess;
    }

    //accecors
    ///////////////////////////////////////////////////////////////////////////////////////
    public void getStockList() {
        System.out.println(stockList.get(0));
    }

    public ArrayList getStockSym() {
        return stockList;
    }

    public ArrayList getNumOfShares() {

        return shares;
    }

    public double getBalance() {
        return balance;
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    //mutators
    ////////////////////////////////////////////////////////////////////////////////////////
    public void setBalance(double b) {

        this.balance += b;
    }

    public void setUserInfo(ArrayList stockName, ArrayList stockAmount, double money) {
        stockList = stockName;
        shares = stockAmount;
        balance = money;
    }

    public void setBankBalance() {
        double balance;

        System.out.println("What is your current bank balance?");
        balance = keboard.nextInt();

        this.balance = balance;
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    //Prints stocks to consol
    public void showStock() throws IOException {
        System.out.println("Your current stocks:");
        //loop showing current stock
        for (int index = 0; index < stockList.size(); index++) {
            System.out.println("\nNASDAQ: " + stockList.get(index) + "  shares: " + shares.get(index) + " current value: " + getCurrentStockPrice(stockList.get(index)));
        }
    }

    public void sellStock(String cp, int ss) throws IOException {
        String company;
        int stockToSell;
        boolean hasStock = false;

        stockToSell = ss;
        company = cp;
        // remove spaces 
        company = company.replaceAll("\\s+", "");

        //check if they have stock for that company
        if (stockList.contains(company)) {
            //ask how much they want to sell
//            System.out.println("How much of your stock do you want to sell? You own:" + shares.get(stockList.indexOf(company)));
//            stockToSell = keboard.nextInt();
            hasStock = true;
            // need to pass in the number of stocks to sell. 

            //check to not sell more that what they own
            while ((shares.get(stockList.indexOf(company)) - stockToSell) < 0) {
                System.out.println("You dont own that much stock!");
                System.out.println("How much of your stock do you want to sell? You own:" + shares.get(stockList.indexOf(company)));
                stockToSell = keboard.nextInt();
            }

            //temp sheres to then replace with how many thay have left
            int tempShares = shares.get(stockList.indexOf(company));
            shares.set(stockList.indexOf(company), (tempShares - stockToSell));

            //Adding the profit to bank balance
            balance += (stockToSell * this.getCurrentStockPrice(company));
            System.out.println("AKI ESTA" + balance);

            //cleanup of arraylist of stocks become 0
            if (shares.get(stockList.indexOf(company)) == 0) {
                shares.remove(stockList.indexOf(company));
                stockList.remove(stockList.indexOf(company));
            }

            System.out.println("successfully sold" + stockToSell + " Shares.");
        } else {
            System.out.println("You don't own any stock for " + company);
        }

    }

    public Double getCurrentStockPrice(String company) throws IOException {
        double tempPrice = 0;
        //searchers for the current stock info for the company

        boolean check = search.checkIfStock(company);

        // Need a way to call just to get the price for this method 
        if (check == true) {

            search.sellStockPrice();
            tempPrice = search.getCurrentStockPrice(); // Needs to check 

        }

        return tempPrice;
    }

    public double portfolioValue() throws IOException {

        double totalPortValue = 0;

        //gets the current stock price of every item in the table
        //multiplies it by how many you have
        //then adds it all to totalPortValue
        for (int index = 0; index < stockList.size(); index++) {
            totalPortValue += shares.get(index) * getCurrentStockPrice(stockList.get(index));
        }

        return totalPortValue;

    }

    public boolean CheckIfStockInPortfolio(String cp) {

        boolean isStock = false;
        String company = cp;
        company = company.replaceAll("\\s+", "");

        //cheks user has stock
        if (stockList.contains(company)) {
            isStock = true;
        }

        return isStock;
    }

    public int CheckHowManyStocksAreOwned(String cp) {
        //returns how many of a stock the user has
        String company = cp;
        company = company.replaceAll("\\s+", "");

        return shares.get(stockList.indexOf(company));
    }

    public int returnNumberOfStockOwned() {
        return stockList.size();
    }

}
