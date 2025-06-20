package com.chrisp.setaraapp.feature.cvGenerate.domain

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.itextpdf.html2pdf.ConverterProperties
import com.itextpdf.html2pdf.HtmlConverter
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider
import com.itextpdf.kernel.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object PdfGenerator {
    fun generatePdf(context: Context, htmlContent: String): Boolean {
        if (htmlContent.isBlank()) {
            Log.e("PdfGenerator", "HTML content is empty")
            return false
        }

        return try {
            val fileName = "${System.currentTimeMillis()}.pdf"
            val outputStream = createOutputStream(context, fileName)

            outputStream?.use { stream ->
                PdfWriter(stream).use { pdfWriter ->
                    com.itextpdf.kernel.pdf.PdfDocument(pdfWriter).use { pdfDocument ->
                        val converterProperties = ConverterProperties().apply {
                            setFontProvider(DefaultFontProvider(true, true, true))
                        }
                        HtmlConverter.convertToPdf(htmlContent, pdfDocument, converterProperties)
                    }
                }
            }
            true
        } catch (e: Exception) {
            Log.e("PdfGenerator", "Error creating PDF", e)
            false
        }
    }

    private fun createOutputStream(context: Context, fileName: String): OutputStream? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?.let { resolver.openOutputStream(it) }
        } else {
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            FileOutputStream(File(downloadsDir, fileName))
        }
    }
}