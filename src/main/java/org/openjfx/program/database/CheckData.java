package org.openjfx.program.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class CheckData {
    public String dataLocation;

    /**
     * Constructs a CheckData object with the specified data location.
     *
     * @param dataLocation The location where the data is stored.
     */
    public CheckData(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    /**
     * Checks if a description exists for a given book title in the database.
     *
     * @param bookTitle The title of the book to check for the existence of description.
     * @return true if a description exists for the book title, false otherwise.
     */
    public boolean checkIfDescriptionExists(String bookTitle) {
        boolean exists = false;

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM book_description WHERE book_title = ?")) {

            preparedStatement.setString(1, bookTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            // If any rows are returned by the query, it means the description exists
            exists = resultSet.next();

        } catch (SQLException exception) {
            System.err.println("Error: " + exception.getMessage());
        }
        return exists;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username The username to check for existence.
     * @return true if the username already exists, false otherwise.
     * This method executes a SQL query to count the number of occurrences of the given username in the "users" table.
     * It returns true if the count is greater than 0, indicating that the username already exists in the database,
     * otherwise, it returns false.
     */
    public boolean CheckIfUserNameAlreadyExist(String username) {
        Logger logger = Logger.getLogger("InsertDataLogger");
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Username checker error", e);
        }
        return false;
    }

    /**
     * Checks if an email already exists in the database.
     *
     * @param email The email to check for existence.
     * @return true if the email already exists, false otherwise.
     * This method executes a SQL query to count the number of occurrences of the given email in the "users" table.
     * It returns true if the count is greater than 0, indicating that the email already exists in the database,
     * otherwise, it returns false.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public boolean CheckIfEmailAlreadyExist(String email) {
        Logger logger = Logger.getLogger("CheckIfEmailAlreadyExist");
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Email Checker error", e);
        }
        return false;
    }

    /**
     * Checks if a book was bought by a specific user.
     *
     * @param bookId the ID of the book to check
     * @param userId the ID of the user
     * @return true if the book was bought by the user, false otherwise
     *          This method queries the "book_owned" table in the database to check if the specified book
     *          was bought by the specified user. It returns true if the book was bought, false otherwise.
     */
    public boolean CheckIfBookWasBought(int bookId, int userId){
        Logger logger = Logger.getLogger("CheckIfBookWasBought");
        String query = "SELECT COUNT(*) FROM book_owned WHERE book_id = ? AND user_id = ?";
        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "CheckIfBookWasBought error", e);
        }
        return false;
    }

}
