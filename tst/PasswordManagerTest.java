import org.junit.*;

import backend.DatabaseCommand;
import backend.DatabaseConnection;
import backend.PasswordManager;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PasswordManagerTest {

    PasswordManager pwManager = new PasswordManager();
    Connection connection = DatabaseConnection.getConnection();
    DatabaseCommand dbCommand = new DatabaseCommand(connection);

    @Before
    public void init() {
        pwManager.init();
    }

    @After
    public void clean() {
        File file = new File("resources/database.db");
        file.delete();
    }

    @Test
    public void checkAlreadyExistTest() {
        pwManager.addPassword("MasterPassword", "Gmail", "GmailPassword");
        assert(dbCommand.checkAlreadyExist("Gmail") == true);
    }

    @Test
    public void checkAlreadyExistDoesNotExistTest() {
        pwManager.init();
        assert(dbCommand.checkAlreadyExist("Hotmail") == false);
    }

    @Test
    public void insertPasswordTest() throws SQLException {
        dbCommand.insertPassword("Gmail", "GmailPassword");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM passwords WHERE name = 'Gmail'");
        assertTrue(rs.getString(1).equals("Gmail"));
        assertTrue(rs.getString(2).equals("GmailPassword"));
    }

    @Test
    public void insertPasswordDuplicateFailTest() {
        try {
            dbCommand.insertPassword("Gmail", "GmailPassword");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM passwords WHERE name = 'Gmail'");
            assertTrue(rs.getString(1).equals("Gmail"));
            assertTrue(rs.getString(2).equals("GmailPassword"));
            dbCommand.insertPassword("Gmail", "GmailPassword");
        } catch (SQLException e) {
            System.out.println("TRYING TO INSERT DUPLICATE");
        }
    }

    @Test
    public void selectPasswordSuccessTest() throws SQLException {
        dbCommand.insertPassword("Gmail", "GmailPassword");
        assert(dbCommand.selectPassword("Gmail").equals("GmailPassword"));
    }

    @Test
    public void retrievePasswordTest() throws SQLException {
        dbCommand.insertPassword("Gmail", "GmailPassword");
        assert(pwManager.retrievePassword("MasterPassword", "Gmail").equals("GmailPassword"));
    }

    @Test
    public void addMasterPasswordTest() throws IOException {
        pwManager.addMasterPassword("MasterPassword");
        BufferedReader reader = new BufferedReader(new FileReader("resources/master.txt"));
        String string;
        while ((string = reader.readLine()) != null) {
            System.out.println(string);
        }
        // delete the file
        File file = new File("resources/master.txt");
        file.delete();
    }

    @Test
    public void checkMasterPasswordTest() throws IOException {
        pwManager.addMasterPassword("MasterPassword");
        BufferedReader reader = new BufferedReader(new FileReader("resources/master.txt"));
        String string;
        while ((string = reader.readLine()) != null) {
            System.out.println(string);
        }
        assert(pwManager.checkMasterPassword("MasterPassword"));
    }

    @Test
    public void checkMasterPasswordExistFalseTest() {
        assertFalse(pwManager.checkMasterPasswordExist());
    }

    @Test
    public void checkMasterPasswordExistTrueTest() throws IOException {
        File file = new File("resources/master.txt");
        file.createNewFile();
        assertTrue(pwManager.checkMasterPasswordExist());
        file.delete();
    }

    @Test
    public void checkStringTest() {
        assertTrue(pwManager.checkString("Hello@13"));
        assertTrue(pwManager.checkString("HHHDJkskd$%&1"));
    }

    @Test
    public void checkStringFalseTest() {
        assertFalse(pwManager.checkString("Hello1234"));
        assertFalse(pwManager.checkString("Hd3#"));
        assertFalse(pwManager.checkString("asdfghjkl"));
    }
}
