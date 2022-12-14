/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.DataAccess;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

/**
 * Represents an attempt to Log into the system or a userName and id combination of a user from mySQL
 * @author courtney
 */
public final class Login {
    private String username;
    private String password;
    private int userID;
    private boolean status;
    private static boolean userNameValid;
    private static ArrayList<Login> allUsers = new ArrayList<>();
    private static Locale userLocale = Locale.getDefault();
    private static ResourceBundle labels = ResourceBundle.getBundle("resources/messages", userLocale);
    
    /**
     * Constructor for login entered in the Login Form Controller
     * @param username entered
     * @param password entered
     */
    public Login(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    /**
     * Constructor for login info transferred from mySQL
     * @param username to assign
     * @param id to assign
     */
    public Login(String username, int id){
        this.username = username;
        this.userID = id;
    }
    
    /**
     * Default constructor, no arguments
     */
    public Login(){
        username = "";
        password = "";
    }
    
    /**
     * Returns username to Data Access for verification
     * @return username entered
     */
    public String getUsername(){
        return username;
    }
    
    /**
     * Returns a password to Data Access for verification
     * @return password entered
     */
    public String getPassword(){
        return password;
    }
    
    /**
     * Filters username and password for SQL injection chars, transfers login for verification.
     * @return Boolean indicating success or failure of login
     * @throws SQLException if DB can not be contacted
     */
    public boolean filterAndVerifyCredentials() throws SQLException{
        status = false;
        
        String userNameEntered = logUserName(username);
        
        //Filter input
        if(password.contains("=") || username.contains("=")) {
            //Log suspicious attempt
            logSuccess(userNameEntered, false, true);
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(labels.getString("error"));
            alert.setContentText(labels.getString("invalidLogin"));
            alert.showAndWait();
            }
        else{
            status = DataAccess.verifyUser(this);
            if(status){
               logSuccess(userNameEntered, true); 
            }
            else{
               logSuccess(userNameEntered, false); 
            }
        }
        
        return status;
    }
    
    /**
     * Returns a list of all Usernames and associated IDs for DataAccess add/update appointment
     * @return a list of all current users in system
     */
    public static ArrayList<Login> getallUsers(){
        return allUsers;
    }
    
    /**
     * Adds users transferred from mySQL to list of all users
     * @param user to add
     */
    public static void addUser(Login user){
        allUsers.add(user);
    }
    
    /**
     * Used to find the username associated with a user id number
     * @param id to find user name for
     * @return username
     */
    public static String findUserName(int id){
        int index = id - 1;
        Login user = allUsers.get(index);
        String userName = user.getUsername();
        return userName;
    }
    
    /**
     * Used to create a list of all users from my SQL data
     */
    public static void initiliazeUsers(){
        DataAccess.getUserIDs();
    }
    
    /**
     * Used to log all userNames entered into the Login form
     * Lambdas used in map and filter of allUsers benefit: encouragement of functional programming
     * @param Enteredusername from username box
     * @return A string containing what was typed
     */
    public String logUserName(String Enteredusername){
        String userNameVerified;
        
        //Find username from list
        long numFound = allUsers.stream()
                .map(user -> user.getUsername())
                .filter(userName -> userName.equals(Enteredusername))
                .count();
        
        if(numFound > 0){
           userNameVerified = "Username valid: " + Enteredusername;
           userNameValid = true;
        }
        else{
           userNameVerified = "Username invalid: " + Enteredusername;
           userNameValid = false;
        }
        
        return userNameVerified;
    }
    
    /**
     * Used to monitor if SQLinjection chars were entered into username or password field of login form
     * @param userNameEntered the userName entered into the login form
     * @param verified Boolean indicating if username is valid
     * @param suspicious Boolean indicating if attempt to log in may be result of attempted intrusion
     */
    public void logSuccess(String userNameEntered, boolean verified, boolean suspicious){
        ZonedDateTime timeOfAttempt = ZonedDateTime.now();
        String passwordVerified= "";
        
        if(userNameValid){
            passwordVerified = "Invalid password";
        }
        else{
            passwordVerified = "N/A";
        }
        
        String suspiciousAttempt = "Alert: Suspicious activity reported";
        String logActivity = timeOfAttempt + "\n" + userNameEntered + "\n" + passwordVerified + "\n" + suspiciousAttempt + "\n";
        
        //Write attempted login to file
        try{
            File file = new File("login_activity.txt");
            
            //Verify file found or create new
            if(file.exists()){
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(logActivity);
                pw.close(); 
            }
            else{
                file.createNewFile();
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(logActivity);
                pw.close(); 
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
    
    /**
     * Used to log attempts to sign onto system
     * @param userNameEntered the username entered into login form
     * @param verified Boolean indicating rather the username matched a known value
     */
    public void logSuccess(String userNameEntered, boolean verified){
        ZonedDateTime timeOfAttempt = ZonedDateTime.now();
        String passwordVerified= "";
        
        if(userNameValid){
            if(verified){
                passwordVerified = "Password match";
            }
            else{
                passwordVerified = "Invalid password";
            }
        }
        else{
            passwordVerified = "N/A";
        }
        
        String logActivity = timeOfAttempt + "\n" + userNameEntered + "\n" + passwordVerified + "\n";
        
        //Write attempted login to file
        try{
            File file = new File("login_activity.txt");
            
            //Verify file found or create new
            if(file.exists()){
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(logActivity);
                pw.close(); 
            }
            else{
                file.createNewFile();
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(logActivity);
                pw.close(); 
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}

