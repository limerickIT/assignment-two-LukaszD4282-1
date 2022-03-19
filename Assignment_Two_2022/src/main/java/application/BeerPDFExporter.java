/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import model.Beer;
import model.Brewery;
import model.Category;
import model.Style;

public class BeerPDFExporter {

    private Beer beer;
    private Brewery brewery;
    private Category category;
    private Style style;

    public BeerPDFExporter(Beer beer, Brewery brewery, Category category, Style style) {
        this.beer = beer;
        this.brewery = brewery;
        this.category = category;
        this.style = style;
    }

    public static void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Beer name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("ABV", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Image", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Sell price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Brewery name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Website", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Beer category", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Beer style", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        table.addCell(String.valueOf(beer.getId()));
        table.addCell(beer.getName());
        table.addCell(beer.getAbv().toString());
        table.addCell(beer.getDescription());
        table.addCell(beer.getImage());
        table.addCell(beer.getSell_price().toString());
        table.addCell(brewery.getName());
        table.addCell(brewery.getWebsite());
        table.addCell(category.getCat_name());
        table.addCell(style.getStyle_name());
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.5f, 1.5f, 3.5f, 3.0f, 1.5f, 2.5f, 2.5f, 1.5f, 2.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
