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
 * Represents reports that are available for creation in reporting display
 * @author courtney
 */
public class Report {
    private static ObservableList<Report> allReports = FXCollections.observableArrayList();
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("Resources/messages", userLocale);
    private String reportName;
    private int reportID;
    
    /**
     * Default constructor, no arguments
     */
    public Report(){
        reportName = "";
    }
    
    /**
     * Constructor for available reports
     * @param name of the report
     * @param id unique identifier used to determine what report was selected
     */
    public Report(String name, int id){
        reportName = name;
        reportID = id;
    }
    
    /**
     * Used to create reports for reporting display upon startup of application
     */
    public static void createReports(){
        Report appointmentsByType = new Report(labels.getString("appointmentByType"), 1);
        allReports.add(appointmentsByType);
        
        Report appointmentsByMonth = new Report(labels.getString("appointmentByMonth"), 2);
        allReports.add(appointmentsByMonth);
        
        Report contactSchedule = new Report(labels.getString("contactSchedule"), 3);
        allReports.add(contactSchedule);
        
        Report customersByDivision = new Report(labels.getString("custByDivision"), 4);
        allReports.add(customersByDivision);
    }
    
    /**
     * Used to print report names for display
     * @return report name as a String
     */
    @Override
    public String toString(){
        return reportName;
    }

    /**
     * Returns an list of reports for reporting drop down
     * @return list of all reports
     */
    public static ObservableList<Report> getAllReports() {
        return allReports;
    }

    /**
     * Used to return the id of the report for comparison
     * @return unique identifier of report
     */
    public int getID() {
        return reportID;
    }
}
