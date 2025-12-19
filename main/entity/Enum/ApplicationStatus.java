package main.entity.Enum;

/**
 * Enum representing the various statuses that an application can have
 * throughout its lifecycle in the BTO system.
 */
public enum ApplicationStatus {
    
    /** Application has been submitted and is awaiting review. */
    PENDING,

    /** Application has been approved successfully. */
    SUCCESSFUL,

    /** Application has been reviewed and rejected. */
    UNSUCCESSFUL,

    /** Applicant has booked a flat, and application is finalized. */
    BOOKED,

    /** Applicant has submitted a request to withdraw the application, pending manager approval. */
    PENDING_WITHDRAWN,

    /** Withdrawal of application has been approved and finalized. */
    WITHDRAWN;
}
