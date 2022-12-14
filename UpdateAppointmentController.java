/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.Contact;
import Model.Schedule;
import Model.Customer;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for view to update appointment fields
 * @author courtney
 */
public class UpdateAppointmentController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Customer custToSchedule;
    private boolean fromCustomerDisplay;
    private boolean fromWeek = false;
    private boolean fromMonth = false;
    private LocalDate dateTransferred;
    private Appointment appointmentToUpdate;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private DateTimeFormatter dtf = Appointment.getDateFormatter();
    
    @FXML
    private TextField AppointmentIDField;
    
    @FXML
    private Label AppointmentIDLabel;
    
    @FXML
    private Button UpdateAppointmentButton;

    @FXML
    private Button CancelButton;

    @FXML
    private ComboBox<Contact> ContactDropDown;

    @FXML
    private Label ContactLabel;

    @FXML
    private TextField CustomerField;

    @FXML
    private Label CustomerLabel;

    @FXML
    private DatePicker DateSelection;

    @FXML
    private Label DateLabel;

    @FXML
    private TextField DescriptionField;

    @FXML
    private Label DescriptionLabel;

    @FXML
    private TextField LocationField;

    @FXML
    private Label LocationLabel;

    @FXML
    private Label UpdateAppointmentLabel;

    @FXML
    private ComboBox<String> TimeDropDown;

    @FXML
    private Label TimeLabel;

    @FXML
    private TextField TitleField;

    @FXML
    private Label TitleLabel;

    @FXML
    private TextField TypeField;

    @FXML
    private Label TypeLabel;

    /**
     * Used to set local language info and fill fields with current appointment info
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        AppointmentIDLabel.setText(labels.getString("appointmentID1"));
        UpdateAppointmentButton.setText(labels.getString("update"));
        CancelButton.setText(labels.getString("cancel"));
        ContactLabel.setText(labels.getString("contact"));
        CustomerLabel.setText(labels.getString("customer"));
        DateLabel.setText(labels.getString("date"));
        DescriptionLabel.setText(labels.getString("description"));
        UpdateAppointmentLabel.setText(labels.getString("updateAppointment"));
        TimeLabel.setText(labels.getString("time"));
        TitleLabel.setText(labels.getString("title"));
        TypeLabel.setText((labels.getString("type")));
        ContactDropDown.setItems(Contact.getAllContacts());
    }
    
    /**
     * Used to get updated info, send for updating in mySQL/view, and transfer back to previous view
     * @param event update button pressed
     * @throws SQLException if mySQL can not be reached 
     */
    @FXML
    void OnActionUpdateAppointment(ActionEvent event) throws SQLException {
        int appointmentID = appointmentToUpdate.getAppointmentID();
        String type = TypeField.getText();
        String title = TitleField.getText();
        String description = DescriptionField.getText();
        String location = LocationField.getText();
        Contact contact = ContactDropDown.getValue();
        LocalDate date = DateSelection.getValue();
        String time = TimeDropDown.getValue();
        String formattedDate = date.format(dtf);
        
        appointmentToUpdate.setType(type);
        appointmentToUpdate.setTitle(title);
        appointmentToUpdate.setDescription(description);
        appointmentToUpdate.setLocation(location);
        appointmentToUpdate.setContact(contact);
        appointmentToUpdate.setFormattedDate(formattedDate);
        LocalDate now = LocalDate.now();
        
        //Can not schedule an appointment in the past
        if(date.compareTo(now) < 0){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setHeaderText(labels.getString("error"));
           alert.setContentText(labels.getString("appointmentDateError"));
           alert.showAndWait();   
        }
        else{
            appointmentToUpdate.setDate(date);
            appointmentToUpdate.setTime(time);
            
            int dashIndex = time.indexOf('-');
            String startTime = time.substring(0, dashIndex);
            startTime = startTime.concat(" " + date.toString());
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            ZonedDateTime startZoned = start.atZone(ZoneId.systemDefault());
            appointmentToUpdate.setStart(startZoned);
            ZonedDateTime endZoned = startZoned.plusHours(1);  
            appointmentToUpdate.setEnd(endZoned);
            
            Schedule.userUpdatesAppointment(appointmentToUpdate);
        }

        if(fromCustomerDisplay){
            //Load customer appointment view
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/CustomerAppointments.fxml"));
                loader.load();
                CustomerAppointmentsController CAController = loader.getController();
                CAController.receiveCustomer(custToSchedule);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("customerAppointments"));
                stage.show();
            
            }catch(Exception e){
                System.out.println(e.getMessage());
            }    
        }
        else if(fromMonth){
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
        else if(fromWeek){
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
        else{   
            //Load scheudle day view
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/DaySchedule.fxml"));
                loader.load();
                DayScheduleController DSController = loader.getController();
                LocalDate thisDate = DateSelection.getValue();
                DSController.receiveDate(thisDate);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("daySchedule"));
                stage.show();
            
            }catch(Exception e){
                System.out.println(e.getMessage());
            }    
        }
    }

    /**
     * Used to return back to previous display
     * @param event cancel button pressed
     */
    @FXML
    void OnActionReturn(ActionEvent event) {
        if(fromCustomerDisplay){
            //Load customer appointment view
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/CustomerAppointments.fxml"));
                loader.load();
                CustomerAppointmentsController CAController = loader.getController();
                CAController.receiveCustomer(custToSchedule);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("customerAppointments"));
                stage.show();
            
            }catch(Exception e){
                System.out.println(e.getMessage());
            }    
        }
        else if(fromMonth){
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
        else if(fromWeek){
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
        else{   
            //Load scheudle day view
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/DaySchedule.fxml"));
                loader.load();
                DayScheduleController DSController = loader.getController();
                LocalDate date = DateSelection.getValue();
                DSController.receiveDate(date);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("daySchedule"));
                stage.show();
            
            }catch(Exception e){
                System.out.println(e.getMessage());
            }    
        }    
    }
    
    /**
     * Used to get available times for the day selected in the date picker
     * @param event Date selected in date picker
     */
    @FXML
    void OnActionGetTimes(ActionEvent event) {
        LocalDate selectedDate = DateSelection.getValue();
        Schedule.filterTimesByDate(selectedDate);
        TimeDropDown.setValue(null);
        TimeDropDown.getItems().clear();
        TimeDropDown.setItems(Schedule.getAvailableTimesByDate());
    }
    
    /**
     * Used to transfer an appointment for editing
     * @param appointment the appointment to update
     * @param fromCustomerDisplay Boolean indicating if the request came from the customer appointment display
     * @param fromWeek Boolean indicating if the request came from the schedule week display
     * @param fromMonth Boolean indicating if the request came from the schedule month display
     */
    public void receiveAppointment(Appointment appointment, boolean fromCustomerDisplay, boolean fromWeek, boolean fromMonth){
        this.fromCustomerDisplay = fromCustomerDisplay;
        this.fromWeek = fromWeek;
        this.fromMonth = fromMonth;
        appointmentToUpdate = appointment;
        
        //Set customer field
        custToSchedule = appointment.getCustomer();
        CustomerField.setText(custToSchedule.getCustomerName());
        
        //If from customer view customer is not editable
        if(fromCustomerDisplay){
            CustomerField.setEditable(false);
            CustomerField.setDisable(true);
        }
        //If from week view of appointments set sunday for transfer back
        if(fromWeek){
            LocalDate appointmentDate = appointment.getDate();
            dateTransferred = appointmentDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        }
        //If from month view of appointments set 1st for transfer back
        if(fromMonth){
           LocalDate appointmentDate = appointment.getDate();
           dateTransferred = appointmentDate.withDayOfMonth(1);
        }
        
        String appointmentIDString = Integer.toString(appointment.getAppointmentID());
        AppointmentIDField.setText(appointmentIDString);
        TypeField.setText(appointment.getType());
        TitleField.setText(appointment.getTitle());
        DescriptionField.setText(appointment.getDescription());
        LocationField.setText(appointment.getLocation());
        ContactDropDown.setValue(appointment.getContact());
        
        //Set date picker with current appointment date & provide available times
        DateSelection.setValue(appointment.getDate());
        Schedule.filterTimesByDate(appointment.getDate());
        TimeDropDown.setItems(Schedule.getAvailableTimesByDate());
        TimeDropDown.setValue(appointment.getTime());
    }
}
