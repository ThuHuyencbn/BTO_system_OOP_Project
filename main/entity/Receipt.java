package main.entity;

import java.time.LocalDate;
import main.entity.Enum.FlatType;
import main.entity.Enum.MaritalStatus;
import main.entity.User.Applicant;

/**
 * Represents a receipt generated after a successful flat booking.
 * Captures details of the applicant and the booked flat.
 */
public class Receipt {

    private String applicantName;
    private String applicantNRIC;
    private int applicantAge;
    private MaritalStatus maritalStatus;
    private FlatType flatType;
    private String projectName;
    private String neighborhood;
    private LocalDate bookingDate;

    /**
     * Constructs a receipt based on applicant data and associated application.
     * @param applicant The applicant who booked the flat.
     */
    public Receipt(Applicant applicant) {
        this.applicantName = applicant.getName();
        this.applicantNRIC = applicant.getUserId();
        this.applicantAge = applicant.getAge();
        this.maritalStatus = applicant.getMaritalStatus();
        this.flatType = applicant.getApplication().getFlatType();
        this.projectName = applicant.getApplication().getProject().getProjectName();
        this.neighborhood = applicant.getApplication().getProject().getNeighborhood();
        this.bookingDate = applicant.getApplication().getSubmissionDate();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
    }

    public int getApplicantAge() {
        return applicantAge;
    }

    public void setApplicantAge(int applicantAge) {
        this.applicantAge = applicantAge;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Generates a printable string version of the flat booking receipt.
     * @return A formatted string containing receipt details.
     */
    public String generateReceipt() {
        return """
               Receipt for Flat Booking
               Applicant Name: """ + applicantName + "\n" +
                "NRIC: " + applicantNRIC + "\n" +
                "Age: " + String.valueOf(applicantAge) + "\n" +
                "Marital Status: " + maritalStatus.toString() + "\n" +
                "Flat Type: " + flatType.toString() + "\n" +
                "Project Name: " + projectName + "\n" +
                "Neighborhood: " + neighborhood + "\n" +
                "Booking Date: " + bookingDate + "\n";
    }
}
