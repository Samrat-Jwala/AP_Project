package org.example.demo1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.demo1.Model.AMPCLASS;
import org.example.demo1.Model.EMRCLASS;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EmergencyController implements Initializable {
    public TableView<EMRCLASS> tableView;

    public TableColumn<EMRCLASS,String> name;
    public TableColumn<EMRCLASS,String> location;
    public TableColumn<EMRCLASS,String> contact;
    public TableColumn<EMRCLASS,String> details;
    @FXML
    private TextField Name;
    @FXML
    private TextField Location;
    @FXML
    private TextField Contact;
    @FXML
    private TextField Details;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        details.setCellValueFactory(new PropertyValueFactory<>("Details"));
    }
    public void add_items(ActionEvent e1) throws IOException {

        String userInput1 = Name.getText().trim();
        String userInput2 = Location.getText().trim();
        String userInput3 = Contact.getText().trim();
        String userInput4 = Details.getText().trim();

        // Validate empty fields
        if (userInput1.isEmpty() || userInput2.isEmpty() || userInput3.isEmpty() || userInput4.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled.");
            return;
        }

        // Validate contact format (10-digit number)
        if (!userInput3.matches("\\d{10}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Contact", "Contact number must be 10 digits.");
            return;
        }

        // Add to table and file
        EMRCLASS newEntry = new EMRCLASS(userInput1, userInput2, userInput3, userInput4);
        tableView.getItems().add(newEntry);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\EmergencyRecords", true))) {
            writer.write(userInput1 + "," + userInput2 + "," + userInput3 + "," + userInput4);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear inputs
        Name.clear();
        Location.clear();
        Contact.clear();
        Details.clear();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void edit_item(ActionEvent e) {
        EMRCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Populate the input fields with selected row data
            Name.setText(selectedItem.getName());
            Location.setText(selectedItem.getLocation());
            Contact.setText(selectedItem.getContact());
            Details.setText(selectedItem.getDetails());

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

        EMRCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
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
    private void deleteFromFile(EMRCLASS item) {
        File originalFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\EmergencyRecords");
        File tempFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\EmergencyRecordsTemp");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length == 4) {
                    boolean isMatch = fields[0].trim().equals(item.getName().trim()) &&
                            fields[1].trim().equals(item.getLocation().trim()) &&
                            fields[2].trim().equals(item.getContact().trim()) &&
                            fields[3].trim().equals(item.getDetails().trim());

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
    }
    @FXML
    private void focusLocation(ActionEvent e5){
        Location.requestFocus();
    }
    @FXML
    private void focusContact(ActionEvent e5){
        Contact.requestFocus();
    }
    @FXML
    private void focusDetails(ActionEvent e5){
        Details.requestFocus();
    }
}
