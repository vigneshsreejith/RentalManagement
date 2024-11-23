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

    public void addHouse(String name, String address, double rentPrice, boolean isRented) {
        try {
            House house = new House(0, name, address, rentPrice, isRented);
            houseService.addHouse(house);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHouse(int id, String name, String address, double rentPrice, boolean isRented) {
        try {
            House house = new House(id, name, address, rentPrice, isRented);
            houseService.updateHouse(house);
        } catch (SQLException e) {
            e.printStackTrace();
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
