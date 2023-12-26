package com.sistemas.demo.reportes;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RGBColor;
import com.sistemas.demo.modelo.Persona;

import jakarta.servlet.http.HttpServletResponse;

public class PersonasExporterPDF {
	
	private List<Persona> listaPersonas;

	public PersonasExporterPDF(List<Persona> listaPersonas) {
		
		this.listaPersonas = listaPersonas;
	}
	
	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(new Color(51, 122, 183)); // Azul oscuro
        celda.setPadding(8);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(RGBColor.WHITE);

        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Nombres", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Apellidos", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Teléfonos", fuente));
        tabla.addCell(celda);
    }

    private void escribirDatosDeLaTabla(PdfPTable tabla) {
        for (Persona persona : listaPersonas) {
            tabla.addCell(String.valueOf(persona.getId()));
            tabla.addCell(persona.getNombre());
            tabla.addCell(persona.getApellido());
            tabla.addCell(persona.getTelefono());
        }
    }

    public void exportar(HttpServletResponse response) throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuenteTitulo.setColor(new Color(51, 122, 183)); // Azul oscuro
        fuenteTitulo.setSize(18);

        Paragraph titulo = new Paragraph("Lista de Personas", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(80);
        tabla.setSpacingAfter(15);
        tabla.setWidths(new float[]{2f, 6f, 8f, 4f});

        escribirCabeceraDeLaTabla(tabla);
        escribirDatosDeLaTabla(tabla);

        documento.add(tabla);
        documento.close();
    }
}