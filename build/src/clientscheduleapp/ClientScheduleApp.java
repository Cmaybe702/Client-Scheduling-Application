/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clientscheduleapp;

import Model.CustomerList;
import Database.JDBC;
import Model.Appointment;
import Model.Locations;
import Model.Login;
import Model.Schedule;
import Model.Contact;
import Model.Report;
import Model.ScheduleDay;
import Model.ScheduleWeek;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @version 1
 * @author Courtney Mayberry
 */
public class ClientScheduleApp extends Application{
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("resources/messages", userLocale);
    
    /**
     * Main method called on startup of application. Used to set format of dates based on locale,
     * initialize data from mySQL, set counters, and create report options
     * @param args the command line arguments
     * @throws SQLException if mySQL can not be reached
     */
    public static void main(String[] args) throws SQLException {
        //Open database connection
        JDBC.openConnection();
        
        //View in French?
        //Locale.setDefault(Locale.CANADA_FRENCH);
        
        //Set format of dates based on Locale
        Appointment.setDateFormat();
        ScheduleWeek.setDateFormat();
        ScheduleDay.setDateFormat();
        
        //Initialize customer list and set counter for IDs
        CustomerList.initializeCustomerList();
        CustomerList.setCustomerIDCounter();
        
        //Initialize country list
        Locations.initializeCountryList();
        
        //Initialize divisions
        Locations.initializeDivisionList();
        
        //Initialize user list
        Login.initiliazeUsers();
        
        //Initialize contact list
        Contact.initiliazeContacts();
        
        //Initialize schedule and appointment id counter
        Appointment.setAppointmentCounter();
        Schedule.initializeSchedule();
        
        //Create reports
        Report.createReports();
        
        launch(args);
        JDBC.closeConnection();
    }
    
    /**
     * First method called by the program opens Login Form
     * @param stage Login form
     * @throws IOException if view can not be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(labels.getString("loginTitle"));
            stage.show();
        }catch(Exception e){
          System.out.println(e.getMessage());
        }
    }  
}
