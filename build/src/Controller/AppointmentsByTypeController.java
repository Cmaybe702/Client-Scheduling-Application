/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Appointment;
import Model.AppointmentByType;
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
 * Controller for report that displays appointments scheduled by type for a range of dates
 * selected in reporting
 * @author courtney
 */
public class AppointmentsByTypeController implements Initializable{
    private Stage stage;
    private Parent scene;
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private TableColumn<AppointmentByType, Long> AppointmentsColumn;

    @FXML
    private Button ExitButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private TableView<AppointmentByType> AppointmentTypeView;

    @FXML
    private TableColumn<AppointmentByType, String> DateColumn;

    @FXML
    private Label AppointmentsByTypeLabel;

    @FXML
    private Label TypeAndRangeLabel;

    /**
     * Used to return back to report view
     * @param event exit button pressed
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
     * /Used to set up view with location and language from locale
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set langugage
        AppointmentsColumn.setText(labels.getString("appointments"));
        ExitButton.setText(labels.getString("exit"));
        DateColumn.setText(labels.getString("date"));
        AppointmentsByTypeLabel.setText(labels.getString("appointmentByType"));
        
        //Set table data
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        AppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("numAppointments"));
    }
    
    /**
     * Used to receive selection from reporting display and generate labels and report
     * @param type type filtered for in report
     * @param start start date of report
     * @param end end date of report
     */
    public void receiveInfo(String type, LocalDate start, LocalDate end){
        //Set display label
        DateTimeFormatter dtf = Appointment.getDateFormatter();
        String startFormatted = start.format(dtf);
        String endFormatted = end.format(dtf);
        String days = startFormatted + "-" + endFormatted;
        String display = labels.getString("type") + ": " + type + "\n" + days;
        TypeAndRangeLabel.setText(display);
        
        //Generate report
        AppointmentTypeView.setItems(AppointmentByType.createAppointmentsByTypeReport(type, start, end));
    }
}
