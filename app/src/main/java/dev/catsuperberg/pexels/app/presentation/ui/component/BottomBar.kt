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
import dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination
import dev.catsuperberg.pexels.app.presentation.ui.appCurrentDestinationAsState
import dev.catsuperberg.pexels.app.presentation.ui.extension.shadow
import dev.catsuperberg.pexels.app.presentation.ui.theme.extendedColors

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = navController.appCurrentDestinationAsState()

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
                val destinationInBackStack = navController.currentBackStack.value
                        .map { it.destination.route }
                        .contains(destination.direction.route)
                val isCurrentDestination = currentDestination.value?.let { it != destination.direction } ?: false
                Button(
                    enabled = isCurrentDestination,
                    onClick = {
                        if (destinationInBackStack)
                            navController.popBackStack(destination.direction.route, inclusive = false, saveState = true)
                        else
                            navController.navigate(destination.direction.route) { restoreState = true }
                    }
                ) {
                    Text(text = destination.buttonText)
                }
            }
        }
    }
}
