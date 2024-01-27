module org.openjfx.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j.nop;

    opens org.openjfx.program to javafx.fxml;
    exports org.openjfx.program;
    exports org.openjfx.program.controller;
    opens org.openjfx.program.controller to javafx.fxml;
}