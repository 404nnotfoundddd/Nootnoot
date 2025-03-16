package org.nootnoot.project.navigation.main.domain.models

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route
}