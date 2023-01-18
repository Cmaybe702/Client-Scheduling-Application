/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Login;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Locale;
import java.time.*;


/**
 * Controller for display to log into the system
 * @author courtney
 */
public class LoginFormController implements Initializable{
   private Stage stage;
   private Parent scene;
   private Locale userLocale = Locale.getDefault();
   private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
   
   @FXML
   private Label LocationLabel;
   
   @FXML
   private Label LoginLabel;
   
   @FXML
   private Label PasswordLabel;
   
   @FXML
   private Label UsernameLabel;

   @FXML
   private PasswordField PasswordBox;

   @FXML
   private Button SignInButton;

   @FXML
   private TextField UsernameBox;
   
   /**
    * Passes credentials entered into login display for verification
    * @param event sign in button pressed
    * @throws IOException if view can not be loaded
    * @throws SQLException if mySQL can not be reached
    */
   @FXML
   void OnActionCheckCredentials(ActionEvent event) throws IOException, SQLException{
        String username = UsernameBox.getText();
        String password = PasswordBox.getText();
        boolean accepted = false;
        
        try{
            Login login = new Login(username, password);
            accepted = login.filterAndVerifyCredentials();
        
            if(accepted){
                System.out.println("Login Accepted");
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/LaunchPad.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("mainMenu"));
                stage.show(); 
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(labels.getString("error"));
                alert.setContentText(labels.getString("invalidLogin"));
                alert.showAndWait();
            }
        }catch(Exception e){
                System.out.println(e.getMessage());
        }
    }

    /**
     * Used to set location label with user zone and textual items to the correct lanaguage based on locale
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set location label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set labels to user language
        LoginLabel.setText(labels.getString("greeting"));
        PasswordLabel.setText(labels.getString("password"));
        UsernameLabel.setText(labels.getString("username"));
        SignInButton.setText(labels.getString("signinbutton"));
    }   
}