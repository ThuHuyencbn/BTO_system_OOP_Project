package main.entity.Enum;

/**
 * Enum representing the status of an enquiry made by an applicant or officer.
 */
public enum EnquiryStatus {

    /** The enquiry has been submitted and is awaiting a reply. */
    PENDING,

    /** The enquiry has received a response from a manager or officer. */
    RESPONDED,

    /** The enquiry has been resolved and is now closed. */
    CLOSED
}