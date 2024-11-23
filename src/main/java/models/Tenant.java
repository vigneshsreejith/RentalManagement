package models;

public class Tenant extends User {
    public Tenant(String username, String password) {
        super(username, password, "tenant");
    }
}