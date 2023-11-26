package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.ui.extension.shimmer

@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    url: String,
    aspectRation: Float,
    author: String? = null,
    description: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = onClick?.let { modifier.clickable { it() } } ?: modifier

    Box(modifier = clickableModifier) {
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
            modifier = Modifier
                .aspectRatio(aspectRation)
        )

        author?.also {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(33.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(bottom = 4.dp)
            )
        }
    }
}
