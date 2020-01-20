package com.hikobe8.view.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.hikobe8.view.R

/**
 * Author : Ray
 * Time : 2020-01-20 14:25
 * Description : 圆形头像
 */
class AvatarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var mBitmap: Bitmap
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mOffsetX = 0f
    private var mOffsetY = 0f
    private var mRadius = 0f
    private val mXfermodeSrcIn = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var mCircleBound = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, R.drawable.avatar, options)
        options.apply {
            inJustDecodeBounds = false
            options.inDensity = options.outWidth
            options.inTargetDensity = w / 2
        }
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar, options)
        mOffsetX = w / 2f - mBitmap.width / 2f
        mOffsetY = h / 2f - mBitmap.height / 2f
        mRadius = mBitmap.width / 3f
        mCircleBound.set(w / 2f - mRadius, h / 2f - mRadius, w / 2f + mRadius, h / 2f + mRadius)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            //绘制外圈 4dp
            drawCircle(width / 2f, height / 2f, mRadius + Util.dp2px(4f), paint)
            val count = saveLayer(mCircleBound, paint)
            drawCircle(width / 2f, height / 2f, mRadius, paint)
            paint.apply {
                xfermode = mXfermodeSrcIn
            }
            drawBitmap(mBitmap, mOffsetX, mOffsetY, paint)
            paint.apply {
                xfermode = null
            }
            restoreToCount(count)
        }
    }

}