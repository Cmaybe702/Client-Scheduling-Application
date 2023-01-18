/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.Schedule;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the main menu of the application
 * @author courtney
 */
public class LaunchPadController implements Initializable{
    private static boolean firstSignIn = true;
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
   
    @FXML
    private Button CustomerButton;

    @FXML
    private Label CustomersLabel;

    @FXML
    private Label LaunchPadLabel;

    @FXML
    private Label LocationLabel;

    @FXML
    private Button ReportingButton;

    @FXML
    private Label ReportingLabel;

    @FXML
    private Button ScheduleButton;

    @FXML
    private Label ScheduleLabel;

    /**
     * Opens view of all customers in system
     * @param event customers button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionOpenCustomers(ActionEvent event) throws IOException{
        try{
            // Load customers page
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("customersTitle"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Used to open reporting display
     * @param event reporting button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionOpenReporting(ActionEvent event) throws IOException{
        try{
            // Load reporting
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
     * Open display for month schedule, day & week view can be accessed via info
     * @param event schedule button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionOpenSchedule(ActionEvent event) throws IOException{
        try{
            // Load month schedule
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MonthSchedule.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("schedule"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Used to set visual items to local language and location label to userZone
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set location label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set labels to user language
        LaunchPadLabel.setText(labels.getString("launchPad"));
        CustomersLabel.setText(labels.getString("customers"));
        ScheduleLabel.setText(labels.getString("schedule"));
        ReportingLabel.setText(labels.getString("reporting")); 
        
        //Alert if there is an appointment within 15 minutes if first sign into the system
        if(firstSignIn) {
          alertOfAppointment(); 
          firstSignIn = false;
        }          
    } 
    
    /**
     * Alert if there is an appointment within 15 minutes of login
     */
    public void alertOfAppointment(){
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime test = now.minusMinutes(30);
        ZonedDateTime within15 = test.plusMinutes(60);

        //Find any matching appointments
        Optional<Appointment> foundAppointment = Schedule.getAllAppointments().stream()
            .filter(appointment -> (appointment.getStart().compareTo(test) > 0))
            .filter(appointment -> (appointment.getStart().compareTo(within15) < 0))
            .findFirst();
        
        //If found get info and alert
        if(foundAppointment.isPresent()){  
           Appointment alertAppointment = foundAppointment.get();
           String alertText = labels.getString("appointmentWithin15") + "\n" + labels.getString("appointmentID1")
                   + ": " + alertAppointment.getAppointmentID() + "\n" + labels.getString("date") + ": " +
                   alertAppointment.getFormattedDate() + "\n" + labels.getString("time") + ": " + 
                   alertAppointment.getTime() + "\n" + labels.getString("createdBy") + ": " +
                   alertAppointment.getUserID();
                   
           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
           alert.setHeaderText(labels.getString("alert"));
           alert.setContentText(alertText);
           alert.showAndWait(); 
        }
        else{
            String noUpcoming = labels.getString("noUpcoming");
            Alert none = new Alert(Alert.AlertType.CONFIRMATION);
            none.setHeaderText(labels.getString("alert"));
            none.setContentText(noUpcoming);
            none.showAndWait(); 
        }
    }
}
