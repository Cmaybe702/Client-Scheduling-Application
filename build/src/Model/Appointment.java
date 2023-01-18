/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.DataAccess;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents appointments scheduled for customers. A customer/contact can have many appointments, but each
 * appointment belongs to one customer, one date, one time slot, and one contact
 * @author courtney
 */
public class Appointment {
    private static ObservableList<String> allAppointmentTypes = FXCollections.observableArrayList();
    private static int appointmentIDCounter;
    private static DateTimeFormatter dtf;
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Customer customer;
    private Contact contact;
    private String time;
    private LocalDate date;
    private String formattedDate;
    private String userID;
    private int customerID;

    /**
     * Default constructor for appointments
     */
    public Appointment(){
        appointmentID = 0;
        title = "";
        description = "";
        location = "";
        type = "";
        start = null;
        end = null;
        customer = null;
        contact = null;
    }
    
    /**
     * Constructor for appointments created from mySQL data upon startup of application
     * @param appointmentID appointmentID assigned
     * @param title title assigned
     * @param description description assigned
     * @param location location assigned
     * @param type type assigned
     * @param start start time assigned
     * @param end end time assigned
     * @param customerID customer ID assigned
     * @param contactID contact ID assigned
     * @param userID user ID assigned
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, ZonedDateTime start,
            ZonedDateTime end, int customerID, int contactID, int userID){
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        customer = CustomerList.findCustomer(customerID);
        date = start.toLocalDate();
        formattedDate = date.format(dtf);
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        time = startTime + "-" + endTime;
        this.userID = Login.findUserName(userID);
        this.contact = Contact.findContact(contactID);
        this.customerID = customer.getID();
    }
    
    /**
     * Constructor for new appointments added by users, constructed with information provided from user in add appointment form
     * @param title title assigned
     * @param description description assigned
     * @param location location assigned
     * @param type type assigned
     * @param date date assigned
     * @param time time range assigned
     * @param customer customer assigned
     * @param contact contact assigned
     */
    public Appointment(String title, String description, String location, String type, LocalDate date,
            String time, Customer customer, Contact contact){
        appointmentIDCounter ++;
        this.appointmentID = appointmentIDCounter;
        String userID = DataAccess.getUsername();
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.date = date;
        formattedDate = date.format(dtf);
        this.time = time;
        this.userID = userID;
        this.contact = contact;
        this.customer = customer;
        customerID = customer.getID();
        
        //create start and end time from time string
        int dashIndex = time.indexOf('-');
        String startTime = time.substring(0, dashIndex);
        startTime = startTime.concat(" " + date.toString());
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        this.start = start.atZone(ZoneId.systemDefault());
        this.end = this.start.plusHours(1);
    }
    
    /**
     * Gets time range string
     * @return returns the time range the appointment is scheduled for display
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the appointment time if appointment info is modified in update appointment display
     * @param time New time string
     */
    public void setTime(String time) {
        this.time = time;
    }
    
    /**
     * Gets date the appointment is scheduled for
     * @return returns the date of appointment for use in comparisons
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the appointment date if appointment info is modified in update appointment display
     * @param date New date the appointment is scheduled for
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the user who created the appointment
     * @return returns the username as a String
     */
    public String getUserID() {
        return userID;
    }
    
