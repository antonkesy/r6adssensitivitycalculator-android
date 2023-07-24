package com.poorskill.r6adssensitivitycalculator.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.poorskill.r6adssensitivitycalculator.R;
import com.poorskill.r6adssensitivitycalculator.ui.Theme;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class UserPreferencesManager implements Settings {
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
        for (Theme t : Theme.values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return Theme.System;
    }

    public void updateLanguage() {
        String langCode = getSharedPreferences().getString(context.getString(R.string.prefApplicationLanguagePrefKey), "system");
        if (langCode.equals("system")) {
            langCode = Locale.getDefault().getLanguage();
        }
        setLocale(langCode);
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources r = context.getResources();
        Configuration c = r.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            c.setLocale(locale);
        }
        r.updateConfiguration(c, r.getDisplayMetrics());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(c);
        }
    }


}
