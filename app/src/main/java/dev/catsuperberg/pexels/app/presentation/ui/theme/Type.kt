package dev.catsuperberg.pexels.app.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.catsuperberg.pexels.app.R

val appFont = FontFamily(
    Font(R.font.mulish),
    Font(R.font.mulish_light, weight = FontWeight.Light),
    Font(R.font.mulish_medium, weight = FontWeight.Medium),
    Font(R.font.mulish_bold, weight = FontWeight.Bold),
    Font(R.font.mulish_italic, style = FontStyle.Italic),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
)
