package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.catsuperberg.pexels.app.presentation.navigation.BottomBarDestination
import dev.catsuperberg.pexels.app.presentation.navigation.DestinationIcon
import dev.catsuperberg.pexels.app.presentation.ui.appCurrentDestinationAsState
import dev.catsuperberg.pexels.app.presentation.ui.destinations.Destination
import dev.catsuperberg.pexels.app.presentation.ui.extension.shadow
import dev.catsuperberg.pexels.app.presentation.ui.theme.extendedColors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = navController.appCurrentDestinationAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .shadow(
                color = MaterialTheme.extendedColors.black.copy(alpha = 0.06f),
                offsetY = (-2).dp,
                blurRadius = 20.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            BottomBarDestination.values().forEach { destination ->
                val destinationInBackStack = navController.currentBackStack.value
                    .map { it.destination.route }
                    .contains(destination.direction.route)
                val isCurrentDestination = currentDestination.value?.let { it == destination.direction } ?: false
                val onClick: () -> Unit = {
                    if (destinationInBackStack)
                        navController.popBackStack(destination.direction.route, inclusive = false, saveState = true)
                    else
                        navController.navigate(destination.direction.route) { restoreState = true }
                }

                Button(
                    enabled = isCurrentDestination.not(),
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = DestinationIcon.enabledColor,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = DestinationIcon.disabledColor
                    ),
                    interactionSource = NoRippleInteractionSource(),
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    val icon = if (isCurrentDestination) destination.icon.disabledId else destination.icon.enabledId
                    Icon(
                        painterResource(icon),
                        contentDescription = stringResource(destination.buttonDescriptionId),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        AnimatedSelector(currentDestination)
    }
}

@Composable
private fun AnimatedSelector(currentDestination: State<Destination?>) {
    val color = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    val selectorWidth = remember { with(density) { 24.dp.toPx() } }
    val buttonCount = remember { BottomBarDestination.values().count() }
    val selectedPosition = 1 + BottomBarDestination.values().indexOfLast { destination ->
        currentDestination.value?.let { it.route == destination.direction.route } ?: false
    }

    val position by animateFloatAsState(
        targetValue = selectedPosition.toFloat(),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Selector position"
    )

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)) {
        val buttonSize = size.width / buttonCount
        val start = position * buttonSize - (buttonSize / 2) - (selectorWidth / 2)
        val yOffset = size.height / 2
        drawLine(
            color = color,
            start = Offset(start, yOffset),
            end = Offset(start + selectorWidth, yOffset),
            cap = StrokeCap.Round,
            strokeWidth = size.height
        )
    }
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}
