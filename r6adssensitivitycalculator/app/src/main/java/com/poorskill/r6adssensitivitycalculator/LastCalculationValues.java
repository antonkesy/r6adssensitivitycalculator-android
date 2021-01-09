package com.poorskill.r6adssensitivitycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LastCalculationValues {
    private static final String prefADSKey = "adsKey";
    private static final String prefFOVKey = "fovKey";
    private static final String prefAspectRatioWidthKey = "aspectWidthKey";
    private static final String prefAspectRatioHeightKey = "aspectHeightKey";

    private static final int[] defaultValues = {50, 60, 16, 9};

    public static int[] getOldValues(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new int[]{preferences.getInt(prefADSKey, defaultValues[0]), preferences.getInt(prefFOVKey, defaultValues[1]), preferences.getInt(prefAspectRatioWidthKey, defaultValues[2]), preferences.getInt(prefAspectRatioHeightKey, defaultValues[3])};
    }

    public static void saveValues(Context context, double ads, double fov, double aspectWidth, double aspectHeight) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(prefADSKey, (int) ads);
        editor.putInt(prefFOVKey, (int) fov);
        editor.putInt(prefAspectRatioWidthKey, (int) aspectWidth);
        editor.putInt(prefAspectRatioHeightKey, (int) aspectHeight);
        editor.apply();
    }
}
