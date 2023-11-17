package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.catsuperberg.pexels.app.presentation.ui.destinations.DetailsScreenDestination

@RootNavGraph
@Destination
@Composable
fun BookmarksScreen(modifier: Modifier = Modifier, navigator: DestinationsNavigator) {
    Column(modifier = modifier)
    {
        Text(text = "Bookmarks")
        Button(onClick = { navigator.navigate(DetailsScreenDestination) }) {
            Text(text = "Details")
        }
    }
}
