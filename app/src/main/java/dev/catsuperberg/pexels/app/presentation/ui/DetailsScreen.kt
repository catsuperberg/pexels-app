package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@Destination
@Composable
fun DetailsScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Details",
        modifier = modifier
    )
}
