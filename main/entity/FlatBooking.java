package main.entity;

import main.entity.Enum.FlatBookingStatus;
import main.entity.Enum.FlatType;
import main.entity.User.Applicant;

/**
 * Represents a flat booking made by an applicant for a specific project.
 * Stores booking details such as flat type, project, applicant, and booking status.
 */
public class FlatBooking {
    private String flatId;
    private Applicant applicant;
    private FlatType flatType;
    private Project project;
    private FlatBookingStatus flatBookingStatus;

    /**
     * Constructs a new FlatBooking object.
     *
     * @param flatId             Unique identifier for the flat booking.
     * @param applicant          The applicant who booked the flat.
     * @param flatType           The type of flat booked (TWO_ROOM or THREE_ROOM).
     * @param project            The project associated with the flat.
     * @param flatBookingStatus  Current status of the booking (e.g., PENDING, APPROVED).
     */
    public FlatBooking(String flatId, Applicant applicant, FlatType flatType, Project project, FlatBookingStatus flatBookingStatus){
        this.flatId = flatId;
        this.applicant = applicant;
        this.flatType = flatType;
        this.project = project;
        this.flatBookingStatus = flatBookingStatus;
    }

    /**
     * @return the applicant who booked the flat.
     */
    public Applicant getApplicant(){
        return applicant;
    }

    /**
     * @return the type of flat booked.
     */
    public FlatType getFlatType(){
        return flatType;
    }

    /**
     * @return the unique booking ID.
     */
    public String getFlatId(){
        return flatId;
    }

    /**
     * @param flatId the unique booking ID to set.
     */
    public void setFlatId(String flatId){
        this.flatId = flatId;
    }

    /**
     * @return the project associated with this booking.
     */
    public Project getProject(){
        return project;
    }

    /**
     * @param applicant the applicant to set.
     */
    public void setApplicant(Applicant applicant){
        this.applicant = applicant;
    }

    /**
     * @param flatType the flat type to set.
     */
    public void setFlatType(FlatType flatType){
        this.flatType = flatType;
    }

    /**
     * @param project the project to set.
     */
    public void setProject(Project project){
        this.project = project;
    }

    /**
     * @return the current status of the flat booking.
     */
    public FlatBookingStatus getFlatBookingStatus(){
        return flatBookingStatus;
    }

    /**
     * @param flatBookingStatus the status to set.
     */
    public void setFlatBookingStatus(FlatBookingStatus flatBookingStatus){
        this.flatBookingStatus = flatBookingStatus;
    }

    /**
     * @return formatted string containing flat booking details.
     */
    @Override
    public String toString() {
        return
            """
            Flat Booking Details:
              Booking ID: """   + flatId                     + "\n" +
            "  Applicant: "    + applicant.getName()        + "\n" +
            "  Flat Type: "    + flatType.toString()        + "\n" +
            "  Project: "      + project.getProjectName()   + "\n" +
            "  Status: "       + flatBookingStatus.toString();
    }
}
