package main.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import main.controller.FlatBookingController;
import main.entity.FlatBooking;
import main.entity.Project;
import main.entity.Enum.FlatBookingStatus;
import main.entity.Enum.FlatType;
import main.entity.User.Applicant;

/**
 * Repository class for handling CSV file operations related to flat bookings.
 * This includes saving and loading {@link FlatBooking} objects to and from disk.
 */
public class BookingRepository {

    /**
     * Writes all flat bookings to a CSV file.
     * Each booking is saved with fields: Flat ID, Applicant ID, Project Name, Flat Type, and Booking Status.
     *
     * @param bookings the list of {@link FlatBooking} objects to write to the file
     */
    public static void writeAllFlatBookings(List<FlatBooking> bookings) {
        String FILE_PATH = "data/flatbookings.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write CSV header
            writer.write("Flat ID,Applicant ID,Project Name,Flat Type,Booking Status");
            writer.newLine();

            // Write each booking entry
            for (FlatBooking booking : bookings) {
                StringBuilder sb = new StringBuilder();
                sb.append(booking.getFlatId()).append(",");
                sb.append(booking.getApplicant().getUserId()).append(",");
                sb.append(booking.getProject().getProjectName()).append(",");
                sb.append(booking.getFlatType().name()).append(",");
                sb.append(booking.getFlatBookingStatus().name());

                writer.write(sb.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing flat bookings to file: " + e.getMessage());
        }
    }

    /**
     * Loads flat bookings from the CSV file and associates them with the given applicant
     * if the applicant ID in the record matches the provided applicant's NRIC.
     * Matches projects by name from the given list.
     *
     * @param applicant the applicant whose bookings are to be loaded
     * @param projects  the list of projects to match project names from CSV records
     */
    public static void loadFlatBookingsForApplicant(Applicant applicant, List<Project> projects) {
        String FILE_PATH = "data/flatbookings.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1); // -1 keeps trailing empty fields

                if (fields.length < 5) continue;

                String applicantID = fields[1];
                if (applicantID.equals(applicant.getUserId())) {
                    String flatId = fields[0];
                    String projectName = fields[2];
                    FlatType flatType = FlatType.valueOf(fields[3]);
                    FlatBookingStatus status = FlatBookingStatus.valueOf(fields[4]);

                    // Find matching project by name
                    Project matchedProject = null;
                    for (Project p : projects) {
                        if (p.getProjectName().equals(projectName)) {
                            matchedProject = p;
                            break;
                        }
                    }

                    if (matchedProject == null) {
                        System.out.println("Project not found for booking: " + flatId);
                        continue;
                    }

                    FlatBooking booking = new FlatBooking(flatId, applicant, flatType, matchedProject, status);
                    applicant.setFlatBooking(booking);  // Save booking to applicant
                    FlatBookingController.flatBookings.add(booking); // Add to global booking list
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading flat bookings: " + e.getMessage());
        }
    }
}
