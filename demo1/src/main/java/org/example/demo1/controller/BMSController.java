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
import org.example.demo1.Model.BMSCLASS;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class BMSController implements Initializable {

    public TableView<BMSCLASS> tableView;

    public TableColumn<BMSCLASS,String> tourist;
    public TableColumn<BMSCLASS,String> guide;
    public TableColumn<BMSCLASS,String> date;
    public TableColumn<BMSCLASS,String> discount;

    @FXML
    private Button Back;
    @FXML
    private Button Book;
    @FXML
    private Button Remove;
    @FXML
    private ComboBox<String> TouristCombo;
    @FXML
    private ComboBox<String> GuideCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton yes;
    @FXML
    private RadioButton no;

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
        loadTourists("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\TMSRecord");
        loadGuides("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\GMSRecord");

        ToggleGroup discountGroup = new ToggleGroup();
        yes.setToggleGroup(discountGroup);
        no.setToggleGroup(discountGroup);

        tourist.setCellValueFactory(new PropertyValueFactory<>("tourist"));
        guide.setCellValueFactory(new PropertyValueFactory<>("guide"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        ObservableList<BMSCLASS> observableList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\BMSRecords"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    observableList.add(new BMSCLASS(fields[0], fields[1], fields[2], fields[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableView.setItems(observableList);
    }
    public void delete_item(ActionEvent e2){
        BMSCLASS selectedItem = tableView.getSelectionModel().getSelectedItem();
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
    private void loadTourists(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    TouristCombo.getItems().add(parts[0]);
                } else {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadGuides(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    GuideCombo.getItems().add(parts[0]);
                } else {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getFestivalDiscount(LocalDate selectedDate) {
        File file = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\FDMRecords");
        int maxDiscount = 0;
        boolean discountFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    LocalDate startDate = LocalDate.parse(parts[1].trim());
                    LocalDate endDate = LocalDate.parse(parts[2].trim());
                    int discount = Integer.parseInt(parts[3].trim());

                    if (!selectedDate.isBefore(startDate) && !selectedDate.isAfter(endDate)) {
                        maxDiscount = Math.max(maxDiscount, discount);
                        discountFound = true;
                    }
                }
            }
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        if (!discountFound) {
            showAlert1(Alert.AlertType.INFORMATION, "No Discount", "No festival discount available for the selected date.");
        }

        return maxDiscount;
    }

    public void add_item(ActionEvent e1){
        String selectedTourist = TouristCombo.getValue();
        String selectedGuide = GuideCombo.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedTourist == null || selectedGuide == null || selectedDate == null) {
            showAlert("Please fill all fields (Tourist, Guide, Date).");
            return;
        }

        for (BMSCLASS record : tableView.getItems()) {
            if (record.getTourist().equals(selectedTourist) &&
                    record.getGuide().equals(selectedGuide)) {
                showAlert("A booking already exists for this Tourist and Guide.");
                return;
            }
        }

        int discountValue = 0;
        if (yes.isSelected()) {
            discountValue = getFestivalDiscount(selectedDate);
        } else if (no.isSelected()) {
            discountValue = 0;
        } else {
            showAlert("Please select if discount is applicable (Yes/No).");
            return;
        }

        BMSCLASS newRecord = new BMSCLASS(selectedTourist, selectedGuide, selectedDate.toString(), String.valueOf(discountValue));
        tableView.getItems().add(newRecord);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\BMSRecords", true))) {
            writer.write(String.format("%s,%s,%s,%d\n", selectedTourist, selectedGuide, selectedDate, discountValue));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        showAlert("Booking added successfully!");

        TouristCombo.getSelectionModel().clearSelection();
        TouristCombo.getEditor().clear();
        GuideCombo.getSelectionModel().clearSelection();
        GuideCombo.getEditor().clear();
        datePicker.setValue(null);

        yes.setSelected(false);
        no.setSelected(false);
    }
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void showAlert1(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void deleteFromFile(BMSCLASS item) {
        File originalFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\BMSRecords");
        File tempFile = new File("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\BMSRecordstemp");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length == 4) {
                    boolean isMatch = fields[0].trim().equals(item.getTourist().trim()) &&
                            fields[1].trim().equals(item.getGuide().trim()) &&
                            fields[2].trim().equals(item.getDate().trim()) &&
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
    }
}

