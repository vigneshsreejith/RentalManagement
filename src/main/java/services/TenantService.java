//package services;
//
//import database.DatabaseConnection;
//import models.Tenant;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TenantService {
//    private final Connection connection;
//
//    public TenantService() throws SQLException {
//        this.connection = DatabaseConnection.getConnection();
//    }
//
//    public List<Tenant> getAllTenants() {
//        List<Tenant> tenants = new ArrayList<>();
//        String query = "SELECT * FROM tenants";
//        try (Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//            while (resultSet.next()) {
//                tenants.add(new Tenant(
//                        resultSet.getInt("id"),
//                        resultSet.getString("name"),
//                        resultSet.getString("contactInfo")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return tenants;
//    }
//
//    public void addTenant(String name, String contactInfo) {
//        String query = "INSERT INTO tenants (name, contactInfo) VALUES (?, ?)";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, contactInfo);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteTenant(int tenantId) {
//        String query = "DELETE FROM tenants WHERE id = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, tenantId);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
