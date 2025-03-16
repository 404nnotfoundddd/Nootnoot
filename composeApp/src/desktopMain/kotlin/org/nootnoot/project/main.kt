package org.nootnoot.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.nootnoot.project.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Nootnoot",
    ) {
        App()
    }
}