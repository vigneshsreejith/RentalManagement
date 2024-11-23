package models;
public abstract class User {
    private String username;
    private String password;
    private String role; // "tenant" or "landlord"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isLandlord() {
        return role.equals("landlord");
    }

    public boolean isTenant() {
        return role.equals("tenant");
    }
}

// Landlord.java (unchanged)


// Tenant.java (unchanged)

