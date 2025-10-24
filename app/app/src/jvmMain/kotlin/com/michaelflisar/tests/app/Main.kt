package com.michaelflisar.tests.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.michaelflisar.tests.core.RootContent
import com.michaelflisar.tests.core.classes.Device
import com.michaelflisar.tests.core.resources.Res
import com.michaelflisar.tests.core.resources.app_name
import org.jetbrains.compose.resources.stringResource

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name)
        ) {
            RootContent(Device.Windows, SharedSetup.TESTS)
        }

    }
}
