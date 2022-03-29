package com.poorskill.r6adssensitivitycalculator.converter;

import androidx.annotation.NonNull;

public final class R6Y5S3SensitivityConverter implements SensitivityConverter {
    private static final double[] fovMultiplier = {.9, .59, .49, .42, .35, .3, .22, .092};
    private static final double[] adsMultiplier = {.6, .59, .49, .42, .35, .3, .22, .14};

    @NonNull
    public int[] calculateNewAdsSensitivity(int oldAds, int fov, double aspectRatio) {
        int[] result = new int[8];
        double horizontalFOV = calculateHorizontalFOV(fov, aspectRatio);
        double verticalFOV = horizontalFOV > 150 ? calculateVerticalFOV(aspectRatio) : fov;

        for (int i = 0; i < result.length; ++i) {
            result[i] = calculateNewAds(adsMultiplier[i], calculateFOVAdjustment(fovMultiplier[i], verticalFOV), oldAds);
        }
        return result;
    }

    private double calculateFOVAdjustment(double fovMultiplier, double verticalFOV) {
        return Math.tan(Math.toRadians(fovMultiplier * verticalFOV / 2.)) / Math.tan(Math.toRadians(verticalFOV / 2.));
    }

    private int calculateNewAds(double adsMultiplier, double fovAdjustment, double oldAds) {
        return (int) ((adsMultiplier / fovAdjustment) * oldAds);
    }

    private double calculateVerticalFOV(double aspectRatio) {
        return 2 * Math.atan(Math.tan(Math.toRadians(75.)) / aspectRatio);
    }

    private double calculateHorizontalFOV(double verticalFOV, double aspectRatio) {
        return 2 * Math.atan(Math.tan(Math.toRadians(verticalFOV / 2.)) * aspectRatio);
    }
}
