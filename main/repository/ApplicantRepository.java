package main.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import main.controller.UserController.ApplicantController;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.User.Applicant;
import main.utility.CSVRead;
import main.utility.CSVWrite;

/**
 * The ApplicantRepository class handles saving and loading applicant data
 * to and from a CSV file. It provides persistence for applicant-related data.
 */
public class ApplicantRepository {

    private static final String FILE_PATH = "data/ApplicantUpdatedList.csv";

    /**
     * Saves the list of applicants to a CSV file.
     *
     * @param applicants The list of applicants to be saved.
     */
    public static void saveApplicantsToCSV(List<Applicant> applicants) {
        List<LinkedHashMap<String, String>> csvData = new ArrayList<>();
        for (Applicant app : applicants) {
            LinkedHashMap<String, String> row = new LinkedHashMap<>();
            row.put("Name", app.getName());
            row.put("NRIC", app.getUserId());
            row.put("Age", String.valueOf(app.getAge()));
            row.put("Marital Status", app.getMaritalStatus().toString());
            row.put("Password", app.getPassword());
            csvData.add(row);
        }
        CSVWrite.CSVWrite(FILE_PATH, csvData);
    }

    /**
     * Loads all applicants from the specified CSV file and populates the system.
     * Ensures no duplicates are added based on NRIC.
     *
     * @param filePath The path to the CSV file.
     */
    public static void loadAllApplicants(String filePath) {
        var applicantRaw = CSVRead.CSVRead(filePath, true);
        for (var row : applicantRaw) {
            String userId = row.get("NRIC");
            if (ApplicantController.getUserById(userId) != null) continue;

            String password = row.get("Password");
            String name = row.get("Name");
            int age = Integer.parseInt(row.get("Age"));
            String status = row.get("Marital Status").trim().toUpperCase();
            MaritalStatus maritalStatus = MaritalStatus.valueOf(status);

            ApplicantController.addApplicant(userId, password, name, Role.APPLICANT, maritalStatus, age);
        }
    }

    /**
     * Checks if the updated applicants file contains any data beyond the header row.
     *
     * @return true if the file contains applicant data, false otherwise.
     */
    public static boolean updatedApplicantsFileHasData() {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.FileReader("data/ApplicantUpdatedList.csv"))) {
            // Skip header
            reader.readLine();
            // Check if there’s any data after header
            String data = reader.readLine();
            return data != null && !data.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
