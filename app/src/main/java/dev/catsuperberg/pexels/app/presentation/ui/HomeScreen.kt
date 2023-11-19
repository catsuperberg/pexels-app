package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.catsuperberg.pexels.app.presentation.ui.component.FeaturedCollections
import dev.catsuperberg.pexels.app.presentation.ui.component.SearchBar
import dev.catsuperberg.pexels.app.presentation.view.model.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar()
        FeaturedCollections()

        LazyColumn(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp)
        ) {
            items(100) {
                Text(text = "Home", modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
