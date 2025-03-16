package org.nootnoot.project.navigation.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.nootnoot.project.core.theme.ScreenSize
import org.nootnoot.project.navigation.main.domain.models.DrawerLogicActions
import org.nootnoot.project.navigation.main.domain.models.DrawerLogicState

class DrawerLogicViewModel : ViewModel() {
    private var firstTime = true
    private val _state = MutableStateFlow(DrawerLogicState())

    val state = _state
        .onStart {
            if (firstTime) observeScreenSize()
            firstTime = false
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    fun onAction(action: DrawerLogicActions) {
        when (action) {
            is DrawerLogicActions.closeDrawer -> closeDrawer()
            is DrawerLogicActions.openDrawer -> openDrawer()
            is DrawerLogicActions.onDragStart -> onDragStart()
            is DrawerLogicActions.onDragEnd -> onDragEnd()
            is DrawerLogicActions.onDragCancel -> onDragCancel()
            is DrawerLogicActions.onDrag -> onDrag(action.dragAmount)
            is DrawerLogicActions.onMainContentClickWhenDrawerOpen -> closeDrawer()
        }
    }

    private fun onDrag(dragAmount: Float) {
        val maxOffsetX = state.value.maxOffsetX
        val newOffset =
            (state.value.drawerOffsetX + dragAmount).coerceIn(0f, maxOffsetX)

        if (newOffset >= 0 && newOffset <= maxOffsetX) {
            _state.update {
                it.copy(
                    drawerOffsetX = newOffset,
                    isContentUnderDrawer = true,
                )
            }
        }

        println("ON DRAG: offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")

    }

    private fun onDragEnd() {
        if (state.value.isDrawerOpen) {
            println("ON DRAG: drawer open")
            println("ON DRAG: dragStartX Calculation: ${state.value.dragStartX - 200}")
            val isEnoughToSwipe = (state.value.dragStartX - 100) > state.value.drawerOffsetX
            println("ON DRAG END: offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")

            if (isEnoughToSwipe) { // Left swipe while opened -> close
                println("ON DRAG END: isEnoughToSwipe offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")
                closeDrawer()
            } else { // Right swipe while opened -> stay opened
                println("ON DRAG END: !!!isNotEnoughToSwipe offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")
                openDrawer()
            }
        } else {
            println("ON DRAG: drawer closed")

            val isEnoughToSwipe = (state.value.dragStartX + 100) < state.value.drawerOffsetX
            println("ON DRAG END: offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")

            if (isEnoughToSwipe) { // Right swipe while closed -> open
                openDrawer()
            } else { // Left swipe while closed -> stay closed
                println("ON DRAG END: !!!isNotEnoughToSwipe offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")
                closeDrawer()
            }
        }
    }

    private fun onDragCancel() {
        println("ON DRAG CANCEL: offsetX: ${state.value.drawerOffsetX}, dragStartX: ${state.value.dragStartX}")
        val maxOffsetX = state.value.maxOffsetX

        _state.update {
            it.copy(
                isDragging = false,
                drawerOffsetX = if (it.isDrawerOpen) maxOffsetX else 0f,
                isContentUnderDrawer = false,
            )
        }
    }


    private fun onDragStart() {
        _state.update {
            it.copy(
                isDragging = true,
                dragStartX = it.drawerOffsetX,
                isContentUnderDrawer = false,
            )
        }
    }


    private fun closeDrawer() {
        _state.update {
            it.copy(
                isDragging = false,
                drawerOffsetX = 0f,
                isContentUnderDrawer = false,
            )
        }
    }

    private fun openDrawer() {
        state.value.maxOffsetX.let { maxOffsetX ->
            _state.update {
                it.copy(
                    isDrawerOpen = true,
                    drawerOffsetX = maxOffsetX
                )
            }
        }
    }

    private fun observeScreenSize() {
        viewModelScope.launch {
            ScreenSize.collect { screenSize ->
                _state.update {
                    it.copy(
                        maxOffsetX = screenSize.first / 1.5f
                    )
                }
            }
        }
    }
}

