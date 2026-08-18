# r6adssensitivitycalculator-android

[![android](https://github.com/antonkesy/r6adssensitivitycalculator-android/actions/workflows/android.yml/badge.svg)](https://github.com/antonkesy/r6adssensitivitycalculator-android/actions/workflows/android.yml)
[![pre-commit](https://github.com/antonkesy/r6adssensitivitycalculator-android/actions/workflows/pre-commit.yml/badge.svg)](https://github.com/antonkesy/r6adssensitivitycalculator-android/actions/workflows/pre-commit.yml)
[![license](https://img.shields.io/github/license/antonkesy/r6adssensitivitycalculator-android)](LICENSE)
[![last commit](https://img.shields.io/github/last-commit/antonkesy/r6adssensitivitycalculator-android)](https://github.com/antonkesy/r6adssensitivitycalculator-android/commits/main)

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![API 23+](https://img.shields.io/badge/API-23%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/tools/releases/platforms)
[![Google Play](https://img.shields.io/badge/Google%20Play-Install-3DDC84?logo=googleplay&logoColor=white)](https://play.google.com/store/apps/details?id=com.poorskill.r6adssensitivitycalculator)

R6 Siege Y5S3 ADS Sensitivity Calculator Android application

Quick 2-hour project to calculate the new ADS sensitivity for Tom Clancy's Rainbow Six: Siege by Ubisoft.
Not pretty but does it's job :)

## Table of Contents

- [r6adssensitivitycalculator-android](#r6adssensitivitycalculator-android)
  - [Table of Contents](#table-of-contents)
  - [Background](#background)
  - [Screenshots](#screenshots)
  - [Install](#install)

## Background
I have the disease of changing my gaming settings every match ... :( Ubisoft changed the way ADS sensitivity is calculated and I wanted to keep my old settings I switched between. The calculator provided by Ubisoft was to hard to use for me ...

## Screenshots

<p float="left">
<img src=".store/graphics/Screenshot_1.png" width="200" />
<img src=".store/graphics/Screenshot_2.png" width="200" />
</p>

## Tests

```
./gradlew testDebugUnitTest                            # everything, UI tests included
./gradlew testDebugUnitTest --tests "*.MainScreenTest" # a single class
./gradlew assembleDebug testDebugUnitTest lintDebug    # what CI runs
```

No emulator or device needed: the Compose screens are tested with [Robolectric](https://robolectric.org/)
on the JVM, so everything lives in `app/src/test` and `app/src/androidTest` is empty.
