module org.openjfx.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.openjfx.demo to javafx.fxml;
    exports org.openjfx.demo;
}