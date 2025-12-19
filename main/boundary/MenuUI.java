package main.boundary;

import java.util.Scanner;
import main.boundary.Interface.StartInterface;

/**
 * UI class that displays the main menu of the BTO Management System.
 * Provides options for signing up, logging in, or exiting the application.
 */
public class MenuUI {

    /**
     * Scanner instance used for capturing user input from the terminal.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the main menu and handles user input to navigate
     * to the sign-up or login flow. Accepts functional arguments that implement {@link StartInterface}.
     *
     * @param signUpFlow an implementation of StartInterface for handling sign-up logic
     * @param loginFlow an implementation of StartInterface for handling login logic
     */
    public void displayMainMenu(StartInterface signUpFlow, StartInterface loginFlow) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("====== Welcome to the BTO Management System ======");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    signUpFlow.start();
                    break;
                case "2":
                    loginFlow.start();
                    break;
                case "3":
                    System.out.println("Exiting system... Goodbye!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
