package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination
import dev.catsuperberg.pexels.app.presentation.ui.extension.shadow
import dev.catsuperberg.pexels.app.presentation.ui.theme.extendedColors

@Composable
fun BottomBar(
    navController: NavController,
    currentDestination: Destination?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                color = MaterialTheme.extendedColors.black.copy(alpha = 0.06f),
                offsetY = (-2).dp,
                blurRadius = 20.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            BottomBarDestination.values().forEach { destination ->
                Button(
                    enabled = currentDestination?.let { it != destination.direction } ?: false,
                    onClick = {
                        navController.navigate(destination.direction as DirectionDestinationSpec) {
                            popUpTo(destination.direction.route) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = destination.buttonText)
                }
            }
        }
    }
}
