package com.ray.circletabview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Author : hikobe8@github.com
 * Time : 2019-09-19 16:39
 * Description :
 */
class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mShown = false //是否展开
    private var mAnimating = false
    private var mAnimator: ValueAnimator? = null
    var visibilityCallback:VisiblityCallback ?=null

    interface VisiblityCallback{
        fun onOpen()
        fun onClosed()
    }

    /**
     * 打开或关闭菜单
     */
    fun toggle() {
        if (mAnimating)
            return
        mAnimating = true
        mAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            if (mShown) {
                addUpdateListener {
                    mOvalBounds.set(
                            mWidth / 2f * it.animatedFraction,
                            mHeight * it.animatedFraction,
                            mWidth - mWidth / 2f * it.animatedFraction,
                            mHeight * 2f
                    )
                    invalidate()
                }
            } else {
                addUpdateListener {
                    mOvalBounds.set(
                            mWidth / 2f - mWidth / 2f * it.animatedFraction,
                            mHeight - mHeight * it.animatedFraction,
                            mWidth / 2f + mWidth / 2f * it.animatedFraction,
                            mHeight * 2f
                    )
                    invalidate()
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    mAnimating = false
                    if (mShown) {
                        visibilityCallback?.onOpen()
                    }
                }
            })
            duration = 400L
            if (mShown) {
                visibilityCallback?.onClosed()
            }
            start()
        }
        mShown = !mShown
    }

    private var mOvalBounds = RectF()
    private var mWidth = 0f
    private var mHeight = 0f

    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.RED
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawArc(mOvalBounds, 180f, 180f, true, mPaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimator?.cancel()
    }

}