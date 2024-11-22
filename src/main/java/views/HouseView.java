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
        TableColumn<House, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        TableColumn<House, Double> rentPriceColumn = new TableColumn<>("Rent Price");
        rentPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRentPrice()));

        TableColumn<House, Boolean> isRentedColumn = new TableColumn<>("Rented");
        isRentedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isRented()));

        // Add columns to the TableView
        houseTable.getColumns().addAll(addressColumn, rentPriceColumn, isRentedColumn);

        // Set data for the table
        houseTable.setItems(houseData);

        // Button to add a house
        Button addButton = new Button("Add House");
        addButton.setOnAction(event -> houseController.addHouse("123 Main St", 1000, false));

        // Set up the root layout
        root = new VBox(10, houseTable, addButton, navigationButton);
    }

    // Get the main view layout (VBox)
    public VBox getView() {
        return root;
    }

    // Get the navigation button for switching between views
    public Button getNavigationBar() {
        return navigationButton;
    }

    public void start(Stage stage) {
        // Setup scene
        Scene scene = new Scene(getView(), 600, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Houses");
        stage.show();
    }
}
