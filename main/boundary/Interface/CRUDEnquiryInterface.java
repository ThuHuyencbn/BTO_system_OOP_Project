package main.boundary.Interface;

/**
 * Interface for performing Create, Read, Update, and Delete (CRUD) operations on Enquiries.
 * Extends the basic {@link EnquiryInterface}.
 */
public interface CRUDEnquiryInterface extends EnquiryInterface {

    /**
     * Creates a new enquiry.
     */
    public void createEnquiry();

    /**
     * Deletes an existing enquiry.
     */
    public void deleteEnquiry();

    /**
     * Edits an existing enquiry.
     */
    public void editEnquiry();
}

