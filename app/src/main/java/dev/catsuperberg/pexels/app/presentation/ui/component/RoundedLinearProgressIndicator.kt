package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private const val LinearAnimationDuration = 1800

private const val FirstLineHeadDuration = 750
private const val FirstLineTailDuration = 850
private const val SecondLineHeadDuration = 567
private const val SecondLineTailDuration = 533

private const val FirstLineHeadDelay = 0
private const val FirstLineTailDelay = 333
private const val SecondLineHeadDelay = 1000
private const val SecondLineTailDelay = 1267

private val FirstLineHeadEasing = CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
private val FirstLineTailEasing = CubicBezierEasing(0.4f, 0f, 1f, 1f)
private val SecondLineHeadEasing = CubicBezierEasing(0f, 0f, 0.65f, 1f)
private val SecondLineTailEasing = CubicBezierEasing(0.1f, 0f, 0.45f, 1f)

private val LinearIndicatorHeight = ProgressIndicatorDefaults.CircularStrokeWidth
private val LinearIndicatorWidth = 240.dp

@Composable
fun RoundedLinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    val infiniteTransition = rememberInfiniteTransition()
    val firstLineHead by infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at FirstLineHeadDelay with FirstLineHeadEasing
                1f at FirstLineHeadDuration + FirstLineHeadDelay
            }
        )
    )
    val firstLineTail by infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at FirstLineTailDelay with FirstLineTailEasing
                1f at FirstLineTailDuration + FirstLineTailDelay
            }
        )
    )
    val secondLineHead by infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at SecondLineHeadDelay with SecondLineHeadEasing
                1f at SecondLineHeadDuration + SecondLineHeadDelay
            }
        )
    )
    val secondLineTail by infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at SecondLineTailDelay with SecondLineTailEasing
                1f at SecondLineTailDuration + SecondLineTailDelay
            }
        )
    )
    Canvas(
        modifier
            .progressSemantics()
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
    ) {
        val strokeWidth = size.height
        drawLinearIndicatorBackground(backgroundColor, strokeWidth)
        if (firstLineHead - firstLineTail > 0) {
            drawLinearIndicator(
                firstLineHead,
                firstLineTail,
                color,
                strokeWidth
            )
        }
        if ((secondLineHead - secondLineTail) > 0) {
            drawLinearIndicator(
                secondLineHead,
                secondLineTail,
                color,
                strokeWidth
            )
        }
    }
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth, cap = StrokeCap.Round)
}

private fun DrawScope.drawLinearIndicatorBackground(
    color: Color,
    strokeWidth: Float
) = drawLinearIndicator(0f, 1f, color, strokeWidth)
