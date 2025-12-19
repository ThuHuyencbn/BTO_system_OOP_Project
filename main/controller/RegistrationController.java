package main.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.RegistrationStatus;
import main.entity.Project;
import main.entity.Registration;
import main.entity.User.HDBOfficer;
import main.repository.RegistrationRepository;

/**
 * Controller class to manage registration operations for HDB officers.
 */
public class RegistrationController {
    
    /** List to store all registration records. */
    private static List<Registration> registrations = new ArrayList<>();

    /** Counter to generate unique registration IDs. */
    public static int registrationCounter = 1;

    public RegistrationController() {}

    /**
     * Creates a new registration for the given officer and project.
     *
     * @param officer The officer submitting the registration.
     * @param project The project for which the registration is submitted.
     * @throws IllegalArgumentException If registration is not allowed.
     */
    public static void createRegistration(HDBOfficer officer, Project project) throws IllegalArgumentException {
        if (!isValidForRegister(officer, project)) {
            throw new IllegalArgumentException("Error: You are not allowed to register!");
        }
        String registrationId = generateRegistrationId(project);
        Registration registration = new Registration(registrationId, officer, project, RegistrationStatus.PENDING, LocalDate.now());
        registrations.add(registration);
        officer.setRegistration(registration);
        RegistrationRepository.appendRegistrationToCSV(registration);
    }

    /**
     * Updates the status of a given registration.
     *
     * @param registration The registration to be updated.
     * @param newStatus The new status to be set.
     * @throws IllegalArgumentException If the update is invalid.
     */
    public static void updateRegistrationStatus(Registration registration, RegistrationStatus newStatus) throws IllegalArgumentException {
        registration.setRegistrationStatus(newStatus);
        RegistrationRepository.writeAllRegistration(registrations);
    }

    /**
     * Checks whether a given officer is eligible to register for a project.
     *
     * @param officer The officer attempting to register.
     * @param project The project being registered for.
     * @return true if registration is allowed, false otherwise.
     */
    public static boolean isValidForRegister(HDBOfficer officer, Project project){
        if (officer.getApplication() != null && !officer.getApplication().getStatus().equals(ApplicationStatus.UNSUCCESSFUL)){
            return false;
        }
        if(officer.getRegistration() != null && !officer.getRegistration().getRegistrationStatus().equals(RegistrationStatus.REJECTED)){
            return false;
        }
        if (officer.getAssignedProject() != null && officer.getAssignedProject().getApplicationClosingDate().after(project.getApplicationOpeningDate())){
            return false;
        }
        return true;
    }

    /**
     * Generates a unique registration ID for the given project.
     *
     * @param project The project the registration is for.
     * @return A unique registration ID.
     */
    private static String generateRegistrationId(Project project) {
        return "REG-" + project.getProjectName() + "-" + registrationCounter++;
    }

    /**
     * Retrieves all pending registrations for a given project.
     *
     * @param project The project to filter by.
     * @return List of pending registrations.
     */
    public static List<Registration> getPendingRegistrationForProject(Project project){
        List<Registration> filteRegistrations = new ArrayList<>();
        for(Registration reg: registrations){
            if(reg.getProject().equals(project) && reg.getRegistrationStatus().equals(RegistrationStatus.PENDING)){
                filteRegistrations.add(reg);
            }
        }
        return filteRegistrations;
    }

    /**
     * Retrieves all registrations for a specific project.
     *
     * @param project The project to filter by.
     * @return List of registrations.
     */
    public static List<Registration> getRegistrationForProject(Project project){
        List<Registration> filteRegistrations = new ArrayList<>();
        for(Registration reg: registrations){
            if(reg.getProject().equals(project)){
                filteRegistrations.add(reg);
            }
        }
        return filteRegistrations;
    }

    /**
     * Prints the given list of registrations to the console.
     *
     * @param registrations List of registrations to display.
     */
    public static void printRegistration(List<Registration> registrations) {
        System.out.println("----------- List of Registration -----------");
        for (Registration reg: registrations) {
            System.out.println(reg);
        }
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Retrieves a registration by its ID.
     *
     * @param registrationID The ID of the registration.
     * @return The matching Registration object, or null if not found.
     */
    public static Registration getRegistrationByID(String registrationID){
        for(Registration reg: registrations){
            if(reg.getRegistrationId().equals(registrationID)){
                return reg;
            }
        }
        return null;
    }

    /**
     * Returns all registrations currently stored.
     *
     * @return List of all registrations.
     */
    public static List<Registration> getAllRegistrations(){
        return registrations;
    }

    /**
     * Deletes all registrations associated with a given project.
     *
     * @param project The project whose registrations should be removed.
     */
    public static void deleteRegistration(Project project) {
        Iterator<Registration> iterator = registrations.iterator();
        while (iterator.hasNext()) {
            Registration reg = iterator.next();
            if (reg.getProject().equals(project)) {
                iterator.remove();
            }
        }
        RegistrationRepository.writeAllRegistration(registrations);
    }
}
