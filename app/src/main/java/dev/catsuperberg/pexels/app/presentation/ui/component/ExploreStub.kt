package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.catsuperberg.pexels.app.R

@Composable
fun ExploreStub(modifier: Modifier = Modifier, message: String, onExplore: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        TextButton(onClick = onExplore, contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)) {
            Text(
                text = stringResource(R.string.explore),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
