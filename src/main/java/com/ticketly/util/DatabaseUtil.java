package com.ticketly.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**

* Database utility class for managing database connections.
* Uses HikariCP for connection pooling.
  */
  public class DatabaseUtil {
  private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
  private static HikariDataSource dataSource;

  static {
  initializeDataSource();
  // ensure pool is closed on JVM exit
  Runtime.getRuntime().addShutdownHook(new Thread(DatabaseUtil::closeDataSource));
  }

  /**

  * Initialize the database connection pool.
    */
    private static void initializeDataSource() {
    try {
    Properties props = loadDatabaseProperties();

  
     String driver = props.getProperty("db.driver");
     String url = props.getProperty("db.url");
     String user = props.getProperty("db.username");
     String password = props.getProperty("db.password");

     if (driver == null || url == null || user == null) {
         throw new IllegalStateException("Missing required DB properties (db.driver, db.url, db.username). " +
                 "Make sure database.properties is on the classpath (src/main/resources).");
     }

     HikariConfig config = new HikariConfig();
     config.setDriverClassName(driver);
     config.setJdbcUrl(url);
     config.setUsername(user);
     config.setPassword(password);

     // Connection pool settings (with safe defaults)
     config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.connection.pool.max", "20")));
     config.setMinimumIdle(Integer.parseInt(props.getProperty("db.connection.pool.initial", "5")));
     config.setConnectionTimeout(Long.parseLong(props.getProperty("db.connection.timeout", "30000")));
     config.setPoolName(props.getProperty("db.pool.name", "TicketlyPool"));
     // Optional: leak detection threshold (0 = disabled)
     long leakMs = Long.parseLong(props.getProperty("db.connection.leakDetectionThreshold", "0"));
     if (leakMs > 0) config.setLeakDetectionThreshold(leakMs);

     // Prepared statement cache properties (MySQL recommended)
     config.addDataSourceProperty("cachePrepStmts", props.getProperty("db.cachePrepStmts", "true"));
     config.addDataSourceProperty("prepStmtCacheSize", props.getProperty("db.prepStmtCacheSize", "250"));
     config.addDataSourceProperty("prepStmtCacheSqlLimit", props.getProperty("db.prepStmtCacheSqlLimit", "2048"));

     dataSource = new HikariDataSource(config);
     logger.info("Database connection pool initialized successfully (url={}, user={})",
             stripUrlParams(url), user);
    

    } catch (Exception e) {
    logger.error("Failed to initialize database connection pool", e);
    throw new RuntimeException("Database initialization failed", e);
    }
    }

  /**

  * Load database properties from configuration file.
    */
    private static Properties loadDatabaseProperties() throws IOException {
    Properties props = new Properties();
    try (InputStream input = DatabaseUtil.class.getClassLoader()
    .getResourceAsStream("database.properties")) {
    if (input == null) {
    throw new IOException("database.properties not found in classpath (expected in src/main/resources/)");
    }
    props.load(input);
    }
    return props;
    }

  /**

  * Get a database connection from the pool.
    */
    public static Connection getConnection() throws SQLException {
    if (dataSource == null) {
    throw new SQLException("DataSource not initialized");
    }
    return dataSource.getConnection();
    }

  /**

  * Get the data source.
    */
    public static DataSource getDataSource() {
    return dataSource;
    }

  /**

  * Test the database connection by executing SELECT 1.
    */
    public static boolean testConnection() {
    try (Connection conn = getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT 1")) {

    
     if (rs.next()) {
         logger.info("Database connection test successful: {}", rs.getInt(1));
         return true;
     }
    

    } catch (SQLException e) {
    logger.error("Database connection test failed", e);
    }
    return false;
    }

  /**

  * Close the data source when application shuts down.
    */
    public static void closeDataSource() {
    if (dataSource != null) {
    try {
    dataSource.close();
    logger.info("Database connection pool closed");
    } catch (Exception e) {
    logger.warn("Error while closing data source", e);
    } finally {
    dataSource = null;
    }
    }
    }

  /**

  * Utility to remove query parameters so logs don't leak credentials.
    */
    private static String stripUrlParams(String url) {
    if (url == null) return null;
    int q = url.indexOf('?');
    return q >= 0 ? url.substring(0, q) : url;
    }
    }
