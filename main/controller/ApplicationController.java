package main.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import main.entity.Application;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.FlatType;
import main.entity.Enum.MaritalStatus;
import main.entity.Project;
import main.entity.User.Applicant;
import main.repository.ApplicationRepository;

/**
 * Controller class responsible for handling operations related to Applications,
 * including creation, withdrawal, status updates, and filtering by project.
 */
public class ApplicationController {
    /** Stores all applications in memory. */
    private static List<Application> applications = new ArrayList<>();

    /** Counter to generate unique application IDs. */
    public static int applicationCounter = 1;

    /**
     * Creates a new application form for a given applicant and project.
     *
     * @param applicant The applicant submitting the form.
     * @param project The project being applied to.
     * @param flatType The type of flat selected.
     * @param maritalStatus The applicant's marital status.
     * @param age The applicant's age.
     * @return A new {@code Application} object.
     * @throws IllegalArgumentException If the applicant does not meet eligibility criteria.
     */
    public static Application createApplicationForm(Applicant applicant, Project project, FlatType flatType, MaritalStatus maritalStatus, int age) throws IllegalArgumentException {
        if (maritalStatus == MaritalStatus.SINGLE && age >= 35 && flatType != FlatType.TWO_ROOM) {
            throw new IllegalArgumentException("Error: Singles, 35 years old and above, can ONLY apply for 2-Room.");
        }
        if (maritalStatus == MaritalStatus.MARRIED && age >= 21 && flatType != FlatType.TWO_ROOM && flatType != FlatType.THREE_ROOM) {
            throw new IllegalArgumentException("Error: Married, 21 years old and above, can apply for 2-Room or 3-Room only.");
        }

        String applicationId = generateApplicationId(project);
        Application newApplication = new Application(applicationId, applicant, project, ApplicationStatus.PENDING, LocalDate.now(), flatType);
        applications.add(newApplication);
        ApplicationRepository.appendApplicationToCSV(newApplication);
        return newApplication;
    }

    /**
     * Marks an application as pending withdrawal.
     *
     * @param application The application to be withdrawn.
     * @throws IllegalArgumentException If the input application is null.
     */
    public static void withdrawApplication(Application application) throws IllegalArgumentException {
        if (application != null) {
            application.setStatus(ApplicationStatus.PENDING_WITHDRAWN);
            ApplicationRepository.writeAllApplication(applications);
            System.out.println("Application with ID " + application.getApplicationId() + " has been requested to be withdrawn.");
        } else {
            throw new IllegalArgumentException("Error: Invalid Input.");
        }
    }

    /**
     * Updates the status of a specific application.
     *
     * @param applicationId The ID of the application to update.
     * @param newStatus The new status to be set.
     */
    public static void updateApplicationStatus(String applicationId, ApplicationStatus newStatus) {
        try {
            for (Application app : applications) {
                if (app.getApplicationId().equals(applicationId)) {
                    app.setStatus(newStatus);
                    ApplicationRepository.writeAllApplication(applications);
                    System.out.println("Application status updated: " + app);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Error to update status");
        }
    }

    /**
     * Retrieves all pending applications for a given project.
     *
     * @param project The project to filter applications by.
     * @return A list of pending applications for the specified project.
     */
    public static boolean hasExistingApplication(Applicant applicant) {
        return applications.stream()
                .anyMatch(app -> app.getApplicant().getUserId().equals(applicant.getUserId()) &&
                        app.getStatus() != ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Generates a unique application ID based on the project name and a counter.
     *
     * @param project The project to generate the ID for.
     * @return A unique application ID.
     */
    private static String generateApplicationId(Project project) {
        return "APP-" + project.getProjectName() + "-" + applicationCounter++;
    }
    /**
     * Retrieves all pending applications for a given project.
     *
     * @param project The project to filter applications by.
     * @return A list of pending applications for the specified project.
     */
    public static List<Application> getPendingApplicationByProject( Project project){
        List<Application> filterApplications = new ArrayList<>();
        for (Application app : applications) {
            if (app.getProject().equals(project) && app.getStatus().equals(ApplicationStatus.PENDING)) {
                filterApplications.add(app);
            }
        }
        return filterApplications;
    }

    /**
     * Prints a formatted list of applications to the console.
     *
     * @param applications The list of applications to print.
     */
    public static void printApplication(List<Application> applications) {
        System.out.println("----------- List of Application -----------");
        for (Application app : applications) {
            System.out.println(app);
        }
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Retrieves a specific application by its ID.
     *
     * @param applicationID The ID of the application to retrieve.
     * @return The corresponding {@code Application} object, or {@code null} if not found.
     */
    public static Application getApplicationByID(String applicationID) {
        for (Application app : applications) {
            if (app.getApplicationId().equals(applicationID)) {
                return app;
            }
        }
        return null;
    }

    /**
     * Retrieves all applications that are pending withdrawal for a given project.
     *
     * @param project The project to filter by.
     * @return A list of applications with status {@code PENDING_WITHDRAWN}.
     */
    public static List<Application> getWithdrawalRequest(Project project) {
        List<Application> withdrawals = new ArrayList<>();
        for (Application app : applications) {
            if (app.getProject().equals(project) && app.getStatus().equals(ApplicationStatus.PENDING_WITHDRAWN)) {
                withdrawals.add(app);
            }
        }
        return withdrawals;
    }

    /**
     * Returns all applications stored in the controller.
     *
     * @return The list of all applications.
     */
    public static List<Application> getAllApplications() {
        return applications;
    }

    /**
     * Counts the number of successful applications for a given project and flat type.
     *
     * @param project The project to filter by.
     * @param flatType The flat type to count.
     * @return The count of successful applications.
     */
    public static int countNumberOfSuccessfulApplication(Project project, FlatType flatType) {
        int count = 0;
        for (Application app : applications) {
            if (app.getProject().equals(project) &&
                app.getFlatType().equals(flatType) &&
                app.getStatus().equals(ApplicationStatus.SUCCESSFUL)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Deletes all applications that belong to a specific project.
     *
     * @param project The project whose applications should be deleted.
     */
    public static void deleteApplication(Project project) {
        Iterator<Application> iterator = applications.iterator();
        while (iterator.hasNext()) {
            Application app = iterator.next();
            if (app.getProject().equals(project)) {
                iterator.remove();
            }
        }
        ApplicationRepository.writeAllApplication(applications);
    }
}
