package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.catsuperberg.pexels.app.presentation.helper.UiText.StringResource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarScaffold(
    messageFlow: SharedFlow<StringResource>,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(true) {
        messageFlow.map { it.asString(context) }.collect(snackbarHostState::showSnackbar)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data -> AppSnackbar(data) },
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 120.dp)
            )
        },
        content = content
    )
}
