/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.Contact;
import Model.FirstLevelDivision;
import Model.Locations;
import Model.Report;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for view that lets a user select from and run available reports
 * @author courtney
 */
public class ReportsController implements Initializable {
    private Stage stage;
    private Parent scene;
    private int reportNumber;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private Label EndDateLabel;

    @FXML
    private DatePicker EndDatePicker;

    @FXML
    private Button ExitButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private ComboBox<Report> ReportDropDown;

    @FXML
    private Label ReportTypeLabel;

    @FXML
    private Label ReportsLabel;

    @FXML
    private Button RunButton;

    @FXML
    private ComboBox<String> SecondLevelDropDown;

    @FXML
    private Label SecondLevelLabel;

    @FXML
    private Label StartDateLabel;

    @FXML
    private DatePicker StartDatePicker;

    /**
     * When a report is selected a second level of selection is made available, if
     * the report includes a date range the start and end picker are enabled
     * @param event Report selected
     */
    @FXML
    void OnActionFillSecondLevel(ActionEvent event) {
        Report selectedReport = ReportDropDown.getValue();
        reportNumber = 0;
        reportNumber = selectedReport.getID();
        
        //Find report selected and fill second level drop down
        if(reportNumber == 1){
           SecondLevelLabel.setText(labels.getString("type"));
           SecondLevelDropDown.setItems(Appointment.getAllAppointmentTypes());
           StartDatePicker.setDisable(false);
           EndDatePicker.setDisable(false);
        }
        else if(reportNumber == 2){
           SecondLevelLabel.setText(labels.getString("year"));
           SecondLevelDropDown.setItems(Appointment.getAllAppointmentYears());
           StartDatePicker.setDisable(true);
           EndDatePicker.setDisable(true);
           
        }
        else if(reportNumber == 3){
           SecondLevelLabel.setText(labels.getString("contact"));
           SecondLevelDropDown.setItems(Contact.getAllContactNames());
           StartDatePicker.setDisable(false);
           EndDatePicker.setDisable(false);
        }
        else if(reportNumber == 4){
          SecondLevelLabel.setText(labels.getString("division"));
          SecondLevelDropDown.setItems(Locations.getAllDivisionNames());
          StartDatePicker.setDisable(true);
          EndDatePicker.setDisable(true);  
        }
    }

    /**
     * Determine report selected and load appropriate display transferring info
     * @param event Run button pressed
     */
    @FXML
    void OnActionRunReport(ActionEvent event) {
        //Verify report is selected
        if(reportNumber == 0){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setHeaderText(labels.getString("error"));
           alert.setContentText(labels.getString("selectReport"));
           alert.showAndWait();    
        }  
        //Run report and open display
        else if(reportNumber == 1){
            //Get type and dates
            String type = SecondLevelDropDown.getValue();
            LocalDate start = StartDatePicker.getValue();
            LocalDate end = EndDatePicker.getValue();
            
            //Verify end date selected is after start date selected
            if(start.compareTo(end) > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("startGend"));
                alert.showAndWait();      
            }
            //Verify type and dates selected
            else if(type == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectType"));
                alert.showAndWait();    
            }
            else if(start == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectStart"));
                alert.showAndWait(); 
            }
            else if(end == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectEnd"));
                alert.showAndWait();  
            }
            else{
                //Transfer report info and load appointments by type
                try{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/AppointmentsByType.fxml"));
                    loader.load();
                    AppointmentsByTypeController ATController = loader.getController();
                    ATController.receiveInfo(type, start, end);
            
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("appointmentByType"));
                    stage.show();
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                } 
            }
        }
        else if(reportNumber == 2){
            //Get year 
            String yearString = SecondLevelDropDown.getValue();
            
            //Check for empty value
            if(yearString == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectYear"));
                alert.showAndWait(); 
            }
            else{
                int year = Integer.parseInt(yearString);
            
                //Transfer report info and load appointments by month
                try{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/AppointmentsByMonth.fxml"));
                    loader.load();
                    AppointmentsByMonthController AMController = loader.getController();
                    AMController.receiveInfo(year);
            
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("appointmentByMonth"));
                    stage.show();
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                } 
            }
        }
        else if(reportNumber == 3){
           String contactName = SecondLevelDropDown.getValue();
           LocalDate start = StartDatePicker.getValue();
           LocalDate end = EndDatePicker.getValue();
           
            //Verify end date selected is after start date selected
            if(start.compareTo(end) > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("startGend"));
                alert.showAndWait();      
            }
            //Verify contact and dates selected
            else if(contactName == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectContact"));
                alert.showAndWait();    
            }
            else if(start == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectStart"));
                alert.showAndWait(); 
            }
            else if(end == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectEnd"));
                alert.showAndWait();  
            }
            else{
                Contact contact = Contact.findContact(contactName);
            
                //Transfer report info and load contact schedule
                try{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/ContactSchedule.fxml"));
                    loader.load();
                    ContactScheduleController CSController = loader.getController();
                    CSController.receiveInfo(contact, start, end);
            
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("contactSchedule"));
                    stage.show();
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }   
            }
        }
        else if(reportNumber == 4){
            String divisionName = SecondLevelDropDown.getValue();
            
            //Verify division selected
            if(divisionName == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectDivision"));
                alert.showAndWait();  
            }
            else{
                FirstLevelDivision division = FirstLevelDivision.findDivision(divisionName);
            
                //Transfer report info and load customers by division report
                try{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/CustomersByDivision.fxml"));
                    loader.load();
                    CustomersByDivisionController CDController = loader.getController();
                    CDController.receiveDivision(division);
            
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("custByDivision"));
                    stage.show();
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }  
            }
        }
    }
    
    /**
     * Used to return back to main menu
     * @param event exit button pressed
     */
    @FXML
    void OnActionReturnToLaunch(ActionEvent event) {
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
     * Used to set language and location for reporting menu
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set local text
        EndDateLabel.setText(labels.getString("endDate"));
        ExitButton.setText(labels.getString("exit"));
        ReportTypeLabel.setText(labels.getString("reportType"));
        ReportsLabel.setText(labels.getString("reporting"));
        RunButton.setText(labels.getString("run"));
        StartDateLabel.setText(labels.getString("startDate")); 
        
        //Set report drop down with available reports
        ReportDropDown.setItems(Report.getAllReports());
    }
}
