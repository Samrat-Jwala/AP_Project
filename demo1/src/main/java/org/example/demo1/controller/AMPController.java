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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.demo1.Model.AMPCLASS;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AMPController implements Initializable {

    public TableView<AMPCLASS> tableView;

    public TableColumn<AMPCLASS,String> name;
    public TableColumn<AMPCLASS,String> location;
    public TableColumn<AMPCLASS,String> description;
    public TableColumn<AMPCLASS,String> preference;

    @FXML
    private TextField Name;
    @FXML
    private TextField Location;
    @FXML
    private TextField Description;
    @FXML
    private TextField Preference;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<AMPCLASS> observableList= FXCollections.observableArrayList();

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
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        preference.setCellValueFactory(new PropertyValueFactory<>("Preference"));

        ObservableList<AMPCLASS> observableList = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\AMPRecords.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    observableList.add(new AMPCLASS(fields[0], fields[1], fields[2], fields[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.setItems(observableList);
    }
    public void add_items(ActionEvent e1) throws IOException {
        if (!"admin".equalsIgnoreCase(Session.getRole())) {
            showAlert("Access Denied", "Only admin is allowed to add items.");
            return;
        }

        String userInput1 = Name.getText().trim();
        String userInput2 = Location.getText().trim();
        String userInput3 = Description.getText().trim();
        String userInput4 = Preference.getText().trim();

        // Validate empty fields
        if (userInput1.isEmpty() || userInput2.isEmpty() || userInput3.isEmpty() || userInput4.isEmpty()) {
            showAlert( "Input Error", "All fields must be filled.");
            return;
        }

        AMPCLASS newEntry = new AMPCLASS(userInput1, userInput2, userInput3, userInput4);
        tableView.getItems().add(newEntry);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\AMPRecords.txt", true))) {
            writer.write(userInput1 + "," + userInput2 + "," + userInput3 + "," + userInput4);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear input fields after adding
        Name.clear();
        Location.clear();
        Description.clear();
        Preference.clear();
    }
    @FXML
    public void edit_item(ActionEvent e) {
        if (!"admin".equalsIgnoreCase(Session.getRole())) {
            showAlert("Access Denied", "Only admin is allowed to edit items.");
            return;
        }
        AMPCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Populate the input fields with selected row data
            Name.setText(selectedItem.getName());
            Location.setText(selectedItem.getLocation());
            Description.setText(selectedItem.getDescription());
            Preference.setText(selectedItem.getPreference());

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
        if (!"admin".equalsIgnoreCase(Session.getRole())) {
            showAlert("Access Denied", "Only admin is allowed to edit items.");
            return;
        }
        AMPCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void deleteFromFile(AMPCLASS item) {
        File originalFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\AMPRecords.txt");
        File tempFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\AMPRecordsTemp.txt");

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
                            fields[2].trim().equals(item.getDescription().trim()) &&
                            fields[3].trim().equals(item.getPreference().trim());

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
    private void focusDescription(ActionEvent e5){
        Description.requestFocus();
    }
    @FXML
    private void focusPreference(ActionEvent e5){
        Preference.requestFocus();
    }
}
