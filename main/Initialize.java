package main;

import java.util.Date;
import main.controller.ProjectController;
import main.controller.UserController.ApplicantController;
import main.controller.UserController.ManagerController;
import main.controller.UserController.OfficerController;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.User.Applicant;
import main.entity.User.HDBOfficer;
import main.repository.ApplicantRepository;
import main.repository.ApplicationRepository;
import main.repository.BookingRepository;
import main.repository.EnquiryRepository;
import main.repository.ProjectRepository;
import main.repository.RegistrationRepository;
import main.utility.CSVRead;
/**
 * The {@code Initialize} class is responsible for bootstrapping the system.
 * It loads all necessary data into memory from the respective CSV files,
 * including applicants, officers, managers, projects, enquiries, applications,
 * flat bookings, and registrations.
 */
public class Initialize {
    private static final String APPLICANT_UPDATED_FILE_PATH = "data/ApplicantUpdatedList.csv";
    private static final String OFFICER_FILE_PATH = "data/OfficerUpdatedList.csv";
    private static final String MANAGER_FILE_PATH = "data/ManagerUpdatedList.csv";
    private static final String PROJECT_UPDATE_FILE_PATH = "data/ProjectUpdatedList.csv";

    private boolean isInitialized = false;

    public void initialize() {
        if (isInitialized) return;

        System.out.println("Initializing and loading system data...");

        //Load Applicant
        if (ApplicantRepository.updatedApplicantsFileHasData()) {
            ApplicantRepository.loadAllApplicants(APPLICANT_UPDATED_FILE_PATH);
        } else {
            ApplicantRepository.loadAllApplicants(APPLICANT_UPDATED_FILE_PATH);
            ApplicantRepository.saveApplicantsToCSV(ApplicantController.getApplicantList()); 
        }

        // Load officer data from CSV and initialize the controller
        var officerRaw = CSVRead.CSVRead(OFFICER_FILE_PATH, true);

        // Initialize officers with loaded data
        for (var row : officerRaw) {
            // Creating Applicants from CSV data and adding to the controller
            String userId = row.get("NRIC");
            String password = row.get("Password");
            String name = row.get("Name");
            Role role = Role.OFFICER;
            MaritalStatus maritalStatus = MaritalStatus.valueOf(row.get("Marital Status").toUpperCase());
            int age = Integer.parseInt(row.get("Age"));

            OfficerController.addOfficer(userId, password, name, role, maritalStatus, age);
        }

        // Log how many officers were loaded
        System.out.println("Total officers loaded: " + OfficerController.getOfficerList().size());

        // Load managers data from CSV and initialize the controller
        var managerRaw = CSVRead.CSVRead(MANAGER_FILE_PATH, true);

        // Initialize ManagerController with loaded data
        for (var row : managerRaw) {
            // Creating Managers from CSV data and adding to the controller
            String userId = row.get("NRIC");
            String password = row.get("Password");
            String name = row.get("Name");
            Role role = Role.MANAGER;
            MaritalStatus maritalStatus = MaritalStatus.valueOf(row.get("Marital Status").toUpperCase());
            int age = Integer.parseInt(row.get("Age"));

            ManagerController.addManager(userId, password, name, role, maritalStatus, age);
        }

        // Log how many managers were loaded
        System.out.println("Total managers loaded: " + ManagerController.getManagerList().size());

        //Load Project
        if (ProjectRepository.updatedProjectFileHasData()) {
            ProjectRepository.loadProjectsFromCSV(PROJECT_UPDATE_FILE_PATH);
        } else {
            ProjectRepository.loadProjectsFromCSV(PROJECT_UPDATE_FILE_PATH);
            ProjectRepository.saveProjectsToCSV(); 
        }
        System.out.println("Total projects loaded: " + ProjectController.projects.size());


        for(Applicant applicant: ApplicantController.getApplicantList()){
            EnquiryRepository.loadEnquiriesForApplicant(applicant, ProjectController.projects);
        }
        for(HDBOfficer applicant: OfficerController.getOfficerList()){
            EnquiryRepository.loadEnquiriesForApplicant((Applicant) applicant, ProjectController.projects);
        }
        for (Applicant applicant : ApplicantController.getApplicantList()) {
            BookingRepository.loadFlatBookingsForApplicant(applicant, ProjectController.projects);
        }
        for (HDBOfficer officer:  OfficerController.getOfficerList()){
            BookingRepository.loadFlatBookingsForApplicant(officer, ProjectController.projects);
        }
        for(Applicant applicant: ApplicantController.getApplicantList()){
            ApplicationRepository.loadApplicationForApplicant(applicant, ProjectController.projects);
        }
        for(HDBOfficer applicant: OfficerController.getOfficerList()){
            ApplicationRepository.loadApplicationForApplicant((Applicant) applicant,  ProjectController.projects);
        }

        for(HDBOfficer officer: OfficerController.getOfficerList()){
            RegistrationRepository.loadRegistrationForOfficer(officer, ProjectController.projects);
        }

        for (HDBOfficer officer: OfficerController.getOfficerList()){
            if (officer.getAssignedProject() != null){
                if(officer.getAssignedProject().getApplicationClosingDate().before(new Date())){
                    officer.setAssignedProject(null);
                    officer.setRegistration(null);
                }
            }
        }

        isInitialized = true;
    }
    
}
