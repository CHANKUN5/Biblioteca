package pe.senati.service;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public final class ReportePdf
{
    private ReportePdf() {}

    public static void escribir(OutputStream salida, String titulo, String[] columnas, List<String[]> filas) throws Exception
    {
        BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        Font tituloFont = new Font(base, 16, Font.BOLD, new Color(61, 24, 120));
        Font celda = new Font(base, 11);
        Font cabeza = new Font(base, 11, Font.BOLD, Color.WHITE);

        Document documento = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(documento, salida);
        documento.open();
        documento.add(new Paragraph(titulo, tituloFont));
        documento.add(new Paragraph(" "));

        if (filas == null || filas.isEmpty())
        {
            documento.add(new Paragraph("No hay registros para este reporte.", celda));
            documento.close();
            return;
        }

        PdfPTable tabla = new PdfPTable(columnas.length);
        tabla.setWidthPercentage(100);
        for (String columna : columnas)
        {
            PdfPCell celdaCabeza = new PdfPCell(new Phrase(columna, cabeza));
            celdaCabeza.setBackgroundColor(new Color(91, 42, 168));
            celdaCabeza.setPadding(8);
            celdaCabeza.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaCabeza);
        }
        for (String[] fila : filas)
        {
            for (String valor : fila)
            {
                PdfPCell celdaFila = new PdfPCell(new Phrase(valor == null ? "" : valor, celda));
                celdaFila.setPadding(7);
                tabla.addCell(celdaFila);
            }
        }
        documento.add(tabla);
        documento.close();
    }
}
