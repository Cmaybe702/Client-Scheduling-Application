/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Represents a country where customers of the business reside
 * @author courtney
 */
public class Country {
   private String countryName;
   private int countryID;
   
   /**
    * Default constructor for countries, no arguments
    */
   public Country(){
       countryName = "";
       countryID = 0;          
   }
   
   /**
    * Used to construct countries from mySQL data to use in country drop down menu
    * @param countryName Name of the country as a string
    * @param countryID Unique identifier from mySQL
    */
   public Country(String countryName, int countryID){
       this.countryName = countryName;
       this.countryID = countryID;
   }
   
   /**
    * Returns a unique identifier for comparing countries
    * @return unique identifier of country
    */
   public int getCountryID(){
       return countryID;
   }
   
   /**
    * Used to return the country name for country drop down in add Contact
    * @return A string containing the country's name
    */
   @Override
   public String toString(){
        return countryName;
    }
   
   /**
    * Used to compare two countries for equality. Used for division sorting
    * @param o object to compare to
    * @return Boolean value of equality T/F
    */
   @Override
   public boolean equals(Object o){
        if((o instanceof Country) && ((Country)o).getCountryID() == this.getCountryID()){
          return true;
        }
        else{
          return false;
        }
    }
}
