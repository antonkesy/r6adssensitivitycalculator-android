package com.poorskill.r6adssensitivitycalculator.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferencesManager {
    private static final String prefADSKey = "adsKey";
    private static final String prefFOVKey = "fovKey";
    private static final String prefPosAspectRatioKey = "aspectPosKey";

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

}
