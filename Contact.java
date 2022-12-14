/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.DataAccess;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a person in the organization that is facilitating an appointment with a customer
 * @author courtney
 */
public class Contact {
    private int contactID;
    private String contactName;
    private String contactEmail;
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     * Default constructor for contacts, no arguments
     */
    public Contact(){
        contactID = 0;
        contactName = "";
        contactEmail = "";
    }
    
    /**
     * Constructor for contacts from mySQL data, created upon startup of application
     * @param id unique identifier for the contact
     * @param name name of the contact
     * @param email email address for the contact
     */
    public Contact(int id, String name, String email){
        contactID = id;
        contactName = name;
        contactEmail = email;
    }
    /**
     * Returns unique identifier
     * @return unique identifier of referenced contact
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Returns the name of the contact
     * @return full name of referenced contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Returns the contact's listed email address
     * @return the email address of the referenced contact
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Adds the contact to allContacts list
     * @param contact contact to add
     */
    public static void addContact(Contact contact){
        allContacts.add(contact);
    }
    /**
     * Used to find a contact based on the contact id number for appointments
     * @param id unique identifier of contact
     * @return the contact if found, or null if not found
     */
    public static Contact findContact(int id){
        Contact foundContact = null;
        
        for(Contact contact: allContacts){
            if(contact.getContactID() == id)
                foundContact = contact;
        }
        return foundContact;
    }
    
    /**
     * Used to find a contact by name for generating contact schedule report
     * @param name contact name to search for
     * @return the contact if found, null if not found
     */
    public static Contact findContact(String name){
        Contact foundContact = null;
        
        for(Contact contact: allContacts){
            if(contact.getContactName().equals(name))
                foundContact = contact;
        }
        return foundContact;
    }
    
    /**
     * Used to return contact name to identify contact in displays
     * @return String of full name
     */
    @Override
    public String toString(){
        return contactName;
    }
    
    /**
     * Used to start setup of contact list from mySQL when application is started
     */
    public static void initiliazeContacts(){
        DataAccess.getContacts();
    }
    
    /**
     * Returns a list of all contacts that is used in Contact drop downs
     * @return A list of all contacts in the system
     */
    public static ObservableList<Contact> getAllContacts(){
        return allContacts;
    }
    
    /**
     * Returns a list of all contact names used for contact report selections
     * Lambdas used to map and collect all Contacts, benefit: functional programming
     * @return A list of all contact's full names
     */
    public static ObservableList<String>getAllContactNames(){
        ArrayList <String> contactNames = new ArrayList<>();
        
        allContacts.stream()
                .map(contact -> contact.getContactName())
                .collect(Collectors.toCollection(()-> contactNames));
        
        ObservableList<String> contactNamesList = FXCollections.observableArrayList(contactNames);
        
        return contactNamesList;
    }
}
