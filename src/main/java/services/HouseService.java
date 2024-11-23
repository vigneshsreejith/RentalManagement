package services;

import database.DatabaseConnection;
import models.House;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                        rs.getString("name"),  // Fetch `name`
                        rs.getString("address"),
                        rs.getDouble("rentPrice"),
                        rs.getBoolean("isRented")
                ));
            }
        }
        return houses;
    }

    public void addHouse(House house) throws SQLException {
        String query = "INSERT INTO houses (name, address, rentPrice, isRented) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, house.getName());  // Insert `name`
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
            pstmt.setString(1, house.getName());  // Update `name`
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
}
