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
import org.example.demo1.Model.FDMClass;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FDMController implements Initializable {
    public TableView<FDMClass> tableView;

    public TableColumn<FDMClass, String> festival;
    public TableColumn<FDMClass, String> start_date;
    public TableColumn<FDMClass, String> end_date;
    public TableColumn<FDMClass, Double> discount;

    @FXML
    private ComboBox<String> festCombo;
    @FXML
    private DatePicker start;
    @FXML
    private DatePicker end;
    @FXML
    private Button add;
    @FXML
    private Button delete;
    @FXML
    private Button back;
    @FXML
    private TextField dis;
    @FXML
    private Label warning;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<FDMClass> observableList = FXCollections.observableArrayList();

    public void switchtoDashboard(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo1/Dashboard.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        festCombo.getItems().addAll("Dashain", "Tihar", "Holi", "Teej");
        festival.setCellValueFactory(new PropertyValueFactory<>("festival"));
        start_date.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        end_date.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        ObservableList<FDMClass> observableList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\FDMRecords"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    observableList.add(new FDMClass(fields[0], fields[1], fields[2], fields[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.setItems(observableList);
    }

    public void add_items(ActionEvent e1) throws IOException {
        if (!"admin".equalsIgnoreCase(Session.getRole())) {
            showAlert("Access Denied", "Only admin is allowed to edit items.");
            return;
        }
        LocalDate selectedDate = start.getValue();
        LocalDate selectedDate1 = end.getValue();

        // Null check for dates
        if (selectedDate == null || selectedDate1 == null) {
            warning.setText("Please select both start and end dates.");
            return;
        }

        // Logical date check
        if (selectedDate1.isBefore(selectedDate)) {
            warning.setText("End date cannot be before start date.");
            return;
        }

        // Get festival name
        String userInput1 = festCombo.isEditable()
                ? festCombo.getEditor().getText().trim()
                : festCombo.getValue();

        if (userInput1 == null || userInput1.isEmpty()) {
            warning.setText("Please enter or select a festival name.");
            return;
        }

        String userInput2 = selectedDate.toString();
        String userInput3 = selectedDate1.toString();

        String discountInput = dis.getText().trim();
        double discountValue;

        // Validate and parse discount
        try {
            discountValue = Double.parseDouble(discountInput);
        } catch (NumberFormatException ex) {
            warning.setText("Discount must be a valid number.");
            return;
        }

        // Convert to string after validation
        String userInput4 = String.valueOf(discountValue);

        FDMClass newEntry = new FDMClass(userInput1, userInput2, userInput3, userInput4);
        tableView.getItems().add(newEntry);

        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\FDMRecords", true))) {
            writer.write(userInput1 + "," + userInput2 + "," + userInput3 + "," + userInput4);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Clear inputs
        festCombo.getSelectionModel().clearSelection();
        festCombo.getEditor().clear();
        start.setValue(null);
        end.setValue(null);
        dis.clear();
        warning.setText("");
    }
    public void delete_item(ActionEvent e2) {
        if (!"admin".equalsIgnoreCase(Session.getRole())) {
            showAlert("Access Denied", "Only admin is allowed to edit items.");
            return;
        }
        FDMClass selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
            deleteFromFile(selectedItem);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a row to delete.");
            alert.showAndWait();
        }}

    private void deleteFromFile(FDMClass item){
        File originalFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\FDMRecords");
        File tempFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\FDMRecordsTemp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length == 4) {
                    boolean isMatch = fields[0].trim().equals(item.getFestival().trim()) &&
                            fields[1].trim().equals(item.getStart_date().trim()) &&
                            fields[2].trim().equals(item.getEnd_date().trim()) &&
                            fields[3].trim().equals(item.getDiscount().trim());

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
        warning.setText("");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


