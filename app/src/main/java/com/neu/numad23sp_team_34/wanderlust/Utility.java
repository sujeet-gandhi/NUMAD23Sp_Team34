package com.neu.numad23sp_team_34.wanderlust;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utility {

    public static float dpToPx(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

}
