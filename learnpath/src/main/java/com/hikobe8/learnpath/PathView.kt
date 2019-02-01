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

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    private val mOuterPath = Path()
    private val mInnerPath = Path()

    private val mOuterPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.RED
    }
    private val mInnerPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.BLUE
    }

    private var mOuterPathLength = 0f
    private var mInnerPathLength = 0f

    init {
        mOuterPath.reset()
        mInnerPath.reset()
    }

    fun play() {
        mInnerPaint.pathEffect = null
        mOuterPaint.pathEffect = null
        invalidate()
        ObjectAnimator.ofFloat(this@PathView, "phase", 0.0f, 1.0f).apply {
            duration = 2000
        }.start()
        postDelayed({
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 2000
                addUpdateListener {
                    val value = it.animatedValue as Float
                    mInnerPaint.pathEffect = createPathEffect(mInnerPathLength, value, 0.0f)
                    invalidate()
                }
            }.start()
        }, 1000)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mOuterPath.addCircle(w / 2f, h / 2f, 400f, Path.Direction.CW)
        mInnerPath.moveTo(w / 2f, h / 2f - 400f)
        mInnerPath.lineTo(w / 2f - 400f, h / 2f)
        mInnerPath.lineTo(w / 2f, h / 2f + 400f)
        mInnerPath.lineTo(w / 2f + 400f, h / 2f)
        mInnerPath.lineTo(w / 2f, h / 2f - 400f)
        mInnerPath.addCircle(w / 2f, h / 2f, 200f, Path.Direction.CW)
        mInnerPath.addPath(Path().apply {
            moveTo(w / 2f, h / 2f - 200f)
            lineTo(w / 2f, h / 2f + 200f)
        })
        mInnerPath.addPath(Path().apply {
            moveTo(w / 2f - 200f, h / 2f)
            lineTo(w / 2f + 200f, h / 2f)
        })
        val pathMeasure = PathMeasure(mOuterPath, false)
        mOuterPathLength = pathMeasure.length
        pathMeasure.setPath(mInnerPath, false)
        mInnerPathLength = pathMeasure.length
        Toast.makeText(context, "开始动态绘制改路径", Toast.LENGTH_SHORT).show()
        play()
    }

    //is called by animtor object
    fun setPhase(phase: Float) {
        Log.d("PathView", "setPhase called with:" + phase.toString())
        mOuterPaint.pathEffect = createPathEffect(mOuterPathLength, phase, 0.0f)
        invalidate()//will calll onDraw
    }

    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect {
        return DashPathEffect(floatArrayOf(pathLength, pathLength),
                pathLength - phase * pathLength)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mOuterPaint.pathEffect != null)
            canvas?.drawPath(mOuterPath, mOuterPaint)
        if (mInnerPaint.pathEffect != null)
            canvas?.drawPath(mInnerPath, mInnerPaint)
    }

}