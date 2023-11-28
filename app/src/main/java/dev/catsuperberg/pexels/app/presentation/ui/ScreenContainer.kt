package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dev.catsuperberg.pexels.app.presentation.ui.component.BottomBar
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(splashScreen: SplashScreen) {
    val navController = rememberNavController()
    val destination = navController.appCurrentDestinationAsState()
    val appReady = remember { mutableStateOf(false) }
    splashScreen.setKeepOnScreenCondition { appReady.value.not() }

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
            dependenciesContainerBuilder = { dependency(HomeScreenDestination) { appReady } },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private val Destination.shouldShowScaffoldElements
    get() = this in dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination.destinations
