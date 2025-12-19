package main.repository;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import main.controller.ProjectController;
import main.controller.UserController.ManagerController;
import main.controller.UserController.OfficerController;
import main.entity.Enum.FlatType;
import main.entity.Project;
import main.entity.User.HDBManager;
import main.entity.User.HDBOfficer;
import main.utility.CSVRead;
import main.utility.CSVWrite;
/**
 * Repository class responsible for saving and loading {@link Project} data
 * to and from the "ProjectUpdatedList.csv" CSV file.
 * Includes support for loading flat types, officer slots, pricing,
 * application dates, visibility settings, and associated officers.
 */
public class ProjectRepository {

    private static final String FILE_PATH = "data/ProjectUpdatedList.csv";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Saves all projects from ProjectController to the CSV file,
     * including visibility and optional flat types.
     */
    public static void saveProjectsToCSV() {
        List<LinkedHashMap<String, String>> csvData = new ArrayList<>();

        for (Project project : ProjectController.projects) {
            LinkedHashMap<String, String> row = new LinkedHashMap<>();
            row.put("Project Name", project.getProjectName());
            row.put("Neighborhood", project.getNeighborhood());

            row.put("Type 1", project.getFlatTypes().get(0).toString());
            row.put("Number of units for Type 1", String.valueOf(project.getUnitsAvailable().get(0)));
            row.put("Selling price for Type 1", String.valueOf(project.getSellingPrice().get(0)));

            String type2 = (project.getFlatTypes().size() > 1) ? project.getFlatTypes().get(1).toString() : "null";
            row.put("Type 2", type2);

            String unitsForType2 = (project.getUnitsAvailable().size() > 1)
                ? String.valueOf(project.getUnitsAvailable().get(1)) : "null";
            row.put("Number of units for Type 2", unitsForType2);

            String priceForType2 = (project.getSellingPrice().size() > 1)
                ? String.valueOf(project.getSellingPrice().get(1)) : "null";
            row.put("Selling price for Type 2", priceForType2);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            row.put("Application opening date", dateFormat.format(project.getApplicationOpeningDate()));
            row.put("Application closing date", dateFormat.format(project.getApplicationClosingDate()));

            row.put("Manager", project.getManagerInCharge().getName());
            row.put("Officer Slot", String.valueOf(project.getOfficerSlot()));

            List<String> officerNames = project.getAssignedOfficers().stream()
                .map(HDBOfficer::getName)
                .collect(Collectors.toList());
            row.put("Officer", String.join(",", officerNames));

            // Save visibility flag
            row.put("Visibility", String.valueOf(project.isVisible()));

            csvData.add(row);
        }

        CSVWrite.CSVWrite(FILE_PATH, csvData);
    }
    /**
     * Parses a flat type string into a valid {@link FlatType} enum.
     *
     * @param type the flat type string (case-insensitive, e.g. "2-room")
     * @return the corresponding {@link FlatType} enum
     * @throws IllegalArgumentException if the type is unknown
     */
    private static FlatType parseFlatType(String type) {
        switch (type.trim().toLowerCase()) {
            case "2-room":
            case "two_room":
            case "2_room":
                return FlatType.TWO_ROOM;
            case "3-room":
            case "three_room":
            case "3_room":
                return FlatType.THREE_ROOM;
            default:
                throw new IllegalArgumentException("Unknown flat type: " + type);
        }
    }
    /**
     * Loads project data from the given CSV file path and creates
     * project objects using {@link ProjectController#createProject}.
     * Links projects to managers and officers based on their names.
     *
     * @param filePath the CSV file path to read project data from
     */
    
    public static void loadProjectsFromCSV(String filePath) {
        var projectRaw = CSVRead.CSVRead(filePath, true);

        for (var row : projectRaw) {
            try {
                String projectName = row.get("Project Name");
                String neighborhood = row.get("Neighborhood");

                // Flat type 1
                FlatType type1 = parseFlatType(row.get("Type 1"));
                int units1 = Integer.parseInt(row.get("Number of units for Type 1"));
                int price1 = Integer.parseInt(row.get("Selling price for Type 1"));

                // Flat type 2 (optional)
                FlatType type2 = null;
                int units2 = 0, price2 = 0;
                boolean hasType2 = !row.get("Type 2").equals("null");

                if (hasType2) {
                    type2 = parseFlatType(row.get("Type 2"));
                    units2 = Integer.parseInt(row.get("Number of units for Type 2"));
                    price2 = Integer.parseInt(row.get("Selling price for Type 2"));
                }

                List<FlatType> flatTypes = new ArrayList<>();
                flatTypes.add(type1);
                if (hasType2) flatTypes.add(type2);

                List<Integer> units = new ArrayList<>();
                units.add(units1);
                if (hasType2) units.add(units2);

                List<Integer> prices = new ArrayList<>();
                prices.add(price1);
                if (hasType2) prices.add(price2);

                // Dates
                Date openingDate = sdf.parse(row.get("Application opening date"));
                Date closingDate = sdf.parse(row.get("Application closing date"));

                // Manager
                String managerName = row.get("Manager");
                HDBManager manager = ManagerController.getUserByName(managerName);
                if (manager == null) {
                    System.err.println("Manager not found: " + managerName);
                    continue;
                }

                // Officers
                int officerSlot = Integer.parseInt(row.get("Officer Slot"));

                List<HDBOfficer> officers = new ArrayList<>();
                String[] officerNames = row.get("Officer").replace("\"", "").split(",");
                for (String name : officerNames) {
                    HDBOfficer officer = OfficerController.getUserByName(name.trim());
                    if (officer != null){
                        officers.add(officer);
                    }
                }

                // Visibility
                boolean isVisible = true;
                if (row.containsKey("Visibility")) {
                    isVisible = Boolean.parseBoolean(row.get("Visibility").trim());
                }

                // Create project
                Project project = ProjectController.createProject(
                    projectName, neighborhood, flatTypes, units,
                    openingDate, closingDate, prices,
                    manager, officerSlot, officers, isVisible
                );

                manager.addCreatedProjects(project);
                for (HDBOfficer officer : officers) {
                    officer.setAssignedProject(project);
                }

            } catch (ParseException e) {
                System.err.println("Invalid date format. Use dd/MM/yyyy");
            } catch (Exception e) {
                System.err.println("Failed to load project row: " + row);
            }
        }
    }
        /**
     * Checks whether the updated project CSV file contains any data.
     *
     * @return true if the updated file exists and contains rows after the header;
     *         false if it doesn't exist, is unreadable, or contains only a header.
     */
    public static boolean updatedProjectFileHasData() {
        File updatedFile = new File("data/ProjectUpdatedList.csv");
    
        // Check if the file exists
        if (!updatedFile.exists()) {
            return false; // If the file doesn't exist, return false (load from original file)
        }
    
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(updatedFile))) {
    
            String line;
            // Read the header
            line = reader.readLine(); // Skip the header
    
            // If the file has only an empty header (no data after), return false
            if (line == null || line.trim().isEmpty()) {
                return false; // No header or no data, load from the original file
            }
    
            // Read the next line (the first data line after the header)
            line = reader.readLine();
    
            // If there's any data after the header, return true
            if (line != null && !line.trim().isEmpty()) {
                return true;
            }
    
            // If there's no data, but the header exists, return true (since we want to load the updated file)
            return true;
    
        } catch (IOException e) {
            return false;  // In case of an error, return false (e.g., file not readable)
        }
    }
    
    
}
