package controllers;

import models.MaintenanceRequest;
import services.MaintenanceService;

import java.sql.SQLException;
import java.util.List;

public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController() throws SQLException {
        this.maintenanceService = new MaintenanceService();
    }

    public List<MaintenanceRequest> getAllRequests() {
        return maintenanceService.getAllRequests();
    }

    public void addRequest(int houseId, String description, String status) {
        maintenanceService.addRequest(houseId, description, status);
    }

    public void updateRequestStatus(int requestId, String status) {
        maintenanceService.updateRequestStatus(requestId, status);
    }
}
