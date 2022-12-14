/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents a day of the week (Sunday-Saturday) used in week view of schedule
 * @author courtney
 */
public class ScheduleDayOfWeek {
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private DayOfWeek weekDay;
    private LocalDate date;
    private long appointmentsOpen;
    private String openDisplay;
    
    /**
     * Default constructor, no arguments
     */
    ScheduleDayOfWeek(){
        weekDay = null;
        date = null;
        appointmentsOpen = 0;
        openDisplay = "";
    }
    
    /**
     * Constructor using the date to be created
     * @param date date to create
     */
    ScheduleDayOfWeek(LocalDate date){
        this.date = date;
        weekDay = date.getDayOfWeek();
        appointmentsOpen = Schedule.getAvailabilityByDay(date);
        openDisplay = "\n" + "  " + appointmentsOpen + " " + labels.getString("available") + "\n" + "\n";   
    }
    
    /**
     * Prints the number of appointments available for the day, used in month schedule display
     * @return number of appointments available for the date
     */
    @Override 
    public String toString(){
        return openDisplay;
    }
}
