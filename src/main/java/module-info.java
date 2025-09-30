module ticketly {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Standard Java
    requires java.sql;
    requires java.desktop;

    // Logging
    requires org.slf4j;
    requires ch.qos.logback.classic;

    // HikariCP (automatic module)
    requires com.zaxxer.hikari;

    // Open your packages for reflection (JavaFX)
    opens com.ticketly to javafx.fxml;

    // Export your packages
    exports com.ticketly;
    exports com.ticketly.util;
}
