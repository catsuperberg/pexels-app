package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catsuperberg.pexels.app.presentation.view.model.HomeViewModel

@Composable
fun FeaturedCollections(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    viewModel: HomeViewModel
) {
    val collections = viewModel.collections.collectAsState()
    val selected = viewModel.collection.collectAsState()

    val present = remember { derivedStateOf { collections.value.isNotEmpty() } }

    if (present.value) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding,
            modifier = modifier
                .fillMaxWidth()
                .height(38.dp)
        ) {
            items(collections.value.count()) { index ->
                Button(
                    onClick = { viewModel.onCollectionSelected(index) },
                    colors = if (selected.value == index) ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) else ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Text(collections.value[index])
                }
            }
        }
    }
}
