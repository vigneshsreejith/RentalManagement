package models;

public class Landlord extends User {
    public Landlord(String username, String password) {
        super(username, password, "landlord");
    }
}
