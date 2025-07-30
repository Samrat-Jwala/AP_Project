package org.example.demo1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.demo1.service.LoginService;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage_Controller implements Initializable {
    @FXML
    private Button button_login;

    @FXML
    private Label user_label;
    @FXML
    private Button New_user;

    @FXML
    public TextField username;

    @FXML
    public PasswordField password;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchtoDashboard(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo1/Dashboard.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }
    public void switchtoSignup(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo1/Signup.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Signup");
        stage.show();
    }

    public void Login(ActionEvent e1) throws FileNotFoundException {
        String enteredUsername = username.getText().trim();
        String enteredPassword = password.getText().trim();

        // The path should be managed properly, not hardcoded.
        // For now, we leave it to show the correction in logic.
        String folderPath = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users";

        // Use ONE authentication method
        LoginService loginService = new LoginService(folderPath);

        if (loginService.authenticate(enteredUsername, enteredPassword)) {
            try {
                Session.setUsername(enteredUsername); // Set the session for the logged-in user
                switchtoDashboard(e1); // Switch to the dashboard
            } catch (IOException ex) {
                showError("Failed to load the dashboard.");
                ex.printStackTrace();
            }
        } else {
            // This 'else' block handles both incorrect credentials and users not found.
            showError("Invalid username or password.");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(msg);
        alert.show();
    }
    //if A then admin dashboard G guide dashboard T tourist dashboard
    public void New_user(ActionEvent e3) throws IOException {
        switchtoSignup(e3);
    }

    @FXML
    private void focusPassword(ActionEvent e4) {
        String userInput = username.getText();
        if (userInput == null || userInput.trim().isEmpty()) {
            user_label.setText("Enter Username");
            user_label.setTextFill(Color.RED);
            username.requestFocus();
        } else {
            password.requestFocus();
        }
    }

    @FXML
    private void focusLogin(ActionEvent e5){
        String userInput = password.getText();
        if (userInput == null || userInput.trim().isEmpty()) {
            user_label.setText("Enter Password");
            user_label.setTextFill(Color.RED);
            password.requestFocus();
        } else {
            button_login.requestFocus();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}