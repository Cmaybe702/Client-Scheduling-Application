/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Schedule;
import Model.ScheduleDay;
import Model.TimeSlot;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Controller for week view of schedule
 * @author courtney
 */
public class ScheduleController implements Initializable {
    private Stage stage;
    private Parent scene;
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private boolean dateTransferred = false;
    private ScheduleDay dayTransferred;
    private LocalDate firstDate;
    private LocalDate lastDate;
    
    @FXML
    private Button AddAppointment;
    
    @FXML
    private Button AllAppointmentsButton;
    
    @FXML
    private Button ExitButton;

    @FXML
    private Button InfoButton;

    @FXML
    private Label LocationLabel;

    @FXML
    private RadioButton MonthViewRadio;

    @FXML
    private DatePicker ScheduleDatePicker;

    @FXML
    private Label ScheduleLabel;

    @FXML
    private TableView<ScheduleDay> WeekDisplay;

    @FXML
    private RadioButton WeekViewRadio;
    
    @FXML
    private TableColumn<ScheduleDay, String> DateColumn;
    
    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot10Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot11Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot12Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot13Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot14Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot15Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot1Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot2Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot3Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot4Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot5Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot6Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot7Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot8Column;

    @FXML
    private TableColumn<ScheduleDay, TimeSlot> TimeSlot9Column;

    /**
     * Used to change to month view to view available slots by day
     * @param event month view radio button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionChangeToMonthView(ActionEvent event) throws IOException {
        try{
            // Load month schedule
            stage = (Stage) ((RadioButton) event.getSource()).getScene().getWindow();
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
     * Used to change week being displayed
     * @param event new Sunday selected in date picker
     */
    @FXML
    void OnActionChangeWeek(ActionEvent event) {
        LocalDate sunday = ScheduleDatePicker.getValue();
        Schedule.setWeekScheduleDays(sunday);
    }

