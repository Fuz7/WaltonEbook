module org.openjfx.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j.nop;
    requires org.apache.pdfbox;

    opens org.openjfx.program to javafx.fxml;
    exports org.openjfx.program;
    exports org.openjfx.program.controller;
    exports org.openjfx.program.security;
    exports org.openjfx.program.model;
    exports org.openjfx.program.database;
    opens org.openjfx.program.controller to javafx.fxml;
    opens org.openjfx.program.database to javafx.fxml;
}