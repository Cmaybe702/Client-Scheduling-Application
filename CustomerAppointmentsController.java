/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.Customer;
import Model.Schedule;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller for view displaying appointments for the customer selected in customers view
 * @author courtney
 */
public class CustomerAppointmentsController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Customer displayCustomer;
    private String displayName;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private Button AddAppointment;

    @FXML
    private TableColumn<Appointment, Integer> AppointmentIDColumn;

    @FXML
    private Label AppointmentsLabel;

    @FXML
    private TableColumn<Appointment, String> ContactColumn;

    @FXML
    private TableView<Appointment> AppointmentDisplay;
    
    @FXML
    private Label CustomerInfoLabel;

    @FXML
    private TableColumn<Appointment, String> DateColumn;

    @FXML
    private Button DeleteAppointment;

    @FXML
    private TableColumn<Appointment, String> DescriptionColumn;

    @FXML
    private Button ExitButton;

    @FXML
    private TableColumn<Appointment, String> LocationColumn;

    @FXML
    private Label LocationLabel;

    @FXML
    private TableColumn<Appointment, String> TimeColumn;

    @FXML
    private TableColumn<Appointment, String> TitleColumn;
    
    @FXML
    private TableColumn<Appointment, String> TypeColumn;

    @FXML
    private Button UpdateAppointment;

    @FXML
    private TableColumn<Appointment, String> UserIDColumn;
    
    /**
     * Used to setup display of customer's appointments and set locale language on visual elements
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set Labels to User Language
        AddAppointment.setText(labels.getString("add"));
        AppointmentIDColumn.setText(labels.getString("id"));
        ContactColumn.setText(labels.getString("contact"));
        DateColumn.setText(labels.getString("date"));
        DescriptionColumn.setText(labels.getString("description"));
        LocationColumn.setText(labels.getString("location"));
        TimeColumn.setText(labels.getString("time"));
        TitleColumn.setText(labels.getString("title"));
        TypeColumn.setText(labels.getString("type"));
        UserIDColumn.setText(labels.getString("createdBy"));
        DeleteAppointment.setText(labels.getString("delete"));
        ExitButton.setText(labels.getString("exit"));
        UpdateAppointment.setText(labels.getString("update"));
        AppointmentsLabel.setText(labels.getString("appointments"));
        
        //Get Table Info
        AppointmentDisplay.setItems(Schedule.getfilteredAppointments());
        AppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        ContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        UserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
    
    /**
     * Gets selected appointment and transfers to update view to modify fields
     * @param event Update button pressed
     */
    @FXML
    void OnActionModifyAppointment(ActionEvent event) {
        try{
            boolean fromCustomerDisplay = true;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/UpdateAppointment.fxml"));
            loader.load();
            UpdateAppointmentController UAController = loader.getController();
            Appointment thisAppointment = AppointmentDisplay.getSelectionModel().getSelectedItem();
            
            //Verify appointment is selected
            if(thisAppointment == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectAppointment"));
                alert.showAndWait(); 
            }
            else{
                UAController.receiveAppointment(thisAppointment, fromCustomerDisplay, false, false);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("updateAppointment"));
                stage.show();
            }
        }catch(Exception e){
           System.out.println(e.getMessage());
        }
    }

    /**
     * Opens add appointment screen for creating a new appointment for the customer
     * @param event Add button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionNewAppointment(ActionEvent event) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/AddAppointment.fxml"));
            loader.load();
            AddAppointmentController AAController = loader.getController();
            AAController.receiveCustomer(displayCustomer);
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("newAppointment"));
            stage.show();
            
        }catch(Exception e){
           System.out.println(e.getMessage());
        }
    }

    /**
     * Used to delete an existing appointment for the customer
     * @param event delete button pressed
     */
    @FXML
    void OnActionRemoveAppointment(ActionEvent event) {
        boolean status = false;
        Appointment thisAppointment = null;
        thisAppointment = AppointmentDisplay.getSelectionModel().getSelectedItem();
        
        //Verify appointment is selected
        if(thisAppointment == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectAppointment"));
                alert.showAndWait(); 
        }
        else{
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText(labels.getString("confirmAppointmentCancel"));
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    thisAppointment = AppointmentDisplay.getSelectionModel().getSelectedItem();
                    status = Schedule.deleteAppointment(thisAppointment);
               
                    //Refresh filter
                    Schedule.filterAppointmentsbyCustomer(displayCustomer);
                    AppointmentDisplay.setItems(Schedule.getfilteredAppointments());
                    AppointmentDisplay.refresh();
                    
                    if(status){ 
                        //Notify of cancellation
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        String cancelConfirm = labels.getString("appointmentCancelled") + "\n" + labels.getString("id")
                            + ": " + thisAppointment.getAppointmentID() + " " + labels.getString("type") + ": " +
                        thisAppointment.getType();
                        confirmation.setContentText(cancelConfirm);
                        confirmation.showAndWait();
                    }
                    else{
                        System.out.println("Cancellation failed");
                    }
                }
            }catch(NullPointerException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(labels.getString("error"));
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("noAppointmentSelected"));
                alert.showAndWait(); 
            }
        }
    }

    /**
     * Used to return back to display of all customers
     * @param event exit button pressed
     */
    @FXML
    void OnActionReturnToCustomers(ActionEvent event) {
        try{
            // Load customers view
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
     * Used to receive a selected customer from customer's display
     * @param customer appointments are displayed for
     */
    public void receiveCustomer(Customer customer) {
        displayCustomer = customer;
        displayName = customer.getCustomerName();
        CustomerInfoLabel.setText(labels.getString("customerLabel") + " " + displayName);
        Schedule.filterAppointmentsbyCustomer(customer);
        AppointmentDisplay.setItems(Schedule.getfilteredAppointments());
    }
}
