package beta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class LoginController implements Initializable {

    //login modifiable fields
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton; 
    @FXML private Label wrongInfo;
    @FXML private Hyperlink createUser;

    private boolean leave = false;

    //Initializing StockFiles class
    StockFiles loginCheck = new StockFiles();

    public void authAccount(ActionEvent event) throws FileNotFoundException, IOException {
        //checks if the the user name and password match the user files
        leave = loginCheck.loginScreenAuth(username.getText(), password.getText());

        //if match loads the all the info for the user
        if (leave == true) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));

            Parent stockScreen = loader.load();

            Scene tableViewScene = new Scene(stockScreen);

            FXMLDocumentController controller = loader.getController();

            controller.setStockFileNameController(username.getText());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();

        } else {
            //if no match displays error
            wrongInfo.setText("Incorrect username/password combination Please try again");
        }
    }

    public void loadCreateNewUserScreen(ActionEvent event) throws IOException {

        //login screen for creating a new user
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RegisterNewUser.fxml"));

        Parent loginScreen = loader.load();

        Scene tableViewScene = new Scene(loginScreen);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wrongInfo.setText("");
    }

}
