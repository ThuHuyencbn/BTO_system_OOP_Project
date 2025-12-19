package main.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import main.controller.UserController.ApplicantController;
import main.controller.UserController.OfficerController;
import main.entity.Enquiry;
import main.entity.Project;
import main.entity.Enum.EnquiryStatus;
import main.entity.User.Applicant;
import main.repository.EnquiryRepository;

/**
 * Controller class to manage Enquiry operations including creation, editing,
 * deletion, and status updates.
 */
public class EnquiryController {
    /** A static list that holds all enquiries. */
    public static List<Enquiry> enquiries = new ArrayList<>();

    /** A counter to generate unique enquiry IDs. */
    public static int enquiryCounter = 1;

    /** Default constructor. */
    public EnquiryController() {}

    /**
     * Creates a new enquiry from an applicant regarding a project.
     *
     * @param applicant The applicant submitting the enquiry.
     * @param project The project the enquiry is about.
     * @param enquiryText The text of the enquiry.
     * @return The created {@code Enquiry} object.
     */
    public static Enquiry createEnquiry(Applicant applicant, Project project, String enquiryText) {
        String enquiryID = "ENQID" + enquiryCounter++;
        Enquiry newEnquiry = new Enquiry(enquiryID, applicant, project, EnquiryStatus.PENDING, enquiryText, LocalDateTime.now());
        enquiries.add(newEnquiry);
        applicant.getEnquiries().add(newEnquiry);
        EnquiryRepository.appendEnquiryToCSV(newEnquiry);
        return newEnquiry;
    }

    /**
     * Retrieves an enquiry by its ID.
     *
     * @param enquiryID The ID of the enquiry.
     * @return The corresponding {@code Enquiry} object, or {@code null} if not found.
     */
    public static Enquiry getEnquiryByID(String enquiryID) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryID().equals(enquiryID)) {
                return enquiry;
            }
        }
        return null; 
    }

    /**
     * Retrieves all enquiries submitted by a specific applicant.
     *
     * @param applicantNRIC The NRIC of the applicant.
     * @return A list of {@code Enquiry} objects submitted by the applicant.
     */
    public static List<Enquiry> getEnquiriesByApplicant(String applicantNRIC) {
        Applicant applicant = ApplicantController.getUserById(applicantNRIC);
        if (applicant == null) {
            applicant = OfficerController.getUserById(applicantNRIC);
            if (applicant == null) {
                throw new IllegalArgumentException("Error: No Applicant with User ID " + applicantNRIC + " found");
            }
        }
        return applicant.getEnquiries();
    }

    /**
     * Edits an existing enquiry if it is still in pending status.
     *
     * @param enquiryID The ID of the enquiry to edit.
     * @param applicantNRIC The NRIC of the applicant who submitted the enquiry.
     * @param newEnquiryText The new enquiry text.
     */
    public static void editEnquiry(String enquiryID, String applicantNRIC, String newEnquiryText) {
        Enquiry enquiry = getEnquiryByID(enquiryID);
        if (enquiry != null && enquiry.getApplicant().getUserId().equals(applicantNRIC)) {
            if (enquiry.getStatus() == EnquiryStatus.PENDING) {
                enquiry.setEnquiryText(newEnquiryText);
                EnquiryRepository.writeAllEnquiries(enquiries);
            } else {
                System.out.println("Enquiry cannot be edited once responded or closed.");
            }
        }
    }

    /**
     * Deletes an enquiry from the global list and the applicant's list.
     *
     * @param enquiry The enquiry to delete.
     */
    public static void deleteEnquiry(Enquiry enquiry) {
        enquiries.remove(enquiry);
        enquiry.getApplicant().getEnquiries().remove(enquiry);
        EnquiryRepository.writeAllEnquiries(enquiries);
    }

    /**
     * Updates the status of an enquiry.
     *
     * @param enquiryID The ID of the enquiry.
     * @param newStatus The new status to be set.
     */
    public static void updateEnquiryStatus(String enquiryID, EnquiryStatus newStatus) {
        Enquiry enquiry = getEnquiryByID(enquiryID);
        if (enquiry != null) {
            for (Enquiry enquiry1 : enquiries) {
                if (enquiry == enquiry1) {
                    enquiry.setStatus(newStatus);
                }
            }
            EnquiryRepository.writeAllEnquiries(enquiries);
        }
    }

    /**
     * Displays all enquiries in the system.
     */
    public static void displayAllEnquiries() {
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            for (Enquiry enquiry : enquiries) {
                System.out.println(enquiry.generateEnquiryDetails());
            }
        }
    }

    /**
     * Displays enquiries created by a specific applicant.
     *
     * @param enquiries The list of enquiries from the applicant.
     */
    public static void displayEnquiriesByApplicant(List<Enquiry> enquiries) {
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            for (Enquiry enquiry : enquiries) {
                System.out.println(enquiry.generateEnquiryDetails());
            }
        }
    }

    /**
     * Retrieves enquiries related to a specific project.
     *
     * @param project The project to filter enquiries by.
     * @return A list of enquiries for the project.
     */
    public static List<Enquiry> getEnquiriesByProject(Project project) {
        List<Enquiry> filteredEnquiries = new ArrayList<>();
        for (Enquiry enq : enquiries) {
            if (enq.getProject().equals(project)) {
                filteredEnquiries.add(enq);
            }
        }
        return filteredEnquiries;
    }

    /**
     * Displays enquiries related to a specific project.
     *
     * @param project The project to display enquiries for.
     */
    public static void displayEnquiriesByProject(Project project) {
        List<Enquiry> filteredEnquiry = getEnquiriesByProject(project);
        for (Enquiry enq : filteredEnquiry) {
            System.out.println(enq.generateEnquiryDetails());
        }
    }
}
