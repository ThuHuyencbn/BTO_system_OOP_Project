package main.entity.User;

import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;

/**
 * Represent a user in the BTO system
 * Parent class of all users in the system
 */

public abstract class User{
    private String userId;
    private String password;
    private String name;
    private Role role;
    private MaritalStatus maritalStatus;
    private int age;

    /**
     * Default constructor for creating auser with no attributes
     * Initialize all attributes to default values
     */
    public User(){
        this(null, null, null, null, null, 0);
    }

    /**
     * Constructs a user with specific details
     * @param userId The User ID of the user
     * @param password The password of the user
     * @param name The name of the user
     * @param role The role of the user
     * @param age The age of the user
     */
    public User(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age){
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.role = role;
        this.maritalStatus = maritalStatus;
        this.age = age;
    }

    //Getter Methods()

    /**
     * Return  the user ID of the user
     * @return the user ID
     */
    public String getUserId(){
        return userId;
    }

    /**
     * Return  the password of the user
     * @return the password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Return  the name of the user
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Return  the role of the user
     * @return the role
     */
    public Role getRole(){
        return role;
    }

    /**
     * Return  the marital status of the user
     * @return the marital status
     */
    public MaritalStatus getMaritalStatus(){
        return maritalStatus;
    }

    /**
     * Return  the age of the user
     * @return the age
     */
    public int getAge(){
        return age;
    }

    //Setter Methods()

    /**
     * Update  the user ID of the user
     * @param the user ID
     */
    public void setUserId(String userId){
        userId = userId.trim();
        userId = userId.toUpperCase();
        this.userId = userId;
    }

    /**
     * Update the password of the user
     * @param password
     */
    public void setPassword(String password){
        this.password = password;
    }

    public void setAge(int age){
        this.age = age;
    }


    /**
     * Update the name of the user
     * @param name
     */
    public void setName(String name){
        name = name.trim();
        this.name = name; // Assign to instance variable
    }


    /**
     * Update  the role of the user
     * @param the role
     */
    public  void setRole(Role role){
        this.role = role;
    }

    /**
     * Update  the marital status of the user
     * @param the marital status
     */
    public void setMaritalStatus(MaritalStatus maritalStatus){
        this.maritalStatus = maritalStatus;
    }

    /**
     * Update  the age of the user
     * @param the age
     */
    public void getAge(int age){
        this.age = age;
    }

    //Other Methods()

    /**
     * Formatted string containing user information
     * @return A formatted string with user details
     */
    public abstract String userInfo();
        
    

}