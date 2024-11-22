package models;

public class MaintenanceRequest {
    private int id;
    private int houseId;
    private String description;
    private String status;

    public MaintenanceRequest(int id, int houseId, String description, String status) {
        this.id = id;
        this.houseId = houseId;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// Getters and setters
}
