/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.CustomerList;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.Customer;
import Model.FirstLevelDivision;
import java.util.Locale;

/**
 * Controller for report of customers by division selected in reporting
 * @author courtney
 */

public class CustomersByDivisionController implements Initializable{
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
  
    @FXML
    private TableColumn<Customer, String> CustomerAddressColumn;

    @FXML
    private TableView<Customer> CustomerDisplay;

    @FXML
    private TableColumn<Customer, Integer> CustomerIDColumn;

    @FXML
    private TableColumn<Customer, String> CustomerNameColumn;

    @FXML
    private TableColumn<Customer, String> CustomerPhoneColumn;

    @FXML
    private TableColumn<Customer, String> CustomerPostalColumn;

    @FXML
    private Label CustomersByDivisionLabel;
    
    @FXML
    private Label DivisionLabel;

    @FXML
    private Button ExitButton;

    @FXML
    private Label LocationLabel;

    /**
     * Set display with local language and zone
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set Labels to User Language
        CustomerAddressColumn.setText(labels.getString("address"));
        CustomerIDColumn.setText(labels.getString("id"));
        CustomerNameColumn.setText(labels.getString("name"));
        CustomerPhoneColumn.setText(labels.getString("phoneNumber"));
        CustomerPostalColumn.setText(labels.getString("postal"));
        CustomersByDivisionLabel.setText(labels.getString("custByDivision"));
        ExitButton.setText(labels.getString("exit"));
   
        //Get Table Info
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        CustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        CustomerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        CustomerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        CustomerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));           
    } 
 
    /**
     * /Used to return to reporting
     * @param event exit pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionReturnToReports(ActionEvent event) throws IOException {
        try{
            // Load launch pad
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("reporting"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Used to receive division selected in reporting display
     * @param division Division to be displayed
     */
    public void receiveDivision(FirstLevelDivision division){
        //Set division label
        String display = labels.getString("customerLabel") + " " + division;
        DivisionLabel.setText(display);
        
        //Generate report
        CustomerDisplay.setItems(CustomerList.filterByDivision(division));
    }
}
