package dev.catsuperberg.pexels.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.catsuperberg.pexels.app.destinations.DetailsScreenDestination
import dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination
import dev.catsuperberg.pexels.app.presentation.theme.ui.PexelsAppTheme
import dev.catsuperberg.pexels.app.destinations.Destination as Dest

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PexelsAppTheme {
                val navController = rememberNavController()
                val destination = navController.appCurrentDestinationAsState()

                Scaffold(
                    bottomBar = {
                        if (destination.value?.shouldShowScaffoldElements == true) {
                            BottomBar(navController, destination.value)
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        DestinationsNavHost(navGraph = NavGraphs.root, navController = navController)
                    }
                }
            }
        }
    }
}

private val Dest.shouldShowScaffoldElements get(): Boolean = this in BottomBarDestination.destinations

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navigator: DestinationsNavigator) {
    Column(modifier = modifier)
    {
        Text(text = "Home")
        Button(onClick = { navigator.navigate(DetailsScreenDestination) } ) {
            Text(text = "Details")
        }
    }
}

@RootNavGraph
@Destination
@Composable
fun BookmarksScreen(modifier: Modifier = Modifier, navigator: DestinationsNavigator) {
    Column(modifier = modifier)
    {
        Text(text = "Bookmarks")
        Button(onClick = { navigator.navigate(DetailsScreenDestination) } ) {
            Text(text = "Details")
        }
    }
}

@RootNavGraph
@Destination
@Composable
fun DetailsScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Details",
        modifier = modifier
    )
}

@Composable
fun BottomBar(
    navController: NavController,
    currentDestination: Dest?
) {
    Row() {
        BottomBarDestination.values().forEach { destination ->
            Button(
                enabled = currentDestination?. let{ it != destination.direction } ?: false,
                onClick = {
                    navController.navigate(destination.direction as DirectionDestinationSpec) {
                        launchSingleTop = true
                    }
                }
            ) {
                Text(text = destination.buttonText)
            }
        }
    }
}
