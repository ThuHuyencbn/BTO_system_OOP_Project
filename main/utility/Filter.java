package main.utility;

import java.util.List;
import java.util.stream.Collectors;
import main.controller.ProjectController;
import main.entity.Enum.FlatType;
import main.entity.Project;
/**
 * Utility class for filtering and sorting a list of {@link Project} objects.
 * Filters can be based on neighborhood (location) and available {@link FlatType}.
 * Sorting can be alphabetical or by the number of remaining flat units.
 */

public class Filter {
    private String location;
    private FlatType flatType;
    private String sortingMethod = "Alphabetical";  // Default sorting method
    /**
     * Constructs a Filter with specified location and flat type.
     *
     * @param location the neighborhood to filter by
     * @param flatType the flat type to filter by
     */
    public Filter(String location, FlatType flatType){
        this.location = location;
        this.flatType = flatType;
    }
    /**
     * Gets the neighborhood filter.
     *
     * @return the location filter
     */

    public String getLocation() {
        return location;
    }
    /**
     * Sets the neighborhood filter.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * Gets the flat type filter.
     *
     * @return the flat type filter
     */

    public FlatType getFlatType() {
        return flatType;
    }
    /**
     * Sets the flat type filter.
     *
     * @param flatType the flat type to set
     */

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType; // Ensure uppercase format
    }

    /**
     * Gets the sorting method.
     *
     * @return the sorting method used (Alphabetical or By Number of Units Remaining)
     */

    public String getSortingMethod() {
        return sortingMethod;
    }

    /**
     * Sets the sorting method.
     * Valid values: "Alphabetical" or "By Number of Units Remaining".
     *
     * @param sortingMethod the sorting method to use
     * @throws IllegalArgumentException if an unsupported sorting method is specified
     */
    public void setSortingMethod(String sortingMethod) {
        // Sorting method can be Alphabetical or By Number of Units Remaining
        if (sortingMethod.equalsIgnoreCase("Alphabetical") || sortingMethod.equalsIgnoreCase("By Number of Units Remaining")) {
            this.sortingMethod = sortingMethod;
        } else {
            throw new IllegalArgumentException("Invalid sorting method. Allowed values are Alphabetical or By Number of Units Remaining.");
        }
    }

    /**
     * Filters and sorts the given list of projects based on location, flat type, and sorting method.
     *
     * @param projects the list of projects to filter and sort
     * @return the filtered and sorted list of projects
     */

    public List<Project> applyFilters(List<Project> projects) {
        return projects.stream()
                .filter(project -> (location == null || project.getNeighborhood().equalsIgnoreCase(location)) &&
                                   (flatType == null || project.getFlatTypes().contains(flatType)))
                .sorted((p1, p2) -> {
                    // Sorting by alphabetical order or number of units remaining
                    if (sortingMethod.equalsIgnoreCase("Alphabetical")) {
                        return p1.getProjectName().compareToIgnoreCase(p2.getProjectName());
                    } else {
                        int remainingUnitsP1 = ProjectController.getRemainingUnitsForFlatType(p1, flatType);
                        int remainingUnitsP2 = ProjectController.getRemainingUnitsForFlatType(p2, flatType);
                        return Integer.compare(remainingUnitsP1, remainingUnitsP2);
                    }
                })
                .collect(Collectors.toList());
    }
}