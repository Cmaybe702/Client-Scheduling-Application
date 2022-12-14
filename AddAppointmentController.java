/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.Contact;
import Model.Schedule;
import Model.Customer;
import Model.CustomerList;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for view to add new appointments
 * @author courtney
 */
public class AddAppointmentController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Customer custToSchedule;
    private LocalDate dateToSchedule;
    private boolean customerTransferred = false;
    private boolean dateTransferred = false;
    private boolean fromWeek = false;
    private boolean fromMonth = false;
    private final Locale userLocale = Locale.getDefault();
    private final ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private Button AddAppointmentButton;

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
    private Label NewAppointmentLabel;

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
     * Used to set up add appointment view and set text to locale
     * @param url resource locator
     * @param rb resource bundle 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        AddAppointmentButton.setText(labels.getString("add"));
        CancelButton.setText(labels.getString("cancel"));
        ContactLabel.setText(labels.getString("contact"));
        CustomerLabel.setText(labels.getString("customer"));
        DateLabel.setText(labels.getString("date"));
        DescriptionLabel.setText(labels.getString("description"));
        NewAppointmentLabel.setText(labels.getString("newAppointment"));
        TimeLabel.setText(labels.getString("time"));
        TitleLabel.setText(labels.getString("title"));
        TypeLabel.setText((labels.getString("type")));
        ContactDropDown.setItems(Contact.getAllContacts());
        DateSelection.setValue(LocalDate.now());
        
        if(!dateTransferred){
            //Set time drop down with current date times
            Schedule.filterTimesByDate(LocalDate.now());
            TimeDropDown.setItems(Schedule.getAvailableTimesByDate());
        }
    }
    
    /**
     * Used to add a new appointment to mySQL and view
     * @param event Action Event that triggers addition
     * @throws SQLException if mySQL can not be reached
     */
    @FXML
    void OnActionAddAppointment(ActionEvent event) throws SQLException {
        String type = TypeField.getText();
        String title = TitleField.getText();
        String description = DescriptionField.getText();
        String location = LocationField.getText();
        Contact contact = ContactDropDown.getValue();
        LocalDate date = DateSelection.getValue();
        String time = TimeDropDown.getValue();
        Appointment appointment = null;
        LocalDate now = LocalDate.now();
        
        //Can not schedule an appointment in the past
        if(date.compareTo(now) < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setHeaderText(labels.getString("error"));
           alert.setContentText(labels.getString("appointmentDateError"));
           alert.showAndWait();   
        }
        //Check to see if time is selected
        else if(contact == null){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setHeaderText(labels.getString("error"));
           alert.setContentText(labels.getString("selectContact"));
           alert.showAndWait();    
        }
        //Check to see a contact is selected
        else if(time == null){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setHeaderText(labels.getString("error"));
           alert.setContentText(labels.getString("selectTime"));
           alert.showAndWait();  
        }
        //If no date selected date is current date
        else if(date == null){
            date = LocalDate.now();
        }
        //All required filled createAppointment
        else{ 
            if(customerTransferred){
            
                appointment = new Appointment(title, description, location, type, date,
                time, custToSchedule, contact);
           
                Schedule.userAddsAppointment(appointment);
              
                //Update appointment filter for view
                Schedule.filterAppointmentsbyCustomer(custToSchedule);
        
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
            else{
                String customerName = CustomerField.getText();
                custToSchedule = CustomerList.findCustomer(customerName);
             
                //If date selection wasn't used date is current date
                if(date == null){
                date = LocalDate.now();
                }
                
                //Verify customer found
                if(custToSchedule == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(labels.getString("error"));
                    alert.setContentText(labels.getString("invalidCustomer"));
                    alert.showAndWait();   
                }
                else{
                    appointment = new Appointment(title, description, location, type, date,
                    time, custToSchedule, contact);  
               
                    Schedule.userAddsAppointment(appointment);
               
                    try{
                        //If no date was transferred date is current
                        if(dateToSchedule == null) {
                            dateToSchedule = LocalDate.now();
                        }
                        if(fromWeek){
                            //if from week view
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../View/Schedule.fxml"));
                            loader.load();
                            ScheduleController SController = loader.getController();
                            SController.receiveDate(dateToSchedule);
                    
                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = loader.getRoot();
                            stage.setScene(new Scene(scene));
                            stage.setTitle(labels.getString("schedule"));
                            stage.show();
                        }
                        else if(fromMonth){
                            //if from month view
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../View/MonthSchedule.fxml"));
                            loader.load();
                            MonthScheduleController MSController = loader.getController();
                            MSController.receiveDate(dateToSchedule);
                    
                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = loader.getRoot();
                            stage.setScene(new Scene(scene));
                            stage.setTitle(labels.getString("schedule"));
                            stage.show();
                        }
                        else{
                            //from day
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../View/DaySchedule.fxml"));
                            loader.load();
                            DayScheduleController DSController = loader.getController();
                            DSController.receiveDate(dateToSchedule);
                    
                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = loader.getRoot();
                            stage.setScene(new Scene(scene));
                            stage.setTitle(labels.getString("schedule"));
                            stage.show();
                        }
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Used to return to previous display
     * @param event Action Event that triggered return
     * @throws IOException if target view can not be loaded
     */
    @FXML
    void OnActionReturn(ActionEvent event) throws IOException {
        if(customerTransferred){
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
        else{
            //Load week schedule
            try{
                //If no date was transferred date is current
                if(dateToSchedule == null) {
                    dateToSchedule = LocalDate.now();
                }
                if(fromWeek){
                    //if from week view
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/Schedule.fxml"));
                    loader.load();
                    ScheduleController SController = loader.getController();
                    SController.receiveDate(dateToSchedule);
                    
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("schedule"));
                    stage.show();
                }
                else if(fromMonth){
                    //if from month view
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/MonthSchedule.fxml"));
                    loader.load();
                    MonthScheduleController MSController = loader.getController();
                    MSController.receiveDate(dateToSchedule);
                    
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("schedule"));
                    stage.show();
                }
                else{
                    //from day
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/DaySchedule.fxml"));
                    loader.load();
                    DayScheduleController DSController = loader.getController();
                    DSController.receiveDate(dateToSchedule);
                    
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.setTitle(labels.getString("schedule"));
                    stage.show();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            } 
        }
    }
    
    /**
     * Used to set times to only what is available for the day
     * @param event Action Event of date being selected
     */
    @FXML
    void OnActionGetTimes(ActionEvent event) {
        LocalDate selectedDate = DateSelection.getValue();
        dateToSchedule = selectedDate;
        Schedule.filterTimesByDate(selectedDate);
        TimeDropDown.getItems().clear();
        TimeDropDown.setItems(Schedule.getAvailableTimesByDate());
    }
    
    /**
     * Used to transfer a customer from customer appointment display for scheduling
     * @param customer Customer being used
     */
    public void receiveCustomer(Customer customer){
        custToSchedule = customer;
        CustomerField.setText(customer.getCustomerName());
        CustomerField.setEditable(false);
        customerTransferred = true;
    }
    
    /**
     * Used to transfer a date from schedule display for scheduling. Boolean values are used to 
     * transfer back to correct controller after operations are complete
     * @param date date being scheduled
     * @param fromMonth Boolean indicating transfer from month view
     * @param fromWeek Boolean indicating transfer from week view
     */
    public void receiveDate(LocalDate date, boolean fromMonth, boolean fromWeek){
        dateToSchedule = date;
        DateSelection.setValue(date);
        dateTransferred = true;
        this.fromWeek = fromWeek;
        this.fromMonth = fromMonth;
        
        //Filter times for date
        Schedule.filterTimesByDate(date);
        TimeDropDown.setItems(Schedule.getAvailableTimesByDate());
    }
}
