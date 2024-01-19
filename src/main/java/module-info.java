module org.openjfx.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j.nop;

    opens org.openjfx.demo to javafx.fxml;
    exports org.openjfx.demo;
}