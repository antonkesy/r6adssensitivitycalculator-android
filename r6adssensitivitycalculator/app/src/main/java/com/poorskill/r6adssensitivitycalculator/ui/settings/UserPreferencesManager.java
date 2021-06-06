package com.poorskill.r6adssensitivitycalculator.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.poorskill.r6adssensitivitycalculator.R;

import java.util.Locale;

public class UserPreferencesManager {
    private static final String prefADSKey = "adsKey";
    private static final String prefFOVKey = "fovKey";
    private static final String prefPosAspectRatioKey = "aspKey";
    private static final String prefUsageKey = "useKey";

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditorSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    private static void setInt(Context context, String key, int newValue) {
        getEditorSharedPreferences(context).putInt(key, newValue).apply();
    }

    public static int getAspectRatioPos(Context context) {
        return getSharedPreferences(context).getInt(prefPosAspectRatioKey, 0);
    }

    public static void setAspectRatio(Context context, int value) {
        setInt(context, prefPosAspectRatioKey, value);
    }

    public static int getADS(Context context) {
        return getSharedPreferences(context).getInt(prefADSKey, 50);
    }

    public static void setADS(Context context, int value) {
        setInt(context, prefADSKey, value);
    }

    public static int getFOV(Context context) {
        return getSharedPreferences(context).getInt(prefFOVKey, 50);
    }

    public static void setFOV(Context context, int value) {
        setInt(context, prefFOVKey, value);
    }

    private static void setUsage(Context context, int value) {
        setInt(context, prefUsageKey, value);
    }

    public static void incrementUsage(Context context) {
        setUsage(context, getUsage(context) + 1);
    }

    public static int getUsage(Context context) {
        return getSharedPreferences(context).getInt(prefUsageKey, 0);
    }

    public static Theme getApplicationTheme(Context context) {
        int id;
        try {
            id = Integer.parseInt(getSharedPreferences(context).getString(context.getString(R.string.prefApplicationThemePrefKey), "0"));
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

    public static void updateLanguage(Context context) {
        String langCode = getSharedPreferences(context).getString(context.getString(R.string.prefApplicationLanguagePrefKey), "system");
        if (langCode.equals("system")) {
            langCode = Locale.getDefault().getLanguage();
        }
        setLocale(context, langCode);
    }

    private static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources r = context.getResources();
        Configuration c = r.getConfiguration();
        c.setLocale(locale);
        r.updateConfiguration(c, r.getDisplayMetrics());
        context.createConfigurationContext(c);
    }


}
