package org.openjfx.program.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateData {
    public String dataLocation;
    public ReturnData Return;
    public InsertData Insert;

    /**
     * Constructs a UpdateData object with the specified data location.
     *
     * @param dataLocation The location where the data is stored.
     */
    public UpdateData(String dataLocation, ReturnData Return, InsertData Insert) {
        this.dataLocation = dataLocation;
        this.Return = Return;
        this.Insert = Insert;
    }

    /**
     * Updates the balance for a user with the specified user ID.
     *
     * @param user_id The ID of the user whose balance is to be updated.
     * @param updated_balance The updated balance for the user.
     *
     * This method updates the balance for a user with the provided user ID in the "users" table of the database.
     */
    public void UpdateUserCash(int user_id, double updated_balance){
        Logger logger = Logger.getLogger("UpdateUserCashLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            // SQL statement to update the balance for a user with a specific user_id
            String updateBalanceSQL = "UPDATE users SET balance = ? WHERE id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceSQL)) {
                // Set values for the parameters in the prepared statement
                preparedStatement.setDouble(1, updated_balance);
                preparedStatement.setInt(2, user_id);

                // Execute the SQL statement to update the balance
                int rowsAffected = preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            // Log the SQLException
            logger.log(Level.SEVERE, "Error updating user balance", e);
        }
    }

    /**
     * Increases the number of copies sold for a book by one.
     *
     * @param bookId The ID of the book for which copies sold are to be increased.
     * This method updates the "copies_sold" field in the "book_details" table by incrementing it by one
     * for the book with the specified ID.
     * Any SQL exceptions encountered are logged using a logger named "UpdateDataLogger".
     */
    public void increaseBookSoldByOne(int bookId){
        Logger logger = Logger.getLogger("UpdateDataLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String updateCopiesSoldSQL = "UPDATE book_details SET copies_sold = copies_sold + 1 WHERE id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateCopiesSoldSQL)) {
                preparedStatement.setInt(1, bookId);

                // Execute the SQL statement to update copies_sold
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Copies sold updated successfully");
                } else {
                    System.out.println("No book found with the given ID");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating copies sold in book_details table", e);
        }
    }

    /**
     * Buys a book for a user.
     *
     * @param user_id The ID of the user who is buying the book.
     * @param book_id The ID of the book being bought.
     * @return True if the book is successfully bought, false otherwise.
     * This method buys a book for a user by performing the following steps:
     * 1. Checks if the user has enough cash to buy the book.
     * 2. If the user has enough cash, deducts the book price from the user's cash balance.
     * 3. Records the purchase by adding a new row for the bought book.
     * 4. Increases the number of copies sold for the book by one.
     * Returns true if the book is successfully bought, false otherwise.
     * Any SQL exceptions encountered are logged using appropriate loggers.
     */
    public boolean buyBook(int user_id, int book_id){
        double usersCash = Return.returnUserCash(user_id);
        double bookPrice = Return.returnBookPrice(book_id);

        if ((usersCash - bookPrice) < 0){
            System.out.println("Cash Not Enough");
        }
        else {
            System.out.println("Successfully bought");
            // Updates users cash
            UpdateUserCash(user_id, usersCash - bookPrice);
            // Record book (Make a new row for book bought)
            Insert.AddBoughtBook(book_id, user_id);
            // Add one to book sold
            increaseBookSoldByOne(book_id);
            return true;
        }
        return false;
    }


}
