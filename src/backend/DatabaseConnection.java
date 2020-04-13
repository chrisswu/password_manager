package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public DatabaseConnection() {}

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            String jarPath = "jdbc:sqlite:resources/database.db";
            System.out.println(jarPath);
            connection = DriverManager.getConnection(jarPath);
            return connection;
        } catch (SQLException e) {

        }
        return null;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
