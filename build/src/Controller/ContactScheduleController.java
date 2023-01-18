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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller for report that shows a schedule for a contact and date range selected
 * in the reporting display
 * @author courtney
 */
public class ContactScheduleController implements Initializable{
    private Stage stage;
    private Parent scene;
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private TableColumn<Appointment, Integer> AppointmentIDColumn;

    @FXML
    private Label ContactLabel;

    @FXML
    private TableView<Appointment> ContactScheduleDisplay;

    @FXML
    private Label ContactScheduleLabel;

    @FXML
    private TableColumn<Appointment, Customer> CustomerColumn;
    
    @FXML
    private TableColumn<Appointment, Integer> CustomerIDColumn;

    @FXML
    private TableColumn<Appointment, String> DateColumn;

    @FXML
    private TableColumn<Appointment, String> DescriptionColumn;

    @FXML
    private Button ExitButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private TableColumn<Appointment, String> TimeColumn;

    @FXML
    private TableColumn<Appointment, String> TitleColumn;

    @FXML
    private TableColumn<Appointment, String> TypeColumn;

    /**
     * Used to return back to reporting display
     * @param event Exit button was pressed
     */
    @FXML
    void OnActionReturnToReports(ActionEvent event) {
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
     * Used to set up locale language for labels, time zone of user, and assign table columns
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set text
        AppointmentIDColumn.setText(labels.getString("appointmentID1"));
        DateColumn.setText(labels.getString("date"));
        DescriptionColumn.setText(labels.getString("description"));
        CustomerColumn.setText(labels.getString("customer"));
        CustomerIDColumn.setText(labels.getString("customerID"));
        TimeColumn.setText(labels.getString("time"));
        TitleColumn.setText(labels.getString("title"));
        TypeColumn.setText(labels.getString("type"));
        ExitButton.setText(labels.getString("exit"));
        
        //Get Table Info
        AppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }
    
    /**
     * Used to receive selection from reporting display and generate labels and report
     * @param contact Contact schedule is created for
     * @param startDate Start date of schedule
     * @param endDate End date of schedule
     */
    public void receiveInfo(Contact contact, LocalDate startDate, LocalDate endDate){
        //Set display label
        DateTimeFormatter dtf = Appointment.getDateFormatter();
        String contactName = contact.getContactName();
        String startFormatted = startDate.format(dtf);
        String endFormatted = endDate.format(dtf);
        String dates = startFormatted + "-" + endFormatted;
        String display = labels.getString("customerLabel") + " " + contactName + "\n" + dates;
        ContactLabel.setText(display);
        
        //Generate report
        ContactScheduleDisplay.setItems(Schedule.getAppointmentsByContact(contact, startDate, endDate));
    }
}