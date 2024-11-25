package services;

import database.DatabaseConnection;
import models.House;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HouseService {
    public List<House> getAllHouses() throws SQLException {
        List<House> houses = new ArrayList<>();
        String query = "SELECT * FROM houses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                houses.add(new House(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDouble("rentPrice"),
                        rs.getBoolean("isRented"),
                        rs.getBoolean("isInterested")


                ));
            }
        }
        return houses;
    }

    public void addHouse(House house) throws SQLException {
        String query = "INSERT INTO houses (name, address, rentPrice, isRented) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, house.getName());
            pstmt.setString(2, house.getAddress());
            pstmt.setDouble(3, house.getRentPrice());
            pstmt.setBoolean(4, house.isRented());
            pstmt.executeUpdate();
        }
    }

    public void updateHouse(House house) throws SQLException {
        String query = "UPDATE houses SET name = ?, address = ?, rentPrice = ?, isRented = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, house.getName());
            pstmt.setString(2, house.getAddress());
            pstmt.setDouble(3, house.getRentPrice());
            pstmt.setBoolean(4, house.isRented());
            pstmt.setInt(5, house.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteHouse(int houseId) throws SQLException {
        String query = "DELETE FROM houses WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, houseId);
            pstmt.executeUpdate();
        }
    }

    public boolean isNameUnique(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM houses WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // True if no house exists with the same name
            }
        }
        return false;
    }

    public boolean isNameUniqueForUpdate(int id, String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM houses WHERE name = ? AND id != ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // True if no house with the same name but different ID exists
            }
        }
        return false;
    }

    // Mark a house as interested
    public void markInterest(int houseId, String tenantId) throws SQLException {
        String getTenantIdsQuery = "SELECT tenant_ids FROM houses WHERE id = ?";
        String updateQuery = "UPDATE houses SET isInterested = TRUE, tenant_ids = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement getStmt = conn.prepareStatement(getTenantIdsQuery)) {

            // Retrieve existing tenantIds
            getStmt.setInt(1, houseId);
            try (ResultSet rs = getStmt.executeQuery()) {
                if (rs.next()) {
                    String tenantIdsString = rs.getString("tenant_ids");
                    Set<String> tenantIdsSet = getStringSet(tenantId, tenantIdsString);

                    // Convert the set back to a comma-separated string
                    String updatedTenantIds = String.join(",", tenantIdsSet);

                    // Now update the house record with the new tenant_ids and set isInterested to TRUE
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, updatedTenantIds);
                        updateStmt.setInt(2, houseId);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Handle case where house with houseId does not exist
                    System.out.println("House with ID " + houseId + " not found.");
                }
            }
        }
}

    private static Set<String> getStringSet(String tenantId, String tenantIdsString) {
        Set<String> tenantIdsSet = new HashSet<>();

        // If tenantIds is not empty, split the string into a Set
        if (tenantIdsString != null && !tenantIdsString.isEmpty()) {
            String[] tenantIdsArray = tenantIdsString.split(",");
            for (String tenant : tenantIdsArray) {
                tenantIdsSet.add(tenant.trim());  // Add each tenantId to the set (duplicate values will be discarded)
            }
        }

        // Add the new tenantId only if it doesn't exist in the set
        tenantIdsSet.add(tenantId);
        return tenantIdsSet;
    }
}
