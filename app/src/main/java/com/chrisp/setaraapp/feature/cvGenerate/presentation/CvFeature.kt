package com.chrisp.setaraapp.feature.cvGenerate.presentation

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.chrisp.setaraapp.feature.cvGenerate.domain.PdfGenerator

@Composable
fun CvFeature(
    onSuccess: () -> Unit = {},
) {
    val navController = rememberNavController()
    val viewModel: FormViewModel = viewModel()
    var showPreview by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showPreview) {
        val html = viewModel.generateHtmlContent()
        PreviewScreen(
            htmlContent = html,
            onDownloadClick = {
                val success = PdfGenerator.generatePdf(context, html)
                if (success) {
                    Toast.makeText(context, "PDF generated successfully!", Toast.LENGTH_SHORT)
                        .show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Failed to generate PDF.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    } else {
        FormScreen(
            viewModel = viewModel,
            navController = navController,
            onNavigateToPreview = { showPreview = true }
        )
    }
}