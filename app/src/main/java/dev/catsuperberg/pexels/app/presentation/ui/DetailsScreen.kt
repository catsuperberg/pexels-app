package dev.catsuperberg.pexels.app.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.ui.component.ExploreStub
import dev.catsuperberg.pexels.app.presentation.ui.component.SnackbarScaffold
import dev.catsuperberg.pexels.app.presentation.ui.component.UpButtonHeader
import dev.catsuperberg.pexels.app.presentation.view.model.DetailsScreenNavArgs
import dev.catsuperberg.pexels.app.presentation.view.model.DetailsViewModel

@RootNavGraph
@Destination (navArgsDelegate = DetailsScreenNavArgs::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val bookmarked = viewModel.bookmarked.collectAsState()
    val photo = viewModel.photo.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val photoNotFound = viewModel.photoNotFound.collectAsState()

    LaunchedEffect(true) { viewModel.navigationEvent.collect { command -> command(navigator)} }

    SnackbarScaffold(messageFlow = viewModel.snackBarMessage) { _ ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxSize()
        ) {
            UpButtonHeader(headerText = photo.value?.author ?: "", loading = loading) { navigator.popBackStack() }

            if (photoNotFound.value) {
                ExploreStub(Modifier.fillMaxSize(), stringResource(R.string.image_not_found), viewModel::onExplore)
                return@SnackbarScaffold
            }
            photo.value?.also {
                DetailsPhotoCard(
                    url = it.url,
                    aspectRation = it.aspectRatio,
                    description = it.description,
                    onFinished = viewModel::onImageLoadFinished,
                    onError = viewModel::onImageLoadFailed,
                    roundedCornerShape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)

                )
                DetailsButtons(
                    bookmarked = bookmarked.value,
                    onDownload = viewModel::onDownload,
                    onBookmarkedChange = viewModel::onBookmarkedChange
                )
            }
        }
    }
}

@Composable
private fun DetailsPhotoCard(
    modifier: Modifier = Modifier,
    url: String,
    aspectRation: Float,
    onFinished: () -> Unit,
    onError: () -> Unit,
    roundedCornerShape: RoundedCornerShape,
    description: String? = null,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
            .fillMaxWidth()
    ) {
        SubcomposeAsyncImage(
            model = url,
            onSuccess = { onFinished() },
            onError = { onError() },
            contentDescription = description ?: stringResource(R.string.default_photo_description),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .clip(roundedCornerShape)
                .aspectRatio(aspectRation)
        )
    }
}

@Composable
private fun DetailsButtons(
    modifier: Modifier = Modifier,
    bookmarked: Boolean,
    onDownload: () -> Unit,
    onBookmarkedChange: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxHeight()
                .width(180.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = onDownload,
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                ) {
                    Icon(
                        painterResource(R.drawable.ic_download),
                        contentDescription = null,
                    )
                }

                Text(
                    text = stringResource(R.string.download),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight()
                )
            }
        }

        val icon = if (bookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
        val contentColor = if (bookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

        Button(
            onClick = onBookmarkedChange,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = contentColor,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        ) {
            Icon(
                painterResource(icon),
                contentDescription = stringResource(R.string.bookmark),
            )
        }
    }
}
