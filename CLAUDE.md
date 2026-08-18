# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Android app (Kotlin) that converts Rainbow Six Siege ADS sensitivity values from the old
sensitivity system to Ubisoft's Y5S3 FOV-based system. Single `:app` module, no backend.

## Commands

Build and test via the Gradle wrapper from the repo root:

```
./gradlew assembleDebug testDebugUnitTest lintDebug   # exactly what CI runs
./gradlew testDebugUnitTest      # all tests, UI included (Robolectric — no device needed)
./gradlew testDebugUnitTest --tests "*.MainScreenTest"   # run a single test class
```

There is no lint/format tool configured beyond `.pre-commit-config.yaml`, which only runs generic
hygiene hooks (trailing whitespace, EOF fixer, YAML check, large-file check) — no Kotlin-specific
linting is enforced. Android lint runs in CI with `abortOnError` and uploads SARIF to code scanning.

**All tests are JVM tests.** `app/src/androidTest` is intentionally empty — the Compose screen
tests run under Robolectric in `app/src/test`, so one fast `./gradlew testDebugUnitTest` covers
everything and CI needs no emulator. Robolectric is pinned to `sdk=35` in
`app/src/test/resources/robolectric.properties` because it has no android-all jar for compileSdk 37;
raise that when a newer Robolectric supports it.

Compose tests compose the screen directly with a `FakeSettings`-backed converter
(`createAndroidComposeRule<ComponentActivity>()`); they deliberately do **not** launch
`MainActivity`, whose `onCreate` calls into Play in-app update/review. Note the Robolectric screen
is phone-sized and the main screen scrolls, so assert with `assertExists()` (or `performScrollTo()`
first) for anything below the fold rather than `assertIsDisplayed()`.

`R6Y5S3SensitivityConverterTest` and `PersistentSensitivityConverterTest` pin the converter's
numeric output — if a change moves those numbers, that is the finding, not a stale test.

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
