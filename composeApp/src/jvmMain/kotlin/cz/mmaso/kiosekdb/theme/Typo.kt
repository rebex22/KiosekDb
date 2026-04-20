package cz.mmaso.kiosekdb.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import kiosekdb.composeapp.generated.resources.Res
import kiosekdb.composeapp.generated.resources.latoblack
import kiosekdb.composeapp.generated.resources.latobold
import kiosekdb.composeapp.generated.resources.latoregular
import kiosekdb.composeapp.generated.resources.pacificoregular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font


@Composable
fun Lato() = FontFamily(
    Font(Res.font.latoregular, FontWeight.Normal),
    Font(Res.font.latobold, FontWeight.Bold),
    Font(Res.font.latoblack, FontWeight.Black),
    Font(Res.font.pacificoregular, FontWeight.Normal),
)

@Composable
fun MyTypography() = Typography().run {
    val fontFamily = Lato()
    copy(
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily ),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily ),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily)
    )
}

val SmallSpacing = 4.dp
val MediumSpacing = 8.dp
val LargeSpacing = 16.dp


@Composable
fun MyShapes() = Shapes().run {
    copy(
        small = RoundedCornerShape(SmallSpacing),
        medium = RoundedCornerShape(MediumSpacing),
        large = RoundedCornerShape(LargeSpacing)
    )
}


