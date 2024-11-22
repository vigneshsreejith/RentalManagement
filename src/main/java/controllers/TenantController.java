package controllers;

import models.Tenant;
import services.TenantService;

import java.sql.SQLException;
import java.util.List;

public class TenantController {
    private final TenantService tenantService;

    public TenantController() throws SQLException {
        this.tenantService = new TenantService();
    }

    public List<Tenant> getAllTenants() {
        return tenantService.getAllTenants();
    }

    public void addTenant(String name, String contactInfo) {
        tenantService.addTenant(name, contactInfo);
    }

    public void deleteTenant(int tenantId) {
        tenantService.deleteTenant(tenantId);
    }
}
