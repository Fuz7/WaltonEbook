package org.openjfx.program.model;

import javafx.scene.image.Image;

public class BookData {
    public String title;
    public String formattedDescription;
    public Image image;
    public String genre;
    public int bookSold;
    public double price;

    public BookData(String title, String formattedDescription, Image image, String genre, int bookSold, double price) {
        this.title = title;
        this.formattedDescription = formattedDescription;
        this.image = image;
        this.genre = genre;
        this.bookSold = bookSold;
        this.price = price;
    }
}
