/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Country;
import Model.Customer;
import Model.CustomerList;
import Model.FirstLevelDivision;
import Model.Locations;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller of view to add new customers to the system
 * @author courtney
 */
public class AddCustomerController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private Button AddCustomerButton;
    
    @FXML
    private Label AddCustomerLabel;

    @FXML
    private TextField AddressField;

    @FXML
    private Label AddressLabel;

    @FXML
    private Button CancelButton;

    @FXML
    private ComboBox<Country> CountryDropDown;

    @FXML
    private Label CountryLabel;

    @FXML
    private ComboBox<FirstLevelDivision> DivisionDropDown;

    @FXML
    private Label DivisionLabel;

    @FXML
    private Label LocationLabel;

    @FXML
    private TextField NameField;

    @FXML
    private Label NameLabel;

    @FXML
    private TextField PhoneNumberField;

    @FXML
    private Label PhoneNumberLabel;

    @FXML
    private TextField PostalCodeField;

    @FXML
    private Label PostalCodeLabel;

    /**
     * Used for setup of view to add customer and set language to locale
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        AddCustomerButton.setText(labels.getString("add"));
        AddressLabel.setText(labels.getString("address"));
        CancelButton.setText(labels.getString("cancel"));
        CountryLabel.setText(labels.getString("country"));
        AddCustomerLabel.setText(labels.getString("addCustomer"));
        DivisionLabel.setText(labels.getString("division"));
        NameLabel.setText(labels.getString("name"));
        PhoneNumberLabel.setText(labels.getString("phoneNumber"));
        PostalCodeLabel.setText(labels.getString("postal"));
        CountryDropDown.setItems(Locations.getAllCountries());
        DivisionDropDown.setItems(Locations.getAllDivisions());
    }
    
    /**
     * Used to add a new customer and return to customers display
     * @param event Action Event that triggered addition
     * @throws SQLException if mySQL can not be reached
     * @throws IOException if new view can not be loaded
     */
    @FXML
    void OnActionAddCustomer(ActionEvent event) throws SQLException, IOException {
        String name = NameField.getText();
        String address = AddressField.getText();
        String postalCode = PostalCodeField.getText();
        String phoneNumber = PhoneNumberField.getText();
        FirstLevelDivision division = DivisionDropDown.getValue();
            
            //Verify name is not empty
        if(name == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(labels.getString("error"));
            alert.setContentText(labels.getString("provideName"));
            alert.showAndWait(); 
        }
        //Verify division selected
        else if(division == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(labels.getString("error"));
            alert.setContentText(labels.getString("selectDivision"));
            alert.showAndWait(); 
        }
        //All required fields create customer
        else{
            try{
                int divisionID = division.getDivisionID();
                Customer thisCustomer = new Customer(name, address, postalCode, phoneNumber, divisionID);
                CustomerList.userAddsCustomer(thisCustomer);
            
                // Load customers
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("customers"));
                stage.show();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * Cancel and return to customers display
     * @param event Action Event of cancel button being pressed
     */
    @FXML
    void OnActionReturn(ActionEvent event) {
        try{
            // Load customers
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("customers"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Filters divisions based on country selected in drop down
     * @param event Event of country being selected
     */
    @FXML
    void OnActionDisplayDivisions(ActionEvent event) {
        int countryID;
        
        Country selected = CountryDropDown.getValue();
        countryID = selected.getCountryID();
        
        DivisionDropDown.setItems(Locations.getFilteredDivisions(countryID));
    }
    
}
