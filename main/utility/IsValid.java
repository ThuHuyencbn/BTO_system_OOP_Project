package main.utility;

import java.util.Date;
import main.entity.User.HDBManager;
/**
 * Utility class for performing various input validations used in the BTO Management System.
 */
public class IsValid {
        /**
     * Validates an NRIC based on length and pattern.
     * The NRIC must start with 'S' or 'T', followed by 7 digits, and end with an alphabet.
     *
     * @param nric the NRIC string to validate
     * @return true if the NRIC format is valid, false otherwise
     */

    public static boolean isValidNric(String nric) {
        // Check if the NRIC has exactly 9 characters
        if (nric.length() != 9) {
            return false;
        }
        
        // Regular expression to match the pattern: S/T + 7 digits + letter
        String regex = "^[ST]\\d{7}[A-Za-z]$";
        
        // Check if the NRIC matches the regular expression
        return nric.matches(regex);
    }
    /**
     * Validates whether a password is strong.
     * A valid password must be at least 8 characters long and include at least one uppercase letter,
     * one lowercase letter, and one digit.
     *
     * @param password the password string to validate
     * @return true if the password meets the strength requirements, false otherwise
     */
    public static boolean isValidPassword(String password){
        if (password.length() < 8){
            return false;
        }
        // Regular expression to match the requirement One Caps, One lowercase, 1 Numeric, 8 characters
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        return password.matches(regex);
    }
    /**
     * Validates a name to ensure it contains only alphabets and optional single spaces between names.
     *
     * @param name the name string to validate
     * @return true if the name format is valid, false otherwise
     */

    public static boolean isValidName(String name){
        // Regular expression to match requirement of at least one alphabet
        String regex = "^[A-Za-z]+([\\s][A-Za-z]+)*$";
        return name.matches(regex);
        
        
    }
        /**
     * Validates whether the input string represents a valid age.
     * Valid ages are from 0 to 120.
     *
     * @param age the age string to validate
     * @return true if the input is a valid age, false otherwise
     */

    public static boolean isValidAge(String age) {
        String regex = "^(?:1[01]?[0-9]|120|[1-9]?[0-9])$";
        return age.matches(regex);
    }
    
    /**
     * Checks if the manager is eligible to create a new project based on application periods.
     * A new project can only be created if its application opening date is after the closing date
     * of the last project created by the same manager.
     *
     * @param manager the HDBManager attempting to create a new project
     * @param openingDate the proposed opening date of the new project
     * @return true if the manager is allowed to create the project, false otherwise
     */
    public static boolean isValidToCreateProject(HDBManager manager, Date openingDate){
        if (!manager.getCreatedProjects().isEmpty()){
            if(manager.getCreatedProjects().get(manager.getCreatedProjects().size() -1).getApplicationClosingDate().after(openingDate)){
                return false;
            }
        }
        return true;
    }
    
}
