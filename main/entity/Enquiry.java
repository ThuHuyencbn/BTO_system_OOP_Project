package main.entity;

import java.time.LocalDateTime;
import main.entity.Enum.EnquiryStatus;
import main.entity.User.Applicant;

/**
 * Represents an enquiry made by an applicant regarding a specific project.
 * Includes details like enquiry text, status, reply, and creation timestamp.
 */
public class Enquiry {

    private String enquiryID;
    private Applicant applicant;
    private Project project;
    private EnquiryStatus status;
    private String enquiryText;
    private String enquiryReply;
    private LocalDateTime dateCreated;

    /**
     * Constructs an Enquiry object without a reply.
     *
     * @param enquiryID   Unique identifier for the enquiry.
     * @param applicant   The applicant who submitted the enquiry.
     * @param project     The project this enquiry is related to.
     * @param status      Current status of the enquiry.
     * @param enquiryText The text or content of the enquiry.
     * @param dateCreated Timestamp of when the enquiry was created.
     */
    public Enquiry(String enquiryID, Applicant applicant, Project project,
                   EnquiryStatus status, String enquiryText, LocalDateTime dateCreated) {
        this.enquiryID = enquiryID;
        this.applicant = applicant;
        this.project = project;
        this.status = status;
        this.enquiryText = enquiryText;
        this.dateCreated = dateCreated;
        this.enquiryReply = null;
    }

    /**
     * Constructs an Enquiry object with a reply.
     *
     * @param enquiryID     Unique identifier for the enquiry.
     * @param applicant     The applicant who submitted the enquiry.
     * @param project       The project this enquiry is related to.
     * @param status        Current status of the enquiry.
     * @param enquiryText   The text or content of the enquiry.
     * @param dateCreated   Timestamp of when the enquiry was created.
     * @param enquiryReply  The reply text to the enquiry.
     */
    public Enquiry(String enquiryID, Applicant applicant, Project project,
                   EnquiryStatus status, String enquiryText, LocalDateTime dateCreated, String enquiryReply) {
        this.enquiryID = enquiryID;
        this.applicant = applicant;
        this.project = project;
        this.status = status;
        this.enquiryText = enquiryText;
        this.dateCreated = dateCreated;
        this.enquiryReply = enquiryReply;
    }

    /**
     * @return the unique enquiry ID.
     */
    public String getEnquiryID() {
        return enquiryID;
    }

    /**
     * @param enquiryID the unique enquiry ID to set.
     */
    public void setEnquiryID(String enquiryID) {
        this.enquiryID = enquiryID;
    }

    /**
     * @return the applicant who made the enquiry.
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * @param applicant the applicant to set.
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * @return the project the enquiry is about.
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the current status of the enquiry.
     */
    public EnquiryStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set.
     */
    public void setStatus(EnquiryStatus status) {
        this.status = status;
    }

    /**
     * @return the enquiry text content.
     */
    public String getEnquiryText() {
        return enquiryText;
    }

    /**
     * @param enquiryText the enquiry text to set.
     */
    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }

    /**
     * @return the timestamp when the enquiry was created.
     */
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the creation date to set.
     */
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the reply text to the enquiry.
     */
    public String getEquiryReply() {
        return enquiryReply;
    }

    /**
     * @param reply the reply text to set.
     */
    public void setEnquiryReply(String reply) {
        this.enquiryReply = reply;
    }

    /**
     * @return a detailed string representation of the enquiry.
     */
    public String generateEnquiryDetails() {
        return """
               Enquiry Details:
               Enquiry ID: """ + enquiryID + "\n" +
                "Applicant Name: " + applicant.getName() + "\n" +
                "Applicant NRIC: " + applicant.getUserId() + "\n" +
                "Project Name: " + project.getProjectName() + "\n" +
                "Status: " + status + "\n" +
                "Enquiry Text: " + enquiryText + "\n" +
                "Enquiry Reply: " + enquiryReply + "\n" +
                "Date Created: " + dateCreated + "\n";
    }
}
