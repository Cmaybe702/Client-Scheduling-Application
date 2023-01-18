/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.DataAccess;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class contains data structures containing appointments for schedule displays, business hours
 * for time slot creation based on user local time, time slots filtered for availability, and a list of 
 * appointments that is filtered by contact.
 * @author courtney
 */
public class Schedule {
    private static ArrayList<ZonedDateTime> BusinessHours = new ArrayList<>();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentsByContact = FXCollections.observableArrayList();
    private static ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> availableTimesByDate = FXCollections.observableArrayList();
    private static ArrayList<ZonedDateTime> currentAppointmentsByDay = new ArrayList<>();
    private static ObservableList<ScheduleDay> appointmentsByDay = FXCollections.observableArrayList();
    private static ObservableList<ScheduleWeek> scheduleByMonth =  FXCollections.observableArrayList();
    private static ZoneId userZone = ZoneId.systemDefault();
    
    /**
     * Used to initialize appointment data from database upon start up of application
     */
    public static void initializeSchedule(){
        DataAccess.getAppointments();
    }
    
    /**
     * Used to add appointments to allAppointments list
     * @param currAppointment appointment to add
     */
    public static void addAppointment(Appointment currAppointment){
        allAppointments.add(currAppointment);
    }
    
    /**
     * Returns all current appointments
     * @return A list containing all appointments scheduled
     */
    public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }
    
    /**
     * Used to verify no appointments are scheduled for a customer before a customer is deleted
     * @param customer customer being checked for appointments
     * @return Boolean value indicating T if appointments are scheduled, F if no appointments are found
     */ 
    public static boolean checkForAppointments(Customer customer){
        boolean customerFound = false;
        
        for(Appointment appointment : allAppointments){
                if(appointment.getCustomer() == customer){
                    customerFound = true;
                }
        }
        return customerFound;
    }
    
    /**
     * Returns a filtered list of appointments filtered by another method(depending on type of request)
     * @return A filtered list of appointments
     */
    public static ObservableList<Appointment> getfilteredAppointments(){
        return filteredAppointments;
    }
    
    /**
     * Filters all appointments by customer for use in customer appointment display
     * @param customer Customer to filter appointments for
     */
    public static void filterAppointmentsbyCustomer(Customer customer){
        //Clear previous filter
        if(!filteredAppointments.isEmpty()){
            filteredAppointments.clear();
        }
        
        //Filter by customer
        for (Appointment appointment : allAppointments) {
            if (appointment.getCustomer() == customer) {
              filteredAppointments.add(appointment);
            }
        }
    }
    
    /**
     * Used to set business hours and convert to local times
     * Lambda used in sorting of business hours, benefit: conciseness of sort statement
     * @param date date requested hours belong to
     */
    public static void setBusinessHours(LocalDate date){
       final long BEGIN = 8;
       final long INCREMENT = 1;
               
       ZoneId eastern = ZoneId.of("US/Eastern");
       ZonedDateTime start = date.atStartOfDay(eastern);
       start = start.plusHours(BEGIN);
       
       //Clear previous
       BusinessHours.clear();
       
       for(int i = 0; i<15; ++i){
           //Add to business hours list and increment time for next loop
           BusinessHours.add(start);
           start = start.plusHours(INCREMENT);
        } 
    }

    /**
     * Used to get a list of business hours in user local time for scheduling 
     * @return A list of business hours converted from eastern to user local time zone
     */
    public static ArrayList<ZonedDateTime> getBusinessHours() {
        return BusinessHours;
    }
    
    /**
     * Used to create a list of all scheduled appointments by day
     * Lambda used in filter of stream, benefit: reduction in code bloat
     * Lambda used in mapping of stream, benefit: conciseness
     * @param date Date to filter for
     */
    public static void filterAppointmentsByDay(LocalDate date){
        //Clear previous filter
        if(!currentAppointmentsByDay.isEmpty()){
            currentAppointmentsByDay.clear();
        }
        
        allAppointments.stream()
        .filter(appointment -> appointment.getDate().equals(date))
        .map(appointment -> appointment.getStart())
        .collect(Collectors.toCollection(()-> currentAppointmentsByDay));
    }
    
    /**
     * Filters scheduled appointments by day
     * Lambda used in filter of stream, benefit: reduction in code bloat
     * BLOCK Lambda used in mapping of stream, benefit: improved readability
     * @param selectedDate Date to filter for
     */
    public static void filterTimesByDate(LocalDate selectedDate){
        boolean needsSort = false;
        
        //Clear list 
        availableTimesByDate.clear();
        
        setBusinessHours(selectedDate);
        filterAppointmentsByDay(selectedDate);
        
        //Convert to local time
        ArrayList <ZonedDateTime> businessHoursInLocal = new ArrayList<>();
        
        BusinessHours.stream()
          .map(appointment -> appointment.withZoneSameInstant(userZone))
          .collect(Collectors.toCollection(()-> businessHoursInLocal));
        
        //If start time is the next day, reformat
        int index = 0;
        for(ZonedDateTime dateTime : businessHoursInLocal){
           LocalDate date = dateTime.toLocalDate();
           if(!(date.equals(selectedDate))){
              dateTime = dateTime.minusDays(1);
              businessHoursInLocal.set(index, dateTime);
              needsSort = true;
           }
           index ++;
        }
           
        //If any date was reformatted, sort
        if(needsSort){
            businessHoursInLocal.sort((item1, item2)-> item1.compareTo(item2));
        }
        
        ArrayList <String> filteredList = new ArrayList<>();
        
        businessHoursInLocal.stream()
          .filter(appointment -> !currentAppointmentsByDay.contains(appointment))
          .map(appointment -> appointment.toLocalTime())
          .map(appointment -> {
              LocalTime end = appointment.plusHours(1);
              String range = appointment + "-" + end;
              return range;
                })
          .collect(Collectors.toCollection(()-> filteredList));
             
        availableTimesByDate = FXCollections.observableArrayList(filteredList);
    }
    
    /**
     * Returns a list of available appointment times by day
     * @return A list of time slots available for date, pre-filtered in another method
     */
    public static ObservableList<String> getAvailableTimesByDate(){
        return availableTimesByDate;
    }
    
    /**
     * Used to add an appointment to allAppointments if successfully added in database
     * @param thisAppointment Appointment to add
     * @throws SQLException if mySQL can not be reached
     */
    public static void userAddsAppointment(Appointment thisAppointment) throws SQLException{
       if(DataAccess.addAppointment(thisAppointment)){
            allAppointments.add(thisAppointment);
        }
        else{
            System.out.println("Request to add failed");
        } 
    }
    
    /**
     * Used to delete an appointment from all Appointments if successfully updated in database
     * @param thisAppointment Appointment to delete
     * @return Boolean value indicating success or failure of operation
     */
    public static boolean deleteAppointment(Appointment thisAppointment){
        boolean verified = false;
        boolean found = false;
        int indexFound = -1;
        int appointmentID = thisAppointment.getAppointmentID();
        Appointment foundAppointment = null;
        
        if(DataAccess.removeAppointment(thisAppointment)){
            int index = 0;
            for(Appointment appointment : allAppointments){
                if(appointment.getAppointmentID() == appointmentID){
                    found = true;
                    indexFound = index;
                }  
                index++;       
            }
            if(found){
                allAppointments.remove(indexFound);
                verified = true;
            }
        }
        return verified;
    }
    
    /**
     * Used to update an appointment from all Appointments if successfully updated in database
     * @param thisAppointment Appointment to update in view
     */
    public static void userUpdatesAppointment(Appointment thisAppointment){
  
        if(DataAccess.updateAppointment(thisAppointment)){
            Customer customer = thisAppointment.getCustomer();
            filterAppointmentsbyCustomer(customer);
        }
    }
    
    /**
     * Used to find a scheduled appointment by date and time
     * Lambda used in filter of stream, benefit: reduction in code bloat
     * @param date Date to find
     * @param time Time to find
     * @return The appointment if found, null if not found
     */
    public static Appointment findAppointment(LocalDate date, String time){
        Appointment match = null;
            match = allAppointments.stream()
                .filter(appointment -> appointment.getDate().equals(date) && 
                        appointment.getTime().equals(time)).findAny().get();
        
        return match;
    }
    
    /**
     * Add a day to view for week
     * @param scheduledDay day to add
     */
    public static void addScheduleDay(ScheduleDay scheduledDay){
        appointmentsByDay.add(scheduledDay);
    }
    
    /**
     * Get all appointments for the day pre-filtered in another method
     * @return A list of appointments for that day
     */
    public static ObservableList<ScheduleDay> getAppointmentsByDay(){
        return appointmentsByDay;
    }
    
    /**
     * Used to create schedule of appointments for week view
     * @param startOfWeek the first day of the week being created
     * @return A list containing all days in the week
     */
    public static ScheduleDay setWeekScheduleDays(LocalDate startOfWeek){
        appointmentsByDay.clear();
        ScheduleDay sunday = new ScheduleDay(startOfWeek);
        
        //Create Monday-Saturday
        for(int i = 0; i < 6; ++i){
           startOfWeek = startOfWeek.plusDays(1);
           ScheduleDay day = new ScheduleDay(startOfWeek);  
        }
        
        return sunday;
    }
    
    /**
     * Filters all appointments by date for use in day schedule
     * @param date Date to filter for
     */
    public static void filterAppointmentsbyDay(LocalDate date){
        //Clear previous filter
        if(!filteredAppointments.isEmpty()){
            filteredAppointments.clear();
        }
        
        //Filter by date
        for (Appointment appointment : allAppointments) {
            if (appointment.getDate().equals(date)) {
              filteredAppointments.add(appointment);
            }
        }
    }
    
   /**
    * Used to filter all appointments list by date
    * @param start start date being filtered for
    * @param end end date being filtered for
    */ 
    public static void filterAppointmentsbyDates(LocalDate start, LocalDate end){
        //Clear previous filter
        if(!filteredAppointments.isEmpty()){
            filteredAppointments.clear();
        }
        
        //Filter by range
        for (Appointment appointment : allAppointments) {
            if ((appointment.getDate().compareTo(start)) >= 0) {
                if((appointment.getDate().compareTo(end)) <= 0){
                   filteredAppointments.add(appointment); 
                }
            }
        }
        
        //Sort appointments by date & time
        filteredAppointments.sort((item1, item2)-> item1.getStart().compareTo(item2.getStart()));
    }
    
    /**
     * Used to create week/day availability for month view
     * @param date Date to create
     */
    public static void createAvailabilityByMonth(LocalDate date){
        boolean firstWeekOfMonth = false;
        Month month = date.getMonth();
        int numDaysInFirstWeek;
        int daysToNextSun;
        
        if(!scheduleByMonth.isEmpty()){
            scheduleByMonth.clear();
        }
        
        //Create first week of month
        ScheduleWeek firstWeek = new ScheduleWeek(date);
        scheduleByMonth.add(firstWeek);
        
        numDaysInFirstWeek = ScheduleWeek.getFirstWeekNumDays();
        daysToNextSun = 8 - numDaysInFirstWeek;
        
        //Create second week of month
        date = date.plusDays(daysToNextSun);
        ScheduleWeek secondWeek = new ScheduleWeek(date, firstWeekOfMonth);
        scheduleByMonth.add(secondWeek);
        
        //Create third week of month
        date = date.plusDays(7);
        ScheduleWeek thirdWeek = new ScheduleWeek(date, firstWeekOfMonth);
        scheduleByMonth.add(thirdWeek);
        
        //Determine if fourth is last week of the month
        date = date.plusDays(7);
        if(!date.getMonth().equals(month)){
            LocalDate lastDate = date.withDayOfMonth(date.getMonth().maxLength());
            ScheduleWeek fourthWeek = new ScheduleWeek(date, lastDate);
            scheduleByMonth.add(fourthWeek); 
        }
        else{
           ScheduleWeek fourthWeek = new ScheduleWeek(date, firstWeekOfMonth); 
           scheduleByMonth.add(fourthWeek);
           
           //Create fifth week
           date = date.plusDays(7);
           LocalDate lastDate = date.withDayOfMonth(date.getMonth().maxLength());
           ScheduleWeek fifthWeek = new ScheduleWeek(date, lastDate);
           scheduleByMonth.add(fifthWeek); 
        }        
    }
    
    /**
     * Returns scheduleByMonth for month view
     * @return A list containing all weeks in the month
     */
    public static ObservableList<ScheduleWeek> getMonthAvailability(){
        return scheduleByMonth;
    }
    
    /**
     * Used to set daily available appointments for month view
     * Lambda used in mapping and filtering of stream, benefit:improved readability of code
     * @param date Date to create availability for
     * @return number of available appointments for the day
     */
    public static long getAvailabilityByDay(LocalDate date){
        long numOpen = 15;
        long numTaken = 0;
        
        numTaken = allAppointments.stream()
            .map(appointment -> appointment.getDate())
            .filter(appointmentDate -> appointmentDate.equals(date))
            .count();
        numOpen = numOpen - numTaken;
        
        return numOpen;
    }
    
    /**
     * Used to create contact schedule report
     * Lambda used in filter of stream, benefit: Reduction in code bloat
     * @param contact Contact the report is requested for
     * @param start Start date of the schedule being created
     * @param end End date of the schedule being created
     * @return A list of appointments filtered for the contact selected in reporting
     */
    public static ObservableList<Appointment> getAppointmentsByContact(Contact contact, LocalDate start, LocalDate end){
       //Clear previous filter
        if(!appointmentsByContact.isEmpty()){
            appointmentsByContact.clear();
        }
        
        ArrayList <Appointment> contactAppointments = new ArrayList<>();
        
        allAppointments.stream()
                .filter(appointment -> appointment.getContact().equals(contact))
                .filter(appointment -> appointment.getDate().compareTo(start) > 1)
                .filter(appointment -> appointment.getDate().compareTo(end) < 0)
                .collect(Collectors.toCollection(()-> contactAppointments));
        
        //Sort by date & time
        contactAppointments.sort((item1, item2)-> item1.getStart().compareTo(item2.getStart()));
                    
        appointmentsByContact = FXCollections.observableArrayList(contactAppointments);
        
        return appointmentsByContact;
    }      
}
