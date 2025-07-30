package org.example.demo1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.demo1.Model.GMSCLASS;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GMSController implements Initializable {
    public TableView<GMSCLASS> tableView;
    public TableColumn<GMSCLASS,String> name;
    public TableColumn<GMSCLASS,String> age;
    public TableColumn<GMSCLASS,String> email;
    public TableColumn<GMSCLASS,String> contact;

    @FXML
    private TextField Name;
    @FXML
    private TextField Age;
    @FXML
    private TextField Email;
    @FXML
    private TextField Contact;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<GMSCLASS> observableList= FXCollections.observableArrayList();
    public void switchtoDashboard(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo1/Dashboard.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        age.setCellValueFactory(new PropertyValueFactory<>("Age"));
        email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        contact.setCellValueFactory(new PropertyValueFactory<>("Contact"));

        ObservableList<GMSCLASS> observableList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\GMSRecord"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    observableList.add(new GMSCLASS(fields[0], fields[1], fields[2], fields[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.setItems(observableList);
    }
    private String getUserFilePath(String fileName) {
        String username = Session.getUsername();  // e.g. GSamrat or TSamrat
        String baseDir = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users";
        String userDir = baseDir + "\\" + username;

        File dir = new File(userDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return userDir + "\\" + fileName;
    }
    public static File ensureUserFileExists() {
        String folderPath = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users\\" + Session.getUsername();
        File folder = new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs(); // Create the user's folder
        }

        File userFile = new File(folder, "GMSRecord.txt");
        try {
            if (!userFile.exists()) {
                userFile.createNewFile(); // Create the file if it doesn't exist
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userFile;
    }
    public void add_items(ActionEvent e1) throws IOException {
        if (!"admin".equalsIgnoreCase(Session.getRole()) && !"guide".equalsIgnoreCase(Session.getRole())) {
            showWarning("Access Denied", "Only admin and guide is allowed to edit items.");
            return;
        }

        String userInput1 = Name.getText().trim();
        String userInput2 = Age.getText().trim();
        String userInput3 = Email.getText().trim();
        String userInput4 = Contact.getText().trim();

        if (userInput1.isEmpty() || userInput2.isEmpty() || userInput3.isEmpty() || userInput4.isEmpty()) {
            showAlert("Missing Information", "All fields are required. Please fill them all out.");
            return; // Stop the method if any field is empty
        }
        try {
            int age = Integer.parseInt(userInput2); // Convert age string to a number
            if (age <= 0 || age > 120) { // Check if the age is in a reasonable range
                showAlert("Invalid Age", "Please enter a valid age between 1 and 120.");
                return;
            }
        } catch (NumberFormatException e) {
            // This catches cases where the input is not a number (e.g., "abc")
            showAlert("Invalid Age", "Age must be a valid number.");
            return;
        }

        // Validate email and contact number
        if (!isValidEmail(userInput3)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!isValidContact(userInput4)) {
            showAlert("Invalid Contact Number", "Please enter a valid 10-digit contact number.");
            return;
        }

        GMSCLASS newEntry = new GMSCLASS(userInput1, userInput2, userInput3, userInput4);
        tableView.getItems().add(newEntry);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\GMSRecord", true))) {
            writer.write(userInput1 + "," + userInput2 + "," + userInput3 + "," + userInput4);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = getUserFilePath("GMSRecord.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(userInput1 + "," + userInput2 + "," + userInput3 + "," + userInput4);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Clear input fields after adding
        Name.clear();
        Age.clear();
        Email.clear();
        Contact.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        // Basic email pattern (you can make it stricter if needed)
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    private boolean isValidContact(String contact) {
        // Must be exactly 10 digits (you can adjust if your format is different)
        return contact.matches("\\d{10}");
    }
    @FXML
    public void edit_item(ActionEvent e) {
        if (!"admin".equalsIgnoreCase(Session.getRole()) && !"guide".equalsIgnoreCase(Session.getRole())) {
            showWarning("Access Denied", "Only admin and guide is allowed to edit items.");
            return;
        }
        GMSCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Populate the input fields with selected row data
            Name.setText(selectedItem.getName());
            Age.setText(selectedItem.getAge());
            Email.setText(selectedItem.getEmail());
            Contact.setText(selectedItem.getContact());

            // Remove from table and file temporarily
            tableView.getItems().remove(selectedItem);
            deleteFromFile(selectedItem);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a row to edit.");
            alert.showAndWait();
        }
    }
    public void delete_item(ActionEvent e2){
        if (!"admin".equalsIgnoreCase(Session.getRole()) && !"guide".equalsIgnoreCase(Session.getRole())) {
            showWarning("Access Denied", "Only admin and guide is allowed to edit items.");
            return;
        }
        GMSCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
            deleteFromFile(selectedItem);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a row to delete.");
            alert.showAndWait();
        }
    }
    private void deleteFromFile(GMSCLASS item) {
        String filePath = getUserFilePath("GMSRecord.txt");
        File originalFile1 = ensureUserFileExists(); // call the method above
        File tempFile1 = new File(originalFile1.getParent(), "GMSRecordTemp.txt");
        File originalFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\GMSRecord");
        File tempFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\GMSRecordtemp");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length == 4) {
                    boolean isMatch = fields[0].trim().equals(item.getName().trim()) &&
                            fields[1].trim().equals(item.getAge().trim()) &&
                            fields[2].trim().equals(item.getEmail().trim()) &&
                            fields[3].trim().equals(item.getContact().trim());

                    if (!isMatch) {
                        writer.write(currentLine);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace original file with updated file
        if (originalFile.delete()) {
            if (!tempFile.renameTo(originalFile)) {
                System.out.println("Failed to rename temp file.");
            }
        } else {
            System.out.println("Failed to delete original file.");
        }
        //User-wala

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile1));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile1))
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length == 4) {
                    boolean isMatch = fields[0].trim().equals(item.getName().trim()) &&
                            fields[1].trim().equals(item.getAge().trim()) &&
                            fields[2].trim().equals(item.getEmail().trim()) &&
                            fields[3].trim().equals(item.getContact().trim());

                    if (!isMatch) {
                        writer.write(currentLine);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace original file with updated file
        if (originalFile1.delete()) {
            if (!tempFile1.renameTo(originalFile1)) {
                System.out.println("Failed to rename temp file.");
            }
        } else {
            System.out.println("Failed to delete original file.");
        }
    }
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void focusAGE(ActionEvent e5){
        Age.requestFocus();
    }
    @FXML
    private void focusEMAIL(ActionEvent e5){
        Email.requestFocus();
    }
    @FXML
    private void focusCONTACT(ActionEvent e5){
        Contact.requestFocus();
    }
}
