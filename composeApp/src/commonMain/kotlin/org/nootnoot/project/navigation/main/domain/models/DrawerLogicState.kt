package org.nootnoot.project.navigation.main.domain.models

data class DrawerLogicState(
    val isDrawerOpen: Boolean = false,
    val isContentUnderDrawer: Boolean = false,
    val drawerOffsetX: Float = 0f,
    val isDragging: Boolean = false,
    val dragStartX: Float = 0f,
    val maxOffsetX: Float = 0f,
    val drawerWidth: Float = 0f,
)