    /**
     * Used to get appointment info for day selected
     * @param event Info button pressed
     * @throws IOException if view can not be loaded
     */
    @FXML
    void OnActionGetInfo(ActionEvent event) throws IOException {
        try{
            ScheduleDay selected = WeekDisplay.getSelectionModel().getSelectedItem();
            LocalDate date = selected.getDate();
            
            //Verify week was selected
            if(date == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(labels.getString("error"));
                alert.setContentText(labels.getString("selectWeek"));
                alert.showAndWait(); 
            }
            else{
                //Load schedule day and transfer date
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../View/DaySchedule.fxml"));
                loader.load();
                DayScheduleController DSController = loader.getController();
                DSController.receiveDate(date);
            
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(labels.getString("daySchedule"));
                stage.show();
            }
        }catch(NullPointerException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Used to transfer to add appointment
     * @param event Add button pressed
     * @throws IOException if view can not be loaded 
     */
    @FXML
    void OnActionNewAppointment(ActionEvent event) throws IOException {
        try{
            ScheduleDay selected = WeekDisplay.getSelectionModel().getSelectedItem();
            LocalDate date = selected.getDate();
            
            //If no date selected transfer current date
            if(date == null){
                date = LocalDate.now();
            }
            
            //Load add appointment and transfer date
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/AddAppointment.fxml"));
            loader.load();
            AddAppointmentController AAController = loader.getController();
            AAController.receiveDate(date, false, true);
            
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(labels.getString("newAppointment"));
            stage.show();
            
        }catch(NullPointerException ex){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText(labels.getString("invalidDate"));
           alert.showAndWait(); 
        }
    }

    /**
     * Opens view with all appointments for the week
     * @param event appointments button pressed
     */
    @FXML
    void OnActionGetAll(ActionEvent event) {
        boolean fromWeek = true;
        boolean fromMonth = false;
        
        firstDate = ScheduleDatePicker.getValue();
        lastDate = firstDate.plusDays(6);
        
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
     * Used to set up schedule with current week info and set local language
     * @param url resource locator
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Location Label
        String zone = ZoneId.systemDefault().toString();
        LocationLabel.setText(zone);
        
        //Set datepicker to Sundays only
        Callback<DatePicker, DateCell> dayCellFactory = this.onlySunday();
        ScheduleDatePicker.setDayCellFactory(dayCellFactory);
        
        //Set local text
        AddAppointment.setText(labels.getString("add"));
        AllAppointmentsButton.setText(labels.getString("appointments"));
        ExitButton.setText(labels.getString("exit"));
        InfoButton.setText(labels.getString("info"));
        MonthViewRadio.setText(labels.getString("monthView"));
        ScheduleLabel.setText(labels.getString("schedule"));
        WeekViewRadio.setText(labels.getString("weekView"));
        DateColumn.setText(labels.getString("date"));
        
        //If date isn't transferred set current day
        ScheduleDay day;
        if(!dateTransferred){
            day = setWeek(LocalDate.now());
        }
        else{
            day = dayTransferred;
        }
        
        //Set timeslots
        TimeSlot1Column.setText(day.getTimeSlot1().getTimeRange());
        TimeSlot2Column.setText(day.getTimeSlot2().getTimeRange());
        TimeSlot3Column.setText(day.getTimeSlot3().getTimeRange());
        TimeSlot4Column.setText(day.getTimeSlot4().getTimeRange());
        TimeSlot5Column.setText(day.getTimeSlot5().getTimeRange());
        TimeSlot6Column.setText(day.getTimeSlot6().getTimeRange());
        TimeSlot7Column.setText(day.getTimeSlot7().getTimeRange());
        TimeSlot8Column.setText(day.getTimeSlot8().getTimeRange());
        TimeSlot9Column.setText(day.getTimeSlot9().getTimeRange());
        TimeSlot10Column.setText(day.getTimeSlot10().getTimeRange());
        TimeSlot11Column.setText(day.getTimeSlot11().getTimeRange());
        TimeSlot12Column.setText(day.getTimeSlot12().getTimeRange());
        TimeSlot13Column.setText(day.getTimeSlot13().getTimeRange());
        TimeSlot14Column.setText(day.getTimeSlot14().getTimeRange());
        TimeSlot15Column.setText(day.getTimeSlot15().getTimeRange());
        
        //Set table info
        WeekDisplay.setItems(Schedule.getAppointmentsByDay());
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        TimeSlot1Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot1"));
        TimeSlot2Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot2"));
        TimeSlot3Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot3"));
        TimeSlot4Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot4"));
        TimeSlot5Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot5"));
        TimeSlot6Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot6"));
        TimeSlot7Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot7"));
        TimeSlot8Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot8"));
        TimeSlot9Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot9"));
        TimeSlot10Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot10"));
        TimeSlot11Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot11"));
        TimeSlot12Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot12"));
        TimeSlot13Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot13"));
        TimeSlot14Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot14"));
        TimeSlot15Column.setCellValueFactory(new PropertyValueFactory<>("TimeSlot15"));
    }
    
    /**
     * Used to filter date picker to only Sundays
     * @return Callback
     */
    private Callback<DatePicker, DateCell> onlySunday(){
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.getDayOfWeek() != DayOfWeek.SUNDAY){
                   setDisable(true);
                   setStyle("-fx-background-color: #D3D3D3");
                }
            }
        };
        return dayCellFactory;
    }
    
    /**
     * Used to transfer a date from another controller
     * @param date Date in week to be displayed
     */
    public void receiveDate(LocalDate date){
        dateTransferred = true;
        ScheduleDay sunday = setWeek(date); 
        dayTransferred = sunday;
    }
    
    /**
     * Find Sunday of the week and set Monday-Saturday
     * @param date date sent from another controller
     * @return Sunday of the week being displayed
     */
    public ScheduleDay setWeek(LocalDate date){
        LocalDate sunday = null;
        
        if(date.getDayOfWeek()!= DayOfWeek.SUNDAY){
           sunday = date.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)); 
        }
        else{
          sunday = date;  
        }
        ScheduleDay day = Schedule.setWeekScheduleDays(sunday);
        ScheduleDatePicker.setValue(sunday);
        
        //Set first and last date of the month
        firstDate = sunday;
        lastDate = sunday.plusDays(6);
        
        return day;
    }
}
