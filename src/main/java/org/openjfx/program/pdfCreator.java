package org.openjfx.program;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class pdfCreator {
    public static void main(String[] args) throws IOException{
        app.db.Insert.AdminBookInserter(
                "How to Stay Married The Most Insane Love Story Ever Told",
                "How to Stay Married The Most Insane Love Story Ever Told.jpeg",
                new String[]{"Humor", "Memoir", "Relationships", "Spirituality"},
                "Harrison Scott Key",
                true,
                11.49,
                57,
                "From Harrison Scott Key, winner of the Thurber Prize for American Humor, How to Stay Married tells the hilarious, shocking, and spiritually profound story of one man’s journey through hell and back when infidelity threatens his marriage.\n"
                        + "\n"
                        + "One gorgeous autumn day, Harrison discovers that his wife—the sweet, funny, loving mother of their three daughters, a woman “who’s spent just about every Sunday of her life in a church”—is having an affair with a family friend. This revelation propels the hysterical, heartbreaking action of How to Stay Married, casting our narrator onto “the factory floor of hell,” where his wife was now in love with a man who “wears cargo shorts, on purpose.” What will he do? Kick her out? Set fire to all her panties in the yard? Beat this man to death with a gardening implement? Ask God for help in winning her back?\n"
                        + "\n"
                        + "Armed with little but a sense of humor and a hunger for the truth, Harrison embarks on a hellish journey into his past, seeking answers to the riddles of faith and forgiveness. Through an absurd series of escalating confessions and betrayals, Harrison reckons with his failure to love his wife in the ways she needed most, resolves to fight for his family, and in a climax almost too ridiculous to be believed, finally learns that love is no joke. How to Stay Married is a comic romp unlike any in contemporary literature, a wild Pilgrim’s Progress through the hellscape of marriage and the mysteries of mercy."
        );

    }

    public static void createBookPdf(String imagePath, String title,String description, String genre,String dirPath) throws IOException {
        PDDocument document = new PDDocument();
        PDRectangle myPageSize = new PDRectangle(600,800);
        PDPage myPage = new PDPage(myPageSize);
        document.addPage(myPage);

        PDPageContentStream contentStream = new PDPageContentStream(document,myPage);
        float pageWidth = document.getPage(0).getMediaBox().getWidth();
        float pageHeight = document.getPage(0).getMediaBox().getHeight();

        String imagePathDecoded = URLDecoder.decode(imagePath, StandardCharsets.UTF_8);
        if (imagePath.startsWith("file:")) {
            imagePathDecoded = imagePathDecoded.substring("file:".length());
        }
        imagePathDecoded = imagePathDecoded.replace("%20", " ");

            PDImageXObject bookImage = PDImageXObject.createFromFile(imagePathDecoded, document);
        // Set the specified image dimensions
        float specifiedWidth = 220;
        float specifiedHeight = 300;

        // Calculate scaling factors for width and height
        float scaleWidth = specifiedWidth / bookImage.getWidth();
        float scaleHeight = specifiedHeight / bookImage.getHeight();

        // Choose the minimum scaling factor to maintain the aspect ratio
        float scaleFactor = Math.min(scaleWidth, scaleHeight);

        // Calculate the scaled width and height
        float scaledWidth = bookImage.getWidth() * scaleFactor;
        float scaledHeight = bookImage.getHeight() * scaleFactor;

        // Set the margins
        float topMargin = 20;
        float sideMargin = 50;

        // Calculate the X-coordinate to center the scaled image horizontally
        float imageXCoordinate = (pageWidth - scaledWidth) / 2;

        // Calculate the Y-coordinate to position the scaled image with the margin at the top of the page
        float imageYCoordinate = pageHeight - topMargin - scaledHeight;


        // Draw the scaled image with margins
        contentStream.drawImage(bookImage, imageXCoordinate, imageYCoordinate, scaledWidth, scaledHeight);


// Set font and font size for the title
        PDFont titleFont = PDType1Font.HELVETICA_BOLD;
        float titleFontSize = 32 ;

// Set the title text
        String titleText = title;

// Calculate the width and height of the title text
        float titleTextWidth = titleFont.getStringWidth(titleText) * titleFontSize / 1000f;
        float titleTextHeight = titleFont.getFontDescriptor().getFontBoundingBox().getHeight() * titleFontSize / 1000f;

// Calculate the X-coordinate to center the title horizontally
        float titleXCoordinate = (pageWidth - titleTextWidth) / 2;

// Calculate the Y-coordinate to position the title below the image
        float titleYCoordinate = imageYCoordinate - titleTextHeight - 10; // Adjust as needed for spacing

// Begin the text section for the title
        contentStream.beginText();

// Set the font and font size
        contentStream.setFont(titleFont, titleFontSize);

// Set the text position
        contentStream.newLineAtOffset(titleXCoordinate, titleYCoordinate);

// Add the title text
        contentStream.showText(titleText);

// End the text section for the title
        contentStream.endText();

// Set font and font size for the paragraph
        PDFont paragraphFont = PDType1Font.HELVETICA;
        float paragraphFontSize = 14;

        // Set the paragraph text
        String paragraphText = sanitizeString(description);

        // Wrap the paragraph text
        String[] wrappedParagraph = wrapText(paragraphText, paragraphFont, paragraphFontSize, pageWidth - 2 * sideMargin);
        float lineHeight = paragraphFont.getFontDescriptor().getFontBoundingBox().getHeight() * paragraphFontSize / 1000f;

        // Calculate the Y-coordinate to position the first line below the title
        float paragraphYCoordinate = titleYCoordinate - lineHeight - 10; // Adjust as needed for spacing
        float firstParagraphYCoordinate = titleYCoordinate - lineHeight - 10; // Adjust as needed for spacing

        // Begin the text section for the paragraph
        contentStream.beginText();

        // Set the font and font size
        contentStream.setFont(paragraphFont, paragraphFontSize);

        // Set the text position
        contentStream.newLineAtOffset(sideMargin, paragraphYCoordinate);

        // Add the wrapped lines of the paragraph text
        for (String line : wrappedParagraph) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
        }

        // End the text section for the paragraph
        contentStream.endText();
        PDFont secondParagraphFont = PDType1Font.HELVETICA;
        float secondParagraphFontSize = 12;

        // Set the second paragraph text
        String secondParagraphText = genre;

        // Wrap the second paragraph text
        String[] wrappedSecondParagraph = wrapText(secondParagraphText, secondParagraphFont, secondParagraphFontSize, pageWidth - 2 * sideMargin);

        // Calculate the Y-coordinate to position the second paragraph below the first one
        float secondParagraphYCoordinate = 100; // Fixed 200px margin from the bottom

        // Begin the text section for the second paragraph
        contentStream.beginText();

        // Set the font and font size for the second paragraph
        contentStream.setFont(secondParagraphFont, secondParagraphFontSize);

        // Set the text position for the second paragraph
        contentStream.newLineAtOffset(sideMargin, secondParagraphYCoordinate);

        // Add the wrapped lines of the second paragraph text
        for (String line : wrappedSecondParagraph) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
        }

        // End the text section for the second paragraph
        contentStream.endText();

        contentStream.close();
        document.save(dirPath + "\\" +title.replaceFirst("\\.*","") + ".pdf");
        document.close();
        // ... (Remaining code)

    }

    private static String[] wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        float currentWidth = 0;

        for (char c : text.toCharArray()) {
            float charWidth = font.getStringWidth(String.valueOf(c)) * fontSize / 1000f;

            if (currentWidth + charWidth > maxWidth) {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(String.valueOf(c));
                currentWidth = charWidth;
            } else {
                currentLine.append(c);
                currentWidth += charWidth;
            }
        }

        lines.add(currentLine.toString().trim());
        return lines.toArray(new String[0]);
    }
    private static String sanitizeString(String input) {
        // Replace or remove illegal characters
        return input.replaceAll("[\u0000-\u001F]", ""); // Replace control characters with an empty string
    }
}


