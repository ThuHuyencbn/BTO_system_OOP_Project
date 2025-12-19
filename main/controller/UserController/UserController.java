package main.controller.UserController;

import java.util.ArrayList;
import java.util.List;
import main.entity.User.*;

/**
 * This class is responsible for managing user accounts.
 * It provides utilities such as checking for existing user IDs and retrieving all users.
 */
public class UserController {

    /** A list of all users (Applicants, Officers, and Managers) in the system. */
    private static List<User> users = new ArrayList<>();

    /**
     * Checks if a user ID (NRIC) already exists in the system across all user types.
     *
     * @param userId The user ID (NRIC) to check.
     * @return {@code true} if the ID is already taken; {@code false} otherwise.
     */
    public static boolean isUserIdTaken(String userId) {
        return !(ApplicantController.getUserById(userId) == null &&
                 ManagerController.getUserById(userId) == null &&
                 OfficerController.getUserById(userId) == null);
    }

    /**
     * Returns the list of all users in the system.
     * This list includes applicants, officers, and managers.
     *
     * @return A list of {@code User} objects.
     */
    public static List<User> getListUser() {
        return users;
    }
}
