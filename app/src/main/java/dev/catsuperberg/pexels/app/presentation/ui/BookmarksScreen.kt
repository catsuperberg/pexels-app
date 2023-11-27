package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.ui.component.PhotoCard
import dev.catsuperberg.pexels.app.presentation.ui.component.TextHeader
import dev.catsuperberg.pexels.app.presentation.view.model.BookmarksViewModel

@RootNavGraph
@Destination
@Composable
fun BookmarksScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val photos = viewModel.photos.collectAsState()

    LaunchedEffect(true) { viewModel.navigationEvent.collect { command -> command(navigator)} }

    Column(modifier = modifier) {
        TextHeader(headerText = stringResource(R.string.bookmarks))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp)
        ) {
            items(photos.value.count()) {
                val photo = photos.value[it]

                PhotoCard(
                    url = photo.url,
                    aspectRation = photo.aspectRatio,
                    description = photo.description,
                    author = photo.author,
                    onClick = { viewModel.onDetails(it) },
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                )
            }
        }
    }
}
