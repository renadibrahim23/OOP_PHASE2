module com.example.phase2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.phase2 to javafx.fxml;
    exports com.example.phase2;
    exports GUI;

    opens Entity to javafx.base;
    exports Entity;

}