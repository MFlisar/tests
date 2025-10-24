package com.michaelflisar.tests.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.michaelflisar.tests.core.RootContent
import com.michaelflisar.tests.core.classes.Device
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
suspend fun main() {
    document.getElementById("ComposeLoading")?.remove()
    CanvasBasedWindow("Tests", canvasElementId = "ComposeTarget") {
        RootContent(Device.Wasm, SharedSetup.TESTS)
    }
}