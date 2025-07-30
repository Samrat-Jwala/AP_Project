package org.example.demo1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SignupController {

    @FXML
    private TextField FName;
    @FXML
    private TextField LName;
    @FXML
    private PasswordField Pass;
    @FXML
    private PasswordField CPass;
    @FXML
    private Label warning_label;
    @FXML
    private Button continue_btn;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchtoLoginpage(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo1/LoginPage.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("LoginPage");
        stage.show();
    }
    public static void createfolder(String Folderpath){
        File folder = new File(Folderpath);
        if(!folder.exists()){
            folder.mkdir();
            System.out.println("Folder created: "+Folderpath );
        }
    }
    public void Continue(ActionEvent e) throws IOException {
        String userInput1 = FName.getText();
        String userInput2 = LName.getText();
        String userInput3 = Pass.getText();
        String userInput4 = CPass.getText();
        if(userInput1 == null || userInput1.trim().isEmpty() ||
                userInput2 == null || userInput2.trim().isEmpty() ||
                userInput3 == null || userInput3.trim().isEmpty() ||
                userInput4 == null || userInput4.trim().isEmpty()){
            warning_label.setText("Please fill all the required info!");
            warning_label.setTextFill(Color.RED);
        }
        else if(!userInput3.equals(userInput4)){
            warning_label.setText("Password doesn't match!");
            warning_label.setTextFill(Color.RED);
        }
        else {
            String folderpath = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users";
            createfolder(folderpath+"\\"+userInput1+userInput2+','+userInput3);
            File credentialsFile = new File(folderpath +"\\"+userInput1+userInput2+','+userInput3+ "\\credentials.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFile))) {
                writer.write(userInput1+userInput2 + "," + userInput3);
                System.out.println("Credentials saved successfully.");
            } catch (IOException e7) {
                System.out.println("Failed to write credentials: " + e7.getMessage());

            }
            warning_label.setText("Account created successfully!");
            warning_label.setTextFill(Color.GREEN);

            FName.clear();
            LName.clear();
            Pass.clear();
            CPass.clear();
            switchtoLoginpage(e);
        }

    }

    @FXML
    private void focusLName(ActionEvent e5){
        LName.requestFocus();
    }
    @FXML
    private void focusPass(ActionEvent e5){
        Pass.requestFocus();
    }
    @FXML
    private void focusCPass(ActionEvent e5){
        CPass.requestFocus();
    }
    @FXML
    private void focusContinue(ActionEvent e5){
        continue_btn.requestFocus();
    }

}
