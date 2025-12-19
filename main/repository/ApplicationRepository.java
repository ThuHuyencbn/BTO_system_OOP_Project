package main.repository;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import main.controller.ApplicationController;
import main.entity.Application;
import main.entity.Enum.ApplicationStatus;
import main.entity.Enum.FlatType;
import main.entity.Project;
import main.entity.User.Applicant;

/**
 * Handles persistence operations for Application entities,
 * including saving to and loading from a CSV file.
 */
public class ApplicationRepository {

    private static final String FILE_PATH = "data/Application.csv";

    /**
     * Writes all applications to the CSV file, replacing existing content.
     *
     * @param applications List of all Application objects to be written.
     */
    public static void writeAllApplication(List<Application> applications) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Application ID, Applicant Id,Project Name,Status,Submission Date, Flat Type");
            writer.newLine();

            for (Application application : applications) {
                StringBuilder sb = new StringBuilder();
                sb.append(application.getApplicationId()).append(",");
                sb.append(application.getApplicant().getUserId()).append(",");
                sb.append(application.getProject().getProjectName()).append(",");
                sb.append(application.getStatus().name()).append(",");
                sb.append(application.getSubmissionDate().toString()).append(",");
                sb.append(application.getFlatType().toString());
                writer.write(sb.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Appends a new application to the CSV file.
     * Adds the header only if the file is new or empty.
     *
     * @param application The Application object to be appended.
     */
    public static void appendApplicationToCSV(Application application) {
        File file = new File(FILE_PATH);
        boolean isNewOrEmpty = !file.exists() || file.length() < 6;

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            if (isNewOrEmpty) {
                writer.println("Application ID,Applicant Id,Project Name,Status,Submission Date,Flat Type");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(application.getApplicationId())
              .append(",")
              .append(application.getApplicant().getUserId())
              .append(",")
              .append(application.getProject().getProjectName())
              .append(",")
              .append(application.getStatus())
              .append(",")
              .append(application.getSubmissionDate())
              .append(",")              
              .append(application.getFlatType());
            writer.println(sb.toString());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    /**
     * Loads applications from the CSV file and associates them with the given applicant
     * if the applicant ID matches.
     *
     * @param applicant The applicant whose applications should be loaded.
     * @param projects  List of all projects used to match project names to actual Project objects.
     */
    public static void loadApplicationForApplicant(Applicant applicant, List<Project> projects) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);

                if (fields.length < 6) continue;

                String applicationID = fields[0].trim();
                String applicantID   = fields[1].trim();

                if (!applicantID.equals(applicant.getUserId())) continue;

                String projectName   = fields[2].trim();
                String statusStr     = fields[3].trim();
                String dateStr       = fields[4].trim();
                String flatTypeStr   = fields[5].trim();
                try {
                    ApplicationStatus status = ApplicationStatus.valueOf(statusStr);
                    LocalDate createdAt = LocalDate.parse(dateStr);
                    FlatType flatType = FlatType.valueOf(flatTypeStr);

                    Project project = null;
                    for (Project p : projects) {
                        if (p.getProjectName().equals(projectName)) {
                            project = p;
                            break;
                        }
                    }

                    if (project == null) {
                        System.err.println("Project not found for application: " + applicationID);
                        continue;
                    }

                    ApplicationController.applicationCounter++;

                    Application application = new Application(
                        applicationID,
                        applicant,
                        project,
                        status,
                        createdAt,
                        flatType
                    );

                    applicant.setApplication(application);
                    ApplicationController.getAllApplications().add(application);

                } catch (Exception e) {
                    System.err.println("Error parsing application row: " + Arrays.toString(fields));
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to load application for " + applicant.getUserId() + ": " + e.getMessage());
        }
    }
}
