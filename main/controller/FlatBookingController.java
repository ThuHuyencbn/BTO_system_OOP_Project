package main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.FlatBookingStatus;
import main.entity.Enum.FlatType;
import main.entity.Enum.MaritalStatus;
import main.entity.FlatBooking;
import main.entity.Project;
import main.entity.User.Applicant;
import main.repository.BookingRepository;

/**
 * Controller for handling flat booking operations such as creating, updating,
 * and filtering flat bookings for applicants.
 */
public class FlatBookingController {

    /** List to hold all flat bookings in memory. */
    public static List<FlatBooking> flatBookings = new ArrayList<>();

    /**
     * Generates a new flat booking request for the specified applicant.
     *
     * @param applicant the applicant making the booking
     * @param flatType the type of flat the applicant is booking
     * @param project the project the flat belongs to
     */
    public static void generateFlatBooking(Applicant applicant, FlatType flatType, Project project){
        if(applicant == null || flatType == null || project == null){
            System.out.println("Invalid input!");
            return;
        }
        String flatId = generateFlatBookingId(applicant);
        FlatBooking flatBooking = new FlatBooking(flatId, applicant, flatType, project, FlatBookingStatus.PENDING);
        applicant.setFlatBooking(flatBooking);
        flatBookings.add(flatBooking);
        BookingRepository.writeAllFlatBookings(flatBookings);
    }

    /**
     * Prints all given flat booking requests.
     *
     * @param flatBookings the list of flat bookings to print
     */
    public static void printFlatBooking(List<FlatBooking> flatBookings) {
        System.out.println("----------- Applicant Flat Booking Request -----------");
        for (FlatBooking flatBooking: flatBookings) {
            System.out.println(flatBooking);
        }
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Generates a unique flat booking ID.
     *
     * @param applicant the applicant requesting the booking
     * @return a unique flat booking ID string
     */
    private static String generateFlatBookingId(Applicant applicant){
        return "FL-" + applicant.getUserId() + "-" + System.currentTimeMillis();
    }

    /**
     * Retrieves a flat booking by its ID.
     *
     * @param flatId the flat booking ID
     * @return the matching FlatBooking or null if not found
     */
    public static FlatBooking getFlatBookingById(String flatId){
        for(FlatBooking flat: flatBookings){
            if(flat.getFlatId().equals(flatId)){
                return flat;
            }
        }
        return null;
    }

    /**
     * Checks if the applicant has already booked a flat.
     *
     * @param applicant the applicant to check
     * @return true if the applicant has a booked application, false otherwise
     */
    public static boolean alreadyBookFlat(Applicant applicant){
        return applicant.getApplication().getStatus().equals(ApplicationStatus.BOOKED);
    }

    /**
     * Gets a list of all pending flat booking requests.
     *
     * @return list of pending FlatBooking objects
     */
    public static List<FlatBooking> getPendingFlatBooking(){
        List<FlatBooking> flatBooking = new ArrayList<>();
        for(FlatBooking flat: flatBookings){
            if(flat.getFlatBookingStatus().equals(FlatBookingStatus.PENDING)){
                flatBooking.add(flat);
            }
        }
        return flatBooking;
    }

    /**
     * Updates the booking status of a given flat booking.
     *
     * @param flatBooking the flat booking to update
     * @param flatBookingStatus the new status to assign
     */
    public static void updateFlatBookingStatus(FlatBooking flatBooking, FlatBookingStatus flatBookingStatus){
        flatBooking.setFlatBookingStatus(flatBookingStatus);
    }

    /**
     * Gets a list of all approved flat bookings.
     *
     * @return list of approved FlatBooking objects
     */
    public static List<FlatBooking> getApprovedFlatBooking(){
        List<FlatBooking> flatBooking = new ArrayList<>();
        for(FlatBooking flat: flatBookings){
            if(flat.getFlatBookingStatus().equals(FlatBookingStatus.APPROVED)){
                flatBooking.add(flat);
            }
        }
        return flatBooking;
    }

    /**
     * Generates a filtered report of flat bookings based on provided filters.
     *
     * @param flatBookings list of flat bookings to filter
     * @param maritalStatusFilter filter by marital status (nullable)
     * @param flatTypeFilter filter by flat type (nullable)
     * @param projectNameFilter filter by project name (nullable)
     * @return filtered list of FlatBooking objects
     */
    public static List<FlatBooking> generateFilteredReport(
        List<FlatBooking> flatBookings,
        MaritalStatus maritalStatusFilter,
        FlatType flatTypeFilter,
        String projectNameFilter) {

        return flatBookings.stream()
            .filter(booking -> {
                if (maritalStatusFilter != null &&
                    booking.getApplicant().getMaritalStatus() != maritalStatusFilter) {
                    return false;
                }

                if (flatTypeFilter != null &&
                    booking.getFlatType() != flatTypeFilter) {
                    return false;
                }

                if (projectNameFilter != null && !projectNameFilter.isEmpty() &&
                    !booking.getProject().getProjectName().equalsIgnoreCase(projectNameFilter)) {
                    return false;
                }

                return true;
            })
            .collect(Collectors.toList());
    }

    /**
     * Prints a summary report of given flat bookings.
     *
     * @param reports list of filtered FlatBooking objects to display
     */
    public static void printReport(List<FlatBooking> reports) {
        System.out.println("----------- Applicant Flat Booking Report -----------");
        System.out.printf("%-15s %-25s %-10s %-15s%n", 
                          "Flat Type", "Project Name", "Age", "Marital Status");
        System.out.println("---------------------------------------------------------------");

        for (FlatBooking report : reports) {
            String flatType = report.getFlatType().name();
            String projectName = report.getProject().getProjectName();
            int age = report.getApplicant().getAge();
            String maritalStatus = report.getApplicant().getMaritalStatus().name();

            System.out.printf("%-15s %-25s %-10d %-15s%n", 
                              flatType, projectName, age, maritalStatus);
        }
    }

}
