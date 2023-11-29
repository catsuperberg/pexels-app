package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination
import dev.catsuperberg.pexels.app.presentation.ui.component.BottomBar
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(splashScreen: SplashScreen) {
    val navController = rememberNavController()
    val destination = navController.appCurrentDestinationAsState()

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
            dependenciesContainerBuilder = { dependency(HomeScreenDestination) { ::onReady } },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private val appReady = MutableStateFlow(false)
private fun onReady() { appReady.value = true }

private val Destination.shouldShowScaffoldElements
    get() = this in BottomBarDestination.destinations
