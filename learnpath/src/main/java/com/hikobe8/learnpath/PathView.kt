package com.hikobe8.learnpath

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2019-02-01 15:51
 *  description :
 */
class PathView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?):this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?):this(context, attrs, 0)

    private val mPath  = Path()
    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.RED
    }
    private lateinit var mPathMeasure:PathMeasure
    private var mPathLength = 0f

    init {
        mPath.reset()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPath.arcTo(RectF(100f, 100f, 200f, 200f), 90f, 270f)
        mPath.arcTo(RectF(600f, 400f, 800f, 800f), 90f, -270f)
        mPathMeasure = PathMeasure(mPath, false)
        mPathLength = mPathMeasure.length
        postDelayed({
            Toast.makeText(context, "开始动态绘制改路径", Toast.LENGTH_SHORT).show()
            ObjectAnimator.ofFloat(this@PathView, "phase", 0.0f, 1.0f).apply {
                duration = 3000
            }.start()

        }, 1000)
    }

    //is called by animtor object
    fun setPhase(phase: Float) {
        Log.d("PathView", "setPhase called with:" + phase.toString())
        mPaint.pathEffect = createPathEffect(mPathLength, phase, 0.0f)
        invalidate()//will calll onDraw
    }

    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect {
        return DashPathEffect(floatArrayOf(pathLength, pathLength),
                pathLength - phase * pathLength)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.strokeWidth = 10f
        mPaint.color = Color.RED
        canvas?.drawPath(mPath, mPaint)
        mPaint.color = Color.BLUE
        mPaint.strokeWidth = 20f
    }

}