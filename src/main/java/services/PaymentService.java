package services;

import database.DatabaseConnection;
import models.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private final Connection connection;

    public PaymentService() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                payments.add(new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("tenant_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("payment_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public void addPayment(int tenantId, double amount) {
        String query = "INSERT INTO payments (tenant_id, amount, payment_date) VALUES (?, ?, CURRENT_DATE)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, tenantId);
            preparedStatement.setDouble(2, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
