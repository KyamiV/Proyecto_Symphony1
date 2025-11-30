/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.mysymphony.proyecto_symphony1.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.OutputStream;

public class GeneradorCertificado {

    static class FondoColor extends PdfPageEventHelper {
        private BaseColor color;

        public FondoColor(BaseColor color) {
            this.color = color;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            Rectangle rect = document.getPageSize();
            canvas.setColorFill(color);
            canvas.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
            canvas.fill();
        }
    }

    public static void generarCertificado(String nombreEstudiante, String instrumento,
                                          String etapa, String fecha,
                                          OutputStream os, String logoPath) {
        try {
            Document documento = new Document(PageSize.A4.rotate(), 60, 60, 60, 60);

            // 🎨 Fondo naranja claro institucional (#fbf8f3)
            BaseColor fondoColor = new BaseColor(251, 248, 243);

            PdfWriter writer = PdfWriter.getInstance(documento, os);
            writer.setPageEvent(new FondoColor(fondoColor));
            documento.open();

            // 🏛️ Logo institucional cuadrado, semi transparente, 3cm arriba y 2cm a la derecha
            try {
                Image logo = Image.getInstance(logoPath);
                logo.scaleToFit(400, 400); // tamaño grande estilo sello

                // 📐 Posición centrada
                float x = (documento.getPageSize().getWidth() - logo.getScaledWidth()) / 2;
                float y = (documento.getPageSize().getHeight() - logo.getScaledHeight()) / 2;
                logo.setAbsolutePosition(x, y);

                // 🎨 Transparencia estilo marca de agua
                PdfContentByte canvas = writer.getDirectContentUnder(); // 👈 debajo del texto
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.25f); // 25% opacidad, tenue
                canvas.setGState(gs);
                canvas.addImage(logo);
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo cargar el logo institucional desde: " + logoPath);
            }

            // Fuentes estándar
            Font fontEncabezado = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK);
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD, BaseColor.BLACK);
            Font fontNombre = new Font(Font.FontFamily.COURIER, 36, Font.BOLD, BaseColor.BLACK);
            Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.BLACK);
            Font fontFirma = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK);

            // 📄 Encabezado
            Paragraph encabezado = new Paragraph("SISTEMA ACADÉMICO ESCUELA SYMPHONY", fontEncabezado);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setSpacingBefore(20);
            documento.add(encabezado);

            // 📄 Título
            Paragraph titulo = new Paragraph("CERTIFICADO", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(10);
            titulo.setSpacingAfter(30);
            documento.add(titulo);

            // 👩‍🎓 Nombre del estudiante
            Paragraph nombre = new Paragraph(nombreEstudiante, fontNombre);
            nombre.setAlignment(Element.ALIGN_CENTER);
            nombre.setSpacingAfter(20);
            documento.add(nombre);

            // 🎶 Texto académico
            Paragraph contenido = new Paragraph(
                "Por haber cursado satisfactoriamente la " + etapa +
                " en el instrumento " + instrumento +
                ", demostrando disciplina, compromiso y excelencia musical dentro del programa académico de la Escuela Symphony.\n\n" +
                "Este reconocimiento se emite como constancia de su avance musical y académico, " +
                "conforme a los lineamientos institucionales establecidos por la Escuela Symphony.\n\n" +
                "Bogotá, " + fecha,
                fontTexto
            );
            contenido.setAlignment(Element.ALIGN_CENTER);
            contenido.setSpacingBefore(20);
            contenido.setSpacingAfter(50);
            documento.add(contenido);

            // ✍️ Firma institucional
            Paragraph firma = new Paragraph(
                "_____________________________\nMtra. Silvia Rodríguez\nDirección Académica SymphonySIAS",
                fontFirma
            );
            firma.setAlignment(Element.ALIGN_CENTER);
            firma.setSpacingBefore(60);
            documento.add(firma);

            documento.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}