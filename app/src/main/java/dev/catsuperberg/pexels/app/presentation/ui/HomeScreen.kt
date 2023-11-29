package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.helper.PaginationEffects
import dev.catsuperberg.pexels.app.presentation.ui.component.ExploreStub
import dev.catsuperberg.pexels.app.presentation.ui.component.FeaturedCollections
import dev.catsuperberg.pexels.app.presentation.ui.component.NetworkStub
import dev.catsuperberg.pexels.app.presentation.ui.component.PhotoCard
import dev.catsuperberg.pexels.app.presentation.ui.component.RoundedLinearProgressIndicator
import dev.catsuperberg.pexels.app.presentation.ui.component.SearchBar
import dev.catsuperberg.pexels.app.presentation.ui.transition.FromLeftHorizontalTransition
import dev.catsuperberg.pexels.app.presentation.view.model.HomeViewModel

@RootNavGraph(start = true)
@Destination(style = FromLeftHorizontalTransition::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    screenReady: MutableState<Boolean>,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val photos = viewModel.photos.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val networkError = viewModel.networkError.collectAsState()
    val listState = rememberLazyStaggeredGridState()

    val empty = remember { derivedStateOf { photos.value.isEmpty() } }

    PaginationEffects(listState, viewModel.pageRequestAvailable.collectAsState(), viewModel::onRequestMorePhotos)
    LaunchedEffect(true) { viewModel.requestSource.collect { listState.scrollToItem(0) } }
    LaunchedEffect(true) { viewModel.navigationEvent.collect { command -> command(navigator)} }
    LaunchedEffect(true) {
        viewModel.loading.collect { if (!it && screenReady.value.not()) screenReady.value = true }
    }

    Column(modifier = modifier.fillMaxSize()) {
        HomeScreenHeader(loading = loading, viewModel = viewModel)

        if (empty.value) {
            if (networkError.value)
                NetworkStub(Modifier.fillMaxSize(), viewModel::onRetry)
            else if (loading.value.not())
                ExploreStub(Modifier.fillMaxSize(), stringResource(R.string.no_results_found), viewModel::onExplore)
            return
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            state = listState,
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            items(photos.value.count()) {
                val photo = photos.value[it]

                PhotoCard(
                    url = photo.url,
                    aspectRation = photo.aspectRatio,
                    description = photo.description,
                    onClick = { viewModel.onDetails(it) },
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                )
            }
        }
    }
}

@Composable
private fun HomeScreenHeader(modifier: Modifier = Modifier, loading: State<Boolean>, viewModel: HomeViewModel) {
    val horizontalPadding = 24.dp
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.padding(vertical = 12.dp)
        ) {
            SearchBar(
                viewModel = viewModel,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = horizontalPadding)
            )
            FeaturedCollections(contentPadding = PaddingValues(horizontal = horizontalPadding),viewModel = viewModel)
        }
        if (loading.value)
            RoundedLinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .height(4.dp)
                    .align(Alignment.BottomCenter)
            )
    }
}
