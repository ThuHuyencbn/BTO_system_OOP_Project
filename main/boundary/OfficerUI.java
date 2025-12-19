package main.boundary;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import main.boundary.Interface.ReplyEnquiryInterface;
import main.controller.*;
import main.controller.UserController.ApplicantController;
import main.controller.UserController.OfficerController;
import main.entity.Application;
import main.entity.Enquiry;
import main.entity.Enum.EnquiryStatus;
import main.entity.FlatBooking;
import main.entity.Project;
import main.entity.Receipt;
import main.entity.Enum.*;
import main.entity.User.Applicant;
import main.entity.User.HDBOfficer;
import main.repository.*;
import main.utility.IsValid;

/**
 * UI class for HDB Officers, supporting both officer-specific
 * operations and applicant-like functionalities.
 */
public class OfficerUI extends ApplicantUI implements ReplyEnquiryInterface {

    /** The currently logged-in HDB officer. */
    private final HDBOfficer currentOfficer;

    /**
     * Constructs an OfficerUI instance.
     *
     * @param currentOfficer the officer currently using the system
     */
    public OfficerUI(HDBOfficer currentOfficer) {
        super(currentOfficer);  // Call superclass constructor
        this.currentOfficer = currentOfficer;  // Officer-specific initialization
    }

    /**
     * Starts the Officer menu loop and handles user interaction.
     */
    @Override
    public void start() {
        while (true) {
            System.out.println("\n=== Officer Menu ===");
            System.out.println("1. Change password");
            System.out.println("2. Apply for a project as Applicant");
            System.out.println("3. Withdraw application");
            System.out.println("4. Book Flat as applicant");
            System.out.println("5. View Application Status");
            System.out.println("6. View Enquiry as Applicant");
            System.out.println("7. Create an Enquiry as Applicant");
            System.out.println("8. Edit Enquiry as Applicant");
            System.out.println("9. Delete Enquiry as Applicant");
            System.out.println("10. Register to handle a Project");
            System.out.println("11. See Registration Status");
            System.out.println("12. View Details of Project Handling");
            System.out.println("13. View Enquiries of Handling Project");
            System.out.println("14. Reply Enquiries of Handling Project");
            System.out.println("15. Flat Selection Responsibilities");
            System.out.println("16. Generate Receipt for Applicant");
            System.out.println("17. View All Projects");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    case 1:
                        changePassword();
                        break;
                    case 2:
                        applyForProject();
                        break;
                    case 3:
                        withdrawApplication();
                        break;
                    case 4:
                        bookFlat(currentOfficer);
                        break;
                    case 5:
                        viewApplicationStatus();
                        break;
                    case 6:
                        viewEnquiry((Applicant) currentOfficer);
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
                        registerProject();
                        break;
                    case 11:
                        viewRegistrationStatus();
                        break;
                    case 12:
                        viewHandlingProject();
                        break;
                    case 13:
                        viewEnquiry();
                        break;
                    case 14:
                        replyEnquiry();
                        break;
                    case 15:
                        flatSelectionResponsibilities();
                        break;
                    case 16:
                        generateReceipt();
                        break;
                    case 17:
                        viewAllProjects();
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
    /**
     * Displays the application status of the current officer (as an applicant).
     * If no application exists, notifies the user accordingly.
     */
    @Override
    protected void viewApplicationStatus() {
        if (currentOfficer.getApplication() == null) {
            System.out.println("You have no application!");
            return;
        }
        System.out.println(currentOfficer.getApplication().toString());
    }
    /**
     * Allows the officer to apply for a project as an applicant.
     * Ensures the officer is not already handling the project or has an existing application.
     * Prompts the user to select a flat type if multiple are available.
     */
    @Override
    public void applyForProject() {
    try {
        Date today = Date.valueOf(LocalDate.now());

        if (ApplicationController.hasExistingApplication(currentOfficer)) {
            System.out.println("Error: You can only apply for one project at a time.");
            return;
        }

        List<Project> availableProjects = ProjectController.getAvailableProjects(currentOfficer, null, null);

        for (Project project : availableProjects) {
            if (today.after(project.getApplicationClosingDate())) {
                continue;
            }
            System.out.println(project);
        }

        System.out.print("Enter Project Name: ");
        String projectName = sc.nextLine();
        Project project = ProjectController.findProjectByName(projectName);

        if (project == null) {
            throw new IllegalArgumentException("No project found!");
        }

        if (today.after(project.getApplicationClosingDate())) {
            throw new IllegalArgumentException("Applications have closed.");
        }

        if (!availableProjects.contains(project)) {
            System.out.println("You are not allowed to apply for this project.");
            return;
        }

        if (project.getAssignedOfficers().contains(currentOfficer)) {
            throw new IllegalArgumentException("You cannot apply for a project you are assigned to.");
        }

        FlatType flatType = null;

        if (project.getFlatTypes().size() == 1) {
            flatType = project.getFlatTypes().get(0);
        } else {
            System.out.println("Choose one flat type available (TWO_ROOM/THREE_ROOM): ");
            System.out.println("1. TWO_ROOM, Available units: " + project.getUnitsAvailable().get(0) +
                    " Price: " + project.getSellingPrice().get(0));
            System.out.println("2. THREE_ROOM, Available units: " + project.getUnitsAvailable().get(1) +
                    " Price: " + project.getSellingPrice().get(1));

            int flatChoice;
            boolean valid = true;

            while (valid) {
                flatChoice = sc.nextInt();
                sc.nextLine();

                switch (flatChoice) {
                    case 1:
                        flatType = FlatType.TWO_ROOM;
                        valid = false;
                        break;
                    case 2:
                        flatType = FlatType.THREE_ROOM;
                        valid = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1 or 2.");
                }
            }
        }

        Application newApplication = ApplicationController.createApplicationForm(
                currentOfficer, project, flatType,
                currentOfficer.getMaritalStatus(), currentOfficer.getAge());

        currentOfficer.setApplication(newApplication);
        System.out.println("New application created successfully.");

    } catch (Exception e) {
        System.out.println("Unexpected error: " + e.getMessage());
    }
}
    /**
     * Allows the officer to change their password.
     * Prompts for a new password and validates its format before updating.
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

            OfficerController.changePassword(currentOfficer, password);
            System.out.println("Password updated successfully");


        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Displays the details of the project currently handled by the officer.
     * Informs the officer if no project is being handled.
     */
    public void viewHandlingProject() {
        try {
            
            Project project = currentOfficer.getAssignedProject();

            if (project != null) {
                System.out.println("Here is the details of the Project you are handling: ");
                System.out.println("Name: " + project.getProjectName());
                System.out.println("Neighborhood: " + project.getNeighborhood());
                System.out.println("Flat Types: " + project.getFlatTypes());
                System.out.println("Units: " + project.getUnitsAvailable());
                System.out.println("Open: " + project.getApplicationOpeningDate());
                System.out.println("Close: " + project.getApplicationClosingDate());
                System.out.println("Visible: " + project.isVisible());
            } else {
                System.out.println(" You are not handling any projects right now");
            }
        } catch (Exception e) {
            System.err.println(" Error retrieving project: " + e.getMessage());
        }
    }
    /**
     * Allows the officer to register for handling a project.
     * Validates project name and registration eligibility before proceeding.
     */
    private void registerProject(){
        if (currentOfficer.getRegistration() != null){
            System.out.println("You have a current registration");
        }
        for (Project pro: ProjectController.projects){
            System.out.println(pro);
            }
        System.out.println("Enter the project name that you want to register: ");
        String projectName = sc.nextLine();
        Project project = ProjectController.findProjectByName(projectName);
        if(project == null){
            System.out.println("The project ID is not found!");
            return;
        }
        if(!RegistrationController.isValidForRegister(currentOfficer, project)){
            System.out.println("You are not allowed to register this project!");
            return;
        }
        RegistrationController.createRegistration(currentOfficer, project);
        System.out.println("Registration submitted successfully!");
    }
    /**
     * Displays the current officer's registration status for a project.
     * Shows the project name and registration status if available.
     */

    private void viewRegistrationStatus(){
        try {
            System.out.println("Here is the details of your Registration: ");
            if (currentOfficer.getRegistration() == null){
                System.out.println("You have no Registration or the Project has been deleted");
                return;
            }
            System.out.println("Project registered to handle: " + currentOfficer.getRegistration().getProject().getProjectName());
            System.out.println("Registration Status: " + currentOfficer.getRegistration().getRegistrationStatus());
        } catch (Exception e) {
            System.out.println("You have no registration");
        }
    }
    /**
     * Displays all enquiries related to the officer's assigned project.
     * If no project is assigned, the method notifies the officer.
     */
    private void viewEnquiry(){
        try {
            Project assignedProject = currentOfficer.getAssignedProject();
            if(assignedProject == null){
                System.out.println("You are not handling any projects");
                return;
            }
            System.out.print("Here is the list of Enquiries for the Project: ");
            List<Enquiry> filteredEnquiries = EnquiryController.getEnquiriesByProject(currentOfficer.getAssignedProject());
            System.out.println(currentOfficer.getAssignedProject().getProjectName());
            if (filteredEnquiries != null) {
                for(Enquiry enq : filteredEnquiries){
                    System.out.println(enq.generateEnquiryDetails());
                }
            } else {
                System.out.println("There is no enquiries");
            }
        } catch (Exception e) {
            System.err.println(" Error retrieving project: " + e.getMessage());
        }
    }
    /**
     * Allows the officer to reply to an enquiry for the project they are handling.
     * Prevents replying to their own enquiries and already responded enquiries.
     */
    @Override
    public void replyEnquiry(){
        Project assignedProject = currentOfficer.getAssignedProject();
            if(assignedProject == null){
                System.out.println("You are not handling any projects");
                return;
            }
        viewEnquiry();
        System.out.println("Enter an Enquiry ID to reply: ");
        String enquiryID = sc.nextLine();
        Enquiry enquiry = EnquiryController.getEnquiryByID(enquiryID);
        if(enquiry == null){
            System.out.println("The Enquiry ID is not found!");
            return;
        }
        if(enquiry.getApplicant().getUserId().equals(currentOfficer.getUserId())){
            System.out.println("You are not allowed to reply your own Enquiry");
            return;
        }
        if(enquiry.getStatus() == EnquiryStatus.RESPONDED){
            System.out.println("This enquiry has already been replied to");
            return;
        }
        System.out.println("Enter the reply text for the Enquiry: ");
        String reply = sc.nextLine();
        enquiry.setEnquiryReply(reply);
        EnquiryController.updateEnquiryStatus(enquiryID, EnquiryStatus.RESPONDED);
        EnquiryRepository.writeAllEnquiries(EnquiryController.enquiries);
    }
    /**
     * Generates and displays a receipt for the specified applicant.
     * Validates the applicant ID and booking status before generating the receipt.
     */
    public void generateReceipt(){
        System.out.println("Enter the Applicant ID that you want to generate receipt: ");
        String applicantID = sc.nextLine();
        Applicant applicant = ApplicantController.getUserById(applicantID);
        if(applicant == null){
            System.out.println("The Applicant ID is not found!");
            return;
        }
        if(applicant.getFlatBooking() == null){
            System.out.println("The Applicant have not booked a flat. You cannot generate receipt.");
            return;
        }
        Receipt receipt = ReceiptController.createReceipt(applicant);
        ReceiptController.displayReceipt(receipt);
    }
    /**
     * Displays all existing projects in the system.
     * Notifies if no projects are available.
     */
    private void viewAllProjects() {
        try {
            List<Project> projects = ProjectController.projects;
            if (projects.isEmpty()) {
                System.out.println("No projects available.");
            } else {
                System.out.println("Here is the list of projects: ");

                // Print project details in table format
                for (Project p : projects) {
                    // Get assigned officers and convert them to a comma-separated string of names
                    System.out.println(p);
                }
                
            }
        }
        catch(Exception e) {
                        System.err.println(" Error viewing projects: " + e.getMessage());
        }
    }
    /**
     * Allows the officer to approve pending flat booking requests.
     * Displays details of the selected booking and allows updates to application status and unit availability.
     */
    private void flatSelectionResponsibilities(){
        System.out.println("Here is the list of Pending Flat Booking Request: ");
        FlatBookingController.printFlatBooking(FlatBookingController.getPendingFlatBooking());
        System.out.println("Enter a Flat Booking ID that you want to view details: ");
        String flatID = sc.nextLine();
        FlatBooking flatBooking = FlatBookingController.getFlatBookingById(flatID);
        if(flatBooking == null){
            System.out.println("The Flat Booking ID is not found!");
            return;
        }
        if(flatBooking.getFlatBookingStatus() == FlatBookingStatus.APPROVED){
            throw new IllegalArgumentException("Flat booking has already been approved");
        }
        System.out.println("Here is the detail of the Flat Booking: ");
        System.out.println(flatBooking);
        System.out.println("Do you want to retrieve Applicant's application ?");
        System.out.println("1.Yes");
        System.out.println("2. No");
        int choice0 = sc.nextInt();
        sc.nextLine();
        switch(choice0){
            case 1:
                System.out.println(flatBooking.getApplicant().getApplication());
                break;
            case 2:
                break;
            default:
                System.out.println("Invalid choice");
        }
        System.out.println("Do you want to update flat units remaining and application status: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice = sc.nextInt();
        sc.nextLine();
        switch(choice){
            case 1:
                FlatBookingController.updateFlatBookingStatus(flatBooking, FlatBookingStatus.APPROVED);
                ProjectController.updateUnitRemaining(flatBooking.getProject(), flatBooking.getFlatType(), false);
                System.out.println("Updated Remaining Unit successfully");
                ApplicationController.updateApplicationStatus(flatBooking.getApplicant().getApplication().getApplicationId(), ApplicationStatus.BOOKED);
                flatBooking.setFlatBookingStatus(FlatBookingStatus.APPROVED);
                System.out.println("Updated Application Status to 'BOOKED' successfully! ");
                flatBooking.getApplicant().setFlatBooking(flatBooking);
                System.out.println("Update Applicant's profile with Flat Type and Project successfully!");
                BookingRepository.writeAllFlatBookings(FlatBookingController.flatBookings);
                break;
            case 2:
                return;
            default:
                throw new AssertionError();
        }
        ApplicationRepository.writeAllApplication(ApplicationController.getAllApplications());
    }

}