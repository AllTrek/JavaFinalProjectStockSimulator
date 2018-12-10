package beta;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StockFiles {

    double balance;
    final private ArrayList<String> filesStockList = new ArrayList<>();
    final private ArrayList<Integer> fileShares = new ArrayList<>();
    private String stockfileName;

    //initializing a user class
    User toFill = new User();

    public void saveStockData(ArrayList<String> sym, ArrayList<Integer> sharesNum, double balance) throws FileNotFoundException {

        try (
                //creates a printwriter with user info
                PrintWriter outputFile = new PrintWriter(stockfileName + ".txt")) {

            //Writes balance at the top of the file
            outputFile.println(balance);
            //saves the rest of the info stock symbol first then amount
            for (int index = 0; index < sym.size(); index++) {

                outputFile.println(sym.get(index));
                outputFile.println(sharesNum.get(index));
            }
        }
    }

    public void getStockData() throws FileNotFoundException {

        int counter = 0; // count for loop 
        int insertValues = 0; // Insert actual values apple, money etc..

        //opens the file depending on user
        File file = new File(stockfileName + ".txt");

        try (Scanner inputFile = new Scanner(file)) {
            counter = 0;

            //gets the balance off the first Line
            balance = Double.parseDouble(inputFile.nextLine());

            //gets the rest of the info off the file
            while (inputFile.hasNext()) {

                String stockInfo = inputFile.nextLine();

                if (counter == 2) {
                    counter = 0;
                    insertValues += 1;
                }

                if (counter == 0) {

                    filesStockList.add(stockInfo);

                } else if (counter == 1) {

                    fileShares.add(Integer.parseInt(stockInfo));

                }
                counter += 1;
            }
        }
    }

    //method to show stock data in system consol
    public void showStockData() {

        for (int index = 0; index < filesStockList.size(); index++) {
            System.out.println(filesStockList.get(index));
            System.out.println(fileShares.get(index));
        }
    }

    public boolean loginScreenAuth(String user, String pass) throws FileNotFoundException {

        //opens the user file
        File file = new File("users.txt");

        //variables
        int i = 0;
        String usernameCheck;

        //booleans for checks
        boolean passwordCheck;
        boolean stay = true;
        boolean auth = false;

        //scanner
        Scanner inputFile = new Scanner(file);

        // break if it finds the username
        while (inputFile.hasNext() & stay) {

            if (inputFile.nextLine().equals(user)) {

                if (inputFile.nextLine().equals(pass)) {
                    passwordCheck = true;

                    stay = false;
                    auth = true;

                } else {

                    passwordCheck = false;
                }
                break;
            }

        }
        // return user only if 
        return auth;

    }

    public boolean createNewUserAuth(String username, String password, String confirmPassword) throws FileNotFoundException {

        boolean success = false;
        File file = new File("users.txt");
        Scanner inputFile = new Scanner(file);

        // First check if the password match otherwise false.  
        if (password.equals(confirmPassword)) {

            // Checking the file to see if the username selected is alredy their 
            while (inputFile.hasNext()) {

                if (inputFile.nextLine().equals(username)) {
                    success = false;
                    break;

                } else {
                    success = true;
                }
            }
        }
        inputFile.close();
        return success;
    }

    public void createNewUser(String username, String password) throws FileNotFoundException, IOException {

        String checkUserFile = "users.txt";
        PrintWriter outputText = new PrintWriter(new FileWriter(checkUserFile, true));

        //adds the new user username
        outputText.append("\n");
        outputText.append(username);

        //add the new user password
        outputText.append("\n");
        outputText.append(password);

        // Create a file for the user to hold there stocks with a default balance of 1k
        FileOutputStream newFile = new FileOutputStream(username + ".txt");
        newFile.close();

        String createFile = username + ".txt";
        PrintWriter outputBalance = new PrintWriter(new FileWriter(createFile));

        outputBalance.append("1000.00");

        outputText.close();
        outputBalance.close();
    }

    //accesors
    //////////////////////////////////////////
    public ArrayList getStockList() {
        return filesStockList;
    }

    public ArrayList getStockShares() {
        return fileShares;
    }

    public double getBalance() {
        return balance;
    }

    public String getStockFileName() {
        return stockfileName;
    }
    //////////////////////////////////////////

    //mutators
    //////////////////////////////////////////
    public void setStockfileName(String name) {
        stockfileName = name;
    }
    //////////////////////////////////////////
}
