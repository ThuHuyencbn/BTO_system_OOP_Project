package main.entity;

import java.time.LocalDate;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.FlatType;
import main.entity.User.Applicant;

/**
 * Represents an application submitted by an applicant for a specific project.
 * It includes the status of the application, submission date, and the type of flat applied for.
 */
public class Application {
    private String applicationId;
    private Applicant applicant;
    private Project project;
    private ApplicationStatus status; // Pending, Successful, Unsuccessful, Booked
    private LocalDate submissionDate;
    private FlatType flatType; // 2-Room or 3-Room

    /**
     * Constructs an Application with specified attributes.
     *
     * @param applicationId   Unique identifier for the application.
     * @param applicant       The applicant who submitted the application.
     * @param project         The project the applicant is applying to.
     * @param status          The current status of the application.
     * @param submissionDate  The date the application was submitted.
     * @param flatType        The type of flat being applied for.
     */
    public Application(String applicationId, Applicant applicant, Project project, ApplicationStatus status, LocalDate submissionDate, FlatType flatType) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.project = project;
        this.status = status;
        this.submissionDate = submissionDate;
        this.flatType = flatType;
    }

    /**
     * Gets the application ID.
     *
     * @return the application ID.
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the application ID.
     *
     * @param applicationId the new application ID.
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Gets the applicant.
     *
     * @return the applicant.
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Sets the applicant.
     *
     * @param applicant the applicant to set.
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Gets the project.
     *
     * @return the project.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project.
     *
     * @param project the project to set.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the current status of the application.
     *
     * @return the application status.
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the application.
     *
     * @param status the status to set.
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Gets the submission date of the application.
     *
     * @return the submission date.
     */
    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Sets the submission date of the application.
     *
     * @param submissionDate the date to set.
     */
    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Gets the type of flat applied for.
     *
     * @return the flat type.
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * Sets the type of flat for the application.
     *
     * @param flatType the flat type to set.
     */
    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    /**
     * Returns a string representation of the application.
     *
     * @return a formatted string with all application details.
     */
    @Override
    public String toString() {
        return
                "ApplicationID: " + applicationId +
                "\nApplicantUserID: " + applicant.getUserId() +
                "\nProject Name: " + project.getProjectName() +
                "\nStatus: " + status.name() +
                "\nSubmission Date: " + submissionDate +
                "\nFlat Type: '" + flatType;
    }
}