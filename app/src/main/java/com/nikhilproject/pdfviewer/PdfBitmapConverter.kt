package com.nikhilproject.pdfviewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class PdfBitmapConverter(
    private val context: Context
) {

    private var renderer: PdfRenderer? = null

    suspend fun pdfToBitmap(contentUri: Uri): List<Bitmap> {

        return withContext(Dispatchers.IO) {
            renderer?.close()

            context
                .contentResolver.openFileDescriptor(contentUri, "r")
                ?.use { descriptor ->
                    with(PdfRenderer(descriptor)) {
                        renderer = this

                        return@withContext (0 until pageCount).map { index ->
                            async {

                                openPage(index).use { page ->
                                    val bimap = Bitmap.createBitmap(
                                        page.width,
                                        page.height,
                                        Bitmap.Config.ARGB_8888
                                    )

                                    val canvas = Canvas(bimap).apply {
                                        drawColor(android.graphics.Color.WHITE)
                                        drawBitmap(bimap, 0f, 0f, null)
                                    }

                                    page.render(
                                        bimap,
                                        null,
                                        null,
                                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                    )
                                    bimap
                                }

                            }

                        }.awaitAll()
                    }
                }
            return@withContext emptyList()

        }
    }

}
