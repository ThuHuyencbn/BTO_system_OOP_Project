package main.controller.UserController;

import java.util.*;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.Project;
import main.entity.Registration;
import main.entity.User.*;
import main.repository.OfficerRepository;

/**
 * The {@code OfficerController} class manages actions related to {@code HDBOfficer} users.
 * It handles officer registration, deletion, password updates, and project/registration resets.
 */
public class OfficerController extends ApplicantController {

    /** A list of all registered HDB officers. */
    private static List<HDBOfficer> officers = new ArrayList<>();

    /**
     * Retrieves an officer by their user ID (NRIC).
     *
     * @param userId The user ID (NRIC) of the officer.
     * @return The {@code HDBOfficer} object if found; otherwise {@code null}.
     */
    public static HDBOfficer getUserById(String userId) {
        for (HDBOfficer officer : officers) {
            if (officer.getUserId().equals(userId)) {
                return officer;
            }
        }
        return null;
    }

    /**
     * Retrieves an officer by their full name (case-insensitive).
     *
     * @param name The name of the officer.
     * @return The {@code HDBOfficer} object if found; otherwise {@code null}.
     */
    public static HDBOfficer getUserByName(String name) {
        for (HDBOfficer officer : officers) {
            if (officer.getName().trim().equalsIgnoreCase(name.trim())) {
                return officer;
            }
        }
        return null;
    }

    /**
     * Returns the list of all officers in the system.
     *
     * @return A list of {@code HDBOfficer} objects.
     */
    public static List<HDBOfficer> getOfficerList() {
        return officers;
    }

    /**
     * Adds a new officer to the system and persists the data to CSV.
     *
     * @param userId         The officer's NRIC.
     * @param password       The officer's password.
     * @param name           The full name of the officer.
     * @param role           The role of the officer.
     * @param maritalStatus  The marital status of the officer.
     * @param age            The age of the officer.
     */
    public static void addOfficer(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age) {
        Project assignedProject = null;
        Registration registration = null;
        User officer = new HDBOfficer(userId, password, name, role, maritalStatus, age, assignedProject, registration);
        officers.add((HDBOfficer) officer);
        UserController.getListUser().add(officer);
        OfficerRepository.saveOfficersToCSV();
    }

    /**
     * Deletes an officer by their user ID and updates the CSV.
     *
     * @param userId The officer's NRIC.
     */
    public void deleteOfficer(String userId) {
        HDBOfficer officer = getUserById(userId);
        if (officer != null) {
            officers.remove(officer);
            UserController.getListUser().remove(officer);
            OfficerRepository.saveOfficersToCSV();
        } else {
            throw new IllegalArgumentException("No Officer with this ID found");
        }
    }

    /**
     * Changes the password of the specified officer and persists the change.
     *
     * @param currentOfficer The officer whose password is being updated.
     * @param password       The new password.
     */
    public static void changePassword(HDBOfficer currentOfficer, String password) {
        try {
            currentOfficer.setPassword(password);
            OfficerRepository.saveOfficersToCSV();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Removes project and registration references from any officer assigned to the given project.
     *
     * @param project The project to dissociate from officers.
     */
    public static void deleteProjectforOfficer(Project project) {
        for (HDBOfficer off : officers) {
            if (off.getAssignedProject() != null && off.getAssignedProject().equals(project)) {
                off.setAssignedProject(null);
            }
            if (off.getRegistration() != null && off.getRegistration().getProject().equals(project)) {
                off.setRegistration(null);
            }
        }
    }
}
