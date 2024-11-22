package models;

public class House {
    private int id;
    private String address;
    private double rentPrice;
    private boolean isRented;

    public House(int id, String address, double rentPrice, boolean isRented) {
        this.id = id;
        this.address = address;
        this.rentPrice = rentPrice;
        this.isRented = isRented;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
// Getters and setters
}
