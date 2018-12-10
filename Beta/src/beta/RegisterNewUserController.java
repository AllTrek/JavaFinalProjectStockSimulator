/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MainFrame
 */
public class RegisterNewUserController implements Initializable {
    
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML PasswordField comfPassword;
    
    @FXML Button createUserButton;
    @FXML Button returnButton;
    
    @FXML Label msgGreen; 
    @FXML Label msgRed; 
    
    StockFiles createUser = new StockFiles();
    
    
    
    public void createNewUser(ActionEvent event) throws FileNotFoundException, IOException{
        
    boolean auth = createUser.createNewUserAuth(username.getText(), password.getText(), comfPassword.getText());
    
    if(auth == true){
            createUser.createNewUser(username.getText(), password.getText());
            msgRed.setText("");
            msgGreen.setText("User Successfuly Created");
            
            username.clear();
            password.clear();
            comfPassword.clear();
    }else{
        msgGreen.setText("");
        msgRed.setText("Error creating user");
    }
  
    }
    
    
    public void returnToLoginScreen(ActionEvent event) throws IOException{
        
        
             FXMLLoader loader = new FXMLLoader();
             loader.setLocation(getClass().getResource("login.fxml"));
             
             Parent loginScreen = loader.load();
             
             Scene tableViewScene = new Scene(loginScreen);
             

             Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
             
             window.setScene(tableViewScene);
             window.show();    
      
    }
    
    
    
    
    
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       


    }    
    
}
