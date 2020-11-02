package com.jiedian.scalableimageview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import com.jiedian.scaleableimageview.R

/**
 * Author : Ray
 * Time : 2020/10/11 11:25 PM
 * Description :
 */
class ScalableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    companion object {
        const val BIG_SCALE_FACTOR = 1.5f
    }

    private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ray)

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var bigScale = 0f
    private var smallScale = 0f
    private val gestureDetector: GestureDetector = GestureDetector(context, this)
    private var big = false
    var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var scaleAnimator: ObjectAnimator? = null

    private var offsetX = 0f
    private var offsetY = 0f
    private var maxOffsetX = 0f
    private var maxOffsetY = 0f

    //Scroller匀速滑动， 不能惯性滑动，所以使用OverScroller
    private var scroller: OverScroller = OverScroller(context)

    init {
        gestureDetector.setOnDoubleTapListener(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (w - bitmap.width) / 2f
        originalOffsetY = (h - bitmap.height) / 2f
        val viewRatio = w.toFloat() / h
        val imageRatio = bitmap.width.toFloat() / h
        if (imageRatio > viewRatio) {
            bigScale = width / bitmap.width.toFloat()
            smallScale = height / bitmap.height.toFloat()
        } else {
            bigScale = height / bitmap.height.toFloat()
            smallScale = width / bitmap.width.toFloat()
        }
        bigScale *= BIG_SCALE_FACTOR
        scaleAnimator = ObjectAnimator.ofFloat(this, "scaleFraction", smallScale, bigScale).apply {
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator?) {
//                    super.onAnimationEnd(animation)
//                    if (!big) {
//                        offsetX = 0f
//                        offsetX = 0f
//                    }
//                }
//            })
        }
        scaleFraction = smallScale
        maxOffsetX = (bitmap.width * bigScale - w) / 2f
        maxOffsetY = (bitmap.height * bigScale - h) / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val scale = (scaleFraction - smallScale) / (bigScale - smallScale)
            translate(offsetX * scale, offsetY * scale)
            scale(scaleFraction, scaleFraction, width / 2f, height / 2f)
            drawBitmap(bitmap, originalOffsetX, originalOffsetY, null)
        }
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        //建立滚动坐标系
        scroller.fling(offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                (-maxOffsetX).toInt(), maxOffsetX.toInt(), (-maxOffsetY).toInt(), maxOffsetY.toInt(),
                50.dp2px().toInt(), 50.dp2px().toInt())
        postOnAnimation(this::animationRunner)
        return false
    }

    private fun animationRunner() {
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()
            invalidate()
            postOnAnimation(this::animationRunner)
        }
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (big) {
            offsetX -= distanceX
            offsetY -= distanceY
            fixMoveBoundary()
            invalidate()
        }
        return false
    }

    private fun fixMoveBoundary() {
        if (offsetX > maxOffsetX) {
            offsetX = maxOffsetX
        }
        if (offsetX < -maxOffsetX) {
            offsetX = -maxOffsetX
        }
        if (offsetY > maxOffsetY) {
            offsetY = maxOffsetY
        }
        if (offsetY < -maxOffsetY) {
            offsetY = -maxOffsetY
        }
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        if (big) {
            scaleAnimator?.reverse()
        } else {
            offsetX = (e!!.x - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
            fixMoveBoundary()
            scaleAnimator?.start()
        }
        big = big.not()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }


}