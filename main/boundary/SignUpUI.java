package main.boundary;

import java.util.Scanner;
import main.boundary.Interface.StartInterface;
import main.controller.UserController.*;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.utility.IsValid;

/**
 * The {@code SignUpUI} class handles the user interface for signing up as a new applicant.
 * It implements the {@code StartInterface} and is responsible for gathering user input,
 * validating the data, and registering the new applicant.
 */
public class SignUpUI implements StartInterface {

    /** Scanner for reading user input. */
    private Scanner sc = new Scanner(System.in);

    /**
     * Starts the sign-up process for a new applicant.
     * Collects NRIC, name, age, and marital status from the user.
     * Validates each input and creates the applicant using {@code ApplicantController}.
     */
    @Override
    public void start() {
        System.out.println("\n|---- Signing Up as a new Applicant ----|");

        Role role = null;
        String userId = "";

        // Prompt user for NRIC and validate
        while (true) {
            System.out.print("Enter NRIC: ");
            userId = sc.nextLine().toUpperCase();
            if (UserController.isUserIdTaken(userId)) {
                System.out.println("This NRIC is already taken.");
            } else if (!IsValid.isValidNric(userId)) {
                System.out.println("This NRIC is not in valid format!");
            } else {
                break;
            }
        }

        String name = null;
        String agedummy = null;

        // Prompt for valid name
        while (true) {
            System.out.print("Enter Name: ");
            name = sc.nextLine();
            if (IsValid.isValidName(name)) {
                break;
            } else {
                System.out.println("Invalid Name format");
            }
        }

        // Prompt for valid age
        while (true) {
            System.out.print("Enter Age: ");
            agedummy = sc.nextLine();
            if (IsValid.isValidAge(agedummy)) {
                break;
            } else {
                System.out.println("Invalid age format");
            }
        }

        int age = Integer.parseInt(agedummy);

        // Prompt for valid marital status
        MaritalStatus status = null;
        while (true) {
            System.out.print("Enter Marital Status (Married/Single): ");
            String input = sc.nextLine().toUpperCase();
            switch (input) {
                case "MARRIED":
                    status = MaritalStatus.MARRIED;
                    break;
                case "SINGLE":
                    status = MaritalStatus.SINGLE;
                    break;
                default:
                    System.out.println("Invalid input.");
                    continue;
            }
            break;
        }

        // Register the applicant with default password "password"
        ApplicantController.addApplicant(userId, "password", name, role, status, age);

        System.out.println("User created successfully!");
    }

}
