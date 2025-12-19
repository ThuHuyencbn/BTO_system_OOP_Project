package main.repository;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import main.controller.RegistrationController;
import main.entity.Enum.RegistrationStatus;
import main.entity.Project;
import main.entity.Registration;
import main.entity.User.HDBOfficer;
/**
 * Repository class responsible for managing the persistence of {@link Registration} objects.
 * Handles operations like writing new entries, updating the CSV, and loading registration data for officers.
 */
public class RegistrationRepository {

    private static final String FILE_PATH = "data/Registration.csv";
    /**
     * Appends a single {@link Registration} record to the CSV file.
     * Adds a header if the file is new or empty.
     *
     * @param registration the registration object to append
     */
    
    public static void appendRegistrationToCSV(Registration registration) {
        File file = new File(FILE_PATH);
        boolean isNewOrEmpty = !file.exists() || file.length() < 6;

    
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            if (isNewOrEmpty) {
                // write header only once
                writer.println("Registration ID, Officer Id,Project Name,Status,Submission Date");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(registration.getRegistrationId())
              .append(",")
              .append(registration.getOfficer().getUserId())
              .append(",")
              .append(registration.getProject().getProjectName())
              .append(",")
              .append(registration.getRegistrationStatus())
              .append(",")
              .append(registration.getSubmissionDate());
            writer.println(sb.toString());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    /**
     * Writes all {@link Registration} records to the CSV file, overwriting the existing contents.
     *
     * @param registrations the list of registrations to write
     */
    public static void writeAllRegistration(List<Registration> registrations) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write the header first
            writer.write("Registration ID, Officer Id,Project Name,Status,Submission Date");
            writer.newLine();
    
            // Write each enquiry
            for (Registration registration : registrations) {
                StringBuilder sb = new StringBuilder();
                sb.append(registration.getRegistrationId()).append(",");
                sb.append(registration.getOfficer().getUserId()).append(",");
                sb.append(registration.getProject().getProjectName()).append(",");
                sb.append(registration.getRegistrationStatus().name()).append(",");
                sb.append(registration.getSubmissionDate().toString());
                writer.write(sb.toString());
                writer.newLine();
            }
    
            System.out.println("Registration successfully written to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Loads registration data from the CSV file and sets it for the given HDB officer.
     * It also increments the registration counter in the {@link RegistrationController}.
     *
     * @param officer  the officer for whom the registration should be loaded
     * @param projects the list of all available projects to match by project name
     */

    public static void loadRegistrationForOfficer(HDBOfficer officer, List<Project> projects) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header
    
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1); // -1 includes trailing empty fields
    
                // Ensure correct number of fields
                if (fields.length < 5) continue;
    
                String registrationID = fields[0].trim();           
                String officerID   = fields[1].trim();
    
                // Match the registration to the current applicant
                if (!officerID.equals(officer.getUserId())) continue;
    
                String projectName   = fields[2].trim();
                String statusStr     = fields[3].trim();
                String dateStr       = fields[4].trim();
    
                try {
                    RegistrationStatus status = RegistrationStatus.valueOf(statusStr);
                    LocalDate createdAt = LocalDate.parse(dateStr);
                    
    
                    // Find project by name
                    Project project = null;
                    for (Project p : projects) {
                        if (p.getProjectName().equals(projectName)) {
                            project = p;
                            break;
                        }
                    }
    
                    if (project == null) {
                        System.err.println("Project not found for application: " + registrationID);
                        continue;
                    }
    
                    // Increment the application counter
                    RegistrationController.registrationCounter++;
    
                    // Create and register application
                    Registration registration = new Registration(
                        registrationID,
                        officer,
                        project,
                        status,
                        createdAt
                    );
    
                    officer.setRegistration(registration);
                    RegistrationController.getAllRegistrations().add(registration);
    
                } catch (Exception e) {
                    System.err.println("Error parsing application row: " + Arrays.toString(fields));
                    e.printStackTrace();
                }
            }
    
        } catch (IOException e) {
            System.out.println("Failed to load registration for " + officer.getUserId() + ": " + e.getMessage());
        }
    }
}
