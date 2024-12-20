package org.openjfx.program.database;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReturnData {

    public String dataLocation;
    public CheckData Check;


    /**
     * Constructs a ReturnData object with the specified data location.
     *
     * @param dataLocation The location where the data is stored.
     */
    public ReturnData(String dataLocation, CheckData Check) {

        this.dataLocation = dataLocation;
        this.Check = Check;
    }

    /**
     * Returns the latest user ID from the database.
     *
     * @return The latest user ID, or -1 if an error occurs or no user ID is found.
     * This method executes a SQL query to retrieve the latest user ID from the "users" table.
     * It returns the latest user ID if found, otherwise, it returns -1.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public int ReturnLatestId(){
        Logger logger = Logger.getLogger("InsertDataLogger");
        int userId;
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt("id");
                        return userId;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Latest Id", e);
        }

        return -1;

    }

    /**
     * Returns the titles of the top three books based on copies sold.
     *
     * @return An array containing the titles of the top three books, or null if an error occurs.
     * This method executes a SQL query to retrieve the titles of the top three books based on the number of copies sold.
     * It returns an array containing the titles of the top three books if found, otherwise, it returns null.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public String[] ReturnTopThreeBookTitle() {
        Logger logger = Logger.getLogger("InsertDataLogger");
        String[] topBookIds = new String[3];

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT title FROM book_details ORDER BY copies_sold DESC LIMIT 3");
             ResultSet resultSet = statement.executeQuery()) {

            for (int i = 0; i < 3; i++){
                if (resultSet.next()){
                    topBookIds[i] = resultSet.getString("title");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error ReturnTopThreeBooks", e);
        }

        return topBookIds;
    }


    /**
     * Returns the ID of a book based on its title.
     *
     * @param title The title of the book.
     * @return The ID of the book, or -1 if the book is not found or an error occurs.
     * This method executes a SQL query to retrieve the ID of the book with the given title from the "book_details" table.
     * It returns the ID of the book if found, otherwise, it returns -1.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public int returnBookIdByTitle(String title) {
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id FROM book_details WHERE title = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, title);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Latest Id", e);
        }

        return -1;
    }


    /**
     * Returns the description of a book based on its title.
     *
     * @param title The title of the book.
     * @return The description of the book, or null if the book is not found or an error occurs.
     * This method executes a SQL query to retrieve the description of the book with the given title
     * from the "book_description" table. It returns the description of the book if found, otherwise, it returns null.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public String returnBookDescriptionByTitle(String title){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT description FROM book_description WHERE book_title = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, title);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("description");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Latest Id", e);
        }

        return null;
    }

    /**
     * Returns the genres of a book based on its title.
     *
     * @param title The title of the book.
     * @return An array containing the genres of the book, or an empty array if the book is not found or an error occurs.
     * This method executes a SQL query to retrieve the genres of the book with the given title
     * from the "book_genre" table. It returns an array containing the genres of the book if found,
     * otherwise, it returns an empty array.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public String[] returnBookGenreByTitle(String title){
        Logger logger = Logger.getLogger("InsertDataLogger");
        List<String> genres = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT genre FROM book_genre WHERE title = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, title);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        genres.add(resultSet.getString("genre"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Book Genre By Title", e);
        }
        return genres.toArray(new String[0]); // Converts arraylist to array
    }

    /**
     * Returns all user data from the database.
     *
     * @return A 2D array containing all user data, or an empty array if no users are found or an error occurs.
     * This method executes a SQL query to retrieve all user data from the "users" table.
     * It returns a 2D array containing all user data if users are found, otherwise, it returns an empty array.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public String[][] returnAllUsers() {
        Logger logger = Logger.getLogger("InsertDataLogger");
        List<String[]> userDataList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Create an array to hold user data
                        String[] userData = new String[6]; // Assuming 6 columns in the "users" table
                        // Populate the array with user data from the result set
                        userData[0] = Integer.toString(resultSet.getInt("id"));
                        userData[1] = resultSet.getString("email");
                        userData[2] = resultSet.getString("username");
                        userData[3] = resultSet.getString("password");
                        userData[4] = resultSet.getBoolean("is_admin") ? "true" : "false";
                        userData[5] = Double.toString(resultSet.getDouble("balance"));
                        // Add the array to the list
                        userDataList.add(userData);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Users", e);
        }

        // Convert the list to a 2D array
        String[][] userDataArray = new String[userDataList.size()][];
        userDataList.toArray(userDataArray);

        return userDataArray;
    }

    /**
     * Returns all book data from the database.
     *
     * @return A 2D array containing all book data, or an empty array if no books are found or an error occurs.
     * This method executes a SQL query to retrieve all book data from the "book_details" table.
     * It returns a 2D array containing all book data if books are found, otherwise, it returns an empty array.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public String[][] returnAllBooks() {
        Logger logger = Logger.getLogger("InsertDataLogger");
        List<String[]> bookData = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT * FROM book_details";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Create an array to hold book data
                        String[] bookRow = new String[9]; // Assuming 7 columns in the "book_details" table
                        // Populate the array with book data from the result set
                        bookRow[0] = Integer.toString(resultSet.getInt("id"));
                        bookRow[1] = resultSet.getString("title");
                        bookRow[2] = resultSet.getString("image_link");
                        bookRow[3] = Integer.toString(resultSet.getInt("author_id"));
                        bookRow[4] = resultSet.getBoolean("is_available") ? "true" : "false";
                        bookRow[5] = Double.toString(resultSet.getDouble("price"));
                        bookRow[6] = Integer.toString(resultSet.getInt("copies_sold"));
                        bookRow[7] = returnBookDescriptionByTitle(bookRow[1]);
                        String[] genres = returnBookGenreByTitle(bookRow[1]);
                        if (genres != null && genres.length > 0) {
                            // Join genres into a single string
                            StringBuilder genreStringBuilder = new StringBuilder();
                            for (String genre : genres) {
                                genreStringBuilder.append(genre).append(", ");
                            }
                            // Remove the last comma and space
                            String genreString = genreStringBuilder.substring(0, genreStringBuilder.length() - 2);
                            bookRow[8] = genreString;
                        } else {
                            bookRow[8] = ""; // No genres found
                        }
                        // Add the array to the list
                        bookData.add(bookRow);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Books", e);
        }

        // Convert the list to a 2D array
        String[][] bookDataArray = new String[bookData.size()][];
        bookData.toArray(bookDataArray);

        return bookDataArray;
    }


    /**
     * Returns the user ID based on the username.
     *
     * @param username The username of the user.
     * @return The user ID corresponding to the username, or null if the username is not found or an error occurs.
     * This method executes a SQL query to retrieve the user ID from the "users" table based on the provided username.
     * It returns the user ID if the username is found, otherwise, it returns null.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public String UserIdByUsername(String username){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return String.valueOf(resultSet.getInt("id"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning id", e);
        }

        return null;
    }

    /**
     * Returns the balance (cash) of a user based on the user ID.
     *
     * @param userId The ID of the user.
     * @return The balance (cash) of the user with the given ID, or 0.0 if no such user is found or an error occurs.
     * This method retrieves the balance (cash) of the user with the provided user ID from the "users" table in the database.
     * It returns the balance if the user is found, otherwise, it returns 0.0.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public double returnUserCash(int userId){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT balance FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("balance");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning cash", e);
        }
        return -0.0;
    }

    public String returnUserName(int userId){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT username FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning cash", e);
        }
        return "";
    }


    /**
     * Returns the price of a book based on the book ID.
     *
     * @param bookId The ID of the book.
     * @return The price of the book with the given ID, or -0.0 if no such book is found or an error occurs.
     * This method retrieves the price of the book with the provided book ID from the "book_details" table in the database.
     * It returns the price if the book is found, otherwise, it returns -0.0.
     * Any SQL exceptions encountered are logged using a logger named "InsertDataLogger".
     */
    public double returnBookPrice(int bookId){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT price FROM book_details WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("price");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning book price", e);
        }
        return -0.0;
    }

    /**
     * Returns an array of book titles based on the given genres.
     *
     * @param genre an array of genre strings
     * @return an array of book titles matching the specified genres
     *          This method queries the "book_genre" table in the database to retrieve book titles
     *          associated with the provided genres. It returns an array containing these titles.
     *          Any SQL exceptions encountered are logged using a logger named "ReturnBookTitleByGenre".
     */
    public String[] returnBookTitleByGenre(String[] genre) {
        Logger logger = Logger.getLogger("ReturnBookTitleByGenre");
        Set<String> set = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT title FROM book_genre WHERE genre = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (String s : genre) {
                    preparedStatement.setString(1, s);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (!resultSet.isBeforeFirst()) {
                            set.clear();
                        } else {
                            while (resultSet.next()) {
                                set.add(resultSet.getString("title"));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning book titles by genre", e);
        }

        // Convert set to array
        return set.toArray(new String[0]);
    }

    /**
     * Returns an array containing specific details of a book based on the book title.
     *
     * @param bookTitle the title of the book
     * @return an array containing specific details of the book, including ID, title, image link, author ID,
     *          availability, price, number of copies sold, description, and genres
     *          This method queries the "book_details" table in the database to retrieve specific details
     *          of the book with the provided title. It returns an array containing these details.
     *          Any SQL exceptions encountered are logged using a logger named "SpecificBookDetails".
     */
    public String[] returnSpecificBookDetailsByTitle(String bookTitle) {
        Logger logger = Logger.getLogger("SpecificBookDetails");
        String[] bookRow = new String[9]; // Create an array to hold book data

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT * FROM book_details WHERE title = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, bookTitle); // Set the bookTitle parameter
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Populate the array with book data from the result set
                        bookRow[0] = Integer.toString(resultSet.getInt("id"));
                        bookRow[1] = resultSet.getString("title");
                        bookRow[2] = resultSet.getString("image_link");
                        bookRow[3] = Integer.toString(resultSet.getInt("author_id"));
                        bookRow[4] = resultSet.getBoolean("is_available") ? "true" : "false";
                        bookRow[5] = Double.toString(resultSet.getDouble("price"));
                        bookRow[6] = Integer.toString(resultSet.getInt("copies_sold"));
                        bookRow[7] = returnBookDescriptionByTitle(bookRow[1]);
                        String[] genres = returnBookGenreByTitle(bookRow[1]);
                        if (genres != null && genres.length > 0) {
                            // Join genres into a single string
                            StringBuilder genreStringBuilder = new StringBuilder();
                            for (String genre : genres) {
                                genreStringBuilder.append(genre).append(", ");
                            }
                            // Remove the last comma and space
                            String genreString = genreStringBuilder.substring(0, genreStringBuilder.length() - 2);
                            bookRow[8] = genreString;
                        } else {
                            bookRow[8] = ""; // No genres found
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Specific Book Details", e);
        }

        return bookRow;
    }

    /**
     * Returns a string representation of genres associated with a book.
     *
     * @param bookRow an array containing details of a book, including its title
     * @return a string containing genres associated with the book, separated by commas
     *          This method retrieves the genres associated with the book specified in the provided bookRow array
     *          and returns them as a single string, with genres separated by commas. If no genres are found,
     *          an empty string is returned.
     */

    public String getGenreString(String[] bookRow) {
        String[] genres = returnBookGenreByTitle(bookRow[1]);
        if (genres != null && genres.length > 0) {
            // Join genres into a single string
            StringBuilder genreStringBuilder = new StringBuilder();
            for (String genre : genres) {
                genreStringBuilder.append(genre).append(", ");
            }
            // Remove the last comma and space
            return genreStringBuilder.substring(0, genreStringBuilder.length() - 2);
        } else {
            return ""; // No genres found
        }
    }

    /**
     * Returns an array of arrays containing details of books matching the specified genres.
     *
     * @param genre an array of genre strings
     * @return an array of arrays containing details of books matching the specified genres
     *          This method retrieves books matching the provided genres by querying the database.
     *          It then returns an array of arrays containing specific details of each matched book.
     *          Each inner array represents the details of a single book.
     */
    public String[][] returnBookDetailsByGenre(String[] genre){
        List<String[]> bookData = new ArrayList<>();
        String[] bookMatchedByGenre = returnBookTitleByGenre(genre); // Get All Book Title That Matches All Genre

        // Get Specific Details For Each Title
        for (String title : bookMatchedByGenre){
            bookData.add(returnSpecificBookDetailsByTitle(title));
        }

        // Convert The Arraylist into A 2d Array
        String[][] bookDataArray = new String[bookData.size()][];
        bookData.toArray(bookDataArray);

        return bookDataArray;

    }

    /**
     * Returns all book reviews submitted by a specific user.
     *
     * @param userId the ID of the user
     * @return a 2D array containing book reviews submitted by the specified user
     *          Each inner array contains the book ID, rating, review text, and ownership status of a book.
     *          This method queries the "book_reviews" table in the database to retrieve all reviews
     *          submitted by the specified user. It returns a 2D array containing details of each review.
     *          Any SQL exceptions encountered are logged using a logger named "returnBoughtBookByUser".
     */
    public String[][] returnBoughtBookByUser(int userId) {
        Logger logger = Logger.getLogger("returnAllBooksReviewByUserId");
        ArrayList<String[]> booksBought = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT review, rating, book_id, is_owned "
                    + "FROM book_reviews WHERE user_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String[] bookRatingReview = new String[4];
                        bookRatingReview[0] = String.valueOf(resultSet.getInt("book_id"));
                        bookRatingReview[1] = String.valueOf(resultSet.getInt("rating"));
                        bookRatingReview[2] = resultSet.getString("review");
                        bookRatingReview[3] = String.valueOf(resultSet.getBoolean("is_owned"));
                        booksBought.add(bookRatingReview);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnBoughtBookByUser", e);
        }

        String[][] bookDataArray = new String[booksBought.size()][2];
        for (int i = 0; i < booksBought.size(); i++) {
            bookDataArray[i] = booksBought.get(i);
        }

        return bookDataArray;
    }

    public String returnAuthorNameByID(int userId) {
        Logger logger = Logger.getLogger("returnAuthorNameByID");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT username FROM users WHERE id = (SELECT author_id FROM book_details WHERE id = ? )";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnAuthorNameByID", e);
        }

        return null;
    }

    public String[] returnBookDataById(int bookId){
        Logger logger = Logger.getLogger("InsertDataLogger");
        String[] bookDataArray = null; // Initialize the array to return

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT * FROM book_details WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, bookId); // Set the parameter for the book ID
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Create an array to hold book data
                        String[] bookRow = new String[9]; // Assuming 9 fields for the book
                        // Populate the array with book data from the result set
                        bookRow[0] = Integer.toString(resultSet.getInt("id")); //id
                        bookRow[1] = resultSet.getString("title"); // title
                        bookRow[2] = returnBookDescriptionByTitle(bookRow[1]); // description
                        bookRow[3] = resultSet.getString("image_link"); // link
                        bookRow[5] = Integer.toString(resultSet.getInt("author_id")); // author id
                        bookRow[6] = resultSet.getBoolean("is_available") ? "true" : "false"; // availability
                        bookRow[7] = Double.toString(resultSet.getDouble("price")); // price
                        bookRow[8] = Integer.toString(resultSet.getInt("copies_sold")); // book sold
                        String[] genres = returnBookGenreByTitle(bookRow[1]);
                        if (genres != null && genres.length > 0) {
                            // Join genres into a single string
                            StringBuilder genreStringBuilder = new StringBuilder();
                            for (String genre : genres) {
                                genreStringBuilder.append(genre).append(", ");
                            }
                            // Remove the last comma and space
                            String genreString = genreStringBuilder.substring(0, genreStringBuilder.length() - 2);
                            bookRow[4] = genreString; // genre
                        }
                        bookDataArray = bookRow; // Assign the book data array to return
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning Books", e);
        }

        return bookDataArray; // Return the book data array
    }

    public int returnLatestUserId(){
        Logger logger = Logger.getLogger("returnLatestUserId");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnLatestUserId", e);
        }

        return -1;
    }


    public int returnAuthorId(int userId){
        Logger logger = Logger.getLogger("returnAuthorId");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id FROM author WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error retrieving author ID", e);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to database", e);
        }

        return -1; // or any other appropriate default value
    }


    public int[] returnAllNotBoughtBooks(int userId) {
        Logger logger = Logger.getLogger("returnAllNotBoughtBooks");
        List<Integer> bookIds = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT book_details.id AS not_owned_book " +
                    "FROM book_details " +
                    "LEFT JOIN book_owned ON book_details.id = book_owned.book_id AND book_owned.user_id = ? " +
                    "WHERE book_owned.book_id IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        bookIds.add(resultSet.getInt("not_owned_book"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving not bought books", e);
        }

        int[] result = new int[bookIds.size()];
        for (int i = 0; i < bookIds.size(); i++) {
            result[i] = bookIds.get(i);
        }

        return result;
    }

    //TODO: Search Empty Return ALL GENRE (use the old method for it)
    //TODO: Possible optimization

    public List<Integer> returnSearch(String searchBy, List<String> genre, String keyword, Boolean ShowOwned, int UserId, double max, double min){
        Logger logger = Logger.getLogger("ReturnSearch");
        List<Integer> bookIds = new ArrayList<>();
        String query = "";

        switch (searchBy) {
            case "Book Name": // HANDLES THE BOOK NAME
                System.out.println("Search by Book Name");
                query = "SELECT bd.id, bd.title FROM book_details bd WHERE title LIKE ? AND is_available = 1 ";
                break;
            case "Author": // HANDLES THE AUTHOR
                System.out.println("Search by Author");
                query = "SELECT bd.id, bd.title " +
                        "FROM book_details bd " +
                        "WHERE author_id = (SELECT id FROM author WHERE user_id = (SELECT id FROM users WHERE username LIKE ?)) AND is_available = 1 ;";
                break;
            case "Description": // HANDLES THE DESCRIPTION
                System.out.println("Search by Description");
                query = "SELECT bd.id, bd.title " +
                        "FROM book_details bd " +
                        "WHERE bd.title IN (" +
                        "    SELECT book_title " +
                        "    FROM book_description " +
                        "    WHERE description LIKE ?" +
                        ") AND is_available = 1 ";
                break;
            default:
                System.out.println("Invalid search option");
                return bookIds;
        }

        // ADDS FILTER FOR GENRE IF THERES A GENRE INSERTED IN LINKED LIST
        if (!genre.isEmpty()) {
            for (String g : genre) {
                System.out.println(g);
                query += String.format(" AND bd.title IN (SELECT title FROM book_genre WHERE genre = '%s')", g);

            }
        }

        if (!ShowOwned){ // Exclude All Bought Book
            System.out.println("Exclude the Book Bought");
            query += String.format(" AND bd.id NOT IN (SELECT book_id FROM book_owned WHERE user_id = %d)", UserId);
        }

        // CANNOT USE OR MUST BE SPECIFIED
        if (keyword.isEmpty() && genre.isEmpty()){ // If keyword and genre is empty just return Everything
            System.out.println("KEYWORD AND GENRE IS EMPTY RETURN ALL BOOK ID INSTEAD");
            return returnAllBookId(genre, ShowOwned, UserId, max, min);
        } else if (keyword.isEmpty() && !genre.isEmpty()) { // If keyword is empty but genre is not
            System.out.println("KEYWORD IS EMPTY RETURN ALL BOOK MATCHES THE GENRE");
            return returnAllBookId(genre, ShowOwned, UserId, max, min);
        }

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1,  "%" + keyword + "%");
                System.out.println(query);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        bookIds.add(resultSet.getInt("id"));

                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving not bought books", e);
        }

        if (max != 9999 && min != 9999) {
            List<Integer> MatchesPrice = returnListMatchedByPrice(max, min);
            bookIds.retainAll(MatchesPrice);

        }

        return bookIds;
    }


    public List<Integer> returnAllBookId(List<String> genre, boolean ShowOwned, int UserId, double max, double min) {
        Logger logger = Logger.getLogger("returnAuthorId");
        boolean need_AND = false;
        List<Integer> bookIds = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT bd.id, bd.title FROM book_details bd";
            boolean first = true;

            // Check If genre Exist
            if (!genre.isEmpty()) {
                for (String g : genre) {
                    if (first) {
                        sql += String.format(" WHERE bd.title IN (SELECT title FROM book_genre WHERE genre = '%s') AND is_available = 1 ", g);
                        first = false;
                    }
                    else{
                        sql += String.format(" AND bd.title IN (SELECT title FROM book_genre WHERE genre = '%s')", g);
                    }
                    need_AND = true;
                }
            }

            if (!ShowOwned){ // Exclude All Bought Book
                System.out.println("Exclude the Book Bought");
                if (!need_AND) {
                    sql += String.format(" WHERE bd.id NOT IN (SELECT book_id FROM book_owned WHERE user_id = %d) AND is_available = 1 ", UserId);
                } else{
                    sql += String.format(" AND bd.id NOT IN (SELECT book_id FROM book_owned WHERE user_id = %d)", UserId);
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    bookIds.add(resultSet.getInt("id"));
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error retrieving author ID", e);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to database", e);
        }

        if (max != 9999 && min != 9999) {
            List<Integer> MatchesPrice = returnListMatchedByPrice(max, min);
            bookIds.retainAll(MatchesPrice);

        }

        return bookIds;
    }


    public int[] returnTenLatestBooks() {
        Logger logger = Logger.getLogger("InsertDataLogger");
        int[] topBookIds = new int[10];
        String sql = "SELECT * FROM book_details " +
                " WHERE is_available = 1 " +
                "ORDER BY id DESC " +
                "LIMIT 10";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            for (int i = 0; i < 10; i++) {
                if (resultSet.next()) {
                    topBookIds[i] = resultSet.getInt("id");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning top 10 books", e);
        }

        return topBookIds;
    }



    public List<Integer> returnOwnedBooks(int userId) {
        Logger logger = Logger.getLogger("InsertDataLogger");
        List<Integer> ownedBooks = new ArrayList<>();

        String sql = "SELECT book_id FROM book_owned WHERE user_id = ? AND is_owned = 1 ";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ownedBooks.add(resultSet.getInt("book_id"));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning owned books", e);
        }

        return ownedBooks;
    }


    public List<Integer> returnBookPublished(int userId) {
        Logger logger = Logger.getLogger("InsertDataLogger");
        List<Integer> ownedBooks = new ArrayList<>();

        String sql = "SELECT id FROM book_details WHERE author_id = (SELECT id FROM author WHERE user_id = ?) AND is_available = 1";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ownedBooks.add(resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning owned books", e);
        }

        return ownedBooks;
    }

    public List<Integer> returnBookPending(int userId) {
        Logger logger = Logger.getLogger("InsertDataLogger");
        List<Integer> ownedBooks = new ArrayList<>();

        String sql = "SELECT id FROM book_details WHERE author_id = (SELECT id FROM author WHERE user_id = ?) AND is_available = 0";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ownedBooks.add(resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning owned books", e);
        }

        return ownedBooks;
    }

    public String returnUserEmail(int userId){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT email FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("email");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning cash", e);
        }
        return "";
    }
    public List<Integer> returnListMatchedByPrice(double max, double min) {
        List<Integer> bookIds = new ArrayList<>();
        Logger logger = Logger.getLogger("InsertDataLogger");

        String sql = "SELECT id FROM book_details " +
                "WHERE price BETWEEN ? AND ? " +
                "AND is_available = 1 " +
                "ORDER BY id DESC";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, min);
            statement.setDouble(2, max);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookIds.add(resultSet.getInt("id"));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving book IDs by price range", e);
        }

        return bookIds;
    }
    public String returnUserUserName(int userId){
        Logger logger = Logger.getLogger("InsertDataLogger");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT username FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error Returning cash", e);
        }
        return "";
    }

    public List<String> returnAllGenreById(int book_id){
        Logger logger = Logger.getLogger("returnAllGenreById");
        List<String> bookGenres = new ArrayList<>();

        String sql = "SELECT genre " +
                "FROM book_genre " +
                "WHERE title = (SELECT title FROM book_details WHERE id = ?)";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book_id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bookGenres.add(resultSet.getString("genre"));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning genre", e);
        }

        return bookGenres;
    }

    public List<String[]> returnAllBookReviewsById(int bookId) {
        Logger logger = Logger.getLogger("returnAllBookReviewsById");
        List<String[]> bookReviews = new ArrayList<>();

        String sql = "SELECT user_id, review " +
                "FROM book_text_review " +
                "WHERE book_id = ?";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] review = new String[4];
                review[0] = resultSet.getString("user_id");
                review[1] = resultSet.getString("review");
                bookReviews.add(review);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning book reviews", e);
        }

        return bookReviews;
    }

    public int returnReviewRating(int userId, int bookId){
        Logger logger = Logger.getLogger("returnReviewRating");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT rating FROM book_rating WHERE user_id = ? and book_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("rating");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnReviewRating", e);
        }
        return 0;
    }

    public  List<int[]> returnBookReviewRatings(int bookId){
        Logger logger = Logger.getLogger("returnReviewRating");
        List<int[]> bookRatings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT user_id,rating FROM book_rating WHERE book_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int[] idAndRating = new int[2];
                        idAndRating[0] = resultSet.getInt("user_id");
                        idAndRating[1] = resultSet.getInt("rating");
                        bookRatings.add(idAndRating);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnReviewRating", e);
        }
        return bookRatings;
    }

    public String returnReviewText(int userId, int bookId){
        Logger logger = Logger.getLogger("returnReviewRating");

        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT review FROM book_text_review WHERE user_id = ? and book_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("review");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnReviewRating", e);
        }
        return null;
    }

    public String[] returnBookReviewDetails(int userId, int bookId){
        String[] details = new String[3];
        details[0] = String.valueOf(returnReviewRating(userId, bookId));
        details[1] = returnReviewText(userId, bookId);
        details[2] = String.valueOf(Check.CheckIfBookWasBought(bookId, userId));

        return details;
    }
    public String[] returnBookRating(int userId, int bookId){
        String[] details = new String[3];
        details[0] = String.valueOf(returnReviewRating(userId, bookId));
        details[1] = returnReviewText(userId, bookId);
        details[2] = String.valueOf(Check.CheckIfBookWasBought(bookId, userId));

        return details;
    }

    public List<String[]> returnBookOwnedOrNot(int userId, boolean owned) {
        Logger logger = Logger.getLogger("returnOwnedBookRating");
        List<String[]> bookReviews = new ArrayList<>();
        String sql;

        if (owned) {
            sql = "SELECT rating, book_id " +
                    "FROM book_rating " +
                    "WHERE book_id IN ( " +
                    "SELECT book_id " +
                    "FROM book_owned " +
                    "WHERE is_owned = 1 " +
                    "AND user_id = ? " +
                    ")";
        } else {
            sql = "SELECT rating, book_id " +
                    "FROM book_rating " +
                    "WHERE book_id IN ( " +
                    "SELECT book_id " +
                    "FROM book_owned " +
                    "WHERE is_owned = 0 " +
                    "AND user_id = ? " +
                    ")";
        }

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] review = new String[2];

                review[0] = String.valueOf(resultSet.getInt("rating"));
                review[1] = String.valueOf(resultSet.getInt("book_id"));
                bookReviews.add(review);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returnOwnedBookRating", e);
        }

        return bookReviews;
    }

    public List<String[]> returnAllUnpublishedBooks() {
        Logger logger = Logger.getLogger("returnAllUnpublishedBooks");
        List<String[]> unpublishedBooks = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id, title, price FROM book_details WHERE is_available = 0";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String[] bookDetails = new String[3];
                        bookDetails[0] = String.valueOf(resultSet.getInt("id"));
                        bookDetails[1] = resultSet.getString("title");
                        bookDetails[2] = String.valueOf(resultSet.getInt("price"));
                        unpublishedBooks.add(bookDetails);

                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving unpublished books", e);
        }
        return unpublishedBooks;
    }

    public List<String[]> returnAllPublishedBooks() {
        Logger logger = Logger.getLogger("returnAllUnpublishedBooks");
        List<String[]> unpublishedBooks = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(this.dataLocation)) {
            String sql = "SELECT id, title, price FROM book_details WHERE is_available = 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String[] bookDetails = new String[3];
                        bookDetails[0] = String.valueOf(resultSet.getInt("id"));
                        bookDetails[1] = resultSet.getString("title");
                        bookDetails[2] = String.valueOf(resultSet.getInt("price"));
                        unpublishedBooks.add(bookDetails);

                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving unpublished books", e);
        }
        return unpublishedBooks;
    }

    public String[] ReturnMetaDataByTitle(String book_title) {
        Logger logger = Logger.getLogger("returnMetaDataById");
        String[] bookMetaData = new String[3];

        String sql = "SELECT isbn, publication_date, language, book_title " +
                "FROM book_metadata " +
                "WHERE book_title = ?";

        try (Connection connection = DriverManager.getConnection(this.dataLocation);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book_title);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                bookMetaData[0] = resultSet.getString("isbn");
                int publicationDate = resultSet.getInt("publication_date");
                bookMetaData[1] = String.valueOf(publicationDate);
                bookMetaData[2] = resultSet.getString("language");

            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning metadata", e);
        }

        return bookMetaData;
    }



}
