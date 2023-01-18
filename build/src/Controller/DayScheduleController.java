/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.Schedule;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
 * Controller of view that displays appointments by the day selected in the week view, or the week selected
 * in the week view, or the month selected in month view
 * @author courtney
 */
public class DayScheduleController implements Initializable{
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private LocalDate dateTransferred;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean weekView;
    private boolean monthView;
    
    @FXML
    private Button AddAppointment;

    @FXML
    private TableView<Appointment> AppointmentDisplay;

    @FXML
    private TableColumn<Appointment, Integer> AppointmentIDColumn;

    @FXML
    private Label AppointmentsLabel;

    @FXML
    private TableColumn<Appointment, Contact> ContactColumn;

    @FXML
    private TableColumn<Appointment, Customer> CustomerColumn;
    
    @FXML
    private TableColumn<Appointment, String> DateColumn;

    @FXML
    private Label DayLabel;

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
     * Used to transfer an appointment from schedule day view to modify fields
     * @param event Update button pressed
     */
    @FXML
    void OnActionModifyAppointment(ActionEvent event) {
        try{
            boolean fromCustomerDisplay = false;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/UpdateAppointment.fxml"));
            loader.load();
            UpdateAppointmentController UAController = loader.getController();
            Appointment thisAppointment = AppointmentDisplay.getSelectionModel().getSelectedItem();
            
            //Verify appointment selected
            if(thisAppointment == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectAppointment"));
                alert.showAndWait();    
            }
            else{
                UAController.receiveAppointment(thisAppointment, fromCustomerDisplay, weekView, monthView);
            
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
     * Used to create a new appointment for the day
     * @param event Add button pressed
     */
    @FXML
    void OnActionNewAppointment(ActionEvent event) {
        try{
            //Load add appointment and transfer date
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/AddAppointment.fxml"));
            loader.load();
            AddAppointmentController AAController = loader.getController();
            AAController.receiveDate(dateTransferred, false, false);
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("newAppointment"));
            stage.show();
            
        }catch(Exception ex){
           System.out.println(ex.getMessage()); 
        }
    }

    /**
     * Used to delete an appointment from the day
     * @param event delete button pressed
     */
    @FXML
    void OnActionRemoveAppointment(ActionEvent event) {
        boolean status = false;
        Appointment thisAppointment = null;
        
        thisAppointment = AppointmentDisplay.getSelectionModel().getSelectedItem();
        if(thisAppointment == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle(labels.getString("error"));
           alert.setContentText(labels.getString("noAppointmentSelected"));
           alert.showAndWait();
        }
        else{
            try {
                //Confirm delete   
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText(labels.getString("confirmAppointmentCancel"));
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    status = Schedule.deleteAppointment(thisAppointment);
               
                    //Nofify of cancellation
                    if(status){                  
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
                if(weekView || monthView){
                  Schedule.filterAppointmentsbyDates(startDate, endDate);  
                }
                else{
                    Schedule.filterAppointmentsbyDay(dateTransferred);
                }
            }
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Used to transfer back to week/month view of appointments
     * @param event exit button pressed
     */
    @FXML
    void OnActionReturnToSchedule(ActionEvent event) {
        if(monthView){
           try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/MonthSchedule.fxml"));
                loader.load();
                MonthScheduleController MSController = loader.getController();
                MSController.receiveDate(dateTransferred);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("schedule"));
                stage.show();
            }catch(Exception e){
                System.out.println(e.getMessage());
            } 
        }
        else{
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/Schedule.fxml"));
                loader.load();
                ScheduleController SController = loader.getController();
                SController.receiveDate(dateTransferred);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("schedule"));
                stage.show();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * Used to set up view with appointments filtered for the day and set local language
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
        CustomerColumn.setText(labels.getString("customer"));
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
        AppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        ContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        UserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));  
    }
    
    /**
     * Used to transfer a date from another controller
     * @param date Date being transferred for view
     */
    public void receiveDate(LocalDate date){
        dateTransferred = date;
        
        //Format date for display
        DateTimeFormatter dtf;
        if(Locale.getDefault().equals(Locale.US)){
            dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        }
        else{
           dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
        }
        String formattedDate = date.format(dtf);
        
        //Set text for label displaying date
        String displayFor = labels.getString("customerLabel") + " " + formattedDate;
        DayLabel.setText(displayFor);
        
        Schedule.filterAppointmentsbyDay(date);
        AppointmentDisplay.setItems(Schedule.getfilteredAppointments());
    }
    
    /**
     * Used to transfer week or month range to display for from schedule week/month
     * @param start date of display
     * @param end date of display
     * @param fromWeek Boolean indicating request came from week view
     * @param fromMonth Boolean indicating request came from month view
     */
    public void receiveDateRange(LocalDate start, LocalDate end, boolean fromWeek, boolean fromMonth){
        monthView = fromMonth;
        weekView = fromWeek;
        
        startDate = start;
        endDate = end;
        
        dateTransferred = startDate;
        
        //Format date for display
        DateTimeFormatter dtf;
        if(Locale.getDefault().equals(Locale.US)){
            dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        }
        else{
           dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
        }
        
        String formattedStart = start.format(dtf);
        String formattedEnd = end.format(dtf);
        String range = formattedStart + "-" + formattedEnd;
        
        //Set text for label displaying date
        String displayFor = labels.getString("customerLabel") + " " + range;
        DayLabel.setText(displayFor);
        
        Schedule.filterAppointmentsbyDates(start, end);
        AppointmentDisplay.setItems(Schedule.getfilteredAppointments());
    }
}

