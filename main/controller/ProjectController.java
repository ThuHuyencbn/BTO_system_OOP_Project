package main.controller;

import java.util.*;
import main.entity.Enum.FlatType;
import main.entity.Enum.MaritalStatus;
import main.entity.Project;
import main.entity.User.Applicant;
import main.entity.User.HDBManager;
import main.entity.User.HDBOfficer;
import main.repository.ProjectRepository;
import main.utility.Filter;

/**
 * Controller class to manage operations related to HDB projects.
 * Includes creating, editing, filtering, and retrieving project data.
 */
public class ProjectController {

    /** List of all projects in the system. */
    public static List<Project> projects = new ArrayList<>();

    /**
     * Creates a new project and adds it to the list of projects.
     *
     * @return the created Project object
     */
    public static Project createProject(String projectName, String neighborhood, List<FlatType> flatTypes, 
                                        List<Integer> units, Date openingDate, Date closingDate, 
                                        List<Integer> sellingPrice,
                                        HDBManager manager, int officerSlot, List<HDBOfficer> officers, boolean isVisible) {
        try {
            if (projectName == null || neighborhood == null || flatTypes == null || units == null ||
                officerSlot == 0 || openingDate == null || closingDate == null || manager == null || officers == null || sellingPrice == null) {
                throw new IllegalArgumentException("Invalid input: Fields cannot be null");
            }
            Project newProject = new Project(projectName, neighborhood, flatTypes, units, 
                                             openingDate, closingDate, sellingPrice, manager, officerSlot, officers, isVisible);
            projects.add(newProject);
            manager.getCreatedProjects().add(newProject);
            ProjectRepository.saveProjectsToCSV();
            return newProject;
        } catch (Exception e) {
            System.err.println("Error creating project: " + e.getMessage());
        }
        return null;
    }

    /**
     * Edits the details of an existing project.
     */
    public static void editProject(Project project, String newName, String newNeighborhood, 
                                   List<FlatType> newFlatTypes, List<Integer> newUnits, List<Integer> newSellingPrice,
                                   int newOfficerSlot, Date newOpeningDate, Date newClosingDate) {
        try {
            if (project == null || !projects.contains(project)) {
                throw new NoSuchElementException("Project not found");
            }
            project.setProjectName(newName);
            project.setNeighborhood(newNeighborhood);
            project.setFlatTypes(newFlatTypes);
            project.setUnitsAvailable(newUnits);
            project.setApplicationOpeningDate(newOpeningDate);
            project.setApplicationClosingDate(newClosingDate);
            project.setOfficerSlot(newOfficerSlot);
            project.setSellingPrice(newSellingPrice);
            ProjectRepository.saveProjectsToCSV();
        } catch (Exception e) {
            System.err.println("Error editing project: " + e.getMessage());
        }
    }

    /**
     * Deletes a project from the system.
     */
    public static void deleteProject(Project project) {
        try {
            if (project == null || !projects.contains(project)) {
                throw new NoSuchElementException("Project not found");
            }
            projects.remove(project);
            ProjectRepository.saveProjectsToCSV();
        } catch (Exception e) {
            System.err.println("Error deleting project: " + e.getMessage());
        }
    }

    /**
     * Adds an officer to a project's assigned officers and updates the slot count.
     */
    public static void addAssignedOfficers(Project project, HDBOfficer officer){
        project.getAssignedOfficers().add(officer);
        project.setOfficerSlot(project.getOfficerSlot() - 1);
        ProjectRepository.saveProjectsToCSV();
    }
    /**
     * Toggles the visibility of a project.
     */
    public static void toggleProjectVisibility(Project project, boolean isVisible) {
        try {
            if (project == null || !projects.contains(project)) {
                throw new NoSuchElementException("Project not found");
            }
            project.setVisible(isVisible);
            ProjectRepository.saveProjectsToCSV();
        } catch (Exception e) {
            System.err.println("Error toggling project visibility: " + e.getMessage());
        }
    }

    /**
     * Returns the project matching the specified name.
     */
    public static Project viewProject(String projectName) {
        try {
            for (Project project : projects) {
                if (project.getProjectName().equalsIgnoreCase(projectName)) {
                    return project;
                }
            }
            throw new NoSuchElementException("Project not found");
        } catch (Exception e) {
            System.err.println("Error viewing project: " + e.getMessage());
            return null;
        }
    }

