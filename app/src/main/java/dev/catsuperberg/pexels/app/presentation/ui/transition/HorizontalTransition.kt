package dev.catsuperberg.pexels.app.presentation.ui.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle
import dev.catsuperberg.pexels.app.presentation.ui.appDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.BookmarksScreenDestination
import dev.catsuperberg.pexels.app.presentation.ui.destinations.HomeScreenDestination

data object FromLeftHorizontalTransition : HorizontalTransition(false)
data object FromRightHorizontalTransition : HorizontalTransition(true)

sealed class HorizontalTransition(slideInFromRight: Boolean = false) : DestinationStyle.Animated {
    private val duration = 600
    private val offset = 1200 * if (slideInFromRight) 1 else -1

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideInHorizontally(
                    initialOffsetX = { offset },
                    animationSpec = tween(duration)
                )
            else -> null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideOutHorizontally(
                    targetOffsetX = { offset },
                    animationSpec = tween(duration)
                )
            else -> fadeOut(animationSpec = tween(duration))
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideInHorizontally(
                    initialOffsetX = { offset },
                    animationSpec = tween(duration)
                )
            else -> null
        }
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            is BookmarksScreenDestination, is HomeScreenDestination ->
                slideOutHorizontally(
                    targetOffsetX = { offset },
                    animationSpec = tween(duration)
                )
            else -> null
        }
    }
}
