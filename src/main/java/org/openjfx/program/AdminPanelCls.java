package org.openjfx.program;

import org.openjfx.program.database.DatabaseManager;

import java.util.*;

public class AdminPanelCls {

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager("jdbc:sqlite:./src/main/java/org/openjfx/program/test.db");
        Scanner input = new Scanner(System.in);

        int choice;
        boolean exit = false;

        do {
            choice = Menu(input);
            switch (choice){
                case 1:
                    unpublishedBookOption(db, input);
                    break;
                case 2:
                    publishedBookOption(db, input);
                    break;
                case 3:
                    adminBookInsert(db, input);
                    //exit = true;
                    break;
                case 4:
                    exit = true;
                        System.out.print("   ____________________________________________________\n" +
                                "  |____________________________________________________|\n" +
                                "  | __     __   ____   ___ ||  ____    ____     _  __  |\n" +
                                "  ||  |__ |--|_| || |_|   |||_|**|*|__|+|+||___| ||  | |\n" +
                                "  ||==|^^||--| |=||=| |=*=||| |~~|~|  |=|=|| | |~||==| |\n" +
                                "  ||  |##||  | | || | |JRO|||-|  | |==|+|+||-|-|~||__| |\n" +
                                "  ||__|__||__|_|_||_|_|___|||_|__|_|__|_|_||_|_|_||__|_|\n" +
                                "  ||_______________________||__________________________|\n" +
                                "  | _____________________  ||      __   __  _  __    _ |\n" +
                                "  ||=|=|=|=|=|=|=|=|=|=|=| __..\\/ |  |_|  ||#||==|  / /|\n" +
                                "  ||W|A|L|T|O|N|E|B|O|O|K|/\\ \\  \\\\|++|=|  || ||==| / / |\n" +
                                "  ||_|_|_|_|_|_|_|_|_|_|_/_/\\_.___\\__|_|__||_||__|/_/__|\n" +
                                "  |____________________ /\\~()/()~//\\ __________________|\n" +
                                "  | __   __    _  _     \\_  (_ .  _/ _    ___     _____|\n" +
                                "  ||~~|_|..|__| || |_ _   \\ //\\\\ /  |=|__|~|~|___| | | |\n" +
                                "  ||--|+|^^|==|1||2| | |__/\\ __ /\\__| |==|x|x|+|+|=|=|=|\n" +
                                "  ||__|_|__|__|_||_|_| /  \\ \\  / /  \\_|__|_|_|_|_|_|_|_|\n" +
                                "  |_________________ _/    \\/\\/\\/    \\_ _______________|\n" +
                                "  | _____   _   __  |/      \\../      \\|  __   __   ___|\n" +
                                "  ||_____|_| |_|##|_||   |   \\/ __|   ||_|==|_|++|_|-|||\n" +
                                "  ||______||=|#|--| |\\   \\   o    /   /| |  |~|  | | |||\n" +
                                "  ||______||_|_|__|_|_\\   \\  o   /   /_|_|__|_|__|_|_|||\n" +
                                "  |_________ __________\\___\\____/___/___________ ______|\n" +
                                "  |__    _  /    ________     ______           /| _ _ _|\n" +
                                "  |\\ \\  |=|/   //    /| //   /  /  / |        / ||%|%|%|\n" +
                                "  | \\/\\ |*/  .//____//.//   /__/__/ (_)      /  ||=|=|=|\n" +
                                "__|  \\/\\|/   /(____|/ //                    /  /||~|~|~|__\n" +
                                "  |___\\_/   /________//   ________         /  / ||_|_|_|\n" +
                                "  |___ /   (|________/   |\\_______\\       /  /| |______|\n" +
                                "      /                  \\|________)     /  / | |\n" +
                                "Thanks!");
                    break;
            }
        } while (!exit);


    }

    // ---------------------------------- Options ---------------------------------- \\
    public static void publishedBookOption(DatabaseManager db, Scanner input){
        do {
            Set<Integer> ids = printUnpublishedBookData(db, true);
            System.out.println("1. Make Book Unavailable Books");
            System.out.println("2. Go Back");
            System.out.print("Choose an option: ");

            int option;
            int id_input;
            try {
                // Options
                option = Integer.parseInt(input.nextLine());
                if (option > 2 || option <= 0)
                {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Invalid option. Please enter a available option.");
                    continue;
                }

                switch (option){
                    case 1:
                        // Get Id
                        System.out.print("Id: ");
                        id_input = Integer.parseInt(input.nextLine());
                        if (!ids.contains(id_input)){
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                            System.out.println("Id not found");
                            continue;
                        }

                        // Make book available
                        db.Update.updateBookToUnavailable(id_input);
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        System.out.println("Book Is Now Unavailable");
                        break;
                    case 2: // Exit
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        return;
                }

            } catch (NumberFormatException e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Invalid option. Please enter a number.");
            }
        } while (true);

    }

    public static void unpublishedBookOption(DatabaseManager db, Scanner input){
        do {
            Set<Integer> ids = printUnpublishedBookData(db, false);
            System.out.println("1. Make Book Available");
            System.out.println("2. Go Back");
            System.out.print("Choose an option: ");

            int option;
            int id_input;
            try {
                // Options
                option = Integer.parseInt(input.nextLine());
                if (option > 2 || option <= 0)
                {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Invalid option. Please enter a available option.");
                    continue;
                }

                switch (option){
                    case 1:
                        // Get Id
                        System.out.print("Id: ");
                        id_input = Integer.parseInt(input.nextLine());
                        if (!ids.contains(id_input)){
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                            System.out.println("Id not found");
                            continue;
                        }

                        // Make book available
                        db.Update.updateBookToAvailable(id_input);
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        System.out.println("Book Is Now Available");
                        break;
                    case 2: // Exit
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        return;
                }

            } catch (NumberFormatException e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Invalid option. Please enter a number.");
            }
        } while (true);

    }

    public static void adminBookInsert(DatabaseManager db, Scanner input){

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        do {
            String title = getInput(input, "Book Title", "string");
            if (title.equals("0264Jorge")) {System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;}

            String titleImage = getInput(input, "Title Image", "string");
            if (titleImage.equals("0264Jorge")) {System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;}

            String authorName = getInput(input, "Author Name", "string");
            if (authorName.equals("0264Jorge")) {System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;}

            String availability = getInput(input, "Availability (true/false)", "boolean");
            if (availability.equals("0264Jorge")) {System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;}

            String bookPrice = getInput(input, "Book Price", "double");
            if (bookPrice.equals("0264Jorge")) {System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;}

            String description = getInput(input, "Description", "string");
            if (description.equals("0264Jorge")) {System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;}


            String[] genres = getGenres(input);

            String confirm_print = "Book Title: " + title +
                    "\nTitle Image: " + titleImage +
                    "\nAuthor Name: " + authorName +
                    "\nAvailability: " + availability +
                    "\nBook Price: " + bookPrice +
                    "\nDescription: " + description +
                    "\nGenres:\n" + Arrays.toString(genres) +
                    "\nConfirm? (yes or no)";

            String confirm = getInput(input, confirm_print, "string");

            if (confirm.equalsIgnoreCase("yes")){
                db.Insert.AdminBookInserter(title, titleImage, genres, authorName, Boolean.parseBoolean(availability),
                        Double.parseDouble(bookPrice), 0, description);
            } else{ return;}

        } while (true);

    }

    public static int Menu(Scanner input){
        do {
            System.out.println("Menu:");
            System.out.println("1. Show All Unavailable Books");
            System.out.println("2. Show All Available Books");
            System.out.println("3. Admin Book Insert");
            System.out.println("4. Exit");


            System.out.print("Choose an option: ");

            int option;
            try {
                option = Integer.parseInt(input.nextLine());
                if (option > 4 || option <= 0)
                {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Invalid option. Please enter a available option.");
                    continue;
                }
                return option;
            } catch (NumberFormatException e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Invalid option. Please enter a number.");
            }
        } while (true);

    }

    // ---------------------------------- Prints ---------------------------------- \\
    public static Set<Integer> printUnpublishedBookData(DatabaseManager db, boolean book_publish){

        List<String[]> data;
        if (book_publish)
            data = db.Return.returnAllPublishedBooks();
        else
            data = db.Return.returnAllUnpublishedBooks();


        Set<Integer> ids = new HashSet<>();
        String leftAlignFormat = "| %-15s | %-20s | %-6.2f |%n";

        System.out.format("+-----------------+----------------------+--------+%n");
        System.out.format("| ID              | Title                | Price  |%n");
        System.out.format("+-----------------+----------------------+--------+%n");
        for (String[] d : data) {
            System.out.format(leftAlignFormat, d[0], d[1], Double.parseDouble(d[2]));
            ids.add(Integer.parseInt(d[0]));
        }
        System.out.format("+-----------------+----------------------+--------+%n");

        return ids;

    }


    public static String getInput(Scanner input, String question, String dataType){

        do {
            String val;
            System.out.printf("%s: (type Exit/exit to go back)%n", question);
            System.out.print("Input: ");
            try {
                val = input.nextLine().toLowerCase();
                if (val.equals("exit"))
                    return "0264Jorge";

                switch (dataType) {
                    case "int":
                        return String.valueOf(Integer.parseInt(val));
                    case "double":
                        return String.valueOf(Double.parseDouble(val));
                    case "boolean":
                        if (val.equals("true") || val.equals("false"))
                            return val;
                        else
                            throw new InputMismatchException();
                    default:
                        return val;
                }
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Input Must Be A Valid " + dataType);
            }
        } while (true);
    }

    public static String[] getGenres(Scanner scanner) {
        ArrayList<String> genresList = new ArrayList<>();
        System.out.println("Enter genres (type 'done' when finished):");

        while (true) {
            System.out.print("Genre: ");
            String genre = scanner.nextLine();
            if (genre.equals("done")) {
                break;
            }
            genresList.add(genre);
        }

        String[] genresArray = new String[genresList.size()];
        genresArray = genresList.toArray(genresArray);
        return genresArray;
    }


}


