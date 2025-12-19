package main.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import main.entity.Enum.FlatType;
import main.entity.User.HDBManager;
import main.entity.User.HDBOfficer;

/**
 * Represents a public housing project managed by HDB.
 * Contains details such as project name, available flat types, units, application dates,
 * assigned officers, and manager in charge.
 */
public class Project {
    private String projectName;
    private String neighborhood;
    private List<FlatType> flatTypes;
    private List<Integer> unitsAvailable;
    private Date applicationOpeningDate;
    private Date applicationClosingDate;
    private HDBManager managerInCharge;
    private List<Integer> sellingPrice;
    private int officerSlot;
    private List<HDBOfficer> assignedOfficers;
    private boolean isVisible;

    /**
     * Constructs a Project object with the specified attributes.
     */
    public Project(String projectName, String neighborhood, List<FlatType> flatTypes,
                   List<Integer> unitsAvailable, Date applicationOpeningDate,
                   Date applicationClosingDate, List<Integer> sellingPrice, HDBManager managerInCharge, int officerSlot,
                   List<HDBOfficer> assignedOfficers, boolean isVisible) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.flatTypes = new ArrayList<>(flatTypes);
        this.unitsAvailable = new ArrayList<>(unitsAvailable);
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.sellingPrice = sellingPrice;
        this.managerInCharge = managerInCharge;
        this.officerSlot = officerSlot;
        this.assignedOfficers = new ArrayList<>(assignedOfficers);
        this.isVisible = isVisible;
    }

    public int getOfficerSlot(){
        return officerSlot;
    }

    public void setOfficerSlot(int officerSlot){
        this.officerSlot = officerSlot;
    }

    public List<Integer> getSellingPrice(){
        return sellingPrice;
    }

    public void setSellingPrice(List<Integer> sellingPrice){
        this.sellingPrice = sellingPrice;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public List<FlatType> getFlatTypes() {
        return new ArrayList<>(flatTypes);
    }

    public void setFlatTypes(List<FlatType> flatTypes) {
        this.flatTypes = new ArrayList<>(flatTypes);
    }

    public List<Integer> getUnitsAvailable() {
        return new ArrayList<>(unitsAvailable);
    }

    public void setUnitsAvailable(List<Integer> unitsAvailable) {
        this.unitsAvailable = new ArrayList<>(unitsAvailable);
    }

    public Date getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public void setApplicationOpeningDate(Date applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    public Date getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(Date applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    /**
     * Checks if the project falls within the given date range.
     * 
     * @param startDateStr the start date in yyyy-MM-dd format
     * @param endDateStr   the end date in yyyy-MM-dd format
     * @return true if the project is open for application during the period
     */
    public boolean isInApplicationPeriod(String startDateStr, String endDateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            return !(applicationClosingDate.before(startDate) || applicationOpeningDate.after(endDate));
        } catch (ParseException e) {
            System.out.println("Date parsing error: " + e.getMessage());
            return false;
        }
    }

    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    public void setManagerInCharge(HDBManager managerInCharge) {
        this.managerInCharge = managerInCharge;
    }

    public List<HDBOfficer> getAssignedOfficers() {
        return assignedOfficers;
    }

    public void addAssignedOfficers(HDBOfficer officer) {
        getAssignedOfficers().add(officer);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * Returns a string representation of the project details.
     */
    @Override
    public String toString() {
        String officers = assignedOfficers.stream()
            .map(HDBOfficer::getName)
            .collect(Collectors.joining(", "));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return
            """
            Project Details:
              Name: """               + projectName                                + "\n" +
            "  Neighborhood: "       + neighborhood                               + "\n" +
            "  Flat Types: "         + flatTypes                                  + "\n" +
            "  Units Available: "    + unitsAvailable                             + "\n" +
            "  Opening Date: "       + sdf.format(applicationOpeningDate)        + "\n" +
            "  Closing Date: "       + sdf.format(applicationClosingDate)        + "\n" +
            "  Selling Price: "      + sellingPrice                               + "\n" +
            "  Manager In Charge: "  + managerInCharge.getName()                  + "\n" +
            "  Assigned Officers: "  + officers;
    }
}