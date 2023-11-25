package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import coil.compose.SubcomposeAsyncImage
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.ui.extension.shimmer

@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    url: String,
    aspectRation: Float,
    description: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = onClick?.let { modifier.clickable { it() } } ?: modifier

    SubcomposeAsyncImage(
        model = url,
        loading = { Box(modifier = Modifier.shimmer()) },
        error = {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_placeholder),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            )
        },
        contentDescription = description ?: stringResource(R.string.default_photo_description),
        contentScale = ContentScale.FillWidth,
        modifier = clickableModifier
            .aspectRatio(aspectRation)
    )
}
