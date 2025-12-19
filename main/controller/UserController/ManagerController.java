package main.controller.UserController;

import java.util.ArrayList;
import java.util.List;
import main.entity.Enum.MaritalStatus;
import main.entity.Enum.Role;
import main.entity.User.HDBManager;
import main.entity.User.User;
import main.repository.ManagerRepository;

/**
 * The {@code ManagerController} class manages actions related to {@code HDBManager} users.
 * It handles user registration, lookup, deletion, and password updates.
 */
public class ManagerController {

    /** A list of all registered HDB managers. */
    private static List<HDBManager> managers = new ArrayList<>();

    /**
     * Retrieves a manager by their unique user ID (NRIC).
     *
     * @param userId The user ID (NRIC) to search for.
     * @return The {@code HDBManager} object if found; otherwise {@code null}.
     */
    public static HDBManager getUserById(String userId) {
        for (HDBManager manager : managers) {
            if (manager.getUserId().equals(userId)) {
                return manager;
            }
        }
        return null;
    }

    /**
     * Retrieves a manager by their name.
     *
     * @param userName The full name of the manager.
     * @return The {@code HDBManager} object if found; otherwise {@code null}.
     */
    public static HDBManager getUserByName(String userName) {
        for (HDBManager manager : managers) {
            if (manager.getName().equals(userName)) {
                return manager;
            }
        }
        return null;
    }

    /**
     * Returns the list of all HDB managers in the system.
     *
     * @return A list of {@code HDBManager} objects.
     */
    public static List<HDBManager> getManagerList() {
        return managers;
    }

    /**
     * Adds a new manager to the system and saves to the CSV repository.
     *
     * @param userId         The manager's NRIC.
     * @param password       The manager's password.
     * @param name           The full name of the manager.
     * @param role           The user role.
     * @param maritalStatus  The marital status of the manager.
     * @param age            The age of the manager.
     */
    public static void addManager(String userId, String password, String name, Role role, MaritalStatus maritalStatus, int age) {
        User manager = new HDBManager(userId, password, name, role, maritalStatus, age);
        managers.add((HDBManager) manager);
        UserController.getListUser().add(manager);
        ManagerRepository.saveManagersToCSV();
    }

    /**
     * Deletes a manager by user ID. Removes the manager from the internal list and updates the CSV.
     *
     * @param userId The user ID (NRIC) of the manager to delete.
     */
    public void deleteManager(String userId) {
        HDBManager manager = getUserById(userId);
        if (manager != null) {
            managers.remove(manager);
            UserController.getListUser().remove(manager);
            ManagerRepository.saveManagersToCSV();
        } else {
            System.out.println("User not found or is not a Manager.");
        }
    }

    /**
     * Changes the password for a given manager and persists the change.
     *
     * @param currentManager The manager whose password needs to be changed.
     * @param password       The new password.
     */
    public static void changePassword(HDBManager currentManager, String password) {
        try {
            currentManager.setPassword(password);
            ManagerRepository.saveManagersToCSV();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

}
