package views;

import controllers.PaymentController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Payment;

public class PaymentView {
    private PaymentController paymentController;
    private TableView<Payment> paymentTable;
    private VBox root;
    private Button navigationButton;

    public PaymentView(PaymentController controller) {
        this.paymentController = controller;
        this.paymentTable = new TableView<>();
        this.navigationButton = new Button("Go to Maintenance View");

        setupTable();
    }

    private void setupTable() {
        // Get data from the controller
        ObservableList<Payment> paymentData = FXCollections.observableArrayList(paymentController.getAllPayments());

        // Define columns for the TableView
        TableColumn<Payment, Integer> tenantIdColumn = new TableColumn<>("Tenant ID");
        tenantIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTenantId()));

        TableColumn<Payment, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));

        TableColumn<Payment, String> dateColumn = new TableColumn<>("Payment Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentDate()));

        // Add columns to the TableView
        paymentTable.getColumns().addAll(tenantIdColumn, amountColumn, dateColumn);

        // Set data for the table
        paymentTable.setItems(paymentData);

        // Input fields for adding payments
        TextField tenantIdField = new TextField();
        tenantIdField.setPromptText("Tenant ID");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // Add button for adding payments
        Button addButton = new Button("Add Payment");
        addButton.setOnAction(event -> {
            paymentController.addPayment(
                    Integer.parseInt(tenantIdField.getText()),
                    Double.parseDouble(amountField.getText())
            );
            refreshTable();
        });

        // Set up the layout with table and buttons
        root = new VBox(10, paymentTable, tenantIdField, amountField, addButton, navigationButton);
    }

    // Get the main view layout (VBox)
    public VBox getView() {
        return root;
    }

    // Get the navigation button for switching between views
    public Button getNavigationBar() {
        return navigationButton;
    }

    // Refresh the table after adding payments
    private void refreshTable() {
        paymentTable.setItems(FXCollections.observableArrayList(paymentController.getAllPayments()));
    }

    // Start method to show the scene (not used in RentalManagementApp but kept for standalone purposes)
    public void start(Stage stage) {
        Scene scene = new Scene(getView(), 600, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Payments");
        stage.show();
    }
}
