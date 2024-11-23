package app;

import controllers.HouseController;
import controllers.MaintenanceController;
import controllers.PaymentController;
import controllers.TenantController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import views.HouseView;
import views.TenantView;
import views.MaintenanceView;
import views.PaymentView;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Rental Management System");

            BorderPane root = new BorderPane();
            Scene mainScene = new Scene(root, 800, 600);

            // Initialize views with controllers
            HouseController houseController = new HouseController();
            HouseView houseView = new HouseView(houseController);
            TenantController tenantController = new TenantController();
            TenantView tenantView = new TenantView(tenantController);
            MaintenanceController maintenanceController = new MaintenanceController();
            MaintenanceView maintenanceView = new MaintenanceView(maintenanceController);
            PaymentController paymentController = new PaymentController();
            PaymentView paymentView = new PaymentView(paymentController);

            // Default to HouseView
            root.setCenter(houseView.getView());

            // Set up navigation between views
            houseView.getNavigationBar().setOnAction(e -> root.setCenter(tenantView.getView()));
            tenantView.getNavigationBar().setOnAction(e -> root.setCenter(maintenanceView.getView()));
            maintenanceView.getNavigationBar().setOnAction(e -> root.setCenter(paymentView.getView()));
            paymentView.getNavigationBar().setOnAction(e -> root.setCenter(houseView.getView()));

            primaryStage.setScene(mainScene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
