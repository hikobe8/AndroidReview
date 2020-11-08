package com.jiedian.multitouchview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.core.util.forEach

/**
 * Author : Ray
 * Time : 2020/11/6 11:31 PM
 * Description : 多点触控 各司其职
 */
class MultiTouchView3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paths = SparseArray<Path>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5.dp2px()
        strokeJoin = Paint.Join.ROUND
        color = Color.RED
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        event?.apply {

            when (actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN,
                MotionEvent.ACTION_DOWN -> {
                    val pointerId = getPointerId(actionIndex)
                    val path = Path()
                    path.moveTo(getX(actionIndex), getY(actionIndex))
                    paths.put(pointerId, path)
                }
                MotionEvent.ACTION_MOVE -> {
                    for (i in 0 until pointerCount) {
                        val pointerId = getPointerId(i)
                        paths[pointerId]?.apply {
                            lineTo(getX(i), getY(i))
                        }
                    }
                    invalidate()
                }
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_UP -> {
                    val pointerId = getPointerId(actionIndex)
                    paths[pointerId]?.apply {
                        reset()
                    }
                    invalidate()
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            paths.forEach { _, value ->
                drawPath(value, paint)
            }
        }
    }

}