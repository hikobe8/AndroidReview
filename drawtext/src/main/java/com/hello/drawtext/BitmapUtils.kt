package com.hello.drawtext

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

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