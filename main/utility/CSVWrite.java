package main.utility;

import java.io.*;
import java.util.*;

public class CSVWrite {

/**
 * Write data to a CSV file.
 * @param filePath The path to the CSV file
 * @param data The data to be written to the csv file
 */
    
 public static void CSVWrite(String filePath, List<LinkedHashMap<String, String>> data) {
    try (PrintWriter writer = new PrintWriter(new File(filePath))) {
        // If data exists, extract headers from the first row
        Set<String> headers;
        if (data != null && !data.isEmpty()) {
            headers = data.get(0).keySet(); // Extract headers from the first row
        } else {
            // If data is empty, still write the header
            System.err.println("No data rows, writing only header.");
            headers = getDefaultHeadersForFile(filePath);  // Get default headers based on file type
        }

        // Write the header to the CSV (always write headers)
        writer.println(String.join(",", headers));

        // If there is data, write the rows
        if (data != null && !data.isEmpty()) {
            for (LinkedHashMap<String, String> row : data) {
                List<String> rowData = new ArrayList<>();
                for (String header : headers) {
                    String value = row.getOrDefault(header, "").replace("\"", "\"\"");
                    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
                        value = "\"" + value + "\"";
                    }
                    rowData.add(value);
                }
                writer.println(String.join(",", rowData));
            }
        }

    } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
    }
}

    /**
     * Updates a user's details in the CSV file based on NRIC.
     * @param filePath The path to the CSV file.
     * @param userId The NRIC of the user to update.
     * @param fieldToUpdate The field (column name) to update.
     * @param newValue The new value to set.
     * @param data The existing data read from the CSV file.
     */
    public static void updateUserInCSV(String filePath, String userId, String fieldToUpdate, String newValue, List<LinkedHashMap<String, String>> data) {
        boolean updated = false;

        for (LinkedHashMap<String, String> row : data) {
            if (row.get("NRIC").equals(userId)) {  
                row.put(fieldToUpdate, newValue);
                updated = true;
                break;
            }
        }

        if (updated) {
            CSVWrite(filePath, data);
            System.out.println("User details updated successfully!");
        } else {
            System.out.println("User not found.");
        }
    }

    private static Set<String> getDefaultHeadersForFile(String filePath) {
        if (filePath.contains("Project")) {
            return new LinkedHashSet<>(List.of("Project Name", "Neighborhood", "Type 1", "Number of units for Type 1",
                                               "Selling price for Type 1", "Type 2", "Number of units for Type 2",
                                               "Selling price for Type 2", "Application opening date", "Application closing date",
                                               "Manager", "Officer Slot", "Officer", "Visibility"));
        } else if (filePath.contains("Applicant")) {
            return new LinkedHashSet<>(List.of("Name", "NRIC", "Age", "Marital Status", "Password"));
        } else if (filePath.contains("Officer")) {
            return new LinkedHashSet<>(List.of("Name", "NRIC", "Age", "Marital Status", "Password"));
        } else if (filePath.contains("Manager")) {
            return new LinkedHashSet<>(List.of("Name", "NRIC", "Age", "Marital Status", "Password"));
        }
        // Add fallback if no conditions match
        return new LinkedHashSet<>(); // Return an empty set as a fallback
    }
    
    
}

    
