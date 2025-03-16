package org.nootnoot.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.nootnoot.project.app.App
import org.nootnoot.project.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Nootnoot",
    ) {
        App()
    }
}