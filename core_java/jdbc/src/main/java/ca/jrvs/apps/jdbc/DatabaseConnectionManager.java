package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {

  private static String url = null;
  private static Properties properties = null;
  private static Connection _connection;
  public DatabaseConnectionManager(String host, String databaseName, String userName,
      String password) {
    url = "jdbc:postgresql://" + host + "/" + databaseName;
    properties = new Properties();
    this.properties.setProperty("user", userName);
    this.properties.setProperty("password", password);
  }

  public static Connection getConnection() throws SQLException {
    if (_connection == null) {
      _connection = DriverManager.getConnection(url, properties);
    }
    return _connection;
  }
}
