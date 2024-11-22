package models;

public class Payment {
    private int id;
    private int tenantId;
    private double amount;
    private String paymentDate;

    public Payment(int id, int tenantId, double amount, String paymentDate) {
        this.id = id;
        this.tenantId = tenantId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
