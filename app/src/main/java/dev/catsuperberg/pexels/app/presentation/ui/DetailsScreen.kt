package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import dev.catsuperberg.pexels.app.presentation.view.model.DetailsViewModel

@RootNavGraph
@Destination
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    Text(
        text = "Details",
        modifier = modifier
    )
}
