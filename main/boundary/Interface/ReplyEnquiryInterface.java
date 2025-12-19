package main.boundary.Interface;

/**
 * Interface that provides functionality for replying to an enquiry.
 * Extends the base {@link EnquiryInterface}.
 */
public interface ReplyEnquiryInterface extends EnquiryInterface {

    /**
     * Allows a user (e.g., HDB officer) to reply to an existing enquiry.
     */
    void replyEnquiry();
}
