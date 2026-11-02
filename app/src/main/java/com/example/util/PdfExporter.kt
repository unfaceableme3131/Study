package com.example.util

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    private const val TAG = "PdfExporter"

    fun generateStudyPdf(
        context: Context,
        title: String,
        subject: String,
        content: String
    ): File? {
        val document = PdfDocument()
        val pageWidth = 595 // Standard A4 width in points
        val pageHeight = 842 // Standard A4 height in points
        var pageNumber = 1

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.rgb(10, 25, 47)
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val metaPaint = Paint().apply {
            color = Color.rgb(2, 132, 199)
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = Color.rgb(30, 41, 59)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.rgb(203, 213, 225)
            strokeWidth = 1f
        }

        val margin = 40f
        val usableWidth = pageWidth - (margin * 2)
        var yPos = margin + 20f

        // Header Title
        canvas.drawText("STUDY COMMAND CENTER • ACADEMIC NOTES", margin, yPos, metaPaint)
        yPos += 20f
        canvas.drawText(title.take(50), margin, yPos, titlePaint)
        yPos += 16f

        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        canvas.drawText("Subject: $subject  |  Generated: $dateStr", margin, yPos, metaPaint)
        yPos += 14f

        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, linePaint)
        yPos += 24f

        // Wrap text line by line
        val lines = content.split("\n")
        for (rawLine in lines) {
            val words = rawLine.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                val measuredWidth = bodyPaint.measureText(testLine)

                if (measuredWidth > usableWidth) {
                    canvas.drawText(currentLine, margin, yPos, bodyPaint)
                    yPos += 14f

                    if (yPos > pageHeight - margin - 30f) {
                        document.finishPage(page)
                        pageNumber++
                        val newPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                        page = document.startPage(newPageInfo)
                        canvas = page.canvas
                        yPos = margin + 20f
                    }
                    currentLine = word
                } else {
                    currentLine = testLine
                }
            }

            if (currentLine.isNotEmpty()) {
                canvas.drawText(currentLine, margin, yPos, bodyPaint)
                yPos += 14f
            }
            yPos += 4f // small paragraph gap

            if (yPos > pageHeight - margin - 30f) {
                document.finishPage(page)
                pageNumber++
                val newPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = document.startPage(newPageInfo)
                canvas = page.canvas
                yPos = margin + 20f
            }
        }

        document.finishPage(page)

        return try {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
            val fileName = "StudyNotes_${System.currentTimeMillis() % 100000}.pdf"
            val file = File(dir, fileName)
            FileOutputStream(file).use { out ->
                document.writeTo(out)
            }
            document.close()
            Log.d(TAG, "Generated PDF at: ${file.absolutePath}")
            file
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write PDF", e)
            document.close()
            null
        }
    }
}
