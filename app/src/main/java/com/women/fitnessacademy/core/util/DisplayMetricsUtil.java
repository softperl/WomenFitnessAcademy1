package com.women.fitnessacademy.core.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Junaid Asghar LLP on 1-7-2019.
 */

public class DisplayMetricsUtil {

    /**
     * Return true if the width in DP of the device is equal or greater than the given value
     */
    public static boolean isScreenW(int widthDp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        return screenWidth >= widthDp;
    }

}