package com.ray.customizedview.scroll

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Author : hikobe8@github.com
 * Time : 2018/12/17 11:45 PM
 * Description : 手势滑动的view,通过layout方法
 */
class CustomizedLayoutScrollView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?):this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?):this(context, attrs, 0)

    private var mLastX = 0f
    private var mLastY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val x = event?.x
        val y = event?.y
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x!!
                mLastY = y!!
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = x!! - mLastX
                val offsetY = y!! - mLastY
                layout((left + offsetX).toInt(), (top + offsetY).toInt(), (right + offsetX).toInt(), (bottom + offsetY).toInt())
            }
        }

        return true
    }

}
