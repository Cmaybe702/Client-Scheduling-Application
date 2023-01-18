/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Appointment;
import Model.Contact;
import Model.Country;
import Model.Customer;
import Model.CustomerList;
import Model.FirstLevelDivision;
import Model.Locations;
import Model.Login;
import Model.Schedule;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Represents requests sent to mySQL to SELECT, UPDATE, INSERT, and DELETE
 * @author courtney
 */
public class DataAccess {
    private static String username;
    private static int userID;
    private static ZoneId userZone = ZoneId.systemDefault();
            
    /**
     * Used to verify users in mySQL users table
     * @param login Login information entered into Login Form
     * @return Boolean value indicating if username and password matched a user
     */
    public static boolean verifyUser(Login login){
        username = login.getUsername();
        String password = login.getPassword();
        String query;
        boolean verified = false;
        
        try {
            //Get connection and verify credentials
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            query = "SELECT User_ID FROM users WHERE User_Name = \"" + 
                    username + "\" AND Password = \"" + password + "\";";
            ResultSet rs = stmt.executeQuery(query);
            
            if(rs.next()){
                //Login valid
                verified = true;
                
                //Set userID
                userID = rs.getInt("User_ID");
                
            }
            else{
                System.out.println("Login invalid");
            }
             
        }
        catch(Exception e){
           System.out.println(e.getMessage());
        }
        
        return verified;
    }
   
