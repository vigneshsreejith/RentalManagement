package controllers;

import models.Payment;
import services.PaymentService;

import java.sql.SQLException;
import java.util.List;

public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController() throws SQLException {
        this.paymentService = new PaymentService();
    }

    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    public void addPayment(int tenantId, double amount) {
        paymentService.addPayment(tenantId, amount);
    }
}
