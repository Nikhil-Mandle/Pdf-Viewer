package com.nikhilproject.pdfviewer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nikhilproject.pdfviewer.ui.theme.PdfViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val pdfUri = intent?.data

        setContent {
            PdfViewerTheme {
                var showPdfViewer by remember { mutableStateOf(false) }
                var selectedPdfUri by remember { mutableStateOf<Uri?>(pdfUri) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PdfViewerScreen(
                        modifier = Modifier.padding(innerPadding),
                        showPdfViewer = showPdfViewer,
                        onBack = { showPdfViewer = false },
                        onPdfSelected = { showPdfViewer = true },
                        pdfUri = selectedPdfUri,
                        onNewPdfUri = { uri -> selectedPdfUri = uri }
                    )
                }

                BackHandler(enabled = showPdfViewer) {
                    showPdfViewer = false
                }
            }
        }
    }
}