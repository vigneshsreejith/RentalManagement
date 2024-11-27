package views;

// LoginView.java

import controllers.HouseController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import services.HouseService;
import services.LoginService;

public class LoginView {
    private final LoginService loginService;
    private final Stage primaryStage;

    public LoginView(LoginService loginService, Stage primaryStage) {
        this.loginService = loginService;
        this.primaryStage = primaryStage;
    }

    public Scene getLoginScene() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Authenticate user
            User user = loginService.authenticate(username, password);

            if (user != null) {
                // Proceed to the main app with the authenticated user
                try {
                    goToMainApp(user);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showAlert("Invalid credentials. Please try again.");
            }
        });

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        return new Scene(layout, 300, 200);
    }

    private void goToMainApp(User user) throws Exception {
        HouseService houseService = new HouseService();
        HouseController houseController = new HouseController(houseService, user);
        HouseView houseView = new HouseView(houseController, user.getRole(), this.primaryStage);

        Scene houseScene = new Scene(houseView.getView(), 800, 600);
        primaryStage.setScene(houseScene);
        primaryStage.setTitle("House Management System");
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

