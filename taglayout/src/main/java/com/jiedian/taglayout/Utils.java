package com.jiedian.taglayout;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Author : Ray
 * Time : 2020/10/11 12:41 PM
 * Description :
 */
class Utils {
    public static float dpToPixel(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
