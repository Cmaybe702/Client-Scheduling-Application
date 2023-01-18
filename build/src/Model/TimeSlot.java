/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents an hour range that is available to schedule appointments for or an hour range that has an 
 * appointment scheduled already
 * @author courtney
 */
public class TimeSlot {
    private boolean available;
    private String timeRange;
    private LocalDate date;
    private Appointment appointmentScheduled;
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    
    /**
     * Default constructor, no arguments
     */
    public TimeSlot(){
        available = true;
        timeRange = "";
        date = null;
        appointmentScheduled = null;
    }
    
    /**
     * Constructor for available time slots using range created in Schedule Day class  
     * and the date the time slot belongs to
     * @param timeRange start to end time of time slot
     * @param date date the time slot is for
     */
    public TimeSlot(String timeRange, LocalDate date){
        available = true;
        this.timeRange= timeRange;
        this.date = date;
    }
    
    /**
     * Constructor for time slot with the appointment reserved for that time slot using 
     * range from Schedule Day and Date Referenced
     * @param timeRange start to end time of time slot
     * @param date date the time slot is for
     * @param appointment scheduled in this time slot
     */
    public TimeSlot(String timeRange, LocalDate date, Appointment appointment){
        this.available = false;
        this.timeRange= timeRange;
        this.date = date;
        appointmentScheduled = appointment;
    }
    
    /**
     * Returns a value indicating if this slot has an appointment scheduled or not
     * @return Boolean indicating if appointment is scheduled here
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Returns a string indicating the time this slot is for in local time 24 hour format
     * @return start - end time of time slot
     */
    public String getTimeRange() {
        return timeRange;
    }

    /**
     * Returns the date associated with this entry
     * @return date the time slot belongs to
     */
    public LocalDate getDate() {
        return date;
    }
    
    /**
     * If there is an appointment scheduled for this time slot, return associated information
     * @return Appointment scheduled
     */
    public Appointment getAppointmentScheduled() {
        return appointmentScheduled;
    }
    
    /**
     * Used to print information about the time slot(availability/appointment) for use in schedule week display
     * @return available if no appointment, appointment details if appointment is scheduled for use in week schedule
     */
    @Override
    public String toString(){
       String availability;
       
       if(available){
           availability = "\n" + labels.getString("open") + "\n" + "\n";
       }
       //Print appointment detail-customer name, title, type, & contact
       else{
           String name = appointmentScheduled.getCustomer().getCustomerName();
           String contact = appointmentScheduled.getContact().getContactName();
           int id = appointmentScheduled.getAppointmentID();
           availability = labels.getString("appointmentID1") + ": " + id + "\n" + name + "\n" + contact + "\n";
       }
       return availability;
    }

}
