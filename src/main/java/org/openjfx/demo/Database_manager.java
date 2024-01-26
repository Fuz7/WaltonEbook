package org.openjfx.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class Database_manager {

    public String data_location;


    //-------------Intialization-------------//
    // Initialize databse Location
    public void init (String data_location) {
        this.data_location = data_location;
    }
    // Create Tables
    public void CreateTablesIfNoExist(){

        try (Connection connection = DriverManager.getConnection(this.data_location);
             Statement statement = connection.createStatement()) {

            // SQL statement to create a table if it does not exist
            String CreateUserDataSQL = "CREATE TABLE IF NOT EXISTS user_data ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "email TEXT NOT NULL,"
                    + "username NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "admin BOOLEAN DEFAULT 0,"
                    + "balance REAL DEFAULT 0.0,"
                    + "books_bought TEXT"
                    + ");";

            String createBookDetailsTableSQL = "CREATE TABLE IF NOT EXISTS book_details ("
                    + "book_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "image_link TEXT,"
                    + "genre TEXT,"
                    + "author_id INTEGER,"
                    + "availability BOOLEAN DEFAULT 1,"
                    + "book_price REAL DEFAULT 0.0,"
                    + "book_sold INTEGER DEFAULT 0,"
                    + "FOREIGN KEY (author_id) REFERENCES user_data (id)"
                    + ");";

            String createBookDescriptionSQL = "CREATE TABLE IF NOT EXISTS book_description ("
                    + "description_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "description TEXT"
                    + ");";


            String createAuthorTableSQL = "CREATE TABLE IF NOT EXISTS author ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER,"
                    + "description TEXT,"
                    + "FOREIGN KEY (user_id) REFERENCES user_data (id)"
                    + ");";


            // Execute the SQL statement
            statement.execute(CreateUserDataSQL);
            statement.execute(createBookDescriptionSQL);
            statement.execute(createBookDetailsTableSQL);
            statement.execute(createAuthorTableSQL);

            System.out.println("Table created successfully (if not existed)");

        } catch (SQLException e) {
            e.printStackTrace();}

    }



    //-------------Insert-------------//
    // inesrt new user
    public void InsertNewUser(String email, String username, String password, Boolean isAdmin, double balance, String booksBought) {
        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to insert data into the "user_data" table
            String insertUserDataSQL = "INSERT INTO user_data (email, username, password, admin, balance, books_bought) VALUES (?, ?, ?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserDataSQL)) {
                // Set values for the parameters in the prepared statement
                preparedStatement.setString(1, email); // Assuming email is the first parameter
                preparedStatement.setString(2, username); // Assuming username is the second parameter
                preparedStatement.setString(3, password);
                preparedStatement.setBoolean(4, isAdmin);
                preparedStatement.setDouble(5, balance);
                preparedStatement.setString(6, booksBought); // Assuming booksBought is the sixth parameter

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into user_data table successfully");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Insert new book
    public void InsertNewBook(String title, String imageLink, String genre, int authorId,
                              boolean availability, double bookPrice, int bookSold, String description){
        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to insert data into the "book_details" table
            String insertBookDetailsSQL = "INSERT INTO book_details (title, image_link, genre, author_id, availability, book_price, book_sold) VALUES (?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookDetailsSQL)) {
                // Set values for the parameters in the prepared statement
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, imageLink);
                preparedStatement.setString(3, genre);
                preparedStatement.setInt(4, authorId);
                preparedStatement.setBoolean(5, availability);
                preparedStatement.setDouble(6, bookPrice);
                preparedStatement.setInt(7, bookSold);

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into book_details table successfully");

                // Insert description to the description table
                InsertNewDescription(description);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Insert new details (Part of insert)
    public void InsertNewDescription(String description){
        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to insert data into the "book_details" table
            String insertBookDetailsSQL = "INSERT INTO book_description (description) VALUES (?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookDetailsSQL)) {
                // Set values for the parameters in the prepared statement
                preparedStatement.setString(1, description);

                // Execute the SQL statement to insert data
                preparedStatement.executeUpdate();

                System.out.println("Data inserted into book_description table successfully");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // Buy new Book
    public boolean BuyBook(double cost, int user_id, int book_id){
        String updateQuery = "UPDATE user_data SET balance = ?, books_bought = ? WHERE id = ?";

        String[] CurrentUserData = ReturnUserDetailsById(user_id);

        Double balance = Double.valueOf(CurrentUserData[5]);


        if ((balance - cost) < 0) {
            System.out.println("Not Enough");
            return false;
        } else {
            System.out.println("Successfully bought");
            int User_id = Integer.valueOf(CurrentUserData[0]);
            // Updates User's Cash
            UpdateUserCash(User_id, (balance - cost));
            // Update Bought Books Of The User
            AddNewBoughtBook(User_id, String.valueOf(book_id));
            // Add One to Book Sold Count
            IncreaseBookSoldByOne(book_id);
            return true;
        }


    }



    //-------------Specific Update-------------//
    // Updates The User's Cash
    public void UpdateUserCash(int user_id, double updated_balance){
        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to update the balance for a user with a specific user_id
            String updateBalanceSQL = "UPDATE user_data SET balance = ? WHERE id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateBalanceSQL)) {
                // Set values for the parameters in the prepared statement
                preparedStatement.setDouble(1, updated_balance);
                preparedStatement.setInt(2, user_id);

                // Execute the SQL statement to update the balance
                int rowsAffected = preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }
    }
    // Add new Book
    public void AddNewBoughtBook(int userId, String newBook) {
        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to update the books_bought for a user with a specific user_id
            String getBooksBoughtSQL = "SELECT books_bought FROM user_data WHERE id = ?;";
            String updateBooksBoughtSQL = "UPDATE user_data SET books_bought = ? WHERE id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(getBooksBoughtSQL)) {
                // Retrieve the current books_bought value
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String currentBooksBought = resultSet.getString("books_bought");
                    String updatedBooksBought = currentBooksBought + "," + newBook;

                    try (PreparedStatement updateStatement = connection.prepareStatement(updateBooksBoughtSQL)) {
                        // Set values for the parameters in the prepared statement
                        updateStatement.setString(1, updatedBooksBought);
                        updateStatement.setInt(2, userId);

                        // Execute the SQL statement to update books_bought
                        int rowsAffected = updateStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("New book added successfully for user with ID: " + userId);
                        } else {
                            System.out.println("No user found with ID: " + userId);
                        }
                    }
                } else {
                    System.out.println("No user found with ID: " + userId);
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }
    }
    public void IncreaseBookSoldByOne(int bookId) {
        String incrementSoldQuery = "UPDATE book_details SET book_sold = book_sold + 1 WHERE book_id = ?";

        try (Connection connection = DriverManager.getConnection(this.data_location);
             PreparedStatement preparedStatement = connection.prepareStatement(incrementSoldQuery)) {

            preparedStatement.setInt(1, bookId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book sold count incremented successfully for book ID: " + bookId);
            } else {
                System.out.println("Failed to increment book sold count for book ID: " + bookId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    //-------------RETURN ALL-------------//
    // Return all book data
    public void ReturnAllBooks(){
        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to retrieve all data from the "book_details" table
            String retrieveBooksSQL = "SELECT * FROM book_details;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveBooksSQL)) {
                // Execute the SQL statement and get the result set
                ResultSet resultSet = preparedStatement.executeQuery();

                // Iterate through the result set and print the book details
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    String description = returnBookDescriptionById(bookId);
                    String imageLink = resultSet.getString("image_link");
                    String genre = resultSet.getString("genre");
                    int authorId = resultSet.getInt("author_id");
                    boolean availability = resultSet.getBoolean("availability");
                    double bookPrice = resultSet.getDouble("book_price");
                    int bookSold = resultSet.getInt("book_sold");

                    // Print or process the retrieved book details as needed
                    System.out.println("Book ID: " + bookId);
                    System.out.println("Title: " + title);
                    System.out.println("Description: " + description);
                    System.out.println("Image Link: " + imageLink);
                    System.out.println("Genre: " + genre);
                    System.out.println("Author ID: " + authorId);
                    System.out.println("Availability: " + availability);
                    System.out.println("Book Price: " + bookPrice);
                    System.out.println("Books Sold: " + bookSold);
                    System.out.println();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Return all User data
    public Object[][] ReturnAllUsers(){
        List<Object[]> dataList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            // SQL statement to retrieve all data from the "user_data" table
            String retrieveUserDataSQL = "SELECT * FROM user_data;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveUserDataSQL)) {
                // Execute the SQL statement and get the result set
                ResultSet resultSet = preparedStatement.executeQuery();

                // Iterate through the result set and add data to the list
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String email = resultSet.getString("email");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    boolean isAdmin = resultSet.getBoolean("admin");
                    double balance = resultSet.getDouble("balance");
                    String booksBought = resultSet.getString("books_bought");

                    // Add the retrieved data to the list
                    dataList.add(new Object[]{id, email, username, password, isAdmin, balance, booksBought});
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert the list to a 2D array
        return dataList.toArray(new Object[0][0]);
    }


    public String[] ReturnTopThreeBooks() {
        String[] topBookIds = new String[3];
        int count = 0;

        try (Connection connection = DriverManager.getConnection(this.data_location);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT book_id FROM book_details ORDER BY book_sold DESC LIMIT 3");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next() && count < 3) {
                topBookIds[count++] = resultSet.getString("book_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topBookIds;}





    //-------------Specific Return-------------//
    // Return Specific Book Details
    public List<Object> ReturnBookDetailsById(int bookId){
        List<Object> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            String retrieveDetailsSQL = "SELECT * FROM book_details WHERE book_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveDetailsSQL)) {
                preparedStatement.setInt(1, bookId);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    result.add(resultSet.getInt("book_id"));
                    result.add(resultSet.getString("title"));
                    result.add(returnBookDescriptionById(resultSet.getInt("book_id")));
                    result.add(resultSet.getString("image_link"));
                    result.add(resultSet.getString("genre"));
                    result.add(resultSet.getInt("author_id"));
                    result.add(resultSet.getBoolean("availability"));
                    result.add(resultSet.getDouble("book_price"));
                    result.add(resultSet.getInt("book_sold"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    // Return Specific Book Description (Part of ReturnBookDetailsById but can be used for more stuff)
    public String returnBookDescriptionById(int bookId) {
        String result = null;

        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            String retrieveDetailsSQL = "SELECT * FROM book_description WHERE description_id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveDetailsSQL)) {
                preparedStatement.setInt(1, bookId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Assuming 'description' is the column name in your table
                    result = resultSet.getString("description");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    // Return Specific Author name
    public String ReturnAuthorNameById(int user_id){

        String authorName = "";

        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            String retrieveDetailsSQL = "SELECT username FROM user_data WHERE id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveDetailsSQL)) {
                preparedStatement.setInt(1, user_id);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    authorName = resultSet.getString("username");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authorName;

    }
    // Return Specific Book Id By Genre
    public List<String> ReturnBookIdByGenre(String[] genres) {
        List<String> result = new ArrayList<>();
        String query = "SELECT book_id FROM book_details WHERE " + buildCondition(genres);

        try (Connection connection = DriverManager.getConnection(data_location);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                result.add(String.valueOf(bookId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    // Part of ReturnBookIdByGenre
    public String buildCondition(String[] genres){
        StringBuilder condition = new StringBuilder();

        for (String genre : genres) {
            condition.append("genre LIKE '%").append(genre).append("%' OR ");
        }

        if (condition.length() > 0) {
            // Remove the last " OR "
            condition.setLength(condition.length() - 4);
        }

        return condition.toString();

    }
    // Return Specific User Details By Id
    public String[] ReturnUserDetailsById(int user_id) {
        String[] data = new String[7]; // Adjust if adding/removing column

        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            String retrieveDetailsSQL = "SELECT * FROM user_data WHERE id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveDetailsSQL)) {
                preparedStatement.setInt(1, user_id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        data[0] = String.valueOf(resultSet.getInt("id"));
                        data[1] = resultSet.getString("email");
                        data[2] = resultSet.getString("username");
                        data[3] = resultSet.getString("password");
                        data[4] = String.valueOf(resultSet.getBoolean("admin"));
                        data[5] = String.valueOf(resultSet.getDouble("balance"));
                        data[6] = resultSet.getString("books_bought");
                    } else {
                        System.out.println("No user found with ID " + user_id);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    // Return Array Of Books Bought By Id
    public String[] ReturnBooksBoughtById(int user_id) {
        String[] booksArray;

        try (Connection connection = DriverManager.getConnection(this.data_location)) {
            String retrieveBooksBoughtSQL = "SELECT books_bought FROM user_data WHERE id = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveBooksBoughtSQL)) {
                preparedStatement.setInt(1, user_id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String booksBought = resultSet.getString("books_bought");
                        if (booksBought != null) {
                            // Split the books_bought string into an array
                            booksArray = booksBought.startsWith(",") ? booksBought.substring(1).split(",") : booksBought.split(",");                        } else {
                            // If books_bought is null, initialize an empty array
                            booksArray = new String[0];
                        }
                    } else {
                        System.out.println("No user found with ID " + user_id);
                        return new String[0]; // Return an empty array if no user found
                    }
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
            return new String[0]; // Return an empty array in case of an exception
        }

        return booksArray;
    }







}