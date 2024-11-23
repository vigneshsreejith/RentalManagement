package services;

import database.DatabaseConnection;
import models.MaintenanceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceService {
    private final Connection connection;

    public MaintenanceService() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<MaintenanceRequest> getAllRequests() {
        List<MaintenanceRequest> requests = new ArrayList<>();
        String query = "SELECT * FROM maintenance_requests";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                requests.add(new MaintenanceRequest(
                        resultSet.getInt("id"),
                        resultSet.getInt("houseId"),
                        resultSet.getString("description"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void addRequest(int houseId, String description, String status) {
        String query = "INSERT INTO maintenance_requests (houseId, description, status) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, houseId);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRequestStatus(int requestId, String status) {
        String query = "UPDATE maintenance_requests SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, requestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
