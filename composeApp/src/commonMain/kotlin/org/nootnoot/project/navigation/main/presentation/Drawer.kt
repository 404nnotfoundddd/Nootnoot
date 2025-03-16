package org.nootnoot.project.navigation.main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 12.dp)
            .background(Color(0xff4a4a4a))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
        }

    }
}
