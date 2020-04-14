package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCommand {

    private static Connection connection;
    public DatabaseCommand(Connection connection) {
        this.connection = connection;
    }

    public static boolean checkAlreadyExist(String name) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM passwords WHERE name = '" + name + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                System.out.println("EXISTS");
                return true;
            } else {
                System.out.println("DOES NOT EXIST");
                return false;
            }
        } catch (SQLException e) {

        }
        return false;
    }

    public static void insertPassword(String name, String password) throws SQLException {
        Statement statement = connection.createStatement();
        StringBuilder query = new StringBuilder().append("INSERT INTO passwords(name, password)\n");
        query.append(String.format("VALUES('%s', '%s');\n", name, password));
        statement.executeUpdate(query.toString());
    }

    public static String selectPassword(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM passwords WHERE name = '" + name + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) return resultSet.getString(2);
        return null;
    }

    public static void deletePassword(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DELETE FROM passwords WHERE name = '" + name + "'";
        statement.executeUpdate(query);
    }

    public static void printCurrentTables() throws SQLException {
        // print tables
        ResultSet rs = connection.getMetaData().getTables(null, null, "%", null);
        while (rs.next()) System.out.println(rs.getString(3));
    }
}
