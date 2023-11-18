package dev.catsuperberg.pexels.app.presentation.navigation

import dev.catsuperberg.pexels.app.presentation.ui.destinations.BookmarksScreenDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination

enum class BottomBarDestination(
    val direction: Destination,
    val buttonText: String
) {
    Home(HomeScreenDestination, "home"),
    Bookmarks(BookmarksScreenDestination, "bookmarks");

    companion object {
        val destinations = BottomBarDestination.values().map { it.direction }
    }
}
