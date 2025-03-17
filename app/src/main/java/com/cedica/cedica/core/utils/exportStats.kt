package com.cedica.cedica.core.utils

import java.text.SimpleDateFormat

import android.os.Environment
import android.util.Log
import com.cedica.cedica.data.user.PlaySession
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.Locale

fun exportToCSV(gameSessions: List<PlaySession>) {
    val csvHeader = "Fecha,Nivel,Aciertos,Errores,Tiempo (segundos)\n"
    val csvRows = gameSessions.joinToString("\n") { session ->
        "${session.date},${session.difficultyLevel},${session.correctAnswers},${session.incorrectAnswers},${session.timeSpent}"
    }

    val file = File(getDateForName("csv"))
    OutputStreamWriter(file.outputStream(), StandardCharsets.UTF_8).use { writer ->
        writer.write(csvHeader)
        writer.write(csvRows)
    }
}

fun exportToPDF(gameSessions: List<PlaySession>) {
    val pdfWriter = PdfWriter(getDateForName("pdf"))
    val pdfDocument = PdfDocument(pdfWriter)
    val document = Document(pdfDocument)

    // Título del PDF
    document.add(
        Paragraph("Historial de Partidas")
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(20f))

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

fun getDateForName(extension: String): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val dateStr = dateFormat.format(Date())

    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val filePath = File(downloadsDir, "historial_partidas_$dateStr.$extension").absolutePath
    Log.d("FileDebug", "La descarga se guardó en $filePath")
    return filePath
}

