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
import java.util.ArrayList;
import java.util.List;

public class wordToPdfCreator {
    public static void main(String[] args) throws IOException{
        wordToPdfCreator.createBookPdf();
    }

    public static void createBookPdf() throws IOException {
        PDDocument document = new PDDocument();
        PDRectangle myPageSize = new PDRectangle(600,800);
        PDPage myPage = new PDPage(myPageSize);
        document.addPage(myPage);

        PDPageContentStream contentStream = new PDPageContentStream(document,myPage);
        float pageWidth = document.getPage(0).getMediaBox().getWidth();
        float pageHeight = document.getPage(0).getMediaBox().getHeight();

        PDImageXObject bookImage = PDImageXObject.createFromFile("C:\\Users\\Fuz\\Downloads\\ds.jpg", document);

// Get the width and height of the image
        float imageWidth = bookImage.getWidth();
        float imageHeight = bookImage.getHeight();

// Set the margins
        float topMargin = 20;
        float sideMargin = 50;

// Calculate the X-coordinate to center the image horizontally
        float imageXCoordinate = (pageWidth - imageWidth) / 2;

// Calculate the Y-coordinate to position the image with the margin at the top of the page
        float imageYCoordinate = pageHeight - imageHeight - topMargin;

// Draw the image
        contentStream.drawImage(bookImage, imageXCoordinate, imageYCoordinate);

// Set font and font size for the title
        PDFont titleFont = PDType1Font.HELVETICA_BOLD;
        float titleFontSize = 32 ;

// Set the title text
        String titleText = "Your Title";

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
        String paragraphText = "A masterpiece of rebellion and imprisonment where war is peace, freedom is slavery, and Big Brother is watching. Thought Police, Big Brother, Orwellian—these words have entered our vocabulary because of George Orwells classic dystopian novel, 1984. The story of one mans nightmare odyssey as he pursues a forbidden love affair through a world ruled by warring states and a power structure that c ontrols not only information but also individual thought and memory. 1984 is a prophetic, haunting tale. More relevant than ever before, 1984 exposes the worst crimes imaginable: the destruction of truth, freedom, and individual ity. With a foreword by Thomas Pynchon. A masterpiece of rebellion and imprisonment where war is peace, freedom is slavery, and Big Brother is watching. View our feature on George Orwell's 1984 Thought Police, Big Brother, Orwel lian—these words have entered our vocabulary because of George Orwell's classic dystopian novel, 1984. The story of one man's nightmare odyssey as he pursues a forbidden love affair through a world r relon" +
                "ASDKHJSAKDHHKSASHD" +
                "ASDKJHSHAHDKSADSH" +
                "ASDHJKSAHDKSAJQWKE JLQWJEKLQWJLKEJWQLKE SA DKSADKHSAKDKSA.";

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
        String secondParagraphText = "Another paragraph below the first KALSJDLKASJDKSLAJDLJSALD JLASDASDSA ASDJLASDJLSAJDLSADJ D one.";

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
        document.save("D:\\file.pdf");
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

}


