package main.entity.User;

import java.util.ArrayList;
import java.util.List;
import main.entity.Application;
import main.entity.Enquiry;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.FlatBooking;


/**
 * Represent an Applicant in the project
 * extends {@link User} class
 */
public class Applicant extends User{
    protected  Application application = null;
    protected  List<Enquiry> enquiries = new ArrayList<>();
    protected  FlatBooking flatBooking = null;
    
    /**
     * Default constructor for creating an Appllicant with no attributes */
    public Applicant(){
        super();
        this.application = null;
        this.enquiries = new ArrayList<>();
        this.flatBooking = null;
    }

    /**
     * Constructor for creating an Applicar with specific attributes
     * @param userId The User ID of the Applicant
     * @param password The password of the Applicant
     * @param name The name of the Applicant
     * @param role The role of the Applicant (must be {@link Role#Applicant})
     * @param maritalStatus The marital status of the Applicant
     * @param age The age of the Applicant
     */
    public Applicant(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age){
        super(userId, password, name, role, maritalStatus, age);
    }

    /**
     * Constructor for creating an Applicar with specific attributes
     * @param userId The User ID of the Applicant
     * @param password The password of the Applicant
     * @param name The name of the Applicant
     * @param role The role of the Applicant (must be {@link Role#Applicant})
     * @param maritalStatus The marital status of the Applicant
     * @param age The age of the Applicant
     * @param application The application shwoing the project that Applicant applied for
     * @param enquiries The list of enquiries that the Applicant submitted
     */
    public Applicant(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age, Application application, List<Enquiry> enquiries, FlatBooking flatBooking){
        super(userId, password, name, role, maritalStatus, age);
        this.application = application;
        this.enquiries = (enquiries != null) ? enquiries : new ArrayList<>();
        this.flatBooking = flatBooking;
    }

    //Getter Methods()

    /**
     * Return the applied project of the Applicant
     * @return applied project
     */
    public Application getApplication(){
        return application;
    }

    /**
     * Return the list of enquiries of the Applicant
     * @return list of enquiries
     */
    public List<Enquiry> getEnquiries(){
        return enquiries;
    }

    public FlatBooking getFlatBooking(){
        return flatBooking;
    }
    //Setter Methods()
    /**
     * Update the applied project of the Applicant
     * @param applied project
     */
    public void setApplication(Application newApplication){
        this.application = newApplication;
    }

    public void setFlatBooking(FlatBooking flatBooking){
        this.flatBooking = flatBooking;
    }

    //Other Methods
    /**
     * Formatted string with Applicant information
     * Overrides the {@link User#userInfor()} method
     * @return a formatted string with Applicant information
     */
    @Override
    public String userInfo(){
        return String.format("[UserID = %s, Password = %s, Name = %s, Role = %s, Marital Status = %s, Age = %s, Project Applied = %s, Submitted Enquiries = %s]", 
        getUserId(), getPassword(), getName(), getRole().toString(), getMaritalStatus().toString(), getAge(), getApplication().toString(), getEnquiries().toString());
    }

    
}
