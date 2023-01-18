/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Represents a customer of the business and their associated information
 * @author courtney
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionID;
    
    /**
     * Default Constructor no arguments
     */
    public Customer(){
        customerName = "";
        address = "";
        postalCode = "";
        phoneNumber = "";
        divisionID = 0;
        customerID = setCustomerID();   
    }
    
    /**
     * Constructor for a customer created by a user in add Customer view
     * @param customerName the full name of the customer being created
     * @param address the address of the the customer being created
     * @param postalCode the local postal code of the the customer being created
     * @param phoneNumber the phone number of the the customer being created
     * @param divisionID the division the customer being created resides in
     */
    public Customer(String customerName, String address, String postalCode, String phoneNumber, int divisionID) {
       this.customerName = customerName;
       this.address = address;
       this.postalCode = postalCode;
       this.phoneNumber = phoneNumber;
       this.divisionID = divisionID;
       customerID = setCustomerID();      
    }
    
    /**
     * Constructor of customers from mySQL customers table
     * @param customerID Unique Identifier
     * @param customerName Name of the customer
     * @param address Address of the customer
     * @param postalCode Postal code of the customer
     * @param phoneNumber Phone number of the customer
     * @param divisionID Division id where the customer resides
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionID) {
       this.customerName = customerName;
       this.address = address;
       this.postalCode = postalCode;
       this.phoneNumber = phoneNumber;
       this.divisionID = divisionID;
       this.customerID = customerID;     
    }
    
    /**
     * Generates the next customer ID to be created when user adds new customer
     * @return unique id number
     */
    public int setCustomerID(){
        customerID = CustomerList.getNextCustomerId();
        return customerID;
    }
    
    /**
     * Used to print the customers name to use in appointment views and customer table
     * @return the customer's name 
     */
    @Override
    public String toString(){
        return customerName;
    }
    
    /**
     * Used to compare two customers for equality
     * @return a Boolean value indicating if the two referenced customers are the same
     * @param o customer to compare to
     */
    @Override
    public boolean equals(Object o){
        if((o instanceof Customer) && ((Customer)o).getID() == this.getID()){
          return true;
        }
        else{
          return false;
        }
    }
    
    /** 
     * @return unique identifier
     */
    public int getID(){
        return customerID;
    }
    
    /** 
     * @return division id where customer resides
     */
    public int getDivisionID(){
        return divisionID;
    }
    
    /** 
     * @return the full name of the customer
     */
    public String getCustomerName(){
        return customerName;
    }
    
    /** 
     * @return the address of the customer
     */
    public String getAddress(){
        return address;
    }
    
    /** 
     * @return the postal code of the customer
     */
    public String getPostalCode(){
        return postalCode;
    }
    
    /** 
     * @return the phone number of the customer
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    /** 
     * Used to update customer info when user updates in Modify Customer 
     * @param name name assigned
     */
    public void setName(String name){
       customerName = name; 
    }
    
    /** 
     * Used to update customer info when user updates in Modify Customer 
     * @param address address assigned
     */
    public void setAddress(String address){
        this.address = address;
    }
    
    /** 
     * Used to update customer info when user updates in Modify Customer 
     * @param postalCode postal code assigned
     */
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    
    /** 
     * Used to update customer info when user updates in Modify Customer 
     * @param phoneNumber phone number assigned
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    /** 
     * Used to update customer info when user updates in Modify Customer 
     * @param divisionID division ID being assigned
     */
    public void setDivisionID(int divisionID){
        this.divisionID = divisionID;
    }
}
