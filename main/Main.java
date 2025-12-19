package main;

import main.boundary.Interface.StartInterface;
import main.boundary.LoginUI;
import main.boundary.MenuUI;
import main.boundary.SignUpUI;
/**
 * The entry point of the HDB BTO Management System application.
 * This class initializes the system, loads necessary data,
 * and launches the main menu for user interaction.
 */
public class Main {
        /**
     * The main method initializes the application and displays the main menu.
     *
     * @param args command-line arguments (not used in this application)
     */

    public static void main(String[] args) {
        // Initialize the system (App initialization, loading CSV data, etc.)
        Initialize app = new Initialize();
        app.initialize();
        StartInterface login = new LoginUI();
        StartInterface signup = new SignUpUI();
        // Start the Menu UI to let the user interact with the system
        MenuUI menuUI = new MenuUI();
        menuUI.displayMainMenu(signup, login);
    }
}
