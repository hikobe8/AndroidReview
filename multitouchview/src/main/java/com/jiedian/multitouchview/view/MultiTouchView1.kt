package com.jiedian.multitouchview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.jiedian.multitouchview.R
import com.jiedian.multitouchview.utils.BitmapUtils

/**
 * Author : Ray
 * Time : 2020/11/6 11:31 PM
 * Description :
 */
class MultiTouchView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val bitmap: Bitmap = BitmapUtils.decodeSampledBitmapFromResource(resources, R.drawable.ray, 100.dp2px().toInt(), 100.dp2px().toInt())

    private var downX = 0
    private var downY = 0
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var trackingPointerId = 0

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = x.toInt()
                    downY = y.toInt()
                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }
                MotionEvent.ACTION_MOVE -> {
                    val index = findPointerIndex(trackingPointerId)
                    offsetX = getX(index) - downX + originalOffsetX
                    offsetY = getY(index) - downY + originalOffsetY
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {

                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    //判断新的手指按下，接管滑动操作
                    trackingPointerId = getPointerId(actionIndex)
                    downX = getX(actionIndex).toInt()
                    downY = getY(actionIndex).toInt()
                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    //抬起手指后，让当前事件中最后按下的手指接管滑动操作
                    val pointerId = getPointerId(actionIndex)
                    //判断抬起的手指为当前滑动的手指
                    if (pointerId == trackingPointerId) {
                        val newIndex = if (actionIndex == pointerCount - 1) {
                            //已经为最后一个手指的话，获取前一个
                            pointerCount - 2
                        } else {
                            //不是最后一个手指，获取最后一个
                            pointerCount - 1
                        }
                        trackingPointerId = getPointerId(newIndex)
                        downX = getX(newIndex).toInt()
                        downY = getY(newIndex).toInt()
                        originalOffsetX = offsetX
                        originalOffsetY = offsetY
                    }
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

fun Int.dp2px(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)
}