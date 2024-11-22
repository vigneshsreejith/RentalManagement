package views;

import controllers.MaintenanceController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.MaintenanceRequest;

public class MaintenanceView {
    private MaintenanceController maintenanceController;
    private TableView<MaintenanceRequest> maintenanceTable;
    private VBox root;
    private Button navigationButton;

    public MaintenanceView(MaintenanceController controller) {
        this.maintenanceController = controller;
        this.maintenanceTable = new TableView<>();
        this.navigationButton = new Button("Go to House View");

        setupTable();
    }

    private void setupTable() {
        // Get data from the controller
        ObservableList<MaintenanceRequest> maintenanceData = FXCollections.observableArrayList(maintenanceController.getAllRequests());

        // Define columns for the TableView
        TableColumn<MaintenanceRequest, Integer> houseIdColumn = new TableColumn<>("House ID");
        houseIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHouseId()));

        TableColumn<MaintenanceRequest, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<MaintenanceRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Add columns to the TableView
        maintenanceTable.getColumns().addAll(houseIdColumn, descriptionColumn, statusColumn);

        // Set data for the table
        maintenanceTable.setItems(maintenanceData);

        // Input fields for adding maintenance requests
        TextField houseIdField = new TextField();
        houseIdField.setPromptText("House ID");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        // Add button for adding requests
        Button addButton = new Button("Add Request");
        addButton.setOnAction(event -> {
            maintenanceController.addRequest(
                    Integer.parseInt(houseIdField.getText()),
                    descriptionField.getText(),
                    "Pending"
            );
            refreshTable();
        });

        // Mark request as complete button
        Button markCompleteButton = new Button("Mark Complete");
        markCompleteButton.setOnAction(event -> {
            MaintenanceRequest selectedRequest = maintenanceTable.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                maintenanceController.updateRequestStatus(selectedRequest.getId(), "Completed");
                refreshTable();
            }
        });

        // Set up the layout with table and buttons
        root = new VBox(10, maintenanceTable, houseIdField, descriptionField, addButton, markCompleteButton, navigationButton);
    }

    // Get the main view layout (VBox)
    public VBox getView() {
        return root;
    }

    // Get the navigation button for switching between views
    public Button getNavigationBar() {
        return navigationButton;
    }

    // Refresh the table after adding/updating maintenance requests
    private void refreshTable() {
        maintenanceTable.setItems(FXCollections.observableArrayList(maintenanceController.getAllRequests()));
    }

    // Start method to show the scene (not used in RentalManagementApp but kept for standalone purposes)
    public void start(Stage stage) {
        Scene scene = new Scene(getView(), 600, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Maintenance Requests");
        stage.show();
    }
}
