package com.hello.animationdemo

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue

/**
 * Author : Ray
 * Time : 2020/8/13 4:53 PM
 * Description :
 */
class BitmapUtils {

    companion object {
        fun decodeBitmap(resources: Resources, resId: Int, width: Int): Bitmap =
                BitmapFactory.Options().run {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeResource(resources, resId, this)
                    inJustDecodeBounds = false
                    inDensity = outWidth
                    inTargetDensity = width
                    BitmapFactory.decodeResource(resources, resId, this)
                }
    }

}


fun Float.dp2px(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}