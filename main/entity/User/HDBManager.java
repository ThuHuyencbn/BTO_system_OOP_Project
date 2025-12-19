package main.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.Project;

/**
 * Represents an HDB Manager user who can create and manage HDB projects.
 * Extends the User class and includes functionality specific to project creation and management.
 */
public class HDBManager extends User {
    private List<Project> createdProjects;

    /**
     * Constructs a new HDBManager with the given parameters.
     *
     * @param nric           The NRIC of the manager.
     * @param password       The password for login.
     * @param name           The full name of the manager.
     * @param role           The role of the user (should be MANAGER).
     * @param maritalStatus  The marital status of the manager.
     * @param age            The age of the manager.
     */
    public HDBManager(String nric, String password, String name, Role role, MaritalStatus maritalStatus, int age) {
        super(nric, password, name, role, maritalStatus, age);
        this.createdProjects = new ArrayList<>();
    }

    /**
     * Returns the list of projects created by the manager.
     *
     * @return List of projects created.
     */
    public List<Project> getCreatedProjects() {
        return createdProjects;
    }

    /**
     * Adds a new project to the list of created projects.
     *
     * @param newProjects The new project to be added.
     */
    public void addCreatedProjects(Project newProjects) {
        this.createdProjects.add(newProjects);
    }

    /**
     * Removes a project from the list of created projects.
     *
     * @param project The project to remove.
     */
    public void removeProject(Project project) {
        createdProjects.remove(project);
    }

    /**
     * Checks if the manager is already handling any project during the specified application period.
     *
     * @param startDate The start of the new project period.
     * @param endDate   The end of the new project period.
     * @return true if there's an overlap with an existing project period, false otherwise.
     */
    public boolean isHandlingProjectDuringPeriod(Date startDate, Date endDate) {
        for (Project project : createdProjects) {
            Date existingStart = project.getApplicationOpeningDate();
            Date existingEnd = project.getApplicationClosingDate();

            boolean overlaps = !(endDate.before(existingStart) || startDate.after(existingEnd));
            if (overlaps) return true;
        }
        return false;
    }

    /**
     * Returns a string containing detailed user information for the manager.
     *
     * @return A formatted string with user details.
     */
    @Override
    public String userInfo(){
        return String.format("[UserID = %s, Password = %s, Name = %s, Role = %s, Marital Status = %s, Age = %s, Created Projects = %s]", 
        getUserId(), getPassword(), getName(), getRole().toString(), getMaritalStatus().toString(), getAge(), getCreatedProjects().toString());
    }
}


