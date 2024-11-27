package controllers;

import models.House;
import services.HouseService;
import models.User;
import models.Landlord;
import models.Tenant;

import java.sql.SQLException;
import java.util.List;

public class HouseController {
    private final HouseService houseService;
    private final User currentUser; // This will hold the current logged-in user

    // Constructor to initialize the HouseService and assign the current user
    public HouseController(HouseService houseService, User currentUser) {
        this.houseService = houseService;
        this.currentUser = currentUser;
    }

    // Get all houses (both tenants and landlords can view)
    public List<House> getAllHouses() {
        try {
            return houseService.getAllHouses(currentUser.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Add a house (only landlords can add houses)
    public String addHouse(String name, String address, double rentPrice, boolean isRented) {
        if (!(currentUser instanceof Landlord)) {
            return "Error: Only landlords can add houses.";
        }

        try {
            if (houseService.isNameUnique(name)) {
                House house = new House(0, name, address, rentPrice, isRented, false);
                houseService.addHouse(house);
                return "House added successfully.";
            } else {
                return "Error: House name must be unique.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to add house.";
        }
    }

    // Update a house (only landlords can update houses)
    public String updateHouse(int id, String name, String address, double rentPrice, boolean isRented) {
        if (!(currentUser instanceof Landlord)) {
            return "Error: Only landlords can update houses.";
        }

        try {
            if (houseService.isNameUniqueForUpdate(id, name)) {
                House house = new House(id, name, address, rentPrice, isRented, false);
                houseService.updateHouse(house);
                return "House updated successfully.";
            } else {
                return "Error: House name must be unique.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to update house.";
        }
    }

    // Delete a house (only landlords can delete houses)
    public String deleteHouse(int houseId) {
        if (!(currentUser instanceof Landlord)) {
            return "Error: Only landlords can delete houses.";
        }

        try {
            houseService.deleteHouse(houseId);
            return "House deleted successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to delete house.";
        }
    }

    // Mark a house as interested (only tenants can do this)
    public String markInterest(int houseId) {
        if (!(currentUser instanceof Tenant)) {
            return "Error: Only tenants can mark houses as interested.";
        }

        try {
            houseService.markInterest(houseId, currentUser.getUsername());
            return "House marked as interested.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to mark interest.";
        }
    }

    // Mark a house as interested (only tenants can do this)
    public String removeMarkInterest(int houseId) {
        if (!(currentUser instanceof Tenant)) {
            return "Error: Only tenants can mark houses as not interested.";
        }

        try {
            houseService.markNotInterest(houseId, currentUser.getUsername());
            return "House marked as not interested.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to remove mark interest.";
        }
    }
}
