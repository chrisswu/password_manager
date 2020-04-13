package backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PasswordManager {

    public static void init() {
        try {
            String passwordTable = "CREATE TABLE IF NOT EXISTS passwords(\n"
                    + "    name text PRIMARY KEY,\n"
                    + "    password text NOT NULL\n"
                    + ");";
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(passwordTable);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public static String retrievePassword(String masterPassword, String name) {
        try {
            if (checkMasterPassword(masterPassword)) {
                Connection connection = DatabaseConnection.getConnection();
                DatabaseCommand dbCommand = new DatabaseCommand(connection);
                return Encryption.decrypt(dbCommand.selectPassword(name), masterPassword);
            } else {
                return "Incorrect master password";
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public String addPassword(String masterPassword, String name, String password) {
        try {
            if (checkMasterPassword(masterPassword)) {
                Connection connection = DatabaseConnection.getConnection();
                DatabaseCommand dbCommand = new DatabaseCommand(connection);
                if (dbCommand.checkAlreadyExist(name)) return "Account name already exists";
                else {
                    dbCommand.insertPassword(name, Encryption.encrypt(password, masterPassword));
                    return "Account added successfully";
                }
            } else {
                return "Incorrect master password";
            }
        } catch (SQLException e) {

        }
        return null;
    }

    // TODO: Master password specifications: 8-20 characters, Upper, Special
    // Special: !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
    public static void addMasterPassword(String masterPassword) {
        try {
            FileWriter fileWriter = new FileWriter("resources/master.txt");
            fileWriter.write(PasswordHash.getSaltedHash(masterPassword));
            fileWriter.close();
        } catch (Exception e) {}
    }

    public static boolean checkMasterPassword(String masterPassword) {
        try {
            File file = new File("resources/master.txt");
            BufferedReader reader = new BufferedReader(new FileReader("resources/master.txt"));
            return PasswordHash.checkPassword(masterPassword, reader.readLine());
        } catch (Exception e) {}
        return false;
    }

    public static boolean checkMasterPasswordExist() {
        File file = new File("resources/master.txt");
        return file.exists();
    }

    public static boolean checkString(String str) {
        char ch;
        String special = "!@#$%&*()_+=|<>?{}[]~-";
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean specialFlag = false;
        if (str.length() >= 8 && str.length() <= 20) {
            for(int i = 0; i < str.length(); i++) {
                ch = str.charAt(i);
                if(Character.isDigit(ch)) {
                    numberFlag = true;
                } else if (Character.isUpperCase(ch)) {
                    capitalFlag = true;
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true;
                } else if (special.indexOf(ch) != -1) {
                    specialFlag = true;
                }
                if(numberFlag && capitalFlag && lowerCaseFlag && specialFlag)
                    return true;
            }
        }
        return false;
    }
}
