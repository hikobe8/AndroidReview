package com.jiedian.multitouchview.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jiedian.multitouchview.R
import com.jiedian.multitouchview.utils.BitmapUtils

/**
 * Author : Ray
 * Time : 2020/11/6 11:31 PM
 * Description : 协作，配合型多点触控
 */
class MultiTouchView2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val bitmap: Bitmap = BitmapUtils.decodeSampledBitmapFromResource(resources, R.drawable.ray, 100.dp2px().toInt(), 100.dp2px().toInt())

    private var downX = 0f
    private var downY = 0f
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        event?.apply {
            val focusX: Float
            val focusY: Float
            var sumX = 0f
            var sumY = 0f
            val isPointerUp = actionMasked == MotionEvent.ACTION_POINTER_UP
            for (i in 0 until pointerCount) {
                if (!isPointerUp || i != actionIndex) {
                    sumX += getX(i)
                    sumY += getY(i)
                }
            }
            var count = pointerCount
            if (isPointerUp) {
                count--
            }
            focusX = sumX / count
            focusY = sumY / count
            when (actionMasked) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN,
                MotionEvent.ACTION_POINTER_UP
                -> {
                    downX = focusX
                    downY = focusY
                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }
                MotionEvent.ACTION_MOVE -> {
                    offsetX = focusX - downX + originalOffsetX
                    offsetY = focusY - downY + originalOffsetY
                    invalidate()
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, offsetX, offsetY, null)
    }

}