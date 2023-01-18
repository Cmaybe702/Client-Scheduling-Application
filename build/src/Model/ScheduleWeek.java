/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a week in the schedule (Sunday-Saturday) used in month display. If week is the first
 * or last of the month any days that are part of another month are left blank
 * @author courtney
 */
public class ScheduleWeek {
    private static DateTimeFormatter onlyMonthDay;
    private static int firstWeekNumDays = 0;
    private String dateRange;
    private LocalDate firstDateOfWeek;
    private LocalDate lastDayOfWeek;
    private boolean firstWeek;
    private ScheduleDayOfWeek Sunday;
    private ScheduleDayOfWeek Monday;
    private ScheduleDayOfWeek Tuesday;
    private ScheduleDayOfWeek Wednesday;
    private ScheduleDayOfWeek Thursday;
    private ScheduleDayOfWeek Friday;
    private ScheduleDayOfWeek Saturday;
    
    /**
     * Default constructor, no arguments
     */
    public ScheduleWeek(){
        dateRange = "";
        firstDateOfWeek = null;
        Sunday = null;
        Monday = null;
        Tuesday = null;
        Wednesday = null;
        Thursday = null;
        Friday = null;
        Saturday = null;
    }
    
    /**
     * Constructor for the first week of the month
     * @param date the first date in the month
     */
    public ScheduleWeek(LocalDate date){
       firstWeek = true;
       firstDateOfWeek = date;
       int dayValue = -1;
       
       if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.SUNDAY)
           dayValue = 0;
       else if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.MONDAY)
            dayValue = 1;
       else if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.TUESDAY)
           dayValue = 2;
       else if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.WEDNESDAY)
           dayValue = 3;
       else if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.THURSDAY)
           dayValue = 4;
       else if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.FRIDAY)
           dayValue = 5;
       else if(firstDateOfWeek.getDayOfWeek() == DayOfWeek.SATURDAY)
           dayValue = 6;
       
       firstWeekNumDays = dayValue + 1;
       
       //Set up formatting
       String firstDateFormatted;
       String lastDateFormatted;
               
       //Fill days
        switch(dayValue){
            case 0: Sunday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Monday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Friday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(date);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(6);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 1: Sunday = new ScheduleDayOfWeek();
                    Monday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Friday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(date);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(5);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 2: Sunday = new ScheduleDayOfWeek();
                    Monday = new ScheduleDayOfWeek();
                    Tuesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Friday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(date);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(4);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 3: Sunday = new ScheduleDayOfWeek();
                    Monday = new ScheduleDayOfWeek();
                    Tuesday = new ScheduleDayOfWeek();
                    Wednesday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Friday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(date);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(3);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 4: Sunday = new ScheduleDayOfWeek();
                    Monday = new ScheduleDayOfWeek();
                    Tuesday = new ScheduleDayOfWeek();
                    Wednesday = new ScheduleDayOfWeek();
                    Thursday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Friday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(date);
                    lastDayOfWeek = firstDateOfWeek.plusDays(2);
                    
                    //Create range
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 5: Sunday = new ScheduleDayOfWeek();
                    Monday = new ScheduleDayOfWeek();
                    Tuesday = new ScheduleDayOfWeek();
                    Wednesday = new ScheduleDayOfWeek();
                    Thursday = new ScheduleDayOfWeek();
                    Friday = new ScheduleDayOfWeek(date);
                    date = date.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(date);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(1);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 6: Sunday = new ScheduleDayOfWeek();
                    Monday = new ScheduleDayOfWeek();
                    Tuesday = new ScheduleDayOfWeek();
                    Wednesday = new ScheduleDayOfWeek();
                    Thursday = new ScheduleDayOfWeek();
                    Friday = new ScheduleDayOfWeek();
                    Saturday = new ScheduleDayOfWeek(date);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek;
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
        }
    }
    
    /**
     * Constructor for full weeks(Typically 2nd and 3rd week of month, possibly 4th)
     * @param date of the first day in the week
     * @param firstWeek Boolean value indicating if this week is the first of the month
     */
    public ScheduleWeek(LocalDate date, boolean firstWeek){
       firstDateOfWeek = date;
       firstWeek = false;
       
       Sunday = new ScheduleDayOfWeek(date);
       date = date.plusDays(1);
       Monday = new ScheduleDayOfWeek(date);
       date = date.plusDays(1);
       Tuesday = new ScheduleDayOfWeek(date);
       date = date.plusDays(1);
       Wednesday = new ScheduleDayOfWeek(date);
       date = date.plusDays(1);
       Thursday = new ScheduleDayOfWeek(date);
       date = date.plusDays(1);
       Friday = new ScheduleDayOfWeek(date);
       date = date.plusDays(1);
       Saturday = new ScheduleDayOfWeek(date);
       
       //Create Range
       lastDayOfWeek = firstDateOfWeek.plusDays(6);
       String firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
       String lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
       dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted; 
    }
    
    /**
     * Constructor for last week of the month
     * @param firstDate first date in the week
     * @param lastDate last date in the month
     */
    public ScheduleWeek(LocalDate firstDate, LocalDate lastDate) {
       firstWeek = false;
       firstDateOfWeek = firstDate;
       lastDayOfWeek = lastDate;
       int dayValue = -1;
        
        if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.SUNDAY)
           dayValue = 0;
        else if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.MONDAY)
            dayValue = 1;
        else if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.TUESDAY)
           dayValue = 2;
        else if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.WEDNESDAY)
           dayValue = 3;
        else if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.THURSDAY)
           dayValue = 4;
        else if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.FRIDAY)
           dayValue = 5;
        else if(lastDayOfWeek.getDayOfWeek() == DayOfWeek.SATURDAY)
           dayValue = 6; 
        
        //Set up for formatting
        String firstDateFormatted;
        String lastDateFormatted;
       
        //Fill days
        switch(dayValue){
            case 0: Sunday = new ScheduleDayOfWeek(firstDate);
                    Monday = new ScheduleDayOfWeek();
                    Tuesday = new ScheduleDayOfWeek();
                    Wednesday = new ScheduleDayOfWeek();
                    Thursday = new ScheduleDayOfWeek();
                    Friday = new ScheduleDayOfWeek();
                    Saturday = new ScheduleDayOfWeek();
                    
                    //Create Range
                    lastDayOfWeek = firstDateOfWeek;
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted; 
                    break;
            case 1: Sunday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Monday = new ScheduleDayOfWeek(firstDate);
                    Tuesday = new ScheduleDayOfWeek();
                    Wednesday = new ScheduleDayOfWeek();
                    Thursday = new ScheduleDayOfWeek();
                    Friday = new ScheduleDayOfWeek();
                    Saturday = new ScheduleDayOfWeek();
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(1);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 2: Sunday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Monday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(firstDate);
                    Wednesday = new ScheduleDayOfWeek();
                    Thursday = new ScheduleDayOfWeek();
                    Friday = new ScheduleDayOfWeek();
                    Saturday = new ScheduleDayOfWeek();
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(2);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 3: Sunday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Monday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(firstDate);
                    Thursday = new ScheduleDayOfWeek();
                    Friday = new ScheduleDayOfWeek();
                    Saturday = new ScheduleDayOfWeek();
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(3);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 4: Sunday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Monday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(firstDate);
                    Friday = new ScheduleDayOfWeek();
                    Saturday = new ScheduleDayOfWeek();
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(4);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 5: Sunday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Monday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Friday = new ScheduleDayOfWeek(firstDate);
                    Saturday = new ScheduleDayOfWeek();
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(5);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
            case 6: Sunday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Monday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Tuesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Wednesday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Thursday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Friday = new ScheduleDayOfWeek(firstDate);
                    firstDate = firstDate.plusDays(1);
                    Saturday = new ScheduleDayOfWeek(firstDate);
                    
                    //Create range
                    lastDayOfWeek = firstDateOfWeek.plusDays(6);
                    firstDateFormatted = firstDateOfWeek.format(onlyMonthDay);
                    lastDateFormatted = lastDayOfWeek.format(onlyMonthDay);
                    dateRange = "\n" + firstDateFormatted + "-" + lastDateFormatted;
                    break;
        }
    }
    
    /**
     * @return Returns date span of week for month schedule display
     */
    public String getDateRange() {
        return dateRange;
    }
    
    /**
     * Used to create date range string
     * @return the first date in the week/month
     */
    public LocalDate getFirstDateOfWeek() {
        return firstDateOfWeek;
    }

    /**
     * @return Used to return Sunday value of week for month display
     */
    public ScheduleDayOfWeek getSunday() {
        return Sunday;
    }
    
    /**
     * @return Used to return Monday value of week for month display
     */
    public ScheduleDayOfWeek getMonday() {
        return Monday;
    }

    /**
     * @return Used to return Tuesday value of week for month display
     */
    public ScheduleDayOfWeek getTuesday() {
        return Tuesday;
    }

    /**
     * @return Used to return Wednesday value of week for month display
     */
    public ScheduleDayOfWeek getWednesday() {
        return Wednesday;
    }

    /**
     * @return Used to return Thursday value of week for month display
     */
    public ScheduleDayOfWeek getThursday() {
        return Thursday;
    }

    /**
     * @return Used to return Friday value of week for month display
     */
    public ScheduleDayOfWeek getFriday() {
        return Friday;
    }

    /**
     * @return Used to return Saturday value of week for month display
     */
    public ScheduleDayOfWeek getSaturday() {
        return Saturday;
    }
    
    /**
     * Used to set the format of dates for displays based on the users locale. If user is in U.S. 
     * format is MM/dd/yyyy if U.K. or Canada display is dd/MM/yyyy
     */
    public static void setDateFormat(){
        if(Locale.getDefault().equals(Locale.US)){
            onlyMonthDay = DateTimeFormatter.ofPattern("MM/dd");
        }
        else{
           onlyMonthDay = DateTimeFormatter.ofPattern("dd/MM"); 
        }  
    }

    /**
     * Used to return the number of days in the first week for construction of second week
     * @return number of days in the first week of the month
     */
    public static int getFirstWeekNumDays() {
        return firstWeekNumDays;
    }   
}

