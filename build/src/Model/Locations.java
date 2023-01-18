/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.DataAccess;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class contains a list of all countries the company operates in, all divisions the company operates in 
 * (second level of locations), and all the names of the divisions
 * @author courtney
 */
public class Locations {
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();
    private static ObservableList<String> allDivisionNames = FXCollections.observableArrayList();
    
    /**
     * Used to set up country list from mySQL data
     * @throws SQLException if mySQL can not be contacted
     */
    public static void initializeCountryList() throws SQLException{
       DataAccess.getCountryData(); 
    }
    
    /**
     * Used to set up division list from mySQL data
     * @throws SQLException if mySQL can not be contacted
     */
    public static void initializeDivisionList() throws SQLException{
       DataAccess.getDivisionData();
    }
    
    /**
     * Returns the list of all countries for use in drop downs
     * @return list of all current company country locations
     */
    public static ObservableList<Country> getAllCountries(){
        return allCountries;
    }
    
    /**
     * Returns the list of all divisions for use in drop downs
     * @return list of all current company division locations
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions(){
        return allDivisions;
    }
    
    /**
     * Returns the list of filtered divisions for use in drop downs
     * Lambda used in filtering of divisions, benefit: Readability
     * Lambda used in sorting of division, benefit: Conciseness
     * @param countryID country to filter for
     * @return A list of filtered divisions by the country specified
     */
    public static ObservableList<FirstLevelDivision> getFilteredDivisions(int countryID){
        ArrayList<FirstLevelDivision> filteredList;
        
        filteredList = allDivisions.stream()
                .filter(division -> division.getCountry().getCountryID() == countryID)
                .sorted((d1, d2) -> d1.getDivisionName().compareTo(d2.getDivisionName()))
                .collect(Collectors.toCollection(ArrayList::new));
        
        ObservableList<FirstLevelDivision> filteredDivisions = FXCollections.observableArrayList(filteredList);
        
        return filteredDivisions;
    }
    
    /**
     * Adds a country to the country list
     * @param country country to add
     */
    public static void addCountryToList(Country country){
        allCountries.add(country);
    }
    
    /**
     * Adds a division to the division list
     * @param division to add
     */
    public static void addDivisionToList(FirstLevelDivision division){
        allDivisions.add(division);
    }
    
    /**
     * Used to return a list of all division names for reporting display
     * @return A list of all divisions by name 
     */
    public static ObservableList<String> getAllDivisionNames(){
       ArrayList <String> divisionNames = new ArrayList<>();
        
        allDivisions.stream()
               .map(division -> division.getDivisionName())
               .collect(Collectors.toCollection(()-> divisionNames));
        
        allDivisionNames = FXCollections.observableArrayList(divisionNames);
        
        return allDivisionNames; 
    }
}
