package main.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import main.controller.UserController.ManagerController;
import main.entity.User.HDBManager;
import main.utility.CSVWrite;

/**
 * Repository class responsible for saving manager data to CSV.
 */
public class ManagerRepository {
    private static final String FILE_PATH = "data/ManagerUpdatedList.csv";

    /**
     * Saves all HDB managers to a CSV file.
     * Each manager is written with Name, NRIC, Age, Marital Status, and Password fields.
     */
    public static void saveManagersToCSV() {
        List<LinkedHashMap<String, String>> csvData = new ArrayList<>();
        for (HDBManager manager : ManagerController.getManagerList()) {
            LinkedHashMap<String, String> row = new LinkedHashMap<>();
            row.put("Name", manager.getName());
            row.put("NRIC", manager.getUserId());
            row.put("Age", String.valueOf(manager.getAge()));
            row.put("Marital Status", manager.getMaritalStatus().toString());
            row.put("Password", manager.getPassword());
            csvData.add(row);
        }
        CSVWrite.CSVWrite(FILE_PATH, csvData);
    }

}
