package com.poorskill.r6adssensitivitycalculator.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.poorskill.r6adssensitivitycalculator.ui.Theme

/**
 * Selected theme, held process-wide so that picking one in Settings repaints every open screen
 * without an `Activity.recreate()`. Seeded from [com.poorskill.r6adssensitivitycalculator.settings.Settings]
 * in each activity's `onCreate`.
 */
val appTheme = mutableStateOf(Theme.System)

/**
 * The 9 themes the app has always shipped, as Material 3 colour schemes. The six season palettes
 * are the exact colours the old `res/values/styles.xml` used, so a user's stored theme id keeps
 * meaning what it meant before the Compose rewrite.
 */
@Composable
fun R6Theme(theme: Theme, content: @Composable () -> Unit) {
  val dark =
      when (theme) {
        Theme.Light -> false
        Theme.System -> isSystemInDarkTheme()
        else -> true
      }
  val colorScheme =
      when (theme) {
        Theme.System -> if (dark) DarkScheme else LightScheme
        Theme.Light -> LightScheme
        Theme.Dark -> DarkScheme
        Theme.BlackIce -> season(Color(0xFF2F476C), Color(0xFF4190B3), Color(0xFFA0BED9))
        Theme.DustLine -> season(Color(0xFF73664F), Color(0xFFCBA746), Color(0xFFBF9445))
        Theme.SkullRain -> season(Color(0xFF0C250E), Color(0xFF4A8B3B), Color(0xFF1B4C22))
        Theme.RedCrow -> season(Color(0xFF800601), Color(0xFFB7242E), Color(0xFFFB0015))
        Theme.VelvetShell -> season(Color(0xFF542A7A), Color(0xFF722493), Color(0xFFA33FC5))
        Theme.Health -> season(Color(0xFF2C3359), Color(0xFF2845B3), Color(0xFF2646A6))
      }

  // Season themes are dark regardless of the system setting, so the status bar icons have to be
  // told about it or they go invisible on a light-mode device.
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      WindowCompat.getInsetsController(window, view).run {
        isAppearanceLightStatusBars = !dark
        isAppearanceLightNavigationBars = !dark
      }
    }
  }

  MaterialTheme(colorScheme = colorScheme, content = content)
}

private val LightScheme =
    lightColorScheme(
        primary = Color(0xFF3E5760),
        onPrimary = Color.White,
        secondary = Color(0xFF5B5D5B),
        onSecondary = Color.White,
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF060C0D),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF060C0D),
        surfaceVariant = Color(0xFFE4E9EB),
        onSurfaceVariant = Color(0xFF3E4749),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF5F7F8),
        surfaceContainer = Color(0xFFEFF2F3),
        surfaceContainerHigh = Color(0xFFE8ECEE),
        surfaceContainerHighest = Color(0xFFE1E6E9),
        outline = Color(0xFFB9C2C6),
        outlineVariant = Color(0xFFDCE2E4),
    )

private val DarkScheme =
    darkColorScheme(
        primary = Color(0xFFAFAFAF),
        onPrimary = Color(0xFF141414),
        secondary = Color(0xFFD6D6D6),
        onSecondary = Color(0xFF141414),
        background = Color(0xFF141414),
        onBackground = Color.White,
        surface = Color(0xFF141414),
        onSurface = Color.White,
        surfaceVariant = Color(0xFF2A2A2A),
        onSurfaceVariant = Color(0xFFD6D6D6),
        surfaceContainerLowest = Color(0xFF0E0E0E),
        surfaceContainerLow = Color(0xFF1B1B1B),
        surfaceContainer = Color(0xFF202020),
        surfaceContainerHigh = Color(0xFF272727),
        surfaceContainerHighest = Color(0xFF2E2E2E),
        outline = Color(0xFF5C5C5C),
        outlineVariant = Color(0xFF3A3A3A),
    )

/**
 * Builds a scheme from the three colours a season theme actually defines. Container shades are
 * derived by lightening the background so cards separate from it — the old XML themes had no
 * surface colours of their own.
 */
private fun season(background: Color, primary: Color, secondary: Color): ColorScheme =
    darkColorScheme(
        primary = primary,
        onPrimary = contrastOn(primary),
        secondary = secondary,
        onSecondary = contrastOn(secondary),
        background = background,
        onBackground = Color.White,
        surface = background,
        onSurface = Color.White,
        surfaceVariant = background.lighten(0.12f),
        onSurfaceVariant = Color.White,
        surfaceContainerLowest = background.lighten(0.03f),
        surfaceContainerLow = background.lighten(0.06f),
        surfaceContainer = background.lighten(0.09f),
        surfaceContainerHigh = background.lighten(0.12f),
        surfaceContainerHighest = background.lighten(0.15f),
        outline = background.lighten(0.35f),
        outlineVariant = background.lighten(0.22f),
    )

private fun Color.lighten(fraction: Float) = lerp(this, Color.White, fraction)

// ponytail: luminance threshold instead of a hand-tuned onPrimary per season. Bump a specific
// season to an explicit colour if one of them ever reads badly on a real screen.
private fun contrastOn(color: Color) = if (color.luminance() > 0.35f) Color.Black else Color.White
