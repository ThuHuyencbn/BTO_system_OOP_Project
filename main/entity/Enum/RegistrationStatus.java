package main.entity.Enum;

/**
 * Enumeration representing the status of a registration request by an HDB Officer.
 * This status is used to track the progress of officer registration to handle a project.
 */
public enum RegistrationStatus {

    /** Indicates that the registration has been approved. */
    APPROVED,

    /** Indicates that the registration is currently under review. */
    PENDING,

    /** Indicates that the registration has been rejected. */
    REJECTED,
}
