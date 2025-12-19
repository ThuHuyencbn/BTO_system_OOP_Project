package main.boundary;

import java.text.SimpleDateFormat;
import java.util.*;
import main.boundary.Interface.ReplyEnquiryInterface;
import main.boundary.Interface.UserInterface;
import main.controller.ApplicationController;
import main.controller.EnquiryController;
import main.controller.FlatBookingController;
import main.controller.ProjectController;
import main.controller.RegistrationController;
import main.controller.UserController.ApplicantController;
import main.controller.UserController.ManagerController;
import main.controller.UserController.OfficerController;
import main.entity.*;
import main.entity.Enum.EnquiryStatus;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.FlatType;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.RegistrationStatus;
import main.entity.User.HDBManager;
import main.entity.User.HDBOfficer;
import main.repository.ApplicationRepository;
import main.repository.BookingRepository;
import main.repository.EnquiryRepository;
import main.utility.IsValid;

    /**
     * UI class representing the interface for HDB Managers.
     * Allows managers to create, edit, delete, and view projects;
     * approve/reject applications and registrations;
     * generate booking reports; and respond to applicant enquiries.
     * Implements both {@link UserInterface} and {@link ReplyEnquiryInterface}.
     */
public class ManagerUI implements UserInterface, ReplyEnquiryInterface{
    /**
     * The currently logged-in HDB manager.
     */
    protected final HDBManager currentManager;
    /**
     * Shared scanner for capturing terminal input.
     */
    protected static Scanner sc;

