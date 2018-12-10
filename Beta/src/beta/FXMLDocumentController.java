package beta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    //Labels used in scene builder
    @FXML private Label stockSearchOutputlabelOne;
    @FXML private Label stockSearchOutputlabelTwo;
    @FXML private Label askUserQuestionLabel;
    @FXML private Label balanceLabel;
    @FXML private Label usernameLabel;

    //text fields we manipulate in scene builder
    @FXML private TextField userStockSearch;
    @FXML private TextField userSelection;
    @FXML private Button searchStockButton;
    @FXML private Button buyStockButton;
    @FXML private Button sellStockButton;
    @FXML private Label portValue;

    //scene builder table where we have the stock/price/and amount of shares
   @FXML private TableView<populateTable> tableView;
   @FXML private TableColumn<populateTable,String> SymbolColumn;
   @FXML private TableColumn<populateTable,String> ShareColumn;
   @FXML private TableColumn<populateTable,String> PriceColumn;
   
   @FXML private TextField transferCashTextField;
   @FXML private Button transferCashButton;
   
   @FXML private Hyperlink logoutHyperlink;

    // Servers as a "Show and Wait" 
    private int userChoiceSelected = 0;

    //used to save what the user searched for
    private int numberOfShares;
    private String stockSymbol;

    //decimal format for money
    DecimalFormat df = new DecimalFormat("#.##");

    //initializing classes
    StockSearch searchStocks = new StockSearch();
    User currentUser = new User();
    StockFiles userFiles = new StockFiles();

    @FXML private void searchStockButtonClicked(ActionEvent event) throws IOException {

        String StockInfo;
        String[] displayStockNameAndPrice;
        String[] displayStockInfoSplitScreen;

        // reset empty label from previous search
        askUserQuestionLabel.setText("");

        try{
            //checks that the searched stock is actually a stock
        searchStocks.checkIfStock(userStockSearch.getText());
        StockInfo = searchStocks.showStockInfo();

        //displays the stock info
        displayStockNameAndPrice = StockInfo.split("test");

        displayStockInfoSplitScreen = displayStockNameAndPrice[1].split("second");

        //  saves the sym for the sell function 
        String[] StockSym = displayStockNameAndPrice[0].split(":|\\$");
        stockSymbol = StockSym[1];
        //displays searched stock info the output label
        stockSearchOutputlabelOne.setText("\n" + displayStockNameAndPrice[0] + "\n\n" + displayStockInfoSplitScreen[0]);
        stockSearchOutputlabelTwo.setText("\n\n\n" + displayStockInfoSplitScreen[1]);
        } catch(Exception e){
            askUserQuestionLabel.setText("No company/stock were found for" + userStockSearch.getText());
            userStockSearch.clear();
        }
    }

    @FXML
    private void setBuyStockSelection() throws IOException {

        boolean data;
        double stockPrice;

        //sets the question label
        askUserQuestionLabel.setText("How many shares would you like to purchase?");

//       once the text appears in the askUserQuestionLabel we want to read from the 
//       userSelection textbox. The problem is that the program does not wait.
//       It displays the text and then imeditaly looks for an answer in the userSelection text box
//       so it does not give time for the user to type in there answer which causes an error. 
//               
//       The userChoiceSelected is a way to getting around that. It displays the text, sets the 
//       userChoiceSelected then calls another method once the enter button is clicked which 
//       Determines in what part of the questions process it's in 
        if (userChoiceSelected == 2) {
            //used to check if stock was aquired successfully
            boolean check = false;
            // will always happen if user wants to buy
            if (1 == 1) {
                //gets the number of shares the user wants to buy
                
                //the catch gets skipped
                try {
                    numberOfShares = Integer.parseInt(userSelection.getText());
                } catch (NumberFormatException e) {
                    askUserQuestionLabel.setText("Please inpuit a number");
                    userSelection.clear();
                }
                //checking if stock was aquired successfully
                if(numberOfShares != 0){
                    check = currentUser.buyStock(searchStocks.getStockName(), searchStocks.getStockInfo(), numberOfShares);
                }
                
                //if it was this happens
                if (check == true) {
                    //shows user purchace was successful
                    askUserQuestionLabel.setText("Purchase Succesful");
                    //updates the table showing all stock
                    updatePortfolio();
                    //updates the balance label
                    balanceLabel.setText(String.valueOf(df.format(currentUser.getBalance())));
                    //clears user selection
                    userSelection.clear();
                    // Reset to zero
                    userChoiceSelected = 3;
                } else 
                    //temp workaround since catch is bieng skipped
                    if(numberOfShares == 0){
                    askUserQuestionLabel.setText("Please input a number");
                    userSelection.clear();
                }else {
                    //if purchace is usuccessful shows Insufficient funds
                    askUserQuestionLabel.setText("Insufficient funds");
                }
            }
        }

        //moves the proccess foward
        if (userChoiceSelected == 0) {
            userChoiceSelected = 2;

        } else if (userChoiceSelected == 3) {
            userChoiceSelected = 0;
        }
        //saving to file
        userFiles.saveStockData(currentUser.getStockSym(), currentUser.getNumOfShares(), currentUser.getBalance());
    }

    @FXML
    private void sellStock() throws IOException {

        boolean isInPort;
        int stockOwned;

        // 4 = CheckIfStockInPortfolio = True now checking to see if the number 
        // of shares they typed is withing range of what they have. 
        if (userChoiceSelected == 4) {

            int sharesToSell = 0;
            int sharesAvailable;

            //shares the user wants to sell
            try{
                sharesToSell = Integer.parseInt(userSelection.getText());
            } catch(NumberFormatException exeption){
                askUserQuestionLabel.setText("Please inpuit a number");
                userSelection.clear();
            }
            //checks how many shares the user has
            sharesAvailable = currentUser.CheckHowManyStocksAreOwned(stockSymbol);

            //the if checks if the user wants to sell more than what they have
            if (sharesAvailable >= sharesToSell && sharesToSell != 0) {
                //calls the user class method
                currentUser.sellStock(stockSymbol, sharesToSell);

                //updates the portfolio
                updatePortfolio();

                //displays the stock was sold
                askUserQuestionLabel.setText("Stock successfully sold");
                userSelection.clear();

                //updating the balance label
                balanceLabel.setText(String.valueOf(df.format(currentUser.getBalance())));

                //moving to the default
                userChoiceSelected = 5;

                //saving to file
                userFiles.saveStockData(currentUser.getStockSym(), currentUser.getNumOfShares(), currentUser.getBalance());
            } else
                //temp workaround since catch is bieng skipped
                if(sharesToSell == 0){
                askUserQuestionLabel.setText("Please inpuit a number");
                userSelection.clear();
            }else{
                //if user does not have enough shares this message is displayed
                askUserQuestionLabel.setText("Number of shares entered is out of range");
            }
        }

        if (userChoiceSelected != 5 & userChoiceSelected != 4) {
            //chacks if you have the stock 
            isInPort = currentUser.CheckIfStockInPortfolio(stockSymbol);

            //if you own stock
            if (isInPort == true) {
                //checks how many stocks user owns
                stockOwned = currentUser.CheckHowManyStocksAreOwned(stockSymbol);
                //asks how many stocks the user wants to sell and moves fowards
                askUserQuestionLabel.setText("How many shares would you like sell? " + "\nShares Owned: " + stockOwned);
                userChoiceSelected = 4;
            } else {
                //user does not have enough stock
                askUserQuestionLabel.setText("You don't own any stocks for that company");
            }
        }
        // user choice = 5 sell the stock and reset back to 0 
        if (userChoiceSelected == 5) {
            userChoiceSelected = 0;
        }
        //saving to file
        userFiles.saveStockData(currentUser.getStockSym(), currentUser.getNumOfShares(), currentUser.getBalance());
    }

    @FXML
    private void userSelectionChoice() throws IOException {

        //controller for the different methods
        switch (userChoiceSelected) {
            case 1:
                setBuyStockSelection();
                break;
            case 2:
                setBuyStockSelection();
                break;
            case 4:
                sellStock();
                break;
            default:
                break;
        }
    }

    private void updatePortfolio() throws IOException {

        //updates the user variables in user class
        currentUser.setUserInfo(userFiles.getStockList(), userFiles.getStockShares(), currentUser.getBalance());

        //updates the table to then be shown
        SymbolColumn.setCellValueFactory(new PropertyValueFactory<populateTable, String>("Symbol"));
        ShareColumn.setCellValueFactory(new PropertyValueFactory<populateTable, String>("Share"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<populateTable, String>("Price"));

        //updates the portfolio value label showing the total value of all your stock
        portValue.setText(String.valueOf(df.format(currentUser.portfolioValue())));
        

        //updates the table
        tableView.setItems(getuserPortfolio());
    }
    
    
    public void logoutButtonPressed(ActionEvent event) throws IOException{
        
         FXMLLoader loader = new FXMLLoader();
             loader.setLocation(getClass().getResource("login.fxml"));
             
             Parent loginScreen = loader.load();
             
             Scene tableViewScene = new Scene(loginScreen);
             

             Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
             
             window.setScene(tableViewScene);
             window.show();    
      
        
    }

    // When the program loads it first goes to initiliaze then it goes to filenamecontroller
    // The problem is the name has to be passed to the fileNamecontroller and because 
    // which then renames the file.     
    public void setStockFileNameController(String name) throws IOException {

        //sets usename
        usernameLabel.setText(name.toUpperCase(Locale.ITALY));

        //makes the file as the name of the user
        userFiles.setStockfileName(name);

        // will try to get data off the file
        try {

            userFiles.getStockData();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //updates the user class with info in file
        currentUser.setUserInfo(userFiles.getStockList(), userFiles.getStockShares(), userFiles.getBalance());

        //sets the tablecolumns
        SymbolColumn.setCellValueFactory(new PropertyValueFactory<populateTable, String>("Symbol"));
        ShareColumn.setCellValueFactory(new PropertyValueFactory<populateTable, String>("Share"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<populateTable, String>("Price"));

        //sets the stock in the table
        try {
            tableView.setItems(getuserPortfolio());
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        portValue.setText(String.valueOf(df.format(currentUser.portfolioValue())));
        //updates the balance label
        balanceLabel.setText(String.valueOf(df.format(currentUser.getBalance())));
    }
    
//    
//    public void addBalance(){
//        currentUser.setBalance(Double.parseDouble(transferCashTextField.getText()));
//        balanceLabel.setText(String.valueOf(df.format(currentUser.getBalance())));
//        transferCashTextField.clear();
//    }
//    
//    
   

    
    // Populate the portfolio tabel 
    
    public ObservableList<populateTable> getuserPortfolio() throws IOException {
        ObservableList<populateTable> userPortfolio = FXCollections.observableArrayList();

        int num = currentUser.returnNumberOfStockOwned();

        for (int i = 0; i < num; i++) {
            
            ArrayList<String> setCurrentUserPortInfo = new ArrayList<>();
            setCurrentUserPortInfo = currentUser.getStockSym();
            
            double PortStockPrice = currentUser.getCurrentStockPrice(setCurrentUserPortInfo.get(i));
            userPortfolio.add(new populateTable(String.valueOf(currentUser.getStockSym().get(i)), String.valueOf(currentUser.getNumOfShares().get(i)), Double.toString(PortStockPrice)));
        }

        return userPortfolio;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
