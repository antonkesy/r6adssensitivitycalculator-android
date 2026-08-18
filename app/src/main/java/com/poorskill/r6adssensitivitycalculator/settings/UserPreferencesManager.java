package com.poorskill.r6adssensitivitycalculator.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import com.poorskill.r6adssensitivitycalculator.R;
import com.poorskill.r6adssensitivitycalculator.ui.Theme;

import org.jetbrains.annotations.NotNull;

public class UserPreferencesManager implements Settings {
    public static final String SYSTEM_LANGUAGE = "system";

    private static final String prefADSKey = "adsKey";
    private static final String prefFOVKey = "fovKey";
    private static final String prefPosAspectRatioKey = "aspKey";
    private static final String prefUsageKey = "useKey";

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public UserPreferencesManager(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    private SharedPreferences.Editor getEditorSharedPreferences() {
        return getSharedPreferences().edit();
    }

    private void putInt(String key, int newValue) {
        getEditorSharedPreferences().putInt(key, newValue).apply();
    }

    public int getAspectRatioPos() {
        return getSharedPreferences().getInt(prefPosAspectRatioKey, 0);
    }

    public void putAspectRatio(int value) {
        putInt(prefPosAspectRatioKey, value);
    }

    public int getADS() {
        return getSharedPreferences().getInt(prefADSKey, 50);
    }

    public void putADS(int value) {
        putInt(prefADSKey, value);
    }

    public int getFOV() {
        return getSharedPreferences().getInt(prefFOVKey, 60);
    }

    public void putFOV(int value) {
        putInt(prefFOVKey, value);
    }

    private void putUsage(int value) {
        putInt(prefUsageKey, value);
    }

    public void incrementUsage() {
        putUsage(getUsage() + 1);
    }

    public int getUsage() {
        return getSharedPreferences().getInt(prefUsageKey, 0);
    }

    @NotNull
    public Theme getTheme() {
        int id;
        try {
            id = Integer.parseInt(getSharedPreferences().getString(context.getString(R.string.prefApplicationThemePrefKey), "0"));
        } catch (Exception e) {
            id = 0;
        }
        for (Theme t : Theme.getEntries()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return Theme.System;
    }

    public void putTheme(@NotNull Theme theme) {
        // stored as a string id, unchanged from the ListPreference days
        getEditorSharedPreferences().putString(context.getString(R.string.prefApplicationThemePrefKey), String.valueOf(theme.getId())).apply();
    }

    @NotNull
    public String getLanguage() {
        return getSharedPreferences().getString(context.getString(R.string.prefApplicationLanguagePrefKey), SYSTEM_LANGUAGE);
    }

    public void putLanguage(@NotNull String languageCode) {
        getEditorSharedPreferences().putString(context.getString(R.string.prefApplicationLanguagePrefKey), languageCode).apply();
        updateLanguage();
    }

    /**
     * Applies the stored language. AppCompat persists and re-applies this itself and recreates the
     * running activities, so there is nothing to do beyond handing it the tag.
     */
    public void updateLanguage() {
        String langCode = getLanguage();
        LocaleListCompat locales = SYSTEM_LANGUAGE.equals(langCode)
                ? LocaleListCompat.getEmptyLocaleList()
                : LocaleListCompat.forLanguageTags(langCode);
        if (!locales.equals(AppCompatDelegate.getApplicationLocales())) {
            AppCompatDelegate.setApplicationLocales(locales);
        }
    }
}
