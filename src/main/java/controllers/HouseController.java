package controllers;

import models.House;
import services.HouseService;

import java.sql.SQLException;
import java.util.List;

public class HouseController {
    private HouseService houseService;

    public HouseController() {
        this.houseService = new HouseService();
    }

    public List<House> getAllHouses() {
        try {
            return houseService.getAllHouses();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addHouse(String name, String address, double rentPrice, boolean isRented) {
        try {
            if (houseService.isNameUnique(name)) {
                House house = new House(0, name, address, rentPrice, isRented);
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

    public String updateHouse(int id, String name, String address, double rentPrice, boolean isRented) {
        try {
            if (houseService.isNameUniqueForUpdate(id, name)) {
                House house = new House(id, name, address, rentPrice, isRented);
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

    public void deleteHouse(int houseId) {
        try {
            houseService.deleteHouse(houseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
