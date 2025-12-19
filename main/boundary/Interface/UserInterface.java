package main.boundary.Interface;

/**
 * Interface for user-related actions in the boundary layer.
 * Extends the {@link StartInterface} to include password management functionality.
 */
public interface UserInterface extends StartInterface {

    /**
     * Allows the user to change their password.
     */
    void changePassword();
}
