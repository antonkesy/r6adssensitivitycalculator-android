# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Android app (Kotlin) that converts Rainbow Six Siege ADS sensitivity values from the old
sensitivity system to Ubisoft's Y5S3 FOV-based system. Single `:app` module, no backend.

## Commands

Build and test via the Gradle wrapper from the repo root:

```
./gradlew assembleDebug          # build debug APK
./gradlew test                   # run JVM unit tests (app/src/test)
./gradlew connectedAndroidTest    # run instrumented tests (app/src/androidTest), needs a device/emulator
./gradlew testDebugUnitTest --tests "*.R6Y5S3SensitivityConverterTest"   # run a single unit test class
```

There is no lint/format tool configured beyond `.pre-commit-config.yaml`, which only runs generic
hygiene hooks (trailing whitespace, EOF fixer, YAML check, large-file check) — no Kotlin-specific
linting is enforced.

Test coverage is `R6Y5S3SensitivityConverterTest` only: it pins the converter's output for the
default input and the range extremes so a UI change can't silently move the numbers. There are no
Compose UI tests and `app/src/androidTest` is empty.

## Architecture

**Conversion math is isolated from Android.** The core logic lives in
`converter/R6Y5S3SensitivityConverter.kt`, which implements the `SensitivityConverter` interface
(`converter/SensitivityConverter.kt`) and takes plain data (`RangedValue<Int>` for ADS/FOV,
`AspectRatios` for aspect ratio) with no Android dependencies — this is the piece to touch for any
change to the sensitivity formula itself, and the natural place to add real unit tests.

**Persistence is a decorator, not baked into the math.** `PersistentSensitivityConverter` wraps
`R6Y5S3SensitivityConverter`, backing `ads`/`fov`/`aspectRatio` with a `Settings` instance (read on
construction, written via `onChange` callbacks on `RangedValue`/`AspectRatios`) so that any
edit to those values is auto-persisted. `Settings` is an interface implemented by
`UserPreferencesManager` (Java, wraps `SharedPreferences`) — go through the interface, don't
reach for `SharedPreferences` directly.

**`RangedValue<T>` and `AspectRatios`** (in `converter/data/`) are mutable, observable value
holders that use Kotlin `Delegates.observable` to fire a persistence callback on every mutation.
They deliberately know nothing about Compose: the UI keeps its own `rememberSaveable` state and
writes through to them, which is what triggers the save.

**UI is Jetpack Compose, three activities, no ViewModel/MVVM layer.** Each activity extends
`BaseActivity`, which loads `Settings`, applies the stored language, seeds the theme and exposes
`setThemedContent { }`. Screens live in `ui/screens/` (`MainScreen`, `SettingsScreen`,
`AboutScreen`); `MainScreen` shows the inputs and the 8 converted values on one scrolling screen
with no calculate step — results recompute as the sliders move. Screen-navigation helpers (opening
Settings/About/Help) are top-level functions in `ui/ActivityMapper.kt`.

**Theming lives in `ui/theme/R6Theme.kt`.** All 9 themes (System/Light/Dark plus 6 R6 season
palettes) are Material 3 `ColorScheme`s keyed by the `ui/Theme.kt` enum, whose ids are the values
stored in SharedPreferences — don't renumber them. `appTheme` is a process-wide `mutableStateOf`
so picking a theme in Settings repaints open screens without `recreate()`. XML themes are gone
apart from a bare `Theme.Base` window theme. Activities remain `AppCompatActivity` because
`AppCompatDelegate.setApplicationLocales` (how the language preference is applied) needs it below
API 33.

**`Sensitivity`** (`converter/data/Sensitivity.kt`) is the output data class holding the 8
converted ADS values (x1 through x12); `asArray()` is used when UI code needs to index into the
result by row position.

Google Play services (in-app update/review) are wrapped in `services/google/GoogleServices.kt` and
invoked once from `MainActivity.onCreate`. The review prompt is gated on `Settings.getUsage()`,
which now counts app launches (it counted calculate-button presses before the button went away).
