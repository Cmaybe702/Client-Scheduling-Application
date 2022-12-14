# Client-Scheduling-Application

Java client scheduling application using a MySQL database. Connected via JDBC. Provides internationalization for French users

Purpose of application: 
1. To schedule maintain and cancel appointments for customers of the business 
2. To add, update, and remove relevant customer information
3. To provide reporting to identify opportunities or get information to employees of the business

IDE:
NetBeans 12.6

JDK:
Java SE 17.0.1

JavaFX SDK:
17.0.2

MySQL connector driver version:
8.0.28

How to run application:
1. Log into application using valid credentials
2. From the main menu select Clients
	- this provides you with a table of all current customers
	- from this screen you can add, update, or delete customer information
	- selecting "Appointments" will provide a list of all appointments scheduled 
	  for the customer selected
	- Here you can add, update, or delete appointments for this customer(Customer name displayed 			 	
	  in a label on this screen)
3. From the main menu select Schedule
	- this screen provides the user with available appointments by day for the month. This  						
	  information can be used to find a date with availability to schedule for
	- selecting APPOINTMENTS will give you all appointments scheduled for the month being viewed
	- selecting a week of the month and choosing INFO opens a more detailed view of the week.
	  this view provides available times for scheduling appointments for each day in the week
	  as well as appointmentID, customer, and contact name for any appointments scheduled
	- selecting APPOINTMENTS will give you all appointments scheduled for the week being viewed
	- selecting a day and pressing INFO opens the view of the appointments scheduled for the day
	  this view provides all the information about the appointment and allows the user to Add,
	  Update or Delete appointments for that day
4. From the main menu select Reporting
	- From this view you can create a report for: Appointments by Type(providing the range of 
	  of dates being requested), Appointments by Month(providing the year being requested),
	  A Schedule for a Contact(providing the contact being requested and date range being 
	  requested), and Clients by First Level Division (providing division being requested)
	- Selecting report info and pressing RUN will open a display with all report info
  
  
