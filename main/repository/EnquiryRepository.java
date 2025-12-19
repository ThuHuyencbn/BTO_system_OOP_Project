package main.repository;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import main.controller.EnquiryController;
import main.entity.Enquiry;
import main.entity.Project;
import main.entity.Enum.EnquiryStatus;
import main.entity.User.Applicant;

/**
 * Repository class for handling the saving and loading of Enquiry data to and from CSV.
 */
public class EnquiryRepository {

    private static final String FILE_PATH = "data/Enquiry.csv";

    /**
     * Appends a single enquiry to the CSV file.
     * If the file is new or empty, a header is written first.
     *
     * @param enquiry The enquiry to be appended.
     */
    public static void appendEnquiryToCSV(Enquiry enquiry) {
        File file = new File(FILE_PATH);
        boolean isNewOrEmpty = !file.exists() || file.length() < 6;
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            if (isNewOrEmpty) {
                writer.println("Enquiry ID,Applicant Id,Project Name,Status, Enquiry text, Created Date,Reply Text");
            }

            StringBuilder sb = new StringBuilder();
            sb.append(enquiry.getEnquiryID()).append(",");
            sb.append(enquiry.getApplicant().getUserId()).append(",");
            sb.append(enquiry.getProject().getProjectName()).append(",");
            sb.append(enquiry.getStatus().name()).append(",");

            String cleanEnq = enquiry.getEnquiryText().replace("\"", "\"\"");
            sb.append("\"").append(cleanEnq).append("\",");

            sb.append(enquiry.getDateCreated().toString()).append(",");

            String rawReply = enquiry.getEquiryReply();
            String cleanReply = rawReply == null ? "" : rawReply.replace("\"", "\"\"");
            sb.append("\"").append(cleanReply).append("\"");

            writer.println(sb.toString());

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    /**
     * Overwrites the CSV file with the full list of enquiries.
     *
     * @param enquiries List of all enquiries to write.
     */
    public static void writeAllEnquiries(List<Enquiry> enquiries) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("EnquiryID,Applicant Id,Project Name,Status,Enquiry Text,Created Date,Reply Text");
            writer.newLine();

            for (Enquiry enquiry : enquiries) {
                StringBuilder sb = new StringBuilder();
                sb.append(enquiry.getEnquiryID()).append(",");
                sb.append(enquiry.getApplicant().getUserId()).append(",");
                sb.append(enquiry.getProject().getProjectName()).append(",");
                sb.append(enquiry.getStatus().name()).append(",");

                String cleanEnq = enquiry.getEnquiryText().replace("\"", "\"\"");
                sb.append("\"").append(cleanEnq).append("\",");

                sb.append(enquiry.getDateCreated().toString()).append(",");

                String rawReply = enquiry.getEquiryReply();
                String cleanReply = rawReply == null ? "" : rawReply.replace("\"", "\"\"");
                sb.append("\"").append(cleanReply).append("\"");

                writer.write(sb.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Loads all enquiries from CSV that belong to a given applicant.
     * Enquiries are associated with matching projects.
     *
     * @param applicant The applicant whose enquiries are being loaded.
     * @param projects  The list of available projects for matching.
     */
    public static void loadEnquiriesForApplicant(Applicant applicant, List<Project> projects) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);

                if (fields.length < 7) continue;

                String applicantID = fields[1];
                if (applicantID.equals(applicant.getUserId())) {
                    String enquiryID = fields[0];
                    int numberPart = Integer.parseInt(enquiryID.substring(5));
                    EnquiryController.enquiryCounter = numberPart + 1;

                    String projectName = fields[2];
                    EnquiryStatus status = EnquiryStatus.valueOf(fields[3]);
                    String enquiryText = fields[4].replace("\"\"", "\"").replace("\"", "");
                    LocalDateTime createdAt = LocalDateTime.parse(fields[5]);
                    String replyText = fields[6];

                    Project project = null;
                    for (Project p : projects) {
                        if (p.getProjectName().equals(projectName)) {
                            project = p;
                            break;
                        }
                    }

                    if (project == null) {
                        System.out.println("Project not found for enquiry: " + enquiryID);
                        continue;
                    }

                    Enquiry enquiry = new Enquiry(enquiryID, applicant, project, status, enquiryText, createdAt, replyText);
                    applicant.getEnquiries().add(enquiry);
                    EnquiryController.enquiries.add(enquiry);
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to load enquiries for " + applicant.getUserId() + ": " + e.getMessage());
        }
    }
}
