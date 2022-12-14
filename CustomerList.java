/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.DataAccess;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Utility class for customers. Contains counter for setting customer IDs for new customers, a list of all current
 * customers, and a filtered list for the customer search.
 * @author courtney
 */
public class CustomerList {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Customer> allFilteredCustomers = FXCollections.observableArrayList();
    private static int customerIDCounter;
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("resources/messages", userLocale);
    
    /**
     * Used to set customerID counter from maximum ID in mySQL
     * @throws SQLException if data can not be returned from DB
     */
    public static void setCustomerIDCounter() throws SQLException{
       customerIDCounter = DataAccess.getMaxCustomerID();
    }
    
    /**
     * Used to set up customer list with data from mySQL
     * @throws SQLException if data can not be returned from DB
     */
    public static void initializeCustomerList() throws SQLException{
       DataAccess.getCustomerData(); 
    }
    
    /**
     * Used when a new customer is created. CustomerID is incremented and returned for the new customer
     * @return ID to assign
     */
    public static int getNextCustomerId(){
        customerIDCounter ++;
        return customerIDCounter;
    }
    
    /**
     * Used to add a new customer to the customer list
     * @param thisCustomer customer to add
     */
    public static void addCustomerToList(Customer thisCustomer){
        allCustomers.add(thisCustomer);
    }
    
    /**
     * Used to return a list of all customers in the system
     * @return list of all customers
     */
    public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }
    
    /**
    *@return returns a filtered list of customers based on search criteria
    */
    public static ObservableList<Customer> getAllFilteredCustomers(){
        return allFilteredCustomers;
    }
    
    /**
     * Used to add a new customer to the view if they are successfully added in mySQL
     * @param thisCustomer customer to add
     * @throws SQLException if mySQL can not be contacted
     */
    public static void userAddsCustomer(Customer thisCustomer) throws SQLException{
        if(DataAccess.addCustomer(thisCustomer)){
            allCustomers.add(thisCustomer);
        }
        else{
            System.out.println("Request to add failed");
        } 
    }
    
    /**
     * Used to remove a customer from the view if they are deleted successfully in mySQL
     * @param thisCustomer customer to delete
     * @return Boolean indicating if the operation is successful
     * @throws SQLException if mySQL can not be contacted
     */
    public static boolean userDeletesCustomer(Customer thisCustomer) throws SQLException{
        Customer toRemove = null;
        boolean found = false;
        
        //Verify delete in mySQL
        if(DataAccess.removeCustomer(thisCustomer)){ 
           //Find customer in list
            for(Customer c : allCustomers){
               if(c.equals(thisCustomer)){
                   found = true;
                   toRemove = c;
               }
           }
           //Remove from customer list
           if(found){
                allCustomers.remove(toRemove);
           }
           else{
                   System.out.println("Delete Failed");
               }
        }
        
        return found;
    }
    
    /**
     * Used to update customer fields in view if success in my SQL
     * @param thisCustomer customer to update
     */
    public static void userUpdatesCustomer(Customer thisCustomer){
        //Verify customer was updated in mySQL
        if(DataAccess.updateCustomer(thisCustomer)){ 
            //Find customer in CustomerList and update fields
            for(Customer customer : allCustomers){
                if(thisCustomer.getID() == customer.getID()) {
                    customer.setName(thisCustomer.getCustomerName());
                    customer.setAddress(thisCustomer.getAddress());
                    customer.setPostalCode(thisCustomer.getPostalCode());
                    customer.setPhoneNumber(thisCustomer.getPhoneNumber());
                    customer.setDivisionID(thisCustomer.getDivisionID());
                }
            }
        }
    }
    
    /**
     * Used to find a customer in the customer's list
     * @param id unique identifier of customer
     * @return customer if found or null if not
     */
    public static Customer findCustomer(int id){
        Customer thisCustomer = null;
        
        for(Customer customer : allCustomers){
                if(customer.getID() == id){
                    thisCustomer =customer;
                }
        }
        return thisCustomer;
    }
    
    /**
     * Used to find a customer in the customer's list by name
     * @param name name of customer
     * @return customer if found or null if not
     */
    public static Customer findCustomer(String name){
        Customer thisCustomer = null;
        name = name.toLowerCase();
        
        for(Customer customer : allCustomers){
                if(customer.getCustomerName().toLowerCase().equals(name)){
                    thisCustomer = customer;
                }
        }
        return thisCustomer;
    }
    
    /**
     * Used to filter customers by division for reporting
     * Lambda used in filtering and collection of all customers list benefit: Readability
     * @param division division to filter by
     * @return a list of all customers who reside the in requested division
     */
    public static ObservableList<Customer> filterByDivision(FirstLevelDivision division){
        if(!allFilteredCustomers.isEmpty()){
            allFilteredCustomers.clear();
        }
        
        ArrayList <Customer> custByDivision = new ArrayList<>();
        
        allCustomers.stream()
               .filter(customer-> customer.getDivisionID() == division.getDivisionID())
               .collect(Collectors.toCollection(()-> custByDivision));
        
        allFilteredCustomers = FXCollections.observableArrayList(custByDivision);

        return allFilteredCustomers;
    }
}
