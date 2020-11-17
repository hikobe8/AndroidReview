package com.jiedian.basicviewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import kotlin.math.abs

/**
 * Author : Ray
 * Time : 2020/11/17 4:06 PM
 * Description :
 */
class BasicViewPager(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private var downX = 0f
    private var downY = 0f
    private var downScrollX = 0
    private val overScroller = OverScroller(context)
    private val viewConfiguration = ViewConfiguration.get(context)
    private val velocityTracker = VelocityTracker.obtain()

    private val minVelocity = viewConfiguration.scaledMinimumFlingVelocity
    private val maxVelocity = viewConfiguration.scaledMaximumFlingVelocity

    private var scrolling = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(l + width * i, t, l + width * (i + 1), b)
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(ev)
        var intercept = false
        ev?.apply {
            when (actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    scrolling = false
                    downX = x
                    downY = y
                    downScrollX = scrollX
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!scrolling) {
                        val dx = downX - x
                        if (abs(dx) > viewConfiguration.scaledTouchSlop) {
                            intercept = true
                            scrolling = true
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                }
                else -> {
                    //do nothing
                }
            }
        }
        return intercept
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        event?.apply {
            when (actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = x
                    downY = y
                    downScrollX = scrollX
                }
                MotionEvent.ACTION_MOVE -> {
                    var dx = downX - x + downScrollX
                    //超过屏幕
                    if (dx > width) {
                        dx = width.toFloat()
                    } else if (dx < 0) {
                        dx = 0f
                    }
                    scrollTo(dx.toInt(), 0)
                }
                MotionEvent.ACTION_UP -> {
                    velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat())
                    val vx = velocityTracker.xVelocity
                    val targetPage = if (abs(vx) < minVelocity) {
                        if (scrollX < width / 2) {
                            0
                        } else {
                            1
                        }
                    } else {
                        if (vx < 0) {
                            1
                        } else {
                            0
                        }
                    }
                    val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX
                    overScroller.startScroll(scrollX, 0, scrollDistance, 0)
                    postInvalidateOnAnimation()
                }
                else -> {
                    //do nothing
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }

}