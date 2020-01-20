package com.hikobe8.view.view

import android.content.res.Resources
import android.util.TypedValue

/**
 * Author : Ray
 * Time : 2020-01-20 00:25
 * Description :
 */
class Util {

    companion object {
        fun dp2px(dp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)
        }
    }

}