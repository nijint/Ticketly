module ticketly {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics; // <-- Add this for Stage

    // Standard Java
    requires transitive java.sql; // for Connection
    requires java.desktop;

    // Logging
    requires org.slf4j;
    requires ch.qos.logback.classic;
    

    // HikariCP (automatic module)
    requires com.zaxxer.hikari;

    // Open packages for reflection (FXML, SceneManager, etc.)
    opens com.ticketly to javafx.fxml;
    opens com.ticketly.util to javafx.fxml;

    // Export packages
    exports com.ticketly;
    exports com.ticketly.util;
}
