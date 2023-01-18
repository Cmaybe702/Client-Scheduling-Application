/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.AppointmentMonth;
import java.net.URL;
import java.time.ZoneId;
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
 * View of report that displays appointments scheduled each month for a year set in reporting
 * @author courtney
 */
public class AppointmentsByMonthController implements Initializable{
    private Stage stage;
    private Parent scene;
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    @FXML
    private TableColumn<AppointmentMonth, Long> AppointmentsColumn;

    @FXML
    private Button ExitButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private TableView<AppointmentMonth> MonthAppointmentView;

    @FXML
    private TableColumn<AppointmentMonth, String> MonthColumn;

    @FXML
    private Label MonthlyAppointmentsLabel;

    @FXML
    private Label YearLabel;

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
     * Used to set up view with location and language from locale
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
        MonthColumn.setText(labels.getString("month"));
        MonthlyAppointmentsLabel.setText(labels.getString("monthlyAppointments"));
        
        //Set table data
        MonthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        AppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("numScheduled"));
    }
    
    /**
     * Used to receive selection from reporting display and generate labels and report
     * @param year Year selected in reporting
     */
    public void receiveInfo(int year){
        //Set display label
        String display = labels.getString("customerLabel") + " " + year;
        YearLabel.setText(display);
        
        //Generate report
        MonthAppointmentView.setItems(AppointmentMonth.createMonths(year));
    }
}
