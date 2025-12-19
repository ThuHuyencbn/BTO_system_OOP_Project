package main.boundary;

import java.util.Scanner;
import main.boundary.Interface.StartInterface;
import main.boundary.Interface.UserInterface;
import main.controller.UserController.ApplicantController;
import main.controller.UserController.ManagerController;
import main.controller.UserController.OfficerController;
import main.entity.User.Applicant;
import main.entity.User.HDBManager;
import main.entity.User.HDBOfficer;
import main.utility.IsValid;

/**
 * Boundary class responsible for handling user login via the terminal.
 * Supports authentication for Applicants, HDB Officers, and HDB Managers.
 * Implements the {@link StartInterface}.
 */
public class LoginUI implements StartInterface {

    /**
     * Scanner used for capturing user input.
     */
    private final Scanner sc;

    /**
     * Constructs a LoginUI and initializes the input scanner.
     */
    public LoginUI() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Starts the login process. Prompts the user for credentials,
     * validates them against existing users, and starts the appropriate user interface
     * if authentication is successful.
     */
    @Override
    public void start() {
        try {
            System.out.print("Enter user ID (NRIC): ");
            String userId = sc.nextLine().toUpperCase();
            if(!IsValid.isValidNric(userId)){
                System.out.println("The user ID is not in valid format.");
                return;
            }
    
            System.out.print("Enter password: ");
            String password = sc.nextLine();
    
            UserInterface userUI = null;
    
            HDBOfficer officer = OfficerController.getUserById(userId);
            if (officer != null && officer.getPassword().equals(password)) {

                System.out.println("Login successful as Officer!");
                userUI = new OfficerUI(officer);
            }
    
            Applicant applicant = ApplicantController.getUserById(userId);
            if (applicant != null && applicant.getPassword().equals(password)) {

                System.out.println("Login successful as Applicant!");
                userUI = new ApplicantUI(applicant);
            }
    
            HDBManager manager = ManagerController.getUserById(userId);
            if (manager != null && manager.getPassword().equals(password)) {
                System.out.println("Login successful as Manager!");
                userUI = new ManagerUI(manager);
            }
    
            if (userUI != null) {
                userUI.start();
            } else {
                System.out.println("Invalid credentials! Please try again.");
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    

    




}
