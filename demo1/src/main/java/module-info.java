module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.demo1 to javafx.fxml;
    exports org.example.demo1;
    exports org.example.demo1.controller;
    opens org.example.demo1.controller to javafx.fxml;
    exports org.example.demo1.Model;
    opens org.example.demo1.Model to javafx.fxml;
}