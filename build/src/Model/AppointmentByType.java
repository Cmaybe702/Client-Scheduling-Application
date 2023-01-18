/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents the appointment by type report that is generated in the reporting display. 
 * Returns the number of appointments scheduled per day that match the type and a total for the date range
 * @author courtney
 */
public class AppointmentByType {
    private static ObservableList<AppointmentByType> AppointmentByTypeList = FXCollections.observableArrayList();
    private Locale userLocale = Locale.getDefault();
    private ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private static DateTimeFormatter dtf = Appointment.getDateFormatter();
    private LocalDate date;
    private String formattedDate;
    private long numAppointments;
    
    /**
     * Default constructor, no arguments
     */
    AppointmentByType(){
        date = null;
        formattedDate = "";
        numAppointments = 0; 
    }
    
    /**
     * Constructor using date & type selected in reporting
     * @param date The date being ran
     * @param type The type selected for filter
     */
    AppointmentByType(LocalDate date, String type){
        //Construct date fields
        this.date = date;
        formattedDate = date.format(dtf);
        
        //Find num appointments by type
        numAppointments = Schedule.getAllAppointments().stream()
                .filter(appointment -> appointment.getDate().equals(date))
                .filter(appointment -> appointment.getType().equals(type))
                .count();
        
        AppointmentByTypeList.add(this);
    }
    
    /**
     * Constructor to create total for the date range selected in reporting
     * Lambda used to map and collect numAppointments from type list, benefit: efficient data processing
     * @param type type being filtered for
     */
    AppointmentByType(String type){
        formattedDate = labels.getString("total") + ":";
        
        //Find total
        numAppointments = AppointmentByTypeList.stream()
                .map(day -> day.getNumAppointments())
                .collect(Collectors.summingLong(aLong -> aLong.longValue()));
        
        AppointmentByTypeList.add(this);
    }

    /**
     * Returns a formatted date for display in report
     * @return String representing date formatted by locale
     */
    public String getFormattedDate() {
        return formattedDate;
    }

    /**
     * Returns the number of appointments matching the type selected and day selected 
     * Used in reporting and constructor
     * @return A long value of number of appointments scheduled with type and day
     */
    public long getNumAppointments() {
        return numAppointments;
    }
    
    /**
     * Creates the appointments by type report with information transferred from reporting display
     * @param type type selected to filter for
     * @param start start date for range being created
     * @param end end date for range being created
     * @return returns a list of all appointments by type selected
     */
    public static ObservableList<AppointmentByType> createAppointmentsByTypeReport(String type, LocalDate start, LocalDate end){
        //Clear previous
        if(!AppointmentByTypeList.isEmpty()){
            AppointmentByTypeList.clear();
        }
        
        //Find number of days requested
        long days = ChronoUnit.DAYS.between(start,end);
        
        //Create days
        for(long i = 0; i<= days; ++i){
           AppointmentByType day = new AppointmentByType(start, type);
           start = start.plusDays(1);
        }
        
        //Create total
        AppointmentByType total = new AppointmentByType(type);
        
        return AppointmentByTypeList;
    }
}
