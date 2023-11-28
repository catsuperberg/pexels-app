package dev.catsuperberg.pexels.app.presentation.ui.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle
import dev.catsuperberg.pexels.app.presentation.ui.appDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.BookmarksScreenDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination

object DetailsTransition : DestinationStyle.Animated {
    private const val duration = 600
    private const val offset = 3000

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideInVertically(
                    initialOffsetY = { offset },
                    animationSpec = tween(duration)
                )
            else -> null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideOutVertically(
                    targetOffsetY = { offset },
                    animationSpec = tween(duration)
                )
            else -> null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? = null

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {

        return when (targetState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideOutVertically(
                    targetOffsetY = { offset },
                    animationSpec = tween(duration)
                )
            else -> null
        }
    }
}