    /**
     * Constructs a new ManagerUI for the given manager.
     *
     * @param currentManager the manager using the UI
     */
    public ManagerUI(HDBManager currentManager){
        this.currentManager = currentManager;
        this.sc = new Scanner(System.in);
    }
    /**
     * Starts the manager interface menu loop.
     * Presents multiple options for managing the BTO system.
     */
    @Override
    public void start() {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. Create Project");
            System.out.println("2. Toggle Visibility");
            System.out.println("3. Edit Created Project");
            System.out.println("4. Delete Created project");
            System.out.println("5. Generate Report");
            System.out.println("6. View all Registrations");
            System.out.println("7. Approve/Reject Registration of Officer");
            System.out.println("8. Approve/Reject Application of Applicant");
            System.out.println("9. Change password");
            System.out.println("10. Approve/Reject Application Withdrawal of Applicant");
            System.out.println("11. View All Enquiries of All Project");
            System.out.println("12. View and Reply Enquiries of Handling Project");
            System.out.println("13. View Projects");
            System.out.println("0. Exit");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createProject();
                    break;
                case 2:
                    toggleVisibility();
                    break;
                case 3:
                    editProject();
                    break;
                case 4:
                    deleteProject();
                    break;
                case 5:
                    generateReport();
                    break;
                case 6:
                    viewAllRegistration();
                case 7:
                    approveRejectOfficer();
                    break;
                case 8:
                    approveRejectApplicant();
                    break;
                case 9: 
                    changePassword();
                    break;
                case 10:
                    approveRejectApplicationWithdrawal();
                    break;
                case 11:
                    viewEnquiries();
                    break;
                case 12:
                    replyEnquiry();
                    break;
                case 13:
                    viewProjects();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    /**
     * Displays all registrations for the manager's latest created project.
     */
    private void viewAllRegistration(){
        try {
            // Assuming you want the latest project created by the manager
            List<Project> createdProjects = currentManager.getCreatedProjects();
            if (createdProjects.isEmpty()) {
                System.out.println("You haven't created any projects yet.");
                return;
            }

            System.out.println("Here is the list of registrations for the project you are handling:");
    
            Project latestProject = createdProjects.get(createdProjects.size() - 1);
            List<Registration> registrations = RegistrationController.getRegistrationForProject(latestProject);
            if(registrations == null || registrations.isEmpty()){
                System.out.println("There is no registration for this project!");
                return;
            }
            RegistrationController.printRegistration(registrations);
        }catch(Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Allows the manager to approve or reject officer registrations.
     */
    private void approveRejectOfficer() {
        try {
            
            // Assuming you want the latest project created by the manager
            List<Project> createdProjects = currentManager.getCreatedProjects();
            if (createdProjects.isEmpty()) {
                System.out.println("You haven't created any projects yet.");
                return;
            }

            System.out.println("Here is the list of pending registrations for the project you are handling:");
    
            Project latestProject = createdProjects.get(createdProjects.size() - 1);
            List<Registration> registrations = RegistrationController.getPendingRegistrationForProject(latestProject);
            if(registrations == null || registrations.isEmpty()){
                System.out.println("There is no pending registration for this project!");
                return;
            }
            for(Registration regis:registrations){
                System.out.println(regis);
            }
    
            Scanner sc = new Scanner(System.in);
            Registration selectedRegistration = null;
    
            do {
                System.out.print("Enter one Registration ID to Approve/Reject: ");
                String regID = sc.nextLine().trim();
                selectedRegistration = RegistrationController.getRegistrationByID(regID);
    
                if (selectedRegistration == null || !registrations.contains(selectedRegistration)) {
                    System.out.println("Invalid Registration ID. Please try again.");
                    selectedRegistration = null;
                }
            } while (selectedRegistration == null);
    
            // Proceed to approval or rejection
            System.out.print("Enter 'A' to Approve or 'R' to Reject: ");
            String decision = sc.nextLine().trim().toUpperCase();
            if (decision.equals("A")) {
                Project selectedProject = selectedRegistration.getProject();
                if(selectedProject.getOfficerSlot() == 0){
                    System.out.println("The project has enough officer slots. No more need!");
                    return;
                }
                RegistrationController.updateRegistrationStatus(selectedRegistration, RegistrationStatus.APPROVED);
                // Add the officer to the project's assigned officers list
                HDBOfficer officerToAssign = selectedRegistration.getOfficer();
                ProjectController.addAssignedOfficers(selectedProject, officerToAssign);
                
                // Assign the project to the officer
                officerToAssign.setAssignedProject(latestProject);
                
                System.out.println("Registration approved.");
            } else if (decision.equals("R")) {
                RegistrationController.updateRegistrationStatus(selectedRegistration, RegistrationStatus.REJECTED);
                System.out.println("Registration rejected.");
            } else {
                System.out.println("Invalid decision input. Please enter A or R.");
            }
    
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Allows the manager to approve or reject applications for the current project.
     */
    private void approveRejectApplicant() {
        try {
            // Assuming you want the latest project created by the manager
            List<Project> createdProjects = currentManager.getCreatedProjects();
            if (createdProjects.isEmpty()) {
                System.out.println("You haven't created any projects yet.");
                return;
            }

            System.out.println("Here is the list of applications for the project you are handling:");
    
            Project latestProject = createdProjects.get(createdProjects.size() - 1);
            List<Application> applications = ApplicationController.getPendingApplicationByProject(latestProject);
            if(applications == null || applications.isEmpty()){
                System.out.println("There is no pending applications for this project");
                return;
            }
            ApplicationController.printApplication(applications);
    
            Scanner sc = new Scanner(System.in);
            Application selectedApplication = null;
    
            do {
                System.out.print("Enter one Application ID to Approve/Reject: ");
                String appID = sc.nextLine().trim();
                selectedApplication = ApplicationController.getApplicationByID(appID);
    
                if (selectedApplication == null || !applications.contains(selectedApplication)) {
                    System.out.println("Invalid Application ID. Please try again.");
                    selectedApplication = null;
                }
            } while (selectedApplication == null);
    
            // Proceed to approval or rejection
            System.out.print("Enter 'A' to Approve or 'R' to Reject: ");
            String decision = sc.nextLine().trim().toUpperCase();
            if (decision.equals("A")) {
                if (latestProject.getFlatTypes().size() == 1){
                    if(ApplicationController.countNumberOfSuccessfulApplication(latestProject, selectedApplication.getFlatType()) >= latestProject.getUnitsAvailable().get(0)){
                        System.out.println("No remaining unit available!");
                        return;
                    }
                }else if (latestProject.getFlatTypes().size() ==2){
                    if(selectedApplication.getFlatType().equals(FlatType.TWO_ROOM)){
                        if(ApplicationController.countNumberOfSuccessfulApplication(latestProject, selectedApplication.getFlatType()) >= latestProject.getUnitsAvailable().get(0)){
                            System.out.println("No remaining unit available!");
                            return;
                        }
                    }else{
                        if(ApplicationController.countNumberOfSuccessfulApplication(latestProject, selectedApplication.getFlatType()) >= latestProject.getUnitsAvailable().get(1)){
                            System.out.println("No remaining unit available!");
                            return;
                        }
                    }
                }
                ApplicationController.updateApplicationStatus(selectedApplication.getApplicationId(), ApplicationStatus.SUCCESSFUL);
                System.out.println("Application ID "+ selectedApplication.getApplicationId() + " approved.");
            } else if (decision.equals("R")) {
                ApplicationController.updateApplicationStatus(selectedApplication.getApplicationId(), ApplicationStatus.UNSUCCESSFUL);

                System.out.println("Application rejected.");
            } else {
                System.out.println("Invalid decision input. Please enter A or R.");
            }
            ApplicationRepository.writeAllApplication(ApplicationController.getAllApplications());
    
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Allows the manager to approve or reject application withdrawal requests.
     * Automatically updates bookings and unit availability if withdrawal is approved.
     */
    private void approveRejectApplicationWithdrawal(){
        try {
            // Assuming you want the latest project created by the manager
            List<Project> createdProjects = currentManager.getCreatedProjects();
            if (createdProjects.isEmpty()) {
                System.out.println("You haven't created any projects yet.");
                return;
            }

            System.out.println("Here is the list of withdrawal requests for the project you are handling:");
    
            Project latestProject = createdProjects.get(createdProjects.size() - 1);
            List<Application> withdrawals = ApplicationController.getWithdrawalRequest(latestProject);
            ApplicationController.printApplication(withdrawals);
    
            Scanner sc = new Scanner(System.in);
            Application selectedWithdrawal = null;
    
            do {
                System.out.print("Enter one Application Withdrawal Request ID to Approve/Reject: ");
                String appID = sc.nextLine().trim();
                selectedWithdrawal = ApplicationController.getApplicationByID(appID);
    
                if (selectedWithdrawal == null || !withdrawals.contains(selectedWithdrawal)) {
                    System.out.println("Invalid Application ID. Please try again.");
                    selectedWithdrawal = null;
                }
            } while (selectedWithdrawal == null);
    
            // Proceed to approval or rejection
            System.out.print("Enter 'A' to Approve or 'R' to Reject: ");
            String decision = sc.nextLine().trim().toUpperCase();
            if (decision.equals("A")) {
                ApplicationController.updateApplicationStatus(selectedWithdrawal.getApplicationId(), ApplicationStatus.WITHDRAWN);
                System.out.println("Application ID "+ selectedWithdrawal.getApplicationId() + " withdrawn.");
                selectedWithdrawal.getApplicant().setApplication(null);
                ApplicationController.getAllApplications().remove(selectedWithdrawal);
                ApplicationRepository.writeAllApplication(ApplicationController.getAllApplications());
                if(selectedWithdrawal.getApplicant().getFlatBooking() != null){
                    ProjectController.updateUnitRemaining(selectedWithdrawal.getProject(), selectedWithdrawal.getFlatType(), true);
                    System.out.println("The Applicant already book a flat. The flat remaining is updated successfully");
                    selectedWithdrawal.getApplicant().setFlatBooking(null);
                    FlatBookingController.flatBookings.remove(selectedWithdrawal.getApplicant().getFlatBooking());
                    BookingRepository.writeAllFlatBookings(FlatBookingController.flatBookings);
                }

            } else if (decision.equals("R")) {
                ApplicationController.updateApplicationStatus(selectedWithdrawal.getApplicationId(), ApplicationStatus.SUCCESSFUL);
                System.out.println("Withdrawal rejected.");
                selectedWithdrawal.getApplicant().setApplication(null);
            } else {
                System.out.println("Invalid decision input. Please enter A or R.");
            }
    
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        ApplicationRepository.writeAllApplication(ApplicationController.getAllApplications());
    }
    /**
     * Prompts the manager to change their account password with validation.
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
            ManagerController.changePassword(currentManager, password);
            System.out.println("Password updated successfully");


        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    /**
     * Creates a new BTO project, including flat types, units, prices, and application dates.
     */
    private void createProject() {
        if (!IsValid.isValidToCreateProject(currentManager, new Date())) {
            System.out.println("You are currently handling a project");
            return;
        }
        String name;
        while (true) {
            System.out.print("Enter project name: ");
            name = sc.nextLine();
            if (IsValid.isValidName(name)) break;
            System.out.println("Invalid input. Please enter a valid project name.");
        }
    
        String neighborhood;
        while (true) {
            System.out.print("Enter neighborhood: ");
            neighborhood = sc.nextLine();
            if (IsValid.isValidName(neighborhood)) break;
            System.out.println("Invalid input. Please enter a valid neighborhood.");
        }
    
        List<FlatType> flatTypes = new ArrayList<>();
        List<Integer> units = new ArrayList<>();
        List<Integer> sellingPrice = new ArrayList<>();
    
        while (true) {
            System.out.println("Choose flat types in the project:");
            System.out.println("1. Only TWO_ROOM");
            System.out.println("2. Only THREE_ROOM");
            System.out.println("3. Both TWO_ROOM and THREE_ROOM");
            String choice = sc.nextLine();
    
            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                try {
                    if (choice.equals("1")) {
                        flatTypes = List.of(FlatType.TWO_ROOM);
                        System.out.print("Enter number of available units: ");
                        units = List.of(Integer.parseInt(sc.nextLine()));
                        System.out.print("Enter the selling price: ");
                        sellingPrice = List.of(Integer.parseInt(sc.nextLine()));
                    } else if (choice.equals("2")) {
                        flatTypes = List.of(FlatType.THREE_ROOM);
                        System.out.print("Enter number of available units: ");
                        units = List.of(Integer.parseInt(sc.nextLine()));
                        System.out.print("Enter the selling price: ");
                        sellingPrice = List.of(Integer.parseInt(sc.nextLine()));
                    } else {
                        flatTypes = List.of(FlatType.TWO_ROOM, FlatType.THREE_ROOM);
                        System.out.print("Enter number of available units for TWO_ROOM: ");
                        int unitTwo = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter the selling price for TWO_ROOM: ");
                        int priceTwo = Integer.parseInt(sc.nextLine());
    
                        System.out.print("Enter number of available units for THREE_ROOM: ");
                        int unitThree = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter the selling price for THREE_ROOM: ");
                        int priceThree = Integer.parseInt(sc.nextLine());
    
                        units = List.of(unitTwo, unitThree);
                        sellingPrice = List.of(priceTwo, priceThree);
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format. Please re-enter values.");
                }
            } else {
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        Date openingDate = null, closingDate = null;
    
        while (true) {
            System.out.print("Enter the opening date (Format dd/mm/yy): ");
            String openStr = sc.nextLine();
            try {
                openingDate = dateFormat.parse(openStr);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
    
        while (true) {
            System.out.print("Enter the closing date (Format dd/mm/yy): ");
            String closeStr = sc.nextLine();
            try {
                closingDate = dateFormat.parse(closeStr);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
    


        System.out.println("Enter the number of officer slots: ");
        int slot = sc.nextInt();
        while(slot > 10 && slot <= 0){
            System.out.println("The maximum slot is 10 and minimum is 0");
            System.out.println("Please enter again the number of officer slots");
            slot = sc.nextInt();
        }
        List<HDBOfficer> officers = new ArrayList<>(); 
        boolean visible = true;
    
        ProjectController.createProject(name, neighborhood, flatTypes, units, openingDate, closingDate, sellingPrice, currentManager, slot, officers, visible);
        System.out.println("Project created.");
    }
    
    /**
     * Allows the manager to edit details of a previously created project.
     */

    private void editProject() {
        try {
            System.out.println("Enter project name to edit: ");
            String oldName = sc.nextLine().trim();
            Project project = ProjectController.findProjectByName(oldName);
            if (project== null || !project.getManagerInCharge().equals(currentManager)){
                System.out.println("No Project Name found! or You are not the creator of this Project!");
                return;
            }
            System.out.println("Enter new name: ");
            String name = sc.nextLine();
            System.out.println("Enter new neighborhood: ");
            String neighborhood = sc.nextLine();
            System.out.println("Choose flat types in the project: ");
            System.out.println("1. Only TWO_ROOM");
            System.out.println("2. Only THREE_ROOM");
            System.out.println("3. Both TWO_ROOM and THREE_ROOM");
            int choice = sc.nextInt();
            List<FlatType> flatTypes;
            List<Integer> units;
            List<Integer> sellingPrice;
            switch (choice) {
                case 1:
                    if(project.getFlatTypes().contains(FlatType.THREE_ROOM) && ApplicationController.countNumberOfSuccessfulApplication(project, FlatType.THREE_ROOM)> 0){
                        System.out.println("You cannot eliminate THREE_ROOM type. There are successful applications applying to THREE_ROOM.");
                        return;
                    }
                    flatTypes = List.of(FlatType.TWO_ROOM);
                    System.out.println("Enter number of available units: ");
                    int unitNumber1 = sc.nextInt();
                    if(project.getFlatTypes().contains(FlatType.TWO_ROOM) && unitNumber1 < ApplicationController.countNumberOfSuccessfulApplication(project, FlatType.TWO_ROOM)){
                        System.out.println("The number of units is smaller than the number of successful applications");
                        return;
                    }
                    units = List.of(unitNumber1);
                    System.out.println("Enter the selling price: ");
                    int price1 = sc.nextInt();
                    sc.nextLine();
                    sellingPrice = List.of(price1);
                    break;
                case 2:
                    if(project.getFlatTypes().contains(FlatType.TWO_ROOM) && ApplicationController.countNumberOfSuccessfulApplication(project, FlatType.TWO_ROOM)> 0){
                        System.out.println("You cannot eliminate TWO_ROOM type. There are successful applications applying to TWO_ROOM.");
                        return;
                    }
                    flatTypes = List.of(FlatType.THREE_ROOM);
                    System.out.println("Enter number of available units: ");
                    int unitNumber2 = sc.nextInt();
                    if(project.getFlatTypes().contains(FlatType.THREE_ROOM) && unitNumber2 < ApplicationController.countNumberOfSuccessfulApplication(project, FlatType.THREE_ROOM)){
                        System.out.println("The number of units is smaller than the number of successful applications");
                        return;
                    }
                    units = List.of(unitNumber2);
                    System.out.println("Enter the selling price: ");
                    int price2 = sc.nextInt();
                    sc.nextLine();
                    sellingPrice = List.of(price2);
                    break;
                case 3:
                    flatTypes = List.of(FlatType.TWO_ROOM, FlatType.THREE_ROOM);
                    System.out.println("Enter number of available units for TWO_ROOM: ");
                    int unitNumber0 = sc.nextInt();
                    if(project.getFlatTypes().contains(FlatType.TWO_ROOM)){
                        if (unitNumber0 < ApplicationController.countNumberOfSuccessfulApplication(project, FlatType.TWO_ROOM)){
                            System.out.println("The unit available is smaller than the number of successful applications");
                            return;
                        }
                    }
                    System.out.println("Enter the selling price for Type TWO_ROOM: ");
                    int price11 = sc.nextInt();
                    System.out.println("Enter number of available units for THREE_ROOM: ");
                    int unitNumber3 = sc.nextInt();
                    if(project.getFlatTypes().contains(FlatType.THREE_ROOM)){
                        if (unitNumber0 < ApplicationController.countNumberOfSuccessfulApplication(project, FlatType.THREE_ROOM)){
                            System.out.println("The unit available is smaller than the number of successful applications");
                            return;
                        }
                    }
                    System.out.println("Enter the selling price for Type THREE_ROOM: ");
                    int price12 = sc.nextInt();
                    sc.nextLine();
                    units = List.of(unitNumber0, unitNumber3);
                    sellingPrice = List.of(price11,price12);
                    break;
                default:
                    throw new AssertionError();
            }

            Date openingDate = project.getApplicationOpeningDate();
            Date closingDate = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            dateFormat.setLenient(false);

            System.out.println("Enter the closing date (Format dd/mm/yy): ");
            String closeDateString = sc.nextLine();

            try {
                // Parse the string input to Date
                closingDate = dateFormat.parse(closeDateString);
            } catch (Exception e) {
                System.out.println("Error: Invalid date format.");
            }

            System.out.println("Enter the number of officer slots: ");
            int slot = sc.nextInt();
            while(slot < project.getAssignedOfficers().size() || slot > 10){
                if(slot > 10){
                    System.out.println("The maximum slot is 10");
                    System.out.println("Please enter again the number of officer slots");
                    slot = sc.nextInt();
                }else{
                    System.out.println("The available slot is smaller than the current number of assigned officers");
                    slot = sc.nextInt();
                }
            }
            ProjectController.editProject(project, name, neighborhood, flatTypes, units, sellingPrice, slot, openingDate, closingDate);
            System.out.println("Project edited.");
        }
        catch (Exception e) {
            System.err.println("Failed to edit project: " + e.getMessage());
        }
    }

    /**
     * Allows the manager to toggle a project's visibility on or off.
     */

    private void toggleVisibility() {
        try {
            System.out.print("Enter project name to edit visibility: ");
            Project project = ProjectController.viewProject(sc.nextLine().trim());
            if(project == null){
                System.out.println("No Project with this Name found!");
                return;
            }
            System.out.println("Choose option on/off: ");
            System.out.println("1. On  ");
            System.out.println("2. Off ");
            int choice = sc.nextInt();
            switch(choice){
                case 1: 
                    ProjectController.toggleProjectVisibility(project, true);
                    System.out.println(" Visibility on.");
                    break;
                case 2:
                    ProjectController.toggleProjectVisibility(project, false);
                    System.out.println(" Visibility off.");
                    break;

                default: System.out.println("Inavlid input");
            }
        } catch (Exception e) {
            System.err.println("Error updating visibility: " + e.getMessage());
            System.err.println("Error updating visibility: " + e.getMessage());
        }
    }
    /**
     * Generates a report on flat bookings, filtered by marital status, flat type, and project name.
     */
    private void generateReport() {
        System.out.println("Choose to filter by Marital Status: ");
        System.out.println("1. MARRIED");
        System.out.println("2. SINGLE");
        System.out.println("3. No filter");
        int choice = sc.nextInt();
        MaritalStatus maritalStatusFilter;
        switch (choice) {
            case 1:
                maritalStatusFilter = MaritalStatus.MARRIED;
                break;
            case 2:
                maritalStatusFilter = MaritalStatus.SINGLE;
                break;
            case 3:
                maritalStatusFilter = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid choice for Marital Status");
        }

        System.out.println("Choose to filter by Flat Type: ");
        System.out.println("1. TWO_ROOM");
        System.out.println("2. THREE_ROOM");
        System.out.println("3. No filter");
        int choice2 = sc.nextInt();
        FlatType flatType;
        switch (choice2) {
            case 1:
                flatType = FlatType.TWO_ROOM;
                break;
            case 2:
                flatType = FlatType.THREE_ROOM;
                break;
            case 3:
                flatType = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid choice for Flat Type");
        }

        System.out.println("Choose to filter by Project Name: ");
        System.out.println("1. YES");
        System.out.println("2. No filter");
        int choice3 = sc.nextInt();
        sc.nextLine(); // clear newline
        String projectName;
        switch (choice3) {
            case 1:
                System.out.print("Enter the Project Name to filter: ");
                projectName = sc.nextLine();
                break;
            case 2:
                projectName = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid choice for Project Name");
        }

        FlatBookingController.printReport(FlatBookingController.generateFilteredReport(FlatBookingController.getApprovedFlatBooking(), maritalStatusFilter, flatType, projectName));
    }
    /**
     * Deletes a project and all associated data (applications, bookings, registrations).
     */
    private void deleteProject(){
        System.out.println("Enter the Project Name to delete: ");
        String projectName = sc.nextLine();
        Project project = ProjectController.findProjectByName(projectName);
        if(project == null || !project.getManagerInCharge().equals(currentManager)){
            System.out.println("Project not found or You are not the creator of the Project!");
            return;
        }
        currentManager.getCreatedProjects().remove(project);
        System.out.println("Project " + project.getProjectName() + " deleted");

        ApplicationController.deleteApplication(project);
        RegistrationController.deleteRegistration(project);
        ApplicantController.deleteApplicationforApplicant(project);
        OfficerController.deleteProjectforOfficer(project);
        ProjectController.deleteProject(project);
    }
    /**
     * Displays all enquiries in the system.
     */
    public void viewEnquiries(){
        EnquiryController.displayAllEnquiries();
    }
    /**
     * Allows the manager to respond to enquiries related to their project.
     */
    @Override
    public void replyEnquiry(){
        System.out.println("Here is the list of Enquiries of the Project You are handling: ");
        Project project = null;
        try {
            project = currentManager.getCreatedProjects().get(currentManager.getCreatedProjects().size() -1);
        } catch (Exception e) { System.out.println("There are no enquiries."); return;
        }
        EnquiryController.displayEnquiriesByProject(project);
        System.out.println("Enter the Enquiry ID that you want to reply: ");
        String enquiryID = sc.nextLine();
        Enquiry enquiry = EnquiryController.getEnquiryByID(enquiryID);
        if(enquiry == null || !enquiry.getProject().getManagerInCharge().equals(currentManager)){
            System.out.println("No enquiry found or you are not allowed to reply this enquiry!");
            return;
        }
        if(enquiry.getStatus() == EnquiryStatus.RESPONDED){
            System.out.println("This enquiry has already been replied to");
            return;
        }
        String enquiryReply = "";
        while(true){
        System.out.println("Enter the Enquiry Reply: ");
        enquiryReply = sc.nextLine();
        if ("".equals(enquiryReply)){
            System.out.println("Please enter a reply");
        }
        else{
            break;
        }
    }
        enquiry.setEnquiryReply(enquiryReply);
        enquiry.setStatus(EnquiryStatus.RESPONDED);
        EnquiryRepository.writeAllEnquiries(EnquiryController.enquiries);
    }
    /**
     * Displays projects based on manager-created filters such as neighborhood and flat type.
     * Manager can also choose to view only their own created projects.
     */

    private void viewProjects(){
        try {
            System.out.println("Do you want to view your own created project?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int choice0 = sc.nextInt();
            sc.nextLine();
            boolean isViewOwnCreatedProject = false;
            List<Project> projects = null;
            switch(choice0){
                case 1: 
                    isViewOwnCreatedProject = true;
                    break;
                case 2:
                    break;
            }
            if(isViewOwnCreatedProject){
                System.out.println("Do you want to filter out project by Location?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int choice = sc.nextInt();
                sc.nextLine();
                String location = null;
                switch(choice){
                    case 1:
                        System.out.println("Please enter the Neighborhood you want to search: ");
                        location = sc.nextLine();
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                System.out.println("Do you want to filter out project by Flat Type?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                int choice2 = sc.nextInt();
                sc.nextLine();
                FlatType flatType = null;
                switch(choice2){
                    case 1:
                        System.out.println("Please choose one type of Flat you want to search: ");
                        System.out.println("1. TWO_ROOM");
                        System.out.println("2. THREE_ROOM");
                        int flat = sc.nextInt();
                        if(flat == 1){
                            flatType = FlatType.TWO_ROOM;
                        }else{
                            flatType = FlatType.THREE_ROOM;
                        }
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                projects = ProjectController.viewAllProjectsWithFilters(null, currentManager, flatType, location);
            }else{
                projects = ProjectController.projects;
            }
            if (projects.isEmpty()) {
                System.out.println("No projects available.");
            } else {
                System.out.println("Here is the list of projects: ");

                // Print project details in table format
                for (Project p : projects) {
                    System.out.println(p);
                }
                
            }
        }
        catch(Exception e) {
            System.err.println(" Error viewing projects: " + e.getMessage());
        }
    }
}
