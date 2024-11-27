package services;

import models.House;

import java.sql.SQLException;
import java.util.List;

//interface / inheritence / polymorphism
public interface HouseOperations {
    List<House> getAllHouses(String currentUser) throws SQLException;

    void addHouse(House house) throws SQLException;

    void updateHouse(House house) throws SQLException;

    void deleteHouse(int houseId) throws SQLException;

    boolean isNameUnique(String name) throws SQLException;

    boolean isNameUniqueForUpdate(int id, String name) throws SQLException;

    void markInterest(int houseId, String tenantId) throws SQLException;

    void markNotInterest(int houseId, String tenantId) throws SQLException;

    List<String> getTenantIdsForHouse(int houseId) throws SQLException;

    void updateApprovedList(int houseId, String tenantId) throws SQLException;
}
