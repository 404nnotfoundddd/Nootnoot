package org.nootnoot.project.navigation.main.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.nootnoot.project.core.theme.ScreenSize
import org.nootnoot.project.navigation.main.domain.models.DrawerLogicActions
import org.nootnoot.project.navigation.main.domain.models.DrawerLogicState

@Composable
fun DrawerLogicRoot(
    viewModel: DrawerLogicViewModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    DrawerLogic(
        state = state.value,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun DrawerLogic(
    state: DrawerLogicState,
    onAction: (DrawerLogicActions) -> Unit,
) {
    val navController = rememberNavController()
    val screenSize = ScreenSize.collectAsStateWithLifecycle()

    val animatedOffsetX by animateFloatAsState(
        targetValue = state.drawerOffsetX,
        finishedListener = { finalValue ->
            if (finalValue == state.maxOffsetX) onAction(DrawerLogicActions.closeDrawer)
        }
    )

    val maxOffsetX = remember {
        derivedStateOf { screenSize.value.first / 1.5f }
    }

    val animatedDragProgress = remember(animatedOffsetX) {
        derivedStateOf { (animatedOffsetX / maxOffsetX.value).coerceIn(0f, 1f) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff2b2b2b))
    ) {
        Drawer(
            modifier = Modifier
                .zIndex(if (state.isContentUnderDrawer) 3f else 1f)
                .fillMaxSize()
        )

        val cornerRadius = 20 * animatedDragProgress.value

        Box(
            modifier = Modifier
                .clickable { onAction(DrawerLogicActions.onMainContentClickWhenDrawerOpen) }
                .zIndex(if (state.isContentUnderDrawer) 1f else 3f)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { onAction(DrawerLogicActions.onDragStart) },
                        onDragEnd = { onAction(DrawerLogicActions.onDragEnd) },
                        onDragCancel = { onAction(DrawerLogicActions.onDragCancel) },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            onAction(DrawerLogicActions.onDrag(dragAmount))
                        },
                    )
                }
                .graphicsLayer {
                    translationX = animatedOffsetX
                    shape = RoundedCornerShape(if (cornerRadius > 0) cornerRadius.toDp() else 0.dp)
                    clip = true
                }
        ) {
            MenuButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
            MainNavigationHost(
                navController = navController,
            )
        }
    }
}

