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

public class HouseView {

    private HouseController houseController;
    private TableView<House> houseTable;
    private VBox root;
    private Button navigationButton;

    public HouseView(HouseController controller) {
        this.houseController = controller;
        this.houseTable = new TableView<>();
        this.navigationButton = new Button("Go to Tenant View");

        setupTable();
    }

    private void setupTable() {
        // Get data from the controller
        ObservableList<House> houseData = FXCollections.observableArrayList(houseController.getAllHouses());

        // Define columns for the TableView
        TableColumn<House, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<House, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        TableColumn<House, Double> rentPriceColumn = new TableColumn<>("Rent Price");
        rentPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRentPrice()));

        TableColumn<House, Boolean> isRentedColumn = new TableColumn<>("Rented");
        isRentedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isRented()));

        // Add columns to the TableView
        houseTable.getColumns().addAll(nameColumn, addressColumn, rentPriceColumn, isRentedColumn);

        // Set data for the table
        houseTable.setItems(houseData);

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
        Button clearSelectionButton = new Button("Clear Selection");

        // Initially, only the Add button is enabled
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        clearSelectionButton.setDisable(true);

        // Add house logic
        addButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String address = addressField.getText();
                double rentPrice = Double.parseDouble(rentPriceField.getText());
                boolean isRented = isRentedCheckBox.isSelected();

                // Call the controller to add a new house
                String result = houseController.addHouse(name, address, rentPrice, isRented);

                if (result.contains("Error")) {
                    showError(result); // Show error if the name is not unique
                } else {
                    // Refresh the table to show the new house
                    refreshTable();

                    // Clear input fields
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

                    // Call the controller to update the house
                    String result = houseController.updateHouse(selectedHouse.getId(), name, address, rentPrice, isRented);

                    if (result.contains("Error")) {
                        showError(result); // Show error if the name is not unique
                    } else {
                        // Refresh the table
                        refreshTable();

                        // Clear input fields
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
                        // Call the controller to delete the house
                        houseController.deleteHouse(selectedHouse.getId());

                        // Refresh the table
                        refreshTable();

                        // Clear input fields
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

            addButton.setDisable(false);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            clearSelectionButton.setDisable(true);
        });

        // Add a listener to the TableView selection
        houseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate fields with the selected house data
                nameField.setText(newSelection.getName());
                addressField.setText(newSelection.getAddress());
                rentPriceField.setText(String.valueOf(newSelection.getRentPrice()));
                isRentedCheckBox.setSelected(newSelection.isRented());

                // Enable Update and Delete buttons, disable Add button
                addButton.setDisable(true);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                clearSelectionButton.setDisable(false);
            } else {
                // Clear fields and reset buttons
                clearFields(nameField, addressField, rentPriceField, isRentedCheckBox);

                addButton.setDisable(false);
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                clearSelectionButton.setDisable(true);
            }
        });

        // Set up the root layout
        root = new VBox(10, houseTable, nameField, addressField, rentPriceField, isRentedCheckBox, addButton, updateButton, deleteButton, clearSelectionButton, navigationButton);
    }

    // Get the main view layout (VBox)
    public VBox getView() {
        return root;
    }

    // Get the navigation button for switching between views
    public Button getNavigationBar() {
        return navigationButton;
    }

    // Refresh the table after adding, updating, or deleting houses
    private void refreshTable() {
        houseTable.setItems(FXCollections.observableArrayList(houseController.getAllHouses()));
    }

    // Clear input fields
    private void clearFields(TextField nameField, TextField addressField, TextField rentPriceField, CheckBox isRentedCheckBox) {
        nameField.clear();
        addressField.clear();
        rentPriceField.clear();
        isRentedCheckBox.setSelected(false);
    }

    // Show an error alert
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an info alert
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
