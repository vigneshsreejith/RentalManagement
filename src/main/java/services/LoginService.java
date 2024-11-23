package services;

import models.Landlord;
import models.Tenant;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private final Map<String, String> landlordCredentials;
    private final Map<String, String> tenantCredentials;

    public LoginService() {
        landlordCredentials = new HashMap<>();
        tenantCredentials = new HashMap<>();

        // Sample data for landlords and tenants
        landlordCredentials.put("landlord1", "password1");
        landlordCredentials.put("landlord2", "password2");

        tenantCredentials.put("tenant1", "password1");
        tenantCredentials.put("tenant2", "password2");
    }

    public User authenticate(String username, String password) {
        if (landlordCredentials.containsKey(username) && landlordCredentials.get(username).equals(password)) {
            return new Landlord(username, password);
        } else if (tenantCredentials.containsKey(username) && tenantCredentials.get(username).equals(password)) {
            return new Tenant(username, password);
        } else {
            return null;  // Invalid credentials
        }
    }
}
