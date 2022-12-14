/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Schedule;
import Model.ScheduleWeek;
import java.io.IOException;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller for schedule month view
 * @author courtney
 */
public class MonthScheduleController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private LocalDate dayTransferred;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private boolean dateTransferred = false;
    
    @FXML
    private Button AddAppointment;

     @FXML
    private Button AllAppointmentsButton;
     
    @FXML
    private Button ExitButton;

    @FXML
    private TableColumn<ScheduleWeek, String> FridayColumn;

    @FXML
    private Button InfoButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private TableColumn<ScheduleWeek, String> MondayColumn;

    @FXML
    private RadioButton MonthViewRadio;

    @FXML
    private TableColumn<ScheduleWeek, String> SaturdayColumn;

    @FXML
    private DatePicker ScheduleDatePicker;

    @FXML
    private Label ScheduleLabel;

    @FXML
    private TableColumn<ScheduleWeek, String> SundayColumn;

    @FXML
    private TableColumn<ScheduleWeek, String> ThursdayColumn;

    @FXML
    private TableColumn<ScheduleWeek, String> TuesdayColumn;

    @FXML
    private TableColumn<ScheduleWeek, String> WednesdayColumn;

    @FXML
    private TableColumn<ScheduleWeek, String> WeekColumn;

   @FXML
    private TableView<ScheduleWeek> MonthDisplay;

    @FXML
    private RadioButton WeekViewRadio;

    /**
     * Used to change to week view of schedule
     * @param event week view radio button selected
     */
    @FXML
    void OnActionChangeToWeek(ActionEvent event) {
        try{
            // Load week schedule
            stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/Schedule.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("schedule"));
            stage.show();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Used to change month view to another month selected in date picker
     * @param event new first picked in date picker
     */
    @FXML
    void OnActionChangeMonth(ActionEvent event) {
        LocalDate first = ScheduleDatePicker.getValue();
        Schedule.createAvailabilityByMonth(first);
    }
    
    /**
     * Used to get more detailed information about the week selected
     * @param event info button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionGetInfo(ActionEvent event) throws IOException {
        try{
            ScheduleWeek selected = MonthDisplay.getSelectionModel().getSelectedItem();
            LocalDate date = selected.getFirstDateOfWeek();
            
            //Load schedule(week view) and transfer date
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/Schedule.fxml"));
            loader.load();
            ScheduleController SController = loader.getController();
            SController.receiveDate(date);
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("schedule"));
            stage.show();
        }catch(NullPointerException ex){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText(labels.getString("invalidWeek"));
           alert.showAndWait(); 
        }
    }
    
    /**
     * Used to open view to create a new appointment
     * @param event add button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionNewAppointment(ActionEvent event) throws IOException {
        LocalDate firstSelected = ScheduleDatePicker.getValue();
        
        //If no date was selected get first of current date
        if(firstSelected == null){
            firstSelected = LocalDate.now();
        }
        
        try{
            //Load add appointment and transfer date
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/AddAppointment.fxml"));
            loader.load();
            AddAppointmentController AAController = loader.getController();
            AAController.receiveDate(firstSelected, true, false);
            
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
     * Used to return to main menu from schedule
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
     * Opens display with all appointments for the month
     * @param event appointments button pressed
     */
    @FXML
    void OnActionGetAll(ActionEvent event) {
        boolean fromWeek = false;
        boolean fromMonth = true;
        
        firstDate = ScheduleDatePicker.getValue();
        lastDate = firstDate.withDayOfMonth(firstDate.getMonth().maxLength());
        
        try{
            //Load add appointment and transfer date
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/DaySchedule.fxml"));
            loader.load();
            DayScheduleController DSController = loader.getController();
            DSController.receiveDateRange(firstDate, lastDate, fromWeek, fromMonth);
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("appointments"));
            stage.show();
            
        }catch(Exception ex){
           System.out.println(ex.getMessage()); 
        }
    }
    
    /**
     * Used to setup view for month
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set datepicker to only the first of each month
        Callback<DatePicker, DateCell> dayCellFactory = this.onlyFirst();
        ScheduleDatePicker.setDayCellFactory(dayCellFactory);
        
        //Set local text
        AddAppointment.setText(labels.getString("add"));
        AllAppointmentsButton.setText(labels.getString("appointments"));
        ExitButton.setText(labels.getString("exit"));
        InfoButton.setText(labels.getString("info"));
        MonthViewRadio.setText(labels.getString("monthView"));
        ScheduleLabel.setText(labels.getString("schedule"));
        WeekViewRadio.setText(labels.getString("weekView"));
        WeekColumn.setText(labels.getString("week"));
        SundayColumn.setText(labels.getString("sunday"));
        MondayColumn.setText(labels.getString("monday"));
        TuesdayColumn.setText(labels.getString("tuesday"));
        WednesdayColumn.setText(labels.getString("wednesday"));
        ThursdayColumn.setText(labels.getString("thursday"));
        FridayColumn.setText(labels.getString("friday"));
        SaturdayColumn.setText(labels.getString("saturday"));
        
        //Set table info
        MonthDisplay.setItems(Schedule.getMonthAvailability());
        WeekColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));
        SundayColumn.setCellValueFactory(new PropertyValueFactory<>("Sunday"));
        MondayColumn.setCellValueFactory(new PropertyValueFactory<>("Monday"));
        TuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("Tuesday"));
        WednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("Wednesday"));
        ThursdayColumn.setCellValueFactory(new PropertyValueFactory<>("Thursday"));
        FridayColumn.setCellValueFactory(new PropertyValueFactory<>("Friday"));
        SaturdayColumn.setCellValueFactory(new PropertyValueFactory<>("Saturday"));
        
        //If a date was not transferred, set to current month
        if(!dateTransferred){
            LocalDate first = setMonth(LocalDate.now());
            Schedule.createAvailabilityByMonth(first);
        }
    }
    
    /**
     * Used to set date picker to only the first of each month
     * @return callback 
     */ 
    private Callback<DatePicker, DateCell> onlyFirst(){
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.getDayOfMonth() != 1){
                   setDisable(true);
                   setStyle("-fx-background-color: #D3D3D3");
                }
            }
        };
        return dayCellFactory;
    }
    
    /**
     * Used to transfer a date from week view
     * @param date date to be displayed
     */
    public void receiveDate(LocalDate date){
        dateTransferred = true;
        LocalDate first = setMonth(date); 
        dayTransferred = first;
        Schedule.createAvailabilityByMonth(first);
    }
    
    /**
     * Find 1st of month and set schedule
     * @param date date selected in another view
     * @return the first day of the month of month selected
     */
    public LocalDate setMonth(LocalDate date){
        LocalDate first = null;
        
        if(date.getDayOfMonth()!= 1){
           first = date.withDayOfMonth(1); 
        }
        else{
          first = date;  
        }
        ScheduleDatePicker.setValue(first);
        
        firstDate = first;
        lastDate = first.withDayOfMonth(date.getMonth().maxLength());
        
        return first;
    }
}
