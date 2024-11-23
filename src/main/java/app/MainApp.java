package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;
import services.LoginService;
import views.LoginView;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Rental Management System");

            // Initialize LoginService and LoginView
            LoginService loginService = new LoginService();
            LoginView loginView = new LoginView(loginService, primaryStage);

            // Set the login scene as the initial scene
            Scene loginScene = loginView.getLoginScene();
            primaryStage.setScene(loginScene);

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
