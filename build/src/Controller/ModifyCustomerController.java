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
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller for view to modify fields of a customer entry
 * @author courtney
 */
public class ModifyCustomerController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Customer custToModify;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
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
    private TextField CustomerIDField;

    @FXML
    private Label CustomerIDLabel;

    @FXML
    private ComboBox<FirstLevelDivision> DivisionDropDown;

    @FXML
    private Label DivisionLabel;

    @FXML
    private Label LocationLabel;

    @FXML
    private Button ModifyCustomerButton;

    @FXML
    private Label ModifyCustomerLabel;

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
     * Used to initialize fields with customer info being modified and set language
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        CustomerIDLabel.setText(labels.getString("customerID"));
        ModifyCustomerButton.setText(labels.getString("modify"));
        AddressLabel.setText(labels.getString("address"));
        CancelButton.setText(labels.getString("cancel"));
        CountryLabel.setText(labels.getString("country"));
        ModifyCustomerLabel.setText(labels.getString("modifyCustomer"));
        DivisionLabel.setText(labels.getString("division"));
        NameLabel.setText(labels.getString("name"));
        PhoneNumberLabel.setText(labels.getString("phoneNumber"));
        PostalCodeLabel.setText(labels.getString("postal"));
        CountryDropDown.setItems(Locations.getAllCountries());
        DivisionDropDown.setItems(Locations.getAllDivisions());
    }
    
    /**
     * Used to get information entered in fields and transfer for updating in database and view
     * @param event update button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionUpdateCustomer(ActionEvent event) throws IOException {
        //Get id of customer being modified
        int id = custToModify.getID();
        
        //Get values from text fields
        String name = NameField.getText();
        String address = AddressField.getText();
        String postalCode = PostalCodeField.getText();
        String phoneNumber = PhoneNumberField.getText();
        FirstLevelDivision division = DivisionDropDown.getValue();
        int divisionID = division.getDivisionID();
        
        Customer thisCustomer = new Customer(id, name, address, postalCode, phoneNumber, divisionID);
        CustomerList.userUpdatesCustomer(thisCustomer);
        
        // Load customers
        try{
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("customers"));
            stage.show();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Used to repopulate divisions when country is changed
     * @param event country drop down menu value changed
     */
    @FXML
    void OnClickRefreshDivisions(MouseEvent event) {
        int countryID;
        
        Country selected = CountryDropDown.getValue();
        countryID = selected.getCountryID();
        
        DivisionDropDown.setItems(Locations.getFilteredDivisions(countryID));
    }

    /**
     * Returns the user back to the customer display
     * @param event cancel button pressed
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
     * Used to transfer customer information for update
     * @param customer Customer being modified
     */
    public void receiveCustomer(Customer customer) {
        custToModify = customer;
        
        String custID = Integer.toString(customer.getID());
        CustomerIDField.setText(custID);
        AddressField.setText(customer.getAddress());
        NameField.setText(customer.getCustomerName());
        PhoneNumberField.setText(customer.getPhoneNumber());
        PostalCodeField.setText(customer.getPostalCode());
        
        //Set Country and Division combo boxes
        int divisionID = customer.getDivisionID();
        FirstLevelDivision division = FirstLevelDivision.findDivision(divisionID);
        Country country = division.getCountry();
        
        CountryDropDown.setValue(country);
        DivisionDropDown.setValue(division);
    }
}