    /**
     * Used to get current maximum customer ID from database for counter
     * @return maximum customer ID in customers table
     * @throws SQLException if mySQL can not be reached
     */
    public static int getMaxCustomerID() throws SQLException{
        int MaxID = -1;
         
        //Connect and retreive max
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT MAX(Customer_ID) FROM customers;"; 
            ResultSet rs = stmt.executeQuery(query);
            //Read result set
            while(rs.next()){
                MaxID = rs.getInt("MAX(Customer_ID)");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return MaxID;
    }
    
    /**
     * Used to retrieve the current appointmentID max for counter
     * @return maximum appointment ID listed in appointments
     * @throws SQLException if mySQL can not be reached
     */
    public static int getMaxAppointmentID() throws SQLException{
        int MaxID = -1;
         
        //Connect and retreive max
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT MAX(Appointment_ID) FROM appointments;"; 
            ResultSet rs = stmt.executeQuery(query);
            //Read result set
            while(rs.next()){
                MaxID = rs.getInt("MAX(Appointment_ID)");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return MaxID;
    }
    
    
    /**
     * Used to get all current customer data from MySQL to create customer view
     * @throws SQLException if mySQL can not be reached
     */
    public static void getCustomerData() throws SQLException {
        int customerID;
        String customerName;
        String address;
        String postalCode;
        String phoneNumber;
        int divisionID;
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID"
                    + " FROM customers;"; 
            ResultSet rs = stmt.executeQuery(query);
            //Read result set
            while(rs.next()){
                customerID = rs.getInt("Customer_ID");
                customerName = rs.getString("Customer_Name");
                address = rs.getString("Address");
                postalCode = rs.getString("Postal_Code");
                phoneNumber = rs.getString("Phone");
                divisionID = rs.getInt("Division_ID");
                
                Customer currCustomer = new Customer(customerID, customerName, address, postalCode, phoneNumber, divisionID);
                CustomerList.addCustomerToList(currCustomer);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * Used to get all current appointments from mySQL for schedule views
     */
    public static void getAppointments(){
       int appointmentID;
       String title;
       String description;
       String location;
       String type;
       Timestamp start;
       ZonedDateTime startTime;
       Timestamp end;
       ZonedDateTime endTime;
       int customerID;
       int contactID;
       int userID;        
               
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID,"
                    + " Contact_ID, User_ID FROM appointments;"; 
            ResultSet rs = stmt.executeQuery(query);
            
            //Read result set
            while(rs.next()){
                appointmentID = rs.getInt("Appointment_ID");
                title = rs.getString("Title");
                description = rs.getString("Description");
                location = rs.getString("Location");
                type = rs.getString("Type");
                startTime = ZonedDateTime.ofInstant(rs.getTimestamp("Start").toInstant(), userZone);
                endTime = ZonedDateTime.ofInstant(rs.getTimestamp("End").toInstant(), userZone);
                customerID = rs.getInt("Customer_ID");
                contactID = rs.getInt("Contact_ID");
                userID = rs.getInt("User_ID");
                
                Appointment currAppointment = new Appointment(appointmentID, title, description, location, type, startTime,
                    endTime, customerID, contactID, userID);
                Schedule.addAppointment(currAppointment);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * Removes a customer from mySQL by user request
     * @param custToRemove the customer to be removed
     * @return Boolean value indicating success or failure
     * @throws SQLException if mySQL can not be reached
     */
    public static boolean removeCustomer(Customer custToRemove) throws SQLException{
        boolean complete = false;
        int customerID = custToRemove.getID();
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "DELETE FROM customers WHERE Customer_ID = " + customerID + ";"; 
            int numRows = stmt.executeUpdate(query);
            
            //Verify deletion
            if(numRows > 0){
                complete = true;
                System.out.print("Update Complete");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return complete;
    }
    
    /**
     * Add a customer to mySQL by user request
     * @param custToAdd the customer to add
     * @return Boolean value indicating success or failure
     * @throws SQLException if mySQL can not be reached
     */
    public static boolean addCustomer(Customer custToAdd) throws SQLException{
        boolean complete = false;
        int ID = custToAdd.getID();
        String name = custToAdd.getCustomerName();
        String address = custToAdd.getAddress();
        String postal = custToAdd.getPostalCode();
        String phone = custToAdd.getPhoneNumber();
        int divisionID = custToAdd.getDivisionID();
        
        //get time and date of creation and convert to timestamp
        ZonedDateTime now = java.time.ZonedDateTime.now();
        now = now.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime create = now.toLocalDateTime();
        Timestamp createTime = Timestamp.valueOf(create);
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone,"
                    + " Division_ID, Created_By, Create_Date) VALUES (" + ID + ", \"" + name + "\", \"" + address + "\", \"" + postal + "\", \""
                    + phone + "\", " + divisionID + ", \"" + username + "\", TIMESTAMP('" + createTime + "'));";
            int numRows = stmt.executeUpdate(query);
            
            //Verify addition
            if(numRows > 0){
               complete = true;
               System.out.print("Update Complete");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }   
        return complete;
    }
    
    /**
     * Updates a customer record in mySQL with data provided by the user
     * @param custToUpdate customer to update info 
     * @return Boolean value indicating success or failure
     */
    public static boolean updateCustomer (Customer custToUpdate){
        boolean complete = false;
        int ID = custToUpdate.getID();
        String name = custToUpdate.getCustomerName();
        String address = custToUpdate.getAddress();
        String postal = custToUpdate.getPostalCode();
        String phone = custToUpdate.getPhoneNumber();
        int division = custToUpdate.getDivisionID();
        
        //Get time and date of update and convert to timestamp
        ZonedDateTime now = java.time.ZonedDateTime.now();
        now = now.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime update = now.toLocalDateTime();
        Timestamp updateTime = Timestamp.valueOf(update);
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "UPDATE customers SET Customer_Name = \"" + name + "\", Address = \""
                    + address + "\", Postal_Code = \"" + postal + "\", Phone = \"" + phone +
                    "\", Division_ID = " + division + ", Last_Updated_By = \"" + username + "\", Last_Update = "
                    + "TIMESTAMP('" + updateTime + "') WHERE Customer_ID = " + ID + ";";
            int numRows = stmt.executeUpdate(query);
            
            //Verify update
            if(numRows > 0){
               complete = true;
               System.out.print("Update Complete");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }   
        return complete;
    }
    
    /**
     * Used to get available countries from mySQL countries table
     * @throws SQLException if mySQL can not be reached
     */
    public static void getCountryData() throws SQLException{
        String countryName;
        int countryID;
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT Country_ID, Country FROM countries;"; 
            ResultSet rs = stmt.executeQuery(query);
            //Read result set
            while(rs.next()){
                countryID = rs.getInt("Country_ID");
                countryName = rs.getString("Country");
                
                //create country list
                Country thisCountry = new Country(countryName, countryID);
                Locations.addCountryToList(thisCountry);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * Gets all current divisions from mySQL first_level_divisions table
     * @throws SQLException if mySQL can not be reached
     */
    public static void getDivisionData() throws SQLException{
        String divisionName;
        int divisionID;
        int countryID;
        Country divisionCountry = null;
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions;"; 
            ResultSet rs = stmt.executeQuery(query);
            
            //Read result set
            while(rs.next()){
                divisionID = rs.getInt("Division_ID");
                divisionName = rs.getString("Division");
                countryID = rs.getInt("Country_ID");
                
                //Determine associated country
                List<Country> countries = Locations.getAllCountries();
                for(Country c : countries) {
                    if(c.getCountryID() == countryID){
                        divisionCountry = c;
                    }
                }
                
                //Create division list
                FirstLevelDivision thisDivision = new FirstLevelDivision(divisionName, divisionID, divisionCountry);
                Locations.addDivisionToList(thisDivision);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * Used to get users from mySQL users table
     */
    public static void getUserIDs(){
        String username;
        int userID;
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT User_ID, User_Name FROM users;"; 
            ResultSet rs = stmt.executeQuery(query);
            
            //Read result set
            while(rs.next()){
                userID = rs.getInt("User_ID");
                username = rs.getString("User_Name");
                
                Login newUser = new Login(username, userID);
                
                //create user list
                Login.addUser(newUser); 
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * Used to get all contacts from mySQL contacts table
     */
    public static void getContacts() {
        int id;
        String name;
        String email;
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT Contact_ID, Contact_Name, Email FROM contacts;"; 
            ResultSet rs = stmt.executeQuery(query);
            
            //Read result set
            while(rs.next()){
                id = rs.getInt("Contact_ID");
                name = rs.getString("Contact_Name");
                email = rs.getString("Email");
                
                //create contact list
                Contact contact = new Contact(id, name, email);
                Contact.addContact(contact);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }

    /**
    * Used to get the username of current user logged into system
    * @return the username of the user that is logged in
    */
    public static String getUsername() {
        return username;
    }
    
    /**
     * Used to add a new appointment in mySQL appointments table
     * @param appointment Appointment to add
     * @return Boolean value indicating success or failure
     * @throws SQLException if mySQL can not be reached
     */
    public static boolean addAppointment(Appointment appointment) throws SQLException{
        boolean complete = false;
        int ID = appointment.getAppointmentID();
        String title = appointment.getTitle();
        String description = appointment.getDescription();
        String location = appointment.getLocation();
        String type = appointment.getType();
        
        //Convert start and end to timestamp
        ZonedDateTime start = appointment.getStart();
        start = start.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime startLocal = start.toLocalDateTime();
        Timestamp startTime = Timestamp.valueOf(startLocal);
        
        ZonedDateTime end = appointment.getEnd();
        end = end.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime endLocal = end.toLocalDateTime();
        Timestamp endTime = Timestamp.valueOf(endLocal);
        
        //Get time and date of creation, convert to timestamp
        ZonedDateTime now = java.time.ZonedDateTime.now();
        now = now.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime create = now.toLocalDateTime();
        Timestamp createTime = Timestamp.valueOf(create);
        
        //Get customer id
        Customer customer = appointment.getCustomer();
        int customerID = customer.getID();
        
        //Get contact id
        Contact contact = appointment.getContact();
        int contactID = contact.getContactID();
  
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start,"
                    + " End, Create_Date, Created_By, Customer_ID, User_ID, Contact_ID) VALUES (" + ID + ", \"" + title + "\", \"" + description + "\", \"" + location + "\", \""
                    + type + "\", TIMESTAMP('" + startTime + "'), TIMESTAMP('" + endTime + "'), TIMESTAMP('" + createTime + "'), \"" + username + "\", " + customerID +
                    ", " + userID + ", " + contactID + ");";
            int numRows = stmt.executeUpdate(query);
            //Verify addition
            if(numRows > 0){
               complete = true;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }   
        return complete;
    }
    
    /**
     * Removes an appointment cancelled by a user from appointments table
     * @param appointment Appointment to remove
     * @return Boolean value indicating success or failure of operation
     */
    public static boolean removeAppointment(Appointment appointment){
        boolean complete = false;
        int ID = appointment.getAppointmentID();
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "DELETE FROM appointments WHERE Appointment_ID = " + ID + ";"; 
            int numRows = stmt.executeUpdate(query);
            
            //Verify deletion
            if(numRows > 0){
                complete = true;
                System.out.print("Update Complete");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        return complete;
    }
    
    /**
     * Used to update appointments in mySQL appointment table by user request
     * @param appointment Appointment to update
     * @return Boolean value indicating success or failure of operation
     */
    public static boolean updateAppointment(Appointment appointment){
       boolean complete = false;
       int ID = appointment.getAppointmentID(); 
       String title = appointment.getTitle();
       String description = appointment.getDescription();
       String location = appointment.getLocation();
       String type = appointment.getType();
       Contact contact = appointment.getContact();
       int contactID = contact.getContactID();
       Customer customer = appointment.getCustomer();
       int customerID = customer.getID();
       
       //Get start and end and convert to timestamp
       ZonedDateTime start = appointment.getStart();
       start = start.withZoneSameInstant(ZoneId.of("UTC"));
       LocalDateTime startLocal = start.toLocalDateTime();
       Timestamp startTime = Timestamp.valueOf(startLocal);
       
       ZonedDateTime end = appointment.getEnd();
       end = end.withZoneSameInstant(ZoneId.of("UTC"));
       LocalDateTime endLocal = end.toLocalDateTime();
       Timestamp endTime = Timestamp.valueOf(endLocal);
       
       //Get time and date of update and convert to timestamp
        ZonedDateTime now = java.time.ZonedDateTime.now();
        now = now.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime update = now.toLocalDateTime();
        Timestamp updateTime = Timestamp.valueOf(update);
        
        try{
            Connection connection =JDBC.getConnection();
            Statement stmt = JDBC.connection.createStatement();
            String query = "UPDATE appointments SET Title = \"" + title + "\", Description = \""
                    + description + "\", Location = \"" + location + "\", Type = \"" + type +
                    "\", Customer_ID = " + customerID + ", Contact_ID = " + contactID +
                    ", Last_Updated_By = \"" + username + "\", Last_Update = "
                    + "TIMESTAMP('" + updateTime + "'), Start = TIMESTAMP('" + startTime +
                    "'), End = TIMESTAMP('" + endTime + "') WHERE Appointment_ID = " + ID + ";";
            int numRows = stmt.executeUpdate(query);
            
            //Verify update
            if(numRows > 0){
               complete = true;
               System.out.print("Update Complete");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }   
       
       return complete;
    }
}
