package models;

public class House {
    private int id;
    private String name;       // Added field
    private String address;
    private double rentPrice;
    private boolean isRented;
    private boolean isInterested; // Added field for "I am interested"

    public House(int id, String name, String address, double rentPrice, boolean isRented, boolean isInterested) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rentPrice = rentPrice;
        this.isRented = isRented;
        this.isInterested = isInterested;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public boolean isInterested() {
        return isInterested;
    }

    public void setInterested(boolean interested) {
        isInterested = interested;
    }
}
