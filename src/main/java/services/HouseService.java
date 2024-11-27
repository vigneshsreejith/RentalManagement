package services;

import database.DatabaseConnection;
import models.House;

import java.sql.*;
import java.util.*;

public class HouseService {
    public List<House> getAllHouses(String currentUser) throws SQLException {
        List<House> houses = new ArrayList<>();
        String query = "SELECT * FROM houses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String tenantIdsString = rs.getString("tenant_ids");
                boolean isInterested = false;
                if (tenantIdsString != null && !tenantIdsString.isEmpty()) {
                    // Convert the array to a Set for fast lookups
                    Set<String> tenantIdsSet = new HashSet<>(Arrays.asList(tenantIdsString.split(",")));

                    // Check if tenantName exists in the set
                    isInterested = tenantIdsSet.contains(currentUser.trim());
                }
                houses.add(new House(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDouble("rentPrice"),
                        rs.getBoolean("isRented"),
                        isInterested
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
             PreparedStatement getStmt = conn.prepareStatement(getTenantIdsQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            // Step 1: Retrieve existing tenant_ids
            getStmt.setInt(1, houseId);
            try (ResultSet rs = getStmt.executeQuery()) {
                LinkedHashSet<String> tenantIdsSet = new LinkedHashSet<>();
                if (rs.next()) {
                    String tenantIdsString = rs.getString("tenant_ids");

                    // Step 2: Parse tenant IDs into a LinkedHashSet to preserve order
                    if (tenantIdsString != null && !tenantIdsString.isEmpty()) {
                        String[] tenantIdsArray = tenantIdsString.split(",");
                        tenantIdsSet.addAll(Arrays.asList(tenantIdsArray));
                    }

                    // Step 3: Add the new tenant ID if not already present
                    tenantIdsSet.add(tenantId);
                }

                // Step 4: Update the database with the ordered tenant IDs
                String updatedTenantIds = String.join(",", tenantIdsSet);
                updateStmt.setString(1, updatedTenantIds);
                updateStmt.setInt(2, houseId);
                updateStmt.executeUpdate();
            }
        }
    }

    public void markNotInterest(int houseId, String tenantId) throws SQLException {
        String getTenantIdsQuery = "SELECT tenant_ids FROM houses WHERE id = ?";
        String updateQuery = "UPDATE houses SET isInterested = TRUE, tenant_ids = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement getStmt = conn.prepareStatement(getTenantIdsQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            // Step 1: Retrieve existing tenant_ids
            getStmt.setInt(1, houseId);
            try (ResultSet rs = getStmt.executeQuery()) {
                LinkedHashSet<String> tenantIdsSet = new LinkedHashSet<>();
                if (rs.next()) {
                    String tenantIdsString = rs.getString("tenant_ids");

                    // Step 2: Parse tenant IDs into a LinkedHashSet to preserve order
                    if (tenantIdsString != null && !tenantIdsString.isEmpty()) {
                        String[] tenantIdsArray = tenantIdsString.split(",");
                        tenantIdsSet.addAll(Arrays.asList(tenantIdsArray));
                    }

                    // Step 3: remove the new tenant ID if not already present
                    System.out.println("Removed tenant" + tenantId);
                    tenantIdsSet.remove(tenantId);
                }

                // Step 4: Update the database with the ordered tenant IDs
                String updatedTenantIds = String.join(",", tenantIdsSet);
                updateStmt.setString(1, updatedTenantIds);
                updateStmt.setInt(2, houseId);
                updateStmt.executeUpdate();
            }
        }
    }

    public List<String> getTenantIdsForHouse(int houseId) throws SQLException {
        String query = "SELECT tenant_ids FROM houses WHERE id = ?";
        try ( Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, houseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tenantIds = rs.getString("tenant_ids");
                System.out.println(tenantIds);
                if(tenantIds != null && !tenantIds.isEmpty()){
                    return Arrays.asList(tenantIds.split(","));
                }
            }
        }
        return new ArrayList<>();
    }

    public void updateApprovedList(int houseId, String tenantId) throws SQLException {
        String getApprovedListQuery = "SELECT approved_list FROM houses WHERE id = ?";
        String updateQuery = "UPDATE houses SET approved_list = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement getStmt = conn.prepareStatement(getApprovedListQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            // Step 1: Retrieve existing tenant_ids
            getStmt.setInt(1, houseId);
            try (ResultSet rs = getStmt.executeQuery()) {
                LinkedHashSet<String> approvedListSet = new LinkedHashSet<>();
                if (rs.next()) {
                    String approvedListString = rs.getString("approved_list");

                    // Step 2: Parse tenant IDs into a LinkedHashSet to preserve order
                    if (approvedListString != null && !approvedListString.isEmpty()) {
                        String[] approvalListArray = approvedListString.split(",");
                        approvedListSet.addAll(Arrays.asList(approvalListArray));
                    }

                    // Step 3: Add the new tenant ID if not already present
                    approvedListSet.add(tenantId);
                }

                // Step 4: Update the database with the ordered tenant IDs
                String updatedApprovedList = String.join(",", approvedListSet);
                updateStmt.setString(1, updatedApprovedList);
                updateStmt.setInt(2, houseId);
                updateStmt.executeUpdate();
                markNotInterest(houseId, tenantId);
            }
        }
    }
}
