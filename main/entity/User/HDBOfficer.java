package main.entity.User;

import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.Project;
import main.entity.Registration;

/**
 * Represent an Officer in the project
 * extends {@link User} class
 */
public class HDBOfficer extends Applicant {
    private Project assignedProject;
    private Registration registration;

    /**
     * Default constructor for creating an Officer with no attributes
     */
    public HDBOfficer(){
        super();
        this.assignedProject = null;
        this.registration = null;
    }

    /**
     * Constructor for creating an Officer with specific attributes
     * @param userId The User ID of the Officer
     * @param password The password of the Officer
     * @param name The name of the Officer
     * @param role The role of the Officer (must be {@link Role#Officer})
     * @param maritalStatus The marital status of the Officer
     * @param age The age of the Officer
     * @param assignedProject The project that the Officer is handling
     * @param Project The project that the Officer register to handle
     * @param Registration The registration that the officer submit to handle a project
    */

    public HDBOfficer(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age, Project assignedProject, Registration registration){
        super(userId, password, name, role, maritalStatus, age);
        this.assignedProject = assignedProject;
        this.registration = registration;
    }

    //Getter Methods()

    /**
     * Return the assigned project of the Officer
     * @return assigned project
     */
    public Project getAssignedProject(){
        return assignedProject;
    }

    /**
     * Return the project registration of the Officer 
     * @return registration
     */
    public Registration getRegistration(){
        return registration;
    }
    
    //Setter Methods

    /**
     * Update the assigned project of the Officer
     * @param assigned project
     */
    public void setAssignedProject(Project project){
        this.assignedProject = project;
    }

    /**
     * Update the registration of the Officer
     * @param registration
     */
    public void setRegistration(Registration registration){
        this.registration = registration;
    }


    //Other Methods
    /**
     * Formatted string with Officer information
     * Overrides the {@link User#userInfor()} method
     * @return a formatted string with Officer information
     */
    //Overload
    @Override
    public String userInfo(){
        return String.format("[UserID = %s, Password = %s, Name = %s, Role = %s, Marital Status = %s, Age = %s, Project Handling = %s, Registration = %s]", 
        getUserId(), getPassword(), getName(), getRole(), getMaritalStatus(), getAge(), assignedProject.toString(), registration.toString());
    }
}
