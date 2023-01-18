/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Represents a day that appointments can be scheduled for and all the available time slots  in 
 * user local time for that day in hour increments
 * @author courtney
 */
public class ScheduleDay {
    private static ArrayList<String> timeSlotsScheduled = new ArrayList<>();
    private static DateTimeFormatter dtf;
    private LocalDate date;
    private String formattedDate;
    private TimeSlot timeSlot1;
    private TimeSlot timeSlot2;
    private TimeSlot timeSlot3;
    private TimeSlot timeSlot4;
    private TimeSlot timeSlot5;
    private TimeSlot timeSlot6;
    private TimeSlot timeSlot7;
    private TimeSlot timeSlot8;
    private TimeSlot timeSlot9;
    private TimeSlot timeSlot10;
    private TimeSlot timeSlot11;
    private TimeSlot timeSlot12;
    private TimeSlot timeSlot13;
    private TimeSlot timeSlot14;
    private TimeSlot timeSlot15;
    
    /**
     * Constructs schedule day using the date, Sets up times available for appointments that day based on the 
     * users local time using business hours of 8 a.m. to 10 p.m. Eastern time for availability
     * Lambdas used in stream of business hours to map and collect, benefit: Readability
     * @param date Date being created
     */
    public ScheduleDay(LocalDate date){
        this.date = date;
        formattedDate = date.format(dtf);
        boolean needsSort = false;
        
        //Set local business hours 8am-10pm Eastern
        Schedule.setBusinessHours(date);
        filterTimeSlotsByDay(date);
        
        ArrayList <ZonedDateTime> inLocalTime = new ArrayList<>();
        
        //Convert hours to user's local time
        Schedule.getBusinessHours().stream()
                .map(slot -> slot.withZoneSameInstant(ZoneId.systemDefault()))
                .collect(Collectors.toCollection(()-> inLocalTime));
                
        ArrayList <String> filteredList = new ArrayList<>();
        
        //If start time is the next day, reformat
        int index = 0;
        for(ZonedDateTime dateTime : inLocalTime){
           LocalDate thisDate = dateTime.toLocalDate();
           if(!(thisDate.equals(date))){
              dateTime = dateTime.minusDays(1);
              inLocalTime.set(index, dateTime);
              needsSort = true;
           }
           index ++;
        }

        //If any date was reformatted, sort
        if(needsSort){
            inLocalTime.sort((item1, item2)-> item1.compareTo(item2));
        }
        
        inLocalTime.stream()
                .map(slot -> slot.toLocalTime())
                .map(slot -> {
                    LocalTime end = slot.plusHours(1);
                    String range = slot + "-" + end;
                    return range;
                }).collect(Collectors.toCollection(()-> filteredList));
        
        //Create time slots from hours
        for(int i = 0; i < 15; ++i){  
           String time = filteredList.get(i);
           TimeSlot newTimeSlot;
           
           //Determine availablity
           if(timeSlotsScheduled.contains(time)){
              Appointment appointment = Schedule.findAppointment(date, time);
              newTimeSlot = new TimeSlot(time, date, appointment);
           }else{
              newTimeSlot = new TimeSlot(time, date);
                    }
           
           //Create timeslot fields for day
           switch(i){
            case 0:  timeSlot1 = newTimeSlot;
                     break;
            case 1:  timeSlot2 = newTimeSlot;
                     break;
            case 2:  timeSlot3 = newTimeSlot;
                     break;
            case 3:  timeSlot4 = newTimeSlot;
                     break;
            case 4:  timeSlot5 = newTimeSlot;
                     break;
            case 5:  timeSlot6 = newTimeSlot;
                     break;
            case 6:  timeSlot7 = newTimeSlot;
                     break;
            case 7:  timeSlot8 = newTimeSlot;
                     break;
            case 8:  timeSlot9 = newTimeSlot;
                     break;
            case 9:  timeSlot10 = newTimeSlot;
                     break;
            case 10: timeSlot11 = newTimeSlot;
                     break;
            case 11: timeSlot12 = newTimeSlot;
                     break;
            case 12: timeSlot13 = newTimeSlot;
                     break;
            case 13: timeSlot14 = newTimeSlot;
                     break; 
            case 14: timeSlot15 = newTimeSlot;
                     break;
           }
        }
        //Add the day to the week schedule
        Schedule.addScheduleDay(this);
    }
    
    /**
     * Used to find time slots that are scheduled(not available)
     * Lambdas used in filter, map, and collect of stream. benefit: Conciseness of operations
     * @param date Date to filter for
     */
    public static void filterTimeSlotsByDay(LocalDate date){
        //Clear previous filter
        if(!timeSlotsScheduled.isEmpty()){
            timeSlotsScheduled.clear();
        }
 
        //Compare schedule to availability
        Schedule.getAllAppointments().stream()
        .filter(appointment -> appointment.getDate().equals(date))
        .map(appointment -> appointment.getTime())
        .collect(Collectors.toCollection(()-> timeSlotsScheduled));
    }

    /**
     * Returns the date the schedule day represents for use in comparisons
     * @return the date of the schedule day
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the 1st time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot1() {
        return timeSlot1;
    }

    /**
     * @return the 2nd time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot2() {
        return timeSlot2;
    }

    /**
     * @return the 3rd time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot3() {
        return timeSlot3;
    }

    /**
     * @return the 4th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot4() {
        return timeSlot4;
    }

    /**
     * @return the 5th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot5() {
        return timeSlot5;
    }

    /**
     * @return the 6th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot6() {
        return timeSlot6;
    }

    /**
     * @return the 7th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot7() {
        return timeSlot7;
    }

    /**
     * @return the 8th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot8() {
        return timeSlot8;
    }

    /**
     * @return the 9th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot9() {
        return timeSlot9;
    }

    /**
     * @return the 10th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot10() {
        return timeSlot10;
    }

    /**
     * @return the 11th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot11() {
        return timeSlot11;
    }

    /**
     * @return the 12th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot12() {
        return timeSlot12;
    }

    /**
     * @return the 13th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot13() {
        return timeSlot13;
    }

    /**
     * @return the 14th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot14() {
        return timeSlot14;
    }

    /**
     * @return the 15th time slot of the day for use in schedule week view
     */
    public TimeSlot getTimeSlot15() {
        return timeSlot15;
    }
    
    /**
     * @return Returns the date the schedule day represents formatted by locale for use in week display
     */
    public String getFormattedDate(){
        return formattedDate;
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
}