    /**
     * Finds a project by name, case-insensitive.
     */
    public static Project findProjectByName(String projectName) {
        String input = projectName.trim().toLowerCase();
        for (Project project : projects) {
            if (project.getProjectName().trim().toLowerCase().equals(input)) {
                return project;
            }
        }
        return null;
    }

    /**
     * Displays officers assigned to a project.
     */
    public static void viewHandlingProject(Project project){
        System.out.println(project.getAssignedOfficers());
    }

    /**
     * Gets the remaining units for a specific flat type in a project.
     */
    public static int getRemainingUnitsForFlatType(Project project, FlatType flatType) {
        List<FlatType> flatTypes = project.getFlatTypes();
        List<Integer> unitsAvailable = project.getUnitsAvailable();
        int index = flatTypes.indexOf(flatType);
        return (index != -1) ? unitsAvailable.get(index) : 0;
    }

    /**
     * Returns a filtered list of available projects based on applicant eligibility and optional filters.
     */
    public static List<Project> getAvailableProjects(Applicant applicant, FlatType flatType, String location){
        List<Project> filteredProjects = new ArrayList<>();
        if(applicant.getMaritalStatus().equals(MaritalStatus.SINGLE) && applicant.getAge() >= 35){
            for(Project project: projects){
                if(project.isVisible() && project.getFlatTypes().contains(FlatType.TWO_ROOM)){
                    filteredProjects.add(project);
                }
            }
        } else {
            for(Project project: projects){
                if(project.isVisible()){
                    filteredProjects.add(project);
                }
            }
        }
        Filter filter = new Filter(location, flatType);
        return filter.applyFilters(filteredProjects);
    }

    /**
     * Views all projects based on filters and roles.
     */
    public static List<Project> viewAllProjectsWithFilters(HDBOfficer officer, HDBManager manager, FlatType flatType, String location) {
        Filter filter = new Filter(location, flatType);
        List<Project> filteredProjects = new ArrayList<>();
        List<Project> finalProjects = new ArrayList<>();
        if(manager == null && officer == null){
            filteredProjects = filter.applyFilters(projects);
        } else if(officer == null){
            for(Project project: projects){
                if(project.getManagerInCharge().equals(manager)){
                    filteredProjects.add(project);
                }
            }
            finalProjects = filter.applyFilters(filteredProjects);
        } else if(manager == null){
            for(Project project: projects){
                if(project.getAssignedOfficers().contains(officer)){
                    filteredProjects.add(project);
                }
            }
            finalProjects = filter.applyFilters(filteredProjects);
        }
        return finalProjects;
    }

    /**
     * Updates the number of available units when booking or cancelling.
     */
    public static void updateUnitRemaining(Project project, FlatType flatType, boolean increase){
        if(!increase){
            if (flatType.equals(FlatType.TWO_ROOM)){
                List<Integer> currentUnit = project.getUnitsAvailable();
                    if (currentUnit.size() > 1) {
                        project.setUnitsAvailable(List.of(currentUnit.get(0) - 1, currentUnit.get(1)));
                    } else {
                        project.setUnitsAvailable(List.of(currentUnit.get(0) - 1));
                    }
            }else{
                List<Integer> currentUnit = project.getUnitsAvailable();
                if(currentUnit.size() == 1){
                    project.setUnitsAvailable(List.of(currentUnit.get(0)-1));
                }else{
                    project.setUnitsAvailable(List.of(currentUnit.get(0), currentUnit.get(1)-1));
                }
            }
        }else{
            if (flatType.equals(FlatType.TWO_ROOM)){
                List<Integer> currentUnit = project.getUnitsAvailable();
                    if (currentUnit.size() > 1) {
                        project.setUnitsAvailable(List.of(currentUnit.get(0) + 1, currentUnit.get(1)));
                    } else {
                        project.setUnitsAvailable(List.of(currentUnit.get(0) + 1));
                    }
            }else{
                List<Integer> currentUnit = project.getUnitsAvailable();
                if(currentUnit.size() == 1){
                    project.setUnitsAvailable(List.of(currentUnit.get(0)+1));
                }else{
                    project.setUnitsAvailable(List.of(currentUnit.get(0), currentUnit.get(1)+1));
                }
            }
        }
        ProjectRepository.saveProjectsToCSV();
    }

}
