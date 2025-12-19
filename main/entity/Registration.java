package main.entity;

import java.time.LocalDate;
import main.entity.Enum.RegistrationStatus;
import main.entity.User.HDBOfficer;

/**
 * Represents a registration made by an HDB Officer to handle a BTO project.
 * Each registration has a unique ID, an officer, the target project,
 * its status, and the submission date.
 */
public class Registration {
    private String registrationId;
    private HDBOfficer officer;
    private Project project;
    private RegistrationStatus registrationStatus; // Pending, Approved, Rejected
    private LocalDate submissionDate;

    /**
     * Constructs a new Registration.
     *
     * @param registrationId      The unique ID of the registration.
     * @param officer             The HDB officer registering.
     * @param project             The project the officer is registering to handle.
     * @param registrationStatus  The status of the registration.
     * @param submissionDate      The date the registration was submitted.
     */
    public Registration(String registrationId, HDBOfficer officer, Project project, RegistrationStatus registrationStatus, LocalDate submissionDate) {
        this.registrationId = registrationId;
        this.officer = officer;
        this.project = project;
        this.registrationStatus = registrationStatus;
        this.submissionDate = submissionDate;
    }

    /**
     * @return the registration ID.
     */
    public String getRegistrationId() {
        return registrationId;
    }

    /**
     * Sets the registration ID.
     * @param registrationId the new registration ID.
     */
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    /**
     * @return the HDB officer who registered.
     */
    public HDBOfficer getOfficer() {
        return officer;
    }

    /**
     * Sets the officer for this registration.
     * @param officer the HDB officer.
     */
    public void setOfficer(HDBOfficer officer) {
        this.officer = officer;
    }

    /**
     * @return the project for which the registration was made.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project for this registration.
     * @param project the new project.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the current status of the registration.
     */
    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * Updates the registration status.
     * @param registrationStatus the new registration status.
     */
    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    /**
     * @return the submission date of the registration.
     */
    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Sets the submission date for the registration.
     * @param submissionDate the new submission date.
     */
    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Returns a string representation of the registration details.
     *
     * @return formatted string of registration info.
     */
    @Override
    public String toString() {
        return
            """
            Registration Details:
              Registration ID: """    + registrationId            + "\n" +
            "  HDB Officer ID: "     + officer.getUserId()       + "\n" +
            "  Project Name: "       + project.getProjectName()  + "\n" +
            "  Status: "             + registrationStatus        + "\n" +
            "  Submission Date: "    + submissionDate;
    }
}
