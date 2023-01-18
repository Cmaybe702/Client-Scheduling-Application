/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a calender month with availability per day. Helps users find a day with availability
 * when attempting to schedule a new appointment for a customer
 * @author courtney
 */
public class AppointmentMonth {
    private static ObservableList<AppointmentMonth> numAppointmentsByMonth = FXCollections.observableArrayList();
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private String month;
    private int monthNumber;
    private long numScheduled;
    
    /**
     * Default constructor, no arguments
     */
    public AppointmentMonth (){
        month = "";
        numScheduled = 0;
        monthNumber = 0;
    }
    
    /**
     * Constructor using the name of the month, the number of the month (1-12), and the year
     * Lambda using in map and filter operations to find number of appointment scheduled benefit: 
     * Conciseness of statements
     * @param month Name of the month, in language matching locale
     * @param monthNumber Month number used in comparisons
     * @param year Year the month belongs to
     */
    public AppointmentMonth (String month, int monthNumber, int year){
        this.month = month;
        this.monthNumber = monthNumber;
        
        numScheduled = Schedule.getAllAppointments().stream()
                .map(appointment -> appointment.getDate())
                .filter(date -> date.getMonthValue() == monthNumber)
                .filter(date -> date.getYear() == year)
                .count();
    }
    
    /**
     * Used to create a list containing all days in the month and availability by day
     * for use in schedule month view
     * @param year Year to create report for
     * @return A list containing all days in the month and number of available times per day
     */
    public static ObservableList<AppointmentMonth> createMonths(int year){
       //Clear previous
       if(!numAppointmentsByMonth.isEmpty()){
           numAppointmentsByMonth.clear();
       }
       
       AppointmentMonth jan = new AppointmentMonth(labels.getString("jan"), 1, year);
       numAppointmentsByMonth.add(jan);
       
       AppointmentMonth feb = new AppointmentMonth(labels.getString("feb"), 2, year);
       numAppointmentsByMonth.add(feb);
       
       AppointmentMonth march = new AppointmentMonth(labels.getString("march"), 3, year);
       numAppointmentsByMonth.add(march);
       
       AppointmentMonth april = new AppointmentMonth(labels.getString("april"), 4, year);
       numAppointmentsByMonth.add(april);
       
       AppointmentMonth may = new AppointmentMonth(labels.getString("may"), 5, year);
       numAppointmentsByMonth.add(may);
       
       AppointmentMonth june = new AppointmentMonth(labels.getString("june"), 6, year);
       numAppointmentsByMonth.add(june);
       
       AppointmentMonth july = new AppointmentMonth(labels.getString("july"), 7, year);
       numAppointmentsByMonth.add(july);
       
       AppointmentMonth aug = new AppointmentMonth(labels.getString("aug"), 8, year);
       numAppointmentsByMonth.add(aug);
       
       AppointmentMonth sep = new AppointmentMonth(labels.getString("sep"), 9, year);
       numAppointmentsByMonth.add(sep);
       
       AppointmentMonth oct = new AppointmentMonth(labels.getString("oct"), 10, year);
       numAppointmentsByMonth.add(oct);
       
       AppointmentMonth nov = new AppointmentMonth(labels.getString("nov"), 11, year);
       numAppointmentsByMonth.add(nov);
       
       AppointmentMonth dec = new AppointmentMonth(labels.getString("dec"), 12, year);
       numAppointmentsByMonth.add(dec);
       
       return numAppointmentsByMonth;
    }  

    /**
     * Returns the name of month
     * @return a String representing the month's name in local language
     */
    public String getMonth() {
        return month;
    }

    /**
     * Used to return the number of appointments scheduled per day for monthly schedule
     * @return number of time slots available for the day 
     */
    public long getNumScheduled() {
        return numScheduled;
    }   
}
