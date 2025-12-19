package main.boundary.Interface;

import main.controller.EnquiryController;
import main.entity.User.Applicant;

/**
 * Interface that provides a default method to allow applicants to view their enquiries.
 * Intended to be extended by more specific enquiry-related interfaces.
 */
public interface EnquiryInterface {

    /**
     * Displays the list of enquiries created by the given applicant.
     * Only enquiries with status 'PENDING' are eligible for editing.
     *
     * @param currentApplicant the applicant whose enquiries are to be viewed
     */
    default void viewEnquiry(Applicant currentApplicant) {
        System.out.println("Here is the list of enquiries you have created. You can only edit enquiries with status 'PENDING'.");
        if (currentApplicant.getEnquiries().isEmpty()) {
            System.out.println("You have no enquiries!");
            return;
        }
        EnquiryController.displayEnquiriesByApplicant(currentApplicant.getEnquiries());
    }
}
