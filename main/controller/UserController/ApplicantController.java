package main.controller.UserController;

import java.util.*;
import main.entity.Application;
import main.entity.Enquiry;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.FlatBooking;
import main.entity.Project;
import main.entity.User.*;
import main.repository.ApplicantRepository;

/**
 * The {@code ApplicantController} class manages the logic related to applicants,
 * such as registration, deletion, password updates, and cleanup of applications/bookings.
 */
public class ApplicantController {
    
    /** Internal list to store all applicants. */
    private static List<Applicant> applicants = new ArrayList<>();

    /**
     * Retrieves an applicant by their user ID.
     *
     * @param userId The NRIC or user ID of the applicant.
     * @return The {@code Applicant} object if found; otherwise, {@code null}.
     */
    public static Applicant getUserById(String userId) {
        for (Applicant applicant : applicants) {
            if (applicant.getUserId().equals(userId)) {
                return applicant;
            }
        }
        return null;
    }

    /**
     * Returns the list of all applicants.
     *
     * @return A list of {@code Applicant} objects.
     */
    public static List<Applicant> getApplicantList() {
        return applicants;
    }

    /**
     * Adds a new applicant to the system.
     *
     * @param userId         The NRIC or user ID.
     * @param password       The applicant's password.
     * @param name           The name of the applicant.
     * @param role           The role of the user (can be null or APPLICANT).
     * @param maritalStatus  The marital status of the applicant.
     * @param age            The age of the applicant.
     */
    public static void addApplicant(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age) {
        Application application = null;
        List<Enquiry> enquiries = null;
        FlatBooking flatBooking = null;
        User applicant = new Applicant(userId, password, name, role, maritalStatus, age, application, enquiries, flatBooking);
        applicants.add((Applicant) applicant);
        UserController.getListUser().add(applicant);
        ApplicantRepository.saveApplicantsToCSV(applicants);
    }

    /**
     * Deletes an applicant with the given user ID.
     * Only deletes if the user exists and is of type {@code Applicant}.
     *
     * @param userId The NRIC or user ID of the applicant to delete.
     */
    public void deleteApplicant(String userId) {
        User user = getUserById(userId);
        if (user != null && user instanceof Applicant) {
            Applicant applicant = (Applicant) user;
            applicants.remove(applicant);
            UserController.getListUser().remove(applicant);
            ApplicantRepository.saveApplicantsToCSV(applicants);
        } else {
            System.out.println("User not found or is not an applicant.");
        }
    }

    /**
     * Changes the password for a given applicant.
     *
     * @param currentApplicant The applicant whose password is being changed.
     * @param password         The new password.
     */
    public static void changePassword(Applicant currentApplicant, String password) {
        try {
            currentApplicant.setPassword(password);
            ApplicantRepository.saveApplicantsToCSV(applicants);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Deletes the application and flat booking (if any) of all applicants
     * who applied for the specified project.
     *
     * @param project The project whose applicants' applications should be cleared.
     */
    public static void deleteApplicationforApplicant(Project project) {
        for (Applicant app : applicants) {
            if (app.getApplication() != null && app.getApplication().getProject().equals(project)) {
                app.setApplication(null);
                if (app.getFlatBooking() != null) {
                    app.setFlatBooking(null);
                }
            }
        }
    }

}
