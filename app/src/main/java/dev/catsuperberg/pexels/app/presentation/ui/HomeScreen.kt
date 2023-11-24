package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.ui.component.FeaturedCollections
import dev.catsuperberg.pexels.app.presentation.ui.component.SearchBar
import dev.catsuperberg.pexels.app.presentation.ui.component.shimmer
import dev.catsuperberg.pexels.app.presentation.view.model.HomeViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val photoStrings = viewModel.photos.collectAsState()
    val placeholderImage = painterResource(R.drawable.ic_placeholder)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar()
        FeaturedCollections(viewModel = viewModel)

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp)
        ) {
            items(photoStrings.value.count()) {
                val photo = photoStrings.value[it]

                AsyncImage(
                    model = "bonjour" + photo.url,
                    placeholder = placeholderImage,
                    fallback = placeholderImage,
                    error = placeholderImage,
                    contentDescription = photo.description,
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(min = 64.dp, max = 192.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .shimmer(),
                )
            }
        }
    }
}
