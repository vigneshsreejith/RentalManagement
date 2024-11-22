package views;

import controllers.TenantController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Tenant;

public class TenantView {
    private TenantController tenantController;
    private TableView<Tenant> tenantTable;
    private VBox root;
    private Button navigationButton;

    public TenantView(TenantController controller) {
        this.tenantController = controller;
        this.tenantTable = new TableView<>();
        this.navigationButton = new Button("Go to Payment View");

        setupTable();
    }

    private void setupTable() {
        // Get data from the controller
        ObservableList<Tenant> tenantData = FXCollections.observableArrayList(tenantController.getAllTenants());

        // Define columns for the TableView
        TableColumn<Tenant, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Tenant, String> contactColumn = new TableColumn<>("Contact Info");
        contactColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContactInfo()));

        // Add columns to the TableView
        tenantTable.getColumns().addAll(nameColumn, contactColumn);

        // Set data for the table
        tenantTable.setItems(tenantData);

        // Input fields for adding tenants
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField contactField = new TextField();
        contactField.setPromptText("Contact Info");

        // Add button for adding tenants
        Button addButton = new Button("Add Tenant");
        addButton.setOnAction(event -> {
            tenantController.addTenant(nameField.getText(), contactField.getText());
            refreshTable();
        });

        // Delete button for removing tenants
        Button deleteButton = new Button("Delete Tenant");
        deleteButton.setOnAction(event -> {
            Tenant selectedTenant = tenantTable.getSelectionModel().getSelectedItem();
            if (selectedTenant != null) {
                tenantController.deleteTenant(selectedTenant.getId());
                refreshTable();
            }
        });

        // Set up the layout with table and buttons
        root = new VBox(10, tenantTable, nameField, contactField, addButton, deleteButton, navigationButton);
    }

    // Get the main view layout (VBox)
    public VBox getView() {
        return root;
    }

    // Get the navigation button for switching between views
    public Button getNavigationBar() {
        return navigationButton;
    }

    // Refresh the table after adding or deleting tenants
    private void refreshTable() {
        tenantTable.setItems(FXCollections.observableArrayList(tenantController.getAllTenants()));
    }

    // Start method to show the scene (not used in RentalManagementApp but kept for standalone purposes)
    public void start(Stage stage) {
        Scene scene = new Scene(getView(), 600, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Tenants");
        stage.show();
    }
}
