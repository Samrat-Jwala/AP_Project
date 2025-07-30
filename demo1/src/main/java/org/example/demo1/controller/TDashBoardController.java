package org.example.demo1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;

public class TDashBoardController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<String, Number> lineChart;
    private Stage stage;
    private Scene scene;
    private Parent root;

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
        setupBarChart();
        setupPieChart();
        setupLineChart();
    }

    private void setupBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");

        series.getData().add(new XYChart.Data<>("Nepal", 1500));
        series.getData().add(new XYChart.Data<>("India", 1200));
        series.getData().add(new XYChart.Data<>("USA", 800));
        series.getData().add(new XYChart.Data<>("Germany", 600));

        barChart.getData().add(series);
    }

    private void setupPieChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Annapurna", 35),
                new PieChart.Data("Everest", 25),
                new PieChart.Data("Langtang", 20),
                new PieChart.Data("Manaslu", 15),
                new PieChart.Data("Other", 5)
        );
        pieChart.setData(data);
    }

    private void setupLineChart() {
        XYChart.Series<String, Number> bookings = new XYChart.Series<>();
        bookings.setName("Monthly Bookings");

        bookings.getData().add(new XYChart.Data<>("Jan", 300));
        bookings.getData().add(new XYChart.Data<>("Feb", 500));
        bookings.getData().add(new XYChart.Data<>("Mar", 800));
        bookings.getData().add(new XYChart.Data<>("Apr", 400));
        bookings.getData().add(new XYChart.Data<>("May", 700));

        lineChart.getData().add(bookings);
    }
}