package org.example.demo1.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;

public class SAMController {
    @FXML
    private TextArea textArea;
    @FXML
    private ComboBox<String> Route;
    @FXML
    private ListView<String> listView;

    @FXML
    private Button alert_add;
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
    private final ObservableList<String> items = FXCollections.observableArrayList();
    private final String FILE_PATH = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\java\\org\\example\\demo1\\util\\AlertRecord";  // Can be placed under project directory

    @FXML
    public void initialize() {
        loadListData();
        listView.setItems(items);

        // Populate Route ComboBox
        Route.setItems(FXCollections.observableArrayList(
                "KTM - PKR",
                "KTM - CHT",
                "PKR - BTW",
                "DHR - KTM"
        ));
    }

    @FXML
    private void handleAdd() {
        String route = Route.getValue();  // Get selected route
        String alertText = textArea.getText().trim();

        if (route != null && !alertText.isEmpty()) {
            String combined = "(" + route + ") - " + alertText;
            items.add(combined);
            textArea.clear();
            Route.getSelectionModel().clearSelection(); // Optional: Clear selection after add
            saveListData(); // Save updated list to file
        }
    }

    private void saveListData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadListData() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    items.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
