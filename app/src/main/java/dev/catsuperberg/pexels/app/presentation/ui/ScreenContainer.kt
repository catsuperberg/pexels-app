package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import dev.catsuperberg.pexels.app.presentation.ui.component.BottomBar
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer() {
    val navController = rememberNavController()
    val destination = navController.appCurrentDestinationAsState()

    Scaffold(
        bottomBar = {
            if (destination.value?.shouldShowScaffoldElements == true) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private val Destination.shouldShowScaffoldElements
    get() = this in dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination.destinations
