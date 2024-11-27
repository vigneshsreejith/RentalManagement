package views;

import controllers.HouseController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.House;
import services.LoginService;

import java.util.ArrayList;
import java.util.List;

public class HouseView {

    private final HouseController houseController;
    private final TableView<House> houseTable;
    private final Button navigationButton;
    private final ObservableList<House> allHouses;
    private final String currentUserRole;
    private final Button logoutButton; // Logout button
    private final Stage primaryStage; // Reference to primary stage
    private VBox root;

    public HouseView(HouseController controller, String role, Stage primaryStage) throws Exception {
        this.houseController = controller;
        this.houseTable = new TableView<>();
        this.navigationButton = new Button("Go to Tenant View");
        this.currentUserRole = role;
        this.logoutButton = new Button("Logout"); // Initialize logout button
        this.primaryStage = primaryStage; // Set the primary stage reference
        // Fetch all houses from the controller initially
        this.allHouses = FXCollections.observableArrayList(houseController.getAllHouses());

        setupTable();
    }

    private void setupTable() throws Exception {
        // Create filter dropdown for rented status
        ChoiceBox<String> rentedFilter = new ChoiceBox<>();
        rentedFilter.getItems().addAll("All", "Rented", "Not Rented");
        rentedFilter.setValue("All"); // Default selection

        rentedFilter.setOnAction(event -> applyFilters(rentedFilter.getValue()));

        // Define columns for the TableView
        TableColumn<House, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<House, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        TableColumn<House, Double> rentPriceColumn = new TableColumn<>("Rent Price");
        rentPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRentPrice()));
        rentPriceColumn.setSortable(true); // Enable sorting on the Rent Price column

        TableColumn<House, Boolean> isRentedColumn = new TableColumn<>("Rented");
        isRentedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isRented()));

        TableColumn<House, Boolean> isInterestedColumn = new TableColumn<>("Interested");
        isInterestedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isInterested()));

        TableColumn<House, Boolean> isApprovedColumn = new TableColumn<>("Approved");
        isApprovedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isApproved()));
        boolean isLandlord = "LANDLORD".equalsIgnoreCase(currentUserRole);


        // Add columns to the TableView
        if(isLandlord){
            houseTable.getColumns().addAll(nameColumn, addressColumn, rentPriceColumn, isRentedColumn);
        } else {
            houseTable.getColumns().addAll(nameColumn, addressColumn, rentPriceColumn, isRentedColumn, isInterestedColumn, isApprovedColumn);

        }

        // Set data for the table
        houseTable.setItems(allHouses);

        // Input fields for adding/updating a house
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField rentPriceField = new TextField();
        rentPriceField.setPromptText("Rent Price");

        CheckBox isRentedCheckBox = new CheckBox("Is Rented");

        // Buttons for adding, updating, deleting a house, and clearing selection
        Button addButton = new Button("Add House");
        Button updateButton = new Button("Update House");
        Button deleteButton = new Button("Delete House");
        Button approveButton = new Button("Approve Tenants");
        Button clearSelectionButton = new Button("Clear Selection");
        // Dropdown (ComboBox)
        ComboBox<String> tenantDropdown = new ComboBox<>();
        tenantDropdown.setPromptText("Select Tenants");
        tenantDropdown.setMinWidth(200);
        // Set roles: Enable/disable buttons based on the current role
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        clearSelectionButton.setDisable(true); // Initially disabled

        if (!isLandlord) {
            nameField.setVisible(false);
            addressField.setVisible(false);
            rentPriceField.setVisible(false);
            isRentedCheckBox.setVisible(false);
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
            tenantDropdown.setVisible(false);
            approveButton.setVisible(false);
        }

        // Add house logic
        addButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String address = addressField.getText();
                double rentPrice = Double.parseDouble(rentPriceField.getText());
                boolean isRented = isRentedCheckBox.isSelected();

                String result = houseController.addHouse(name, address, rentPrice, isRented);

                if (result.contains("Error")) {
                    showError(result); // Show error if the name is not unique
                } else {
                    refreshTable(); // Refresh the table to show the new house
                    clearFields(nameField, addressField, rentPriceField, isRentedCheckBox);
                    showInfo(result); // Show success message
                }
            } catch (NumberFormatException e) {
                showError("Invalid input. Please enter a valid rent price.");
            }
        });

        // Update house logic
        updateButton.setOnAction(event -> {
            House selectedHouse = houseTable.getSelectionModel().getSelectedItem();
            if (selectedHouse != null) {
                try {
                    String name = nameField.getText();
                    String address = addressField.getText();
                    double rentPrice = Double.parseDouble(rentPriceField.getText());
                    boolean isRented = isRentedCheckBox.isSelected();

                    String result = houseController.updateHouse(selectedHouse.getId(), name, address, rentPrice, isRented);

                    if (result.contains("Error")) {
                        showError(result); // Show error if the name is not unique
                    } else {
                        refreshTable();
                        clearFields(nameField, addressField, rentPriceField, isRentedCheckBox);
                        showInfo(result); // Show success message
                    }
                } catch (NumberFormatException e) {
                    showError("Invalid input. Please enter a valid rent price.");
                }
            } else {
                showError("No house selected. Please select a house to update.");
            }
        });

        // Delete house logic
        deleteButton.setOnAction(event -> {
            House selectedHouse = houseTable.getSelectionModel().getSelectedItem();
            if (selectedHouse != null) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Confirmation");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to delete this house?");
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        houseController.deleteHouse(selectedHouse.getId());
                        refreshTable();
                        clearFields(nameField, addressField, rentPriceField, isRentedCheckBox);
                        showInfo("House deleted successfully.");
                    }
                });
            } else {
                showError("No house selected. Please select a house to delete.");
            }
        });

        // Clear selection logic
        clearSelectionButton.setOnAction(event -> {
            houseTable.getSelectionModel().clearSelection();
            clearFields(nameField, addressField, rentPriceField, isRentedCheckBox);
        });

        // Add listener for table selection
        houseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && isLandlord) {
                //set the value of dropdown to default
                tenantDropdown.setItems(FXCollections.observableArrayList(new ArrayList<>()));
                tenantDropdown.setVisible(false);
                approveButton.setVisible(false);
                //other values
                nameField.setText(newSelection.getName());
                addressField.setText(newSelection.getAddress());
                rentPriceField.setText(String.valueOf(newSelection.getRentPrice()));
                isRentedCheckBox.setSelected(newSelection.isRented());
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                addButton.setDisable(true);
                clearSelectionButton.setDisable(false);
                List<String> tenants = houseController.getTenantIds(newSelection.getId());
                //approveButton.setDisable(false);
                if(!tenants.isEmpty()){
                    ObservableList<String> tenantList = FXCollections.observableArrayList(tenants);
                    tenantDropdown.setItems(tenantList);
                    tenantDropdown.setVisible(true);
                    approveButton.setVisible(true);
                }
            } else if (newSelection == null && isLandlord) {
                // Clear fields and reset buttons
                clearFields(nameField, addressField, rentPriceField, isRentedCheckBox);
                tenantDropdown.setVisible(false);
                approveButton.setVisible(false);
                addButton.setDisable(false);
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                clearSelectionButton.setDisable(true);
            }
        });
        // Logout button logic
        logoutButton.setOnAction(event -> {
            LoginService loginService = new LoginService(); // Create a new LoginService
            LoginView loginView = new LoginView(loginService, primaryStage);

            Scene loginScene = loginView.getLoginScene();
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");
        });

        Button interestedButton = new Button("Update Interest state");

        // Enable "Interested" button only for tenants
        if ("TENANT".equalsIgnoreCase(currentUserRole)) {
            interestedButton.setVisible(true);

            interestedButton.setOnAction(event -> {
                House selectedHouse = houseTable.getSelectionModel().getSelectedItem();
                if (selectedHouse != null) {
                    String result;// Show success message
// Refresh table to show updated status
                    if(selectedHouse.isInterested()){
                        result = houseController.removeMarkInterest(selectedHouse.getId());
                    } else {
                        result = houseController.markInterest(selectedHouse.getId());
                    }
                    refreshTable();  // Refresh table to show updated status
                    showInfo(result); // Show success message

                } else {
                    showError("Please select a house to mark as interested.");
                }
            });
        } else {
            interestedButton.setVisible(false);
        }

        // Load tenants into dropdown
        //House house = houseTable.getSelectionModel().getSelectedItem();

        approveButton.setOnAction(event -> {
                    House selectedHouse = houseTable.getSelectionModel().getSelectedItem();
                    String selectedTenant = tenantDropdown.getSelectionModel().getSelectedItem();
                    if (selectedHouse!=null && selectedTenant != null) {
                        System.out.println("selected tenant is not null" + selectedTenant);
                        System.out.println("selected house is not null" + selectedHouse.getId());
                        String result;// Show success message
// Refresh table to show updated status
                        result = houseController.approveTenants(selectedHouse.getId(), selectedTenant);
                        refreshTable();  // Refresh table to show updated status
                        showInfo(result); // Show success message

                    } else {
                        showError("Please select a tenant to approve.");
                    }
                });

        // Set up the root layout
        root = new VBox(10, rentedFilter, houseTable, nameField, addressField, rentPriceField, isRentedCheckBox, addButton, updateButton, deleteButton, tenantDropdown, approveButton, clearSelectionButton, interestedButton, navigationButton, logoutButton);
    }

    public VBox getView() {
        return root;
    }

    private void refreshTable() {
        allHouses.setAll(houseController.getAllHouses());
        applyFilters("All");
    }

    private void applyFilters(String rentedStatus) {
        ObservableList<House> filteredHouses = FXCollections.observableArrayList();

        for (House house : allHouses) {
            if ("All".equals(rentedStatus) || ("Rented".equals(rentedStatus) && house.isRented()) || ("Not Rented".equals(rentedStatus) && !house.isRented())) {
                filteredHouses.add(house);
            }
        }

        houseTable.setItems(filteredHouses);
    }

    private void clearFields(TextField nameField, TextField addressField, TextField rentPriceField, CheckBox isRentedCheckBox) {
        nameField.clear();
        addressField.clear();
        rentPriceField.clear();
        isRentedCheckBox.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
