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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.Customer;
import Model.Schedule;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controller for view that displays all active customers of the business
 * @author courtney
 */

public class CustomersController implements Initializable{
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private Button AddAppointmentButton;
    
    @FXML
    private Button AddCustomer;

    @FXML
    private TableColumn<Customer, String> CustomerAddressColumn;

    @FXML
    private TableView<Customer> CustomerDisplay;

    @FXML
    private TableColumn<Customer, Integer> CustomerDivisionColumn;
    
    @FXML
    private TableColumn<Customer, Integer> CustomerIDColumn;

    @FXML
    private TableColumn<Customer, String> CustomerNameColumn;

    @FXML
    private TableColumn<Customer, String> CustomerPhoneColumn;

    @FXML
    private TableColumn<Customer, String> CustomerPostalColumn;

    @FXML
    private TextField CustomerSearch;

    @FXML
    private Label CustomersLabel;

    @FXML
    private Button DeleteCustomer;

    @FXML
    private Button ExitButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private Button UpdateCustomer;
    
    /**
     * Set up display with all customers and set visual elements language to locale
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set Labels to User Language
        AddCustomer.setText(labels.getString("add"));
        CustomerAddressColumn.setText(labels.getString("address"));
        CustomerDivisionColumn.setText(labels.getString("divisionid"));
        CustomerIDColumn.setText(labels.getString("id"));
        CustomerNameColumn.setText(labels.getString("name"));
        CustomerPhoneColumn.setText(labels.getString("phoneNumber"));
        CustomerPostalColumn.setText(labels.getString("postal"));
        CustomerSearch.setPromptText(labels.getString("search"));
        CustomersLabel.setText(labels.getString("customers"));
        DeleteCustomer.setText(labels.getString("delete"));
        ExitButton.setText(labels.getString("exit"));
        UpdateCustomer.setText(labels.getString("update"));
        AddAppointmentButton.setText(labels.getString("appointments"));
        
        //Get Table Info
        CustomerDisplay.setItems(CustomerList.getAllCustomers());
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        CustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        CustomerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        CustomerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        CustomerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        CustomerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));           
    } 
    
    /**
     * Used to load display for all appointments for the selected customer
     * @param event Appointments button pressed
     * @throws IOException if view can not  be loaded
     */
    @FXML
    void OnActionGetAppointments(ActionEvent event) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/CustomerAppointments.fxml"));
            loader.load();
            CustomerAppointmentsController CAController = loader.getController();
            Customer customer = CustomerDisplay.getSelectionModel().getSelectedItem();
            CAController.receiveCustomer(customer);
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("appointments"));
            stage.show();
            
        }catch(NullPointerException ex){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText(labels.getString("noCustomerSelected"));
           alert.showAndWait(); 
        }
    }

    /**
     * Open add customer to enter a new customer into system
     * @param event Add button pressed
     */
    @FXML
    void OnActionAddNewCustomer(ActionEvent event) {
        try{
            // Load add customer
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/AddCustomer.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("addCustomer"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Used to search customer list by ID or customer name
     * @param event entered pressed in search box
     */
    @FXML
    void OnActionSearch(ActionEvent event) {
        // Get input from text box
        String input = CustomerSearch.getText();
        ObservableList <Customer> filteredList;
        
        //Determine if ID or Name was entered. Select corresponding customers if found
        if (input.matches("[0-9]+")) {
            int id = Integer.parseInt(CustomerSearch.getText());
            filteredList = filterCustomers(id);
        }
        else{
            filteredList = filterCustomers(input);
        }
        
        //If filtered list is empty notify user and clear
        if(filteredList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(labels.getString("error"));
            alert.setContentText(labels.getString("matchNotFound"));
            alert.showAndWait();
                
            CustomerSearch.clear();
        }
        else{
            CustomerDisplay.setItems(filteredList);
        }
    }

    /**
     * Used to transfer an existing customer for field modification
     * @param event update button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionModifyCustomer(ActionEvent event) throws IOException {
        //load modify customer if selection is made
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/ModifyCustomer.fxml"));
            loader.load();
            ModifyCustomerController MCController = loader.getController();
            MCController.receiveCustomer(CustomerDisplay.getSelectionModel().getSelectedItem());
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("modifyCustomer"));
            stage.show();
            
        }catch(NullPointerException ex){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle(labels.getString("error"));
           alert.setContentText(labels.getString("noCustomerSelected"));
           alert.showAndWait(); 
       }
    }
    
    /**
     * Used to delete a customer from system given no appointments are scheduled for that customer
     * @param event deleted pressed
     * @throws SQLException if mySQL can not be reached 
     */
    @FXML
    void OnActionRemoveCustomer(ActionEvent event) throws SQLException {
        boolean status = false;
        boolean hasAppointments = false;
        
        try {
           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
           alert.setTitle(labels.getString("confirmation"));
           alert.setContentText(labels.getString("confirmDelete"));
           Optional<ButtonType> result = alert.showAndWait();
           if(result.get() == ButtonType.OK){
                Customer thisCustomer = CustomerDisplay.getSelectionModel().getSelectedItem();
                
                //Verify customer doesn't have scheduled appointments before delete
                hasAppointments = Schedule.checkForAppointments(thisCustomer);
                if(!hasAppointments){
                    status = CustomerList.userDeletesCustomer(thisCustomer);
                }  
                else{
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle(labels.getString("error"));
                    error.setContentText(labels.getString("canNotDelete"));
                    error.showAndWait(); 
                }
           }
           
           //Nofify of delete
           if(status){                  
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setContentText(labels.getString("confirmDeleteComplete"));
                confirmation.showAndWait();
           }
           
        }catch(NullPointerException ex){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle(labels.getString("error"));
           alert.setContentText(labels.getString("noCustomerSelection"));
           alert.showAndWait(); 
        }      
    }

    /**
     * Used to return to the main menu
     * @param event exit button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionReturnToLaunch(ActionEvent event) throws IOException {
        try{
            // Load launch pad
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/LaunchPad.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("launchPad"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Filter customer list by customer name entered in search box
     * @param inputText String containing the text the user typed
     * @return A list of customers with names that match the search text
     */
    public ObservableList<Customer> filterCustomers(String inputText){
        
        //Clear previous filter
        if(!(CustomerList.getAllFilteredCustomers()).isEmpty()){
            CustomerList.getAllFilteredCustomers().clear();
        }
        
        //Filter products
        for (Customer customer : CustomerList.getAllCustomers()) {
            if (customer.getCustomerName().toLowerCase().contains(inputText.toLowerCase())) {
              CustomerList.getAllFilteredCustomers().add(customer);
            }
        }
            return CustomerList.getAllFilteredCustomers();
    }
    
    /**
     * Filter customer list by ID
     * @param id number being searched for
     * @return A list of customers with ids matching input
     */
    public ObservableList<Customer> filterCustomers(int id){
        
        //Clear previous filter
        if(!(CustomerList.getAllFilteredCustomers()).isEmpty()){
            CustomerList.getAllFilteredCustomers().clear();
        }
        
        //Filter products
        for (Customer customer : CustomerList.getAllCustomers()) {
            if (customer.getID() == id) {
              CustomerList.getAllFilteredCustomers().add(customer);
            }
        }
        
        return CustomerList.getAllFilteredCustomers();
    }
}
