package dev.catsuperberg.pexels.app.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.ui.destinations.BookmarksScreenDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination

data class DestinationIcon(
    val enabledId: Int,
    val disabledId: Int,
) {
    companion object {
        val enabledColor: Color
            @Composable
            @ReadOnlyComposable
            get() = MaterialTheme.colorScheme.onSurface

        val disabledColor: Color
            @Composable
            @ReadOnlyComposable
            get() = MaterialTheme.colorScheme.primary
    }
}

enum class BottomBarDestination(
    val direction: Destination,
    val buttonDescriptionId: Int,
    val icon: DestinationIcon
) {
    Home(
        direction = HomeScreenDestination,
        buttonDescriptionId = R.string.home_button,
        icon = DestinationIcon(enabledId = R.drawable.ic_home_outlined, disabledId = R.drawable.ic_home_filled)
    ),
    Bookmarks(
        direction = BookmarksScreenDestination,
        buttonDescriptionId = R.string.bookmarks_button,
        icon = DestinationIcon(enabledId = R.drawable.ic_bookmark_outline, disabledId = R.drawable.ic_bookmark_filled)
    );

    companion object {
        val destinations = BottomBarDestination.values().map { it.direction }
    }
}
