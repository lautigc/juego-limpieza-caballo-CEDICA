package com.cedica.cedica.core.utils

import com.cedica.cedica.ui.home.GameSession
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.FileWriter

fun exportToCSV(gameSessions: List<GameSession>, filePath: String) {
    val csvHeader = "Fecha,Nivel,Aciertos,Errores,Tiempo (segundos)\n"
    val csvRows = gameSessions.joinToString("\n") { session ->
        "${session.date},${session.difficultyLevel},${session.correctAnswers},${session.incorrectAnswers},${session.timeSpent}"
    }

    FileWriter(filePath).use { writer ->
        writer.write(csvHeader)
        writer.write(csvRows)
    }
}

fun exportToPDF(gameSessions: List<GameSession>, filePath: String) {
    val pdfWriter = PdfWriter(filePath)
    val pdfDocument = PdfDocument(pdfWriter)
    val document = Document(pdfDocument)

    // Título del PDF
    document.add(
        Paragraph("Historial de Partidas")
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(20f)
        .setBold())

    // Contenido del PDF
    gameSessions.forEach { session ->
        document.add(
            Paragraph("Fecha: ${session.date}\n" +
                    "Nivel: ${session.difficultyLevel}\n" +
                    "Aciertos: ${session.correctAnswers}\n" +
                    "Errores: ${session.incorrectAnswers}\n" +
                    "Tiempo: ${session.timeSpent} segundos\n")
                .setMarginBottom(10f)
        )
    }

    document.close()
}

