package org.openjfx.program.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class LoginData {
    public String dataLocation;

    /**
     * Constructs a LoginData object with the specified data location.
     *
     * @param dataLocation The location where the data is stored.
     */
    public LoginData(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    /**
     * Returns the user ID based on the entered email and password for login.
     *
     * @param enteredEmail The email entered during login.
     * @param enteredPassword The password entered during login.
     * @return The user ID corresponding to the entered email and password, or -1 if no matching user is found or an error occurs.
     * This method executes a SQL query to retrieve the user ID from the "users" table based on the entered email and password.
     * It returns the user ID if a matching user is found, otherwise, it returns -1.
     * Any SQL exceptions encountered are logged using a logger named "ReturnUserIdByLogIn".
     */
    public int ReturnUserIdByLogIn(String enteredEmail, String enteredPassword) {
        Logger logger = Logger.getLogger("ReturnUserIdByLogIn");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String query = "SELECT id FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, enteredEmail);
                preparedStatement.setString(2, enteredPassword);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // If a record is found, set the userId to the retrieved ID
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error login", e);
        }

        return -1;
    }
}
