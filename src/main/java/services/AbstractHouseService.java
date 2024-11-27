package services;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//abstract class
public abstract class AbstractHouseService implements HouseOperations {

    @Override
    public boolean isNameUnique(String name) throws SQLException {
        System.out.println("name unique in abstract house service");
        String query = "SELECT COUNT(*) FROM houses WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        }
    }

    @Override
    public boolean isNameUniqueForUpdate(int id, String name) throws SQLException {
        System.out.println("name unique for update in abstract house service");
        String query = "SELECT COUNT(*) FROM houses WHERE name = ? AND id != ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        }
    }
}
