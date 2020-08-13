package com.hello.drawtext

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * Author : Ray
 * Time : 2020/8/11 6:12 PM
 * Description :
 */
class ImageTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        const val TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mBitmap: Bitmap

    private val mFontMetrics = Paint.FontMetrics()

    init {
        paint.textSize = 14f.dp2px()
        mBitmap = BitmapUtils.decodeBitmap(resources, R.drawable.head, 100f.dp2px().toInt())
        paint.getFontMetrics(mFontMetrics)
    }

    private val textMeasureWidth = FloatArray(1)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            //绘制图片
            drawBitmap(mBitmap, 0f, 0f, paint)
            //绘制文字
            var start = 0
            var count: Int
            var i = 1
            val size = mBitmap.width
            while (start < TEXT.length - 1) {
                val baseLine = paint.fontSpacing * i
                val top = mFontMetrics.top + baseLine
                val bottom = mFontMetrics.bottom + baseLine
                var maxWidth = width
                var x = 0f
                if ((top > 0 && top < size) || (bottom > 0 && bottom < size)) {
                    maxWidth = (width - size - 8f.dp2px()).toInt()
                    x = size.toFloat() + 8f.dp2px()
                }
                count = paint.breakText(TEXT, start, TEXT.length, true, maxWidth.toFloat(), textMeasureWidth)
                drawText(TEXT, start, start + count, x, paint.fontSpacing * i, paint)
                start += count
                i++
            }
        }
    }

}

fun Float.dp2px(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}