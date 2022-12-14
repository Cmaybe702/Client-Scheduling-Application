/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Represents a company defined geographical location, second level.
 * @author courtney
 */
public class FirstLevelDivision {
    private String divisionName;
    private int divisionID;
    private Country country;
    
    /**
     * Default constructor, no arguments
     */
    public FirstLevelDivision(){
        divisionName = "";
        divisionID = 0;
        country = null;
    }
    
    /**
     * Constructor of divisions from mySQL data, called on app start
     * @param name name of the division 
     * @param id unique identifier
     * @param country country where division is located, first level
     */
    public FirstLevelDivision(String name, int id, Country country){
        divisionName = name;
        divisionID = id;
        this.country = country;
    }
    
    /**
     * @return the name of the division 
     */
    public String getDivisionName(){
        return divisionName;
    }
    
    /**
     * @return the unique identifier of the division 
     */
    public int getDivisionID(){
        return divisionID;
    }
    /**
     * Used to find a division by unique ID
     * @param id unique identifier of division to find
     * @return the division if found, null if not found
     */
    public static FirstLevelDivision findDivision(int id){
        FirstLevelDivision foundDivision = null;
        
        for(FirstLevelDivision division : Locations.getAllDivisions()){
           if(division.getDivisionID() == id) 
            foundDivision = division;   
        }
        
        return foundDivision;   
    }
    /**
     * Used to find division by name for use in reporting
     * @param divisionName name of division to find
     * @return the division if found, null if not
     */
    public static FirstLevelDivision findDivision(String divisionName){
        FirstLevelDivision foundDivision = null;
        
        for(FirstLevelDivision division : Locations.getAllDivisions()){
           if(division.getDivisionName().equals(divisionName)) 
            foundDivision = division;   
        }
        
        return foundDivision;   
    }
     
    /**
     * @return the country where the division is located
     */
    public Country getCountry(){
        return country;
    }
   
    /**
     * Returns the name of the division for use in drop downs
     * @return the division name
     */
    @Override
    public String toString(){
        return divisionName;
    }
   
    /**
     * Used to compare two divisions for equality
     * @param o division to compare to
     * @return a Boolean value representing if the two divisions are the same
    */
    @Override
    public boolean equals(Object o){
        if((o instanceof FirstLevelDivision) && ((FirstLevelDivision)o).getDivisionID() == this.getDivisionID()){
          return true;
        }
        else{
          return false;
        }
    }
}
