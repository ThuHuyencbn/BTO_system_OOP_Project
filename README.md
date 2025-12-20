1. OBJECTIVES
The main objective of this assignment is
• to apply the Object-Oriented (OO) concepts you have learnt in the course,
• to model, design and develop an OO application.
• to gain familiarity with using Java as an object-oriented programming language.
• to work collaboratively as a group to achieve a common goal.

2.Build-To-Order (BTO) Management System  
BTO  Management System is a system for  applicants  and HDB staffs to view, apply and manage for BTO projects.  
The following are information about the system:  
a. Overview of the System: 
• The system will act as a centralized hub for all applicants and HDB staffs 
• All users will need to login to this hub using their Singpass account. 
 o User ID will be their NRIC, that starts with S or T, followed by 7-digit number and ends with another letter 
 o Assume all users use the default password, which is password. 
 o A user can change password in the system. o Additional Information about the user: Age and Marital Status 
• A user list can be initiated through a file uploaded into the system at initialization. 

b. Applicant 
• Can only view the list of projects that are open to their user group (Single or Married) and if their visibility has been toggled “on”. 
• Able to apply for a project – cannot apply for multiple projects. o Singles, 35 years old and above, can ONLY apply for 2-Room o Married, 21 years old and above, can apply for any flat types (2-Room or 3-Room) 
• Able to view the project he/she applied for, even after visibility is turned off, and the application status 
   o Pending:  Entry status upon application  –  No conclusive decision made about the outcome of the application 
   o Successful: Outcome of the application is successful, hence invited to make a flat booking with the HDB Officer 
   o Unsuccessful:  Outcome of the application is unsuccessful, hence cannot make a flat booking for this application. Applicant may apply for another project. 
   o Booked: Secured a unit after a successful application and completed a flat booking with the HDB Officer. 
• If Application status is “Successful”, Applicant can book one flat through the HDB Officer (Only HDB Officer can help to book a flat) - cannot book more  than  one flat,  within a project or across different project
• Allowed to request withdrawal for their BTO application before/after flat booking 
• Able to submit enquiries, a string, regarding the projects. 
• Able to view, edit, and delete their enquiries.  

c. HDB Officer 
• HDB Officer possess all applicant’s capabilities. 
• Able to register to join a project if the following criteria are meant: 
 o No intention to apply for the project as an Applicant (Cannot apply for the project as an Applicant before and after becoming an HDB Officer of the project) 
 o Not a HDB Officer (registration not approved) for another project within an application period (from application opening date, inclusive, to application closing date, inclusive) 
• Able to see the status of their registration to be a HDB Officer for a project
• Registration to be a HDB Officer of the project is subject to approval from the HDB Manager in charge of the project.  Once approved, their profile will reflect the project he/she is a HDB Officer for.  
• Able to apply for other projects in which he/she is not handling – Once applied for a  BTO  project, he/she cannot register  to handle the  same project 
• Able to view the details of the project he/she is handling regardless of the visibility setting. 
• Not allowed to edit the project details. 
• Able to view and reply to enquiries regarding the project he/she is handling 
  o With  Applicant’s  successful BTO application, HDB Officer’s flat selection responsibilities: 
     Update the number of flat for each flat type that are remaining 
     Retrieve applicant’s BTO application with applicant’s NRIC 
     Update applicant’s project status, from “successful” to “booked”. 
     Update applicant’s profile with the type of flat (2-Room or 3-Room) chosen under a project 
• Able to generate receipt of the applicants with their respective flat booking details – Applicant’s Name, NRIC, age, marital status, flat type booked and its project details.

d. HDB Manager 
• Able to create, edit, and delete BTO project listings. 
• A BTO project information entered by the HDB Manager will include information like: 
 o Project Name o Neighborhood (e.g. Yishun, Boon Lay, etc.) 
 o Types of Flat – Assume there are only 2-Room and 3-Room 
 o The number of units for the respective types of flat 
 o Application opening date o Application closing date o HDB Manager in charge (automatically tied to the HDB Manager who created the listing) 
 o Available HDB Officer Slots (max 10) 
• Can only be handling one project within an application period (from application opening date, inclusive, to application closing date, inclusive) 
• Able to toggle the visibility of the project to “on” or “off”. This will be reflected in the project list that will be visible to applicants. 
• Able to view all created  projects, including projects created by other HDB Manager, regardless of visibility setting. 
• Able to filter and view the list of projects that they have created only. 
• Able to view pending and approved HDB Officer registration. 
• Able to  approve  or reject HDB Officer’s  registration as the HDB Manager in-charge of the project  –  update project’s remaining HDB Officer slots 
• Able to approve or reject  Applicant’s  BTO  application  –  approval is limited to the supply of the flats (number of units for the respective flat types) 
• Able to approve or reject Applicant's request to withdraw the application. 
• Able to generate a report of the list of applicants with their respective flat booking – flat type, project name, age, marital status 
 o There should be filters to generate a list based on various categories (e.g. report of married applicants’ choice of flat type) 
• Cannot apply for any BTO project as an Applicant. • Able to view enquiries of ALL projects. 
• Able to view and reply to enquiries regarding the project he/she is handling.
