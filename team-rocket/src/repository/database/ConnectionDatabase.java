package repository.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDatabase {

    public static Connection connect(String jdbcURL, String username, String password) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("ssl", "require");
        return DriverManager.getConnection(jdbcURL, properties);
    }
}
