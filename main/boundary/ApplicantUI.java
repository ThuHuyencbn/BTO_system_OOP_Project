package main.boundary;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import main.boundary.Interface.CRUDEnquiryInterface;
import main.boundary.Interface.UserInterface;
import main.controller.ApplicationController;
import main.controller.EnquiryController;
import main.controller.FlatBookingController;
import main.controller.ProjectController;
import main.controller.UserController.ApplicantController;
import main.entity.Application;
import main.entity.Enquiry;
import main.entity.Enum.EnquiryStatus;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.FlatType;
import main.entity.Project;
import main.entity.User.Applicant;
import main.utility.IsValid;
    /**
     * Boundary class representing the UI for an Applicant user.
     * Implements both {@link UserInterface} and {@link CRUDEnquiryInterface}.
     * Provides methods for user authentication, application management,
     * enquiry creation, and flat booking.
     */
public class ApplicantUI implements UserInterface, CRUDEnquiryInterface{
    /**
     * The currently logged-in applicant.
     */

    private final Applicant currentApplicant;
    /**
     * Scanner used for capturing user input in the terminal.
     */
    protected Scanner sc;

    /**
     * Constructs an ApplicantUI for the specified applicant.
     * 
     * @param currentApplicant the currently authenticated applicant
     */
    public ApplicantUI(Applicant currentApplicant){
        this.currentApplicant = currentApplicant;
        this.sc = new Scanner(System.in);
    }
    /**
     * Starts the applicant UI menu loop.
     * Allows interaction with all applicant-level features.
     */
    @Override
    public void start() {
        while (true) {
            System.out.println("\n=== Applicant Menu ===");
            System.out.println("1. Change password");
            System.out.println("2. View All Available Project");
            System.out.println("3. Apply for a Project");
            System.out.println("4. View Application Status");
            System.out.println("5. Withdraw application");
            System.out.println("6: View Enquiry");
            System.out.println("7. Create an Enquiry");
            System.out.println("8. Edit Enquiry");
            System.out.println("9. Delete Enquiry");
            System.out.println("10. Book a flat");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        changePassword();
                        break;
                    case 2:
                        viewAvailableProjects();
                        break;
                    case 3:
                        applyForProject();
                        break;
                    case 4:
                        viewApplicationStatus();;
                        break;
                    case 5:
                        withdrawApplication();
                        break;
                    case 7:
                        createEnquiry();
                        break;
                    case 8:
                        editEnquiry();
                        break;
                    case 9:
                        deleteEnquiry();
                        break;
                    case 10:
                        bookFlat(currentApplicant);
                        break;
                    case 6:
                        viewEnquiry(currentApplicant);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid choice.");
                }
            }catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
    /**
     * Allows the applicant to change their password with input validation.
     */

