package main.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import main.controller.UserController.OfficerController;
import main.entity.User.HDBOfficer;
import main.utility.CSVWrite;

/**
 * Repository class responsible for saving officer data to a CSV file.
 */
public class OfficerRepository {
    private static final String FILE_PATH = "data/OfficerUpdatedList.csv";

    /**
     * Saves all HDB officers to a CSV file.
     * Each officer is stored with their Name, NRIC, Age, Marital Status, and Password.
     */
    public static void saveOfficersToCSV() {
        List<LinkedHashMap<String, String>> csvData = new ArrayList<>();
        for (HDBOfficer officer : OfficerController.getOfficerList()) {
            LinkedHashMap<String, String> row = new LinkedHashMap<>();
            row.put("Name", officer.getName());
            row.put("NRIC", officer.getUserId());
            row.put("Age", String.valueOf(officer.getAge()));
            row.put("Marital Status", officer.getMaritalStatus().toString());
            row.put("Password", officer.getPassword());
            csvData.add(row);
        }
        CSVWrite.CSVWrite(FILE_PATH, csvData);
    }
}
