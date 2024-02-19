package org.openjfx.program.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class InsertData {

    public String dataLocation;
    public CheckData check;
    public ReturnData Return;



    /**
     * Constructs a InsertData object with the specified data location.
     *
     * @param dataLocation The location where the data is stored.
     */
    public InsertData(String dataLocation, CheckData check, ReturnData Return) {
        this.dataLocation = dataLocation;
        this.check = check;
        this.Return = Return;

    }

    /**
     * Inserts a new user into the database.
     *
     * @param email The email of the new user.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param isAdmin A boolean indicating whether the new user is an admin or not.
     * @param balance The balance of the new user.
     *
     * This method inserts a new user with the provided information into the "users" table of the database.
     */
    public void InsertNewUser(String email, String username, String password, Boolean isAdmin, double balance) {
        Logger logger = Logger.getLogger("InsertDataLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            // SQL statement to insert data into the "user_data" table
            String insertUserDataSQL = "INSERT INTO users (email, username, password, is_admin, balance) VALUES (?, ?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserDataSQL)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setBoolean(4, isAdmin);
                preparedStatement.setDouble(5, balance);

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into user_data table successfully");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting data into user_data table", e);
        }
    }

    /**
     * Inserts a new book into the database.
     *
     * @param title The title of the new book.
     * @param imageName The image link of the new book.
     * @param genres An array of genres associated with the new book.
     * @param authorId The ID of the author of the new book.
     * @param availability A boolean indicating whether the new book is available or not.
     * @param bookPrice The price of the new book.
     * @param bookSold The number of copies sold of the new book.
     * @param description The description of the new book.
     * This method inserts a new book with the provided information into the "book_details" table of the database.
     * It also handles insertion of genres and description associated with the book.
     */
    public void InsertNewBook(String title, String imageName, String[] genres, int authorId, boolean availability, double bookPrice, int bookSold, String description){
        Logger logger = Logger.getLogger("InsertDataLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            // SQL statement to insert data into the "book_details" table
            String insertBookDetailsSQL = "INSERT INTO book_details (title, image_link, author_id, is_available, price, copies_sold) VALUES (?, ?, ?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookDetailsSQL)) {
                // Set values for the parameters in the prepared statement
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, imageName);
                preparedStatement.setInt(3, authorId);
                preparedStatement.setBoolean(4, availability);
                preparedStatement.setDouble(5, bookPrice);
                preparedStatement.setInt(6, bookSold);

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into book_details table successfully");

                // Insert the description to the book_description table
                InsertNewDescription(title, description);

                // Insert all genre of the specific book to a different table (1NF)
                for (String genre : genres){
                    InsertGenre(title, genre);
                }

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error inserting data into book_details table", e);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting data into book_details table", e);
        }
    }

    /**
     * Inserts a new description for a book into the database if it does not already exist.
     *
     * @param book_title The title of the book.
     * @param description The description of the book.
     * This method checks if a description already exists for the given book title. If not, it inserts the description
     * into the "book_description" table of the database.
     */
    public void InsertNewDescription(String book_title, String description){
        Logger logger = Logger.getLogger("InsertDataLogger");
        if (!(this.check.checkIfDescriptionExists(book_title))){
            try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
                // SQL statement to insert data into the "book_description" table
                String insertBookDetailsSQL = "INSERT INTO book_description (book_title , description) VALUES (?, ?);";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookDetailsSQL)) {
                    // Set values for the parameters in the prepared statement
                    preparedStatement.setString(1, book_title);
                    preparedStatement.setString(2, description);

                    // Execute the SQL statement to insert data
                    preparedStatement.executeUpdate();
                    System.out.println("Data inserted into book_description table successfully");

                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error inserting data into book_description table", e);
                }

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error inserting data into book_description table", e);
            }
        }
        else{
            System.out.println("DATA ALREADY EXIST");
        }
    }

    /**
     * Inserts a new genre for a book into the database.
     *
     * @param title The title of the book.
     * @param genre The genre of the book.
     * This method inserts a new genre associated with a book into the "book_genre" table of the database.
     * It logs any SQL exceptions using a logger named "InsertDataLogger".
     */
    public void InsertGenre(String title, String genre){
        Logger logger = Logger.getLogger("InsertDataLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            // SQL statement to insert data into the "user_data" table
            String insertUserDataSQL = "INSERT INTO book_genre (title, genre) VALUES (?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserDataSQL)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, genre);

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into user_data table successfully");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting data into user_data table", e);
        }

    }


    /**
     * Adds a bought book entry to the database.
     *
     * @param bookId The ID of the book being bought.
     * @param userId The ID of the user who bought the book.
     * This method inserts a new record into the "book_owned" table to indicate that a book has been bought by a user.
     * The method sets the "is_owned" field to true for the bought book entry.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public void AddBoughtBook(int bookId, int userId){
        Logger logger = Logger.getLogger("InsertDataLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String insertBookOwnedSQL = "INSERT INTO book_owned (book_id, user_id, is_owned) VALUES (?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookOwnedSQL)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.setInt(2, userId);
                preparedStatement.setBoolean(3, true);

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into book_owned table successfully");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting data into book_owned table", e);
        }

    }

    /**
     * Inserts a book review into the database.
     *
     * @param userId the ID of the user submitting the review
     * @param bookId the ID of the book being reviewed
     * @param rating the rating given to the book
     * @param reviewText the text of the review
     *          This method inserts a book review into the "book_reviews" table in the database.
     *          It includes the user ID, book ID, rating, review text, and whether the user owns the book.
     *          The review is inserted into the database after checking if the user owns the book.
     *          Any SQL exceptions encountered are logged using a logger named "InsertBookReview".
     */
    public void InsertBookReview(int userId, int bookId, int rating, String reviewText){
        Logger logger = Logger.getLogger("InsertBookReview");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            // SQL statement to insert data into the "book_reviews" table
            String insertBookReviewSQL = "INSERT INTO book_reviews (book_id, user_id, rating, review, is_owned) VALUES (?, ?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookReviewSQL)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, rating);
                preparedStatement.setString(4, reviewText);
                preparedStatement.setBoolean(5, this.check.CheckIfBookWasBought(bookId, userId));

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into book_reviews table successfully");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting data into book_reviews table", e);
        }
    }

    public void AdminBookInserter(String title, String imageLink, String[] genre, String authorName,
                                  boolean availability, double bookPrice, int bookSold, String description){

        int userId;
        int authorId;

        if (this.check.CheckIfUserNameAlreadyExist(authorName)){
            // Get user id
            userId = Integer.parseInt(Return.UserIdByUsername(authorName));
            // Get author id (safe to say if user exist then author id exist)
            authorId = Return.returnAuthorId(userId);
            System.out.println("Author already exist name = " + authorName);
        } else{
            // Insert User
            InsertNewUser("DefaultEmail@gmail.com", authorName, "DEFAULT_PASSWORD", false, 0);
            // Get the user id of new inserted user
            userId = Integer.parseInt(Return.UserIdByUsername(authorName));
            // Insert it to the author with blank description
            insertAuthor(userId, "");
            // Get the author id
            authorId = Return.returnAuthorId(userId);
            System.out.println("Author Not In Database. Making New User. Name = " + authorName + "Author Id = " + userId);


        }
        InsertNewBook(title, imageLink, genre, authorId, availability, bookPrice, bookSold, description);

    }


    public void insertAuthor(int userId, String description){
        Logger logger = Logger.getLogger("InsertBookReview");
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            // SQL statement to insert data into the "book_reviews" table
            String insertBookReviewSQL = "INSERT INTO author (user_id, description) VALUES (?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookReviewSQL)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, description);
                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into author table successfully");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting data into author table", e);
        }
    }



}
