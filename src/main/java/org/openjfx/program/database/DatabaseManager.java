package org.openjfx.program.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager class serves as the parent class for managing database connections and operations.
 */
@SuppressWarnings("SqlDialectInspection")
public class DatabaseManager {

    /**
     * The location where the data is stored.
     */
    public String dataLocation;
    public InsertData Insert;
    public CheckData Check;
    public UpdateData Update;
    public ReturnData Return;
    public LoginData Login;


    /**
     * Constructs a DatabaseManager object to manage the specified database.
     *
     * @param dataLocation The location where the database is stored.
     * This constructor initializes the DatabaseManager with the provided data location and creates instances
     * of CheckData, InsertData, UpdateData, and ReturnData to handle different database operations.
     */
    public DatabaseManager(String dataLocation) {
        this.dataLocation = dataLocation;
        this.Check = new CheckData(this.dataLocation);
        this.Return = new ReturnData(this.dataLocation, this.Check);
        this.Insert = new InsertData(this.dataLocation, this.Check, this.Return);
        this.Update = new UpdateData(this.dataLocation, this.Return, this.Insert, this.Check);
        this.Insert.InsertDataAfter(this.Update);
        this.Login = new LoginData(this.dataLocation);
    }

    /**
     * Creates all required tables for the database.
     * Ensures normalization such as 1NF, 2NF, and referential integrity.
     * This method creates the following tables:
     * - "users" table: Contains user information such as email, username, password, admin status, and balance.
     * - "book_bought" table: Maps users to the books they have bought.
     * - "book_genre" table: Stores the genres associated with each book.
     * - "book_details" table: Stores details about each book including title, image link, author ID, availability, price, and copies sold.
     * - "book_description" table: Stores descriptions for each book.
     * - "author" table: Stores information about authors including user ID and description.
     * - "book_reviews" table: Stores reviews for books including book ID, user ID, rating, review text, and ownership status.
     * - "book_owned" table: Maps users to books they own.
     * Foreign key constraints are used to maintain referential integrity between tables.
     */
    public void createTablesIfNotExist() {
        Logger logger = Logger.getLogger("InsertDataLogger");
        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             Statement statement = connection.createStatement()) {

            // SQL statements to create tables if they do not exist
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "email TEXT NOT NULL,"
                    + "username TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "is_admin BOOLEAN DEFAULT 0,"
                    + "balance REAL DEFAULT 0.0"
                    + ");";

            String createBookGenreTableSQL = "CREATE TABLE IF NOT EXISTS book_genre ("
                    + "title TEXT,"
                    + "genre TEXT"
                    + ");";

            String createBookDetailsTableSQL = "CREATE TABLE IF NOT EXISTS book_details ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "image_link TEXT,"
                    + "author_id INTEGER,"
                    + "is_available BOOLEAN DEFAULT 1,"
                    + "price REAL DEFAULT 0.0,"
                    + "copies_sold INTEGER DEFAULT 0,"
                    + "FOREIGN KEY (author_id) REFERENCES author (id)"
                    + ");";

            String createBookDescriptionTableSQL = "CREATE TABLE IF NOT EXISTS book_description ("
                    + "description_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "book_title TEXT,"
                    + "description TEXT,"
                    + "FOREIGN KEY (book_title) REFERENCES book_details (title)"
                    + ");";
            String createBookMetaDataSQL = "CREATE TABLE IF NOT EXISTS book_metadata ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "isbn TEXT NOT NULL,"
                    + "publication_date INTEGER,"
                    + "language TEXT NOT NULL,"
                    + "book_title TEXT NOT NULL,"
                    + "FOREIGN KEY (book_title) REFERENCES book_details (title)"
                    + ");";
        
            String createAuthorTableSQL = "CREATE TABLE IF NOT EXISTS author ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER,"
                    + "description TEXT,"
                    + "FOREIGN KEY (user_id) REFERENCES users (id)"
                    + ");";

            String createReviewTableSQL = "CREATE TABLE IF NOT EXISTS book_reviews ("
                    + "book_id INTEGER,"
                    + "user_id INTEGER,"
                    + "rating INTEGER,"
                    + "review TEXT,"
                    + "is_owned BOOLEAN,"
                    + "FOREIGN KEY (book_id) REFERENCES book_details (id),"
                    + "FOREIGN KEY (user_id) REFERENCES users (id)"
                    + ");";

            String createRatingTableSQL = "CREATE TABLE IF NOT EXISTS book_rating ("
                    + "book_id INT,"
                    + "user_id INT,"
                    + "rating INT,"
                    + "FOREIGN KEY (book_id) REFERENCES book_details(id),"
                    + "FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")";

            String createBookReviewTableSQL = "CREATE TABLE IF NOT EXISTS book_text_review ("
                    + "book_id INT,"
                    + "user_id INT,"
                    + "review TEXT,"
                    + "FOREIGN KEY (book_id) REFERENCES book_details(id),"
                    + "FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")";


            String createOwnedTableSQL = "CREATE TABLE IF NOT EXISTS book_owned ("
                    + "book_id INTEGER,"
                    + "user_id INTEGER,"
                    + "is_owned BOOLEAN,"
                    + "FOREIGN KEY (book_id) REFERENCES book_details(id),"
                    + "FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ");";

            // Execute the SQL statements
            statement.execute(createUserTableSQL);
            statement.execute(createBookDescriptionTableSQL);
            statement.execute(createBookGenreTableSQL);
            statement.execute(createBookDetailsTableSQL);
            statement.execute(createBookMetaDataSQL);
            statement.execute(createAuthorTableSQL);
            statement.execute(createReviewTableSQL);
            statement.execute(createOwnedTableSQL);
            statement.execute(createRatingTableSQL);
            statement.execute(createBookReviewTableSQL);



            System.out.println("Tables created successfully");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error create tables", e);
        }
    }

}