    @Override
    public void changePassword(){
        try {
            
            String password;
            while(true){
            System.out.print("Passwords should be 8 characters long with at least one lower, upper case and one number\nEnter New Password:");
            password = sc.nextLine();
            if (IsValid.isValidPassword(password) == false){
                System.out.println("Incorrect password format");
            }
            else{
                break;
            }
            }
            ApplicantController.changePassword(currentApplicant, password);
            System.out.println("Password updated successfully");


        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Displays all available projects, with optional filters by location or flat type.
     */
    private void viewAvailableProjects() {
        try {
            Date today = Date.valueOf(LocalDate.now());
            String choice = null;
            while(true){
            System.out.println("Do you want to filter out project by Location?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            choice = sc.nextLine();
            if ("1".equals(choice) || "2".equals(choice)){
                break;
            }
            else{
                System.out.println("Invalid input");
            }
            }
            String location = null;
            switch(choice){
                case "1":
                    System.out.println("Please enter the Neighborhood you want to search: ");
                    location = sc.nextLine();
                    break;
                case "2":
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            String choice2 = null;
            while(true){
            System.out.println("Do you want to filter out project by Flat Type?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            choice2 = sc.nextLine();
            if ("1".equals(choice) || "2".equals(choice)){
                break;
            }
            else{
                System.out.println("Invalid input");
            }
            }
            FlatType flatType = null;
            switch(choice2){
                case "1":
                    System.out.println("Please choose one type of Flat you want to search: ");
                    System.out.println("1. TWO_ROOM");
                    System.out.println("2. THREE_ROOM");
                    String flat = sc.nextLine();
                    if("1".equals(flat)){
                        flatType = FlatType.TWO_ROOM;
                    }else{
                        flatType = FlatType.THREE_ROOM;
                    }
                    break;
                case "2":
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            List<Project> projects = ProjectController.getAvailableProjects(currentApplicant, flatType, location);
            if (projects.isEmpty()) {
                System.out.println("No projects available.");
            } else {
                System.out.println("Here is the list of projects: ");

                // Print project details in table format
                for (Project p : projects) {
                    if (today.after(p.getApplicationClosingDate())){
                        continue;
                    }
                    System.out.println(p);
                }
                
            }
        }
        catch(Exception e) {
                        System.err.println("Error viewing projects: " + e.getMessage());
        }
    }
    /**
     * Allows the applicant to apply for a project and choose a flat type.
     * Only allows one application at a time.
     */
    public void applyForProject() {
        try {
            Date today = Date.valueOf(LocalDate.now());
            if(ApplicationController.hasExistingApplication(currentApplicant)){
                System.out.println("Error: You can only apply for one project at a time.");
                return;
            }
            List<Project> availableProjects = ProjectController.getAvailableProjects(currentApplicant, null, null);
            for(Project project: availableProjects){
                if (today.after(project.getApplicationClosingDate())){
                    continue;
                }
                System.out.println(project);
            }
            System.out.print("Enter Project Name: ");
            String projectName = sc.nextLine();
            Project project = ProjectController.findProjectByName(projectName);
            if(project == null){
                throw new IllegalArgumentException("No project found!");
            }
            if (today.after(project.getApplicationClosingDate())){
                throw new IllegalArgumentException("Applications have closed");
            }
            if(!availableProjects.contains(project)){
                System.out.println("You are not allowed to apply for this project");
                return;
            }
            FlatType flatType = null;
            if(project.getFlatTypes().size() ==1){
                flatType = project.getFlatTypes().get(0);
            }else{
                System.out.println("Choose one flat type avaiable(TWO_ROOM/THREE_ROOM): ");
                System.out.println("1. TWO_ROOM, Available units: "+ project.getUnitsAvailable().get(0)+" Price: "+ project.getSellingPrice().get(0));
                System.out.println("2. THREE_ROOM, Available units: " + project.getUnitsAvailable().get(1)+" Price: "+ project.getSellingPrice().get(1));
                int flatChoice;
                boolean valid = true;
                while(valid){
                    flatChoice = sc.nextInt();
                    sc.nextLine();

                    switch(flatChoice){
                        case 1:
                            flatType = FlatType.TWO_ROOM;
                            valid = false;
                            break;
                        case 2:
                            flatType = FlatType.THREE_ROOM;
                            valid = false;
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                }
            }
            Application newApplication = ApplicationController.createApplicationForm(currentApplicant, project, flatType, currentApplicant.getMaritalStatus(), currentApplicant.getAge());
            currentApplicant.setApplication(newApplication);
            System.out.println("New application created successfully");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Displays the applicant’s current application status and flat booking if available.
     */
    protected void viewApplicationStatus(){
        if(currentApplicant.getApplication() == null){
            System.out.println("You have no application or the Project has been deleted!");
            return;
        }
        System.out.println(currentApplicant.getApplication().toString());
        if (currentApplicant.getFlatBooking() != null){
            System.out.println("You have a Flat Booking\n" + currentApplicant.getFlatBooking().toString());
        }
    }
    /**
     * Withdraws the applicant's current application, if any.
     */
    protected void withdrawApplication() {
        try {
            if (currentApplicant.getApplication() == null){
                throw new IllegalArgumentException("You have no applications");
            }
            ApplicationController.withdrawApplication(currentApplicant.getApplication());
            System.out.println(" Application withdrawn successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Creates a new enquiry for a selected visible project.
     */
    @Override
    public void createEnquiry() {
        try {
            System.out.println("Here is the list of projects");
            for (Project proj : ProjectController.projects){
                if (proj.isVisible()){
                System.out.println(proj);}
            }
            System.out.print("Enter Project Name: ");
            String projectName = sc.nextLine();
            Project project = ProjectController.findProjectByName(projectName);
            if(project == null){
                throw new IllegalArgumentException("No project found");
            }
            String enquiryText = null;
            while(true){
            System.out.print("Enter enquiry: ");
            enquiryText = sc.nextLine();
            if (enquiryText != null || !"".equals(enquiryText)){
                break;
            }
            else{
                System.out.println("Please enter an enquiry");
            }
            }

            EnquiryController.createEnquiry(currentApplicant, project, enquiryText);
            System.out.println(" Enquiry submitted successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Edits an existing enquiry submitted by the applicant if it is still pending.
     */

    @Override
    public void editEnquiry() {
        try {
            // Display the enquiries created by the applicant
            System.out.println("Here is the list of enquiries you have created. You can only edit enquiries with status 'PENDING'.");
            if(currentApplicant.getEnquiries().isEmpty()){
                System.out.println("You have no enquiries!");
                return;
            }
            EnquiryController.displayEnquiriesByApplicant(currentApplicant.getEnquiries());
            
    
            // Start the do-while loop to allow repeated attempts at entering a valid enquiry ID
            String enquiryID = "";
            Enquiry enquiry = null;
            do {
                System.out.print("Enter Enquiry ID you want to edit: ");
                enquiryID = sc.nextLine().trim().toUpperCase();
                if (enquiryID.isEmpty()) {
                    throw new IllegalArgumentException("");
                      // Re-prompt user for a valid ID
                }
    
                // Retrieve the enquiry by its ID
                enquiry = EnquiryController.getEnquiryByID(enquiryID);
                if (enquiry == null) {
                    System.out.println("No enquiry found with this ID. Please try again.");
                } else {
                    // If a valid enquiry is found, check if it is pending
                    if (enquiry.getStatus() != EnquiryStatus.PENDING) {
                        System.out.println("You can only edit enquiries with status 'PENDING'.");
                        enquiry = null;  // Reset enquiry if it's not pending
                    }
                }
            } while (enquiry == null);  // Continue looping until a valid "PENDING" enquiry is found
    
            // Prompt for the new enquiry text
            System.out.print("Enter new enquiry text: ");
            String newEnquiryText = sc.nextLine().trim();
            if (newEnquiryText.isEmpty()) {
                throw new IllegalArgumentException("Enquiry text cannot be empty.");
            }
    
            // Update the enquiry with the new text (instead of creating a new one)
            EnquiryController.editEnquiry(enquiry.getEnquiryID(), currentApplicant.getUserId(), newEnquiryText);
            System.out.println("Enquiry edited successfully.");
    
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Deletes an existing enquiry submitted by the applicant.
     * Only allows deletion of the applicant's own enquiries.
     */

    @Override
    public void deleteEnquiry(){
        try {
            List<Enquiry> enq = new ArrayList<>();
            enq = EnquiryController.getEnquiriesByApplicant(currentApplicant.getUserId());
            if (enq.isEmpty()){
                throw new IllegalArgumentException("You have no enquiries");
            }
            // Display the enquiries created by the applicant
            System.out.println("Here is the list of enquiries you have created.");

            EnquiryController.displayEnquiriesByApplicant(currentApplicant.getEnquiries());

            // Start the do-while loop to allow repeated attempts at entering a valid enquiry ID
            String enquiryID;
            Enquiry enquiry = null;
            do {
                System.out.print("Enter Enquiry ID you want to delete: ");
                enquiryID = sc.nextLine().trim().toUpperCase();
                if (enquiryID.isEmpty()) {
                    System.out.println("Enquiry ID cannot be empty. Please try again.");
                    continue;  // Re-prompt user for a valid ID
                }

                // Retrieve the enquiry by its ID
                enquiry = EnquiryController.getEnquiryByID(enquiryID);
                if (enquiry == null) {
                    System.out.println("No enquiry found with this ID. Please try again.");
                } else if(!enquiry.getApplicant().equals(currentApplicant)){
                    System.out.println("You are not allowed to delete enquiry of others");
                }
                else {
                    // If a valid enquiry is found, check if it is pending
                    EnquiryController.deleteEnquiry(enquiry);
                }
            } while (enquiry == null);  // Continue looping until a valid "PENDING" enquiry is found
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Allows the applicant to book a flat after their application has been marked as successful.
     * Prevents double bookings and checks application status before proceeding.
     * 
     * @param currentApplicant the current applicant attempting to book
     */
    public void bookFlat(Applicant currentApplicant){
        if(currentApplicant.getApplication().getStatus().equals(ApplicationStatus.BOOKED)){
            System.out.println("You already booked a flat.  You are not allowed to book more!");
            System.out.println("Here is the information of your Flat Booking.");
            System.out.println(currentApplicant.getFlatBooking());
            return;
        }else if (!currentApplicant.getApplication().getStatus().equals(ApplicationStatus.SUCCESSFUL)) {
            System.out.println("Your application must be successfull to book a flat");
            return;
        }else{
            System.out.println("Here is your information about application\n" + currentApplicant.getApplication().toString());
            System.out.println("Do you want to book a flat: ");
            System.out.println("1. Yes");
            System.out.println("2. No");
            String choice = sc.nextLine();
            switch(choice){
                case "1":
                    FlatBookingController.generateFlatBooking(currentApplicant, currentApplicant.getApplication().getFlatType(), currentApplicant.getApplication().getProject());
                    System.out.println("Booking flat successfully! We will notify Officer to book a flat for you. ");
                    break;
                case "2":
                    break;
                default:
                    System.out.println("Invalid choice.");
            }            
        }
    }

}