    /**
     * Gets the appointmentID for use in views
     * @return returns the integer appointment id number
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the appointment title for use in views
     * @return returns a String representing the title for the appointment set by the user
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the appointment title if appointment info is modified in update appointment display
     * @param title New title of the appointment
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the appointment description for use in views
     * @return returns a String representing the description of the appointment set by the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the appointment description if appointment info is modified in update appointment display
     * @param description New description of the appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the appointment location for use in views
     * @return returns a String representing the Location of the appointment set by the user
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the appointment location if appointment info is modified in update appointment display
     * @param location New location of the appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the appointment type for use in views and comparison for appointment by type
     * @return returns a String representing the type of the appointment set by the user
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the appointment type if appointment info is modified in update appointment display
     * @param type New type of the appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the appointment start time and date for use in comparisons and sorting
     * @return returns a ZoneDateTime representing the start time and date of the appointment set by the user in add appointment
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * Sets the appointment start time and date if appointment info is modified in update appointment display
     * @param start New start time and date of the appointment
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * Gets the appointment end time and date for use in comparisons and sorting
     * @return returns a ZoneDateTime representing the end time and date of the appointment set by the user in add appointment
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * Sets the appointment start end and date if appointment info is modified in update appointment display
     * @param end New end time and date of the appointment
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * Gets the customer the appointment is for. Used in displays and comparisons 
     * @return returns a customer representing the customer the appointment belongs to set by the user in add appointment
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Gets the customer id of the customer the appointment is for. Used in contact schedule display
     * @return returns the id of the customer the appointment is for
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets the customer the appointment belongs to if appointment info is modified in update appointment display
     * @param customer New customer for the appointment
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    /**
     * Sets the formatted version of the date if appointment info is modified in update appointment display
     * @param formattedDate String to set
     */
    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
    
    

    /**
     * Gets the contact the appointment is for. Used in displays and comparisons 
     * @return returns a contact representing the contact the appointment belongs to set by the user in add appointment
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the contact the appointment belongs to if appointment info is modified in update appointment display
     * @param contact New contact for the appointment
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    /**
     * Gets the formatted date based on locale. Used in displays for internationalization 
     * @return returns a String representing the date for the appointment formatted by locale
     */
    public String getFormattedDate(){
        return formattedDate;
    }
    
    /**
     * Gets a representation of the appointment for printing
     * @return returns a String with all relevant info about the appointment
     */
    @Override
    public String toString(){
        String appointmentData = "Appointment ID: " + appointmentID + "\nTitle: " + title + "\nDescription: " +
                description + "\nLocation: " + location + "\nType: " + type + "\nDate: " + date + "\nStart: " + start + "\nEnd: " + end;
        return appointmentData;
    }
    
    /**
     * Used to set the appointmentID counter with current max from mySQL. Called upon startup of application 
     * to prevent duplicate appointmentIDs
     * @throws SQLException if mySQL can not be reached
     */
    public static void setAppointmentCounter() throws SQLException{
        appointmentIDCounter = DataAccess.getMaxAppointmentID();
    }
    
    /**
     * Used to set the format of dates for displays based on the users locale. If user is in U.S. 
     * format is MM/dd/yyyy if U.K. or Canada display is dd/MM/yyyy
     */
    public static void setDateFormat(){
        if(Locale.getDefault().equals(Locale.US)){
            dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        }
        else{
           dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
        }  
    } 
    
    /**
     * Used to return all appointment types for appointment by type report generation
     * Lambda used in mapping and collection of appointment Types benefit: Efficient data processing
     * @return an ArrayList containing all current appointment types for appointments scheduled
     */
    public static ObservableList<String> getAllAppointmentTypes(){
        ArrayList <String> appointmentTypes = new ArrayList<>();
        
        Schedule.getAllAppointments().stream()
               .map(appointment -> appointment.getType())
               .distinct()
               .collect(Collectors.toCollection(()-> appointmentTypes));
        
        ObservableList<String> appointmentTypeList = FXCollections.observableArrayList(appointmentTypes);
        
        return appointmentTypeList;
    }
    
    /**
     * Used to return all years that have appointments scheduled for appointments by month report
     * @return an ArrayList containing all years with appointments scheduled
     */
    public static ObservableList<String> getAllAppointmentYears(){
        ArrayList <String> appointmentYears = new ArrayList<>();
        
        Schedule.getAllAppointments().stream()
               .map(appointment -> appointment.getDate())
               .map(date -> date.getYear())
               .map(number -> number.toString())
               .distinct()
               .collect(Collectors.toCollection(()-> appointmentYears));
        
        ObservableList<String> appointmentYearList = FXCollections.observableArrayList(appointmentYears);
        
        return appointmentYearList;
    }
    
    /**
     * Used to return a DateTimeFormatter for classes that do not have a method to construct one
     * @return an Formatter based on location of user
     */
    public static DateTimeFormatter getDateFormatter(){
        return dtf;
    }
}
