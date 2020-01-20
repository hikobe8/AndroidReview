package com.hikobe8.view.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Author : Ray
 * Time : 2020-01-20 00:11
 * Description : 仪表盘
 */
class DashboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        var RADIUS = Util.dp2px(150f)
    }

    private var mScaleSpacing: Float = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = Util.dp2px(4f)
    }

    private val mBound = RectF()

    private val mPath = Path()

    private val mScaleCount = 20 //刻度总数
    private val mScaleWidth = Util.dp2px(4f) //刻度宽度 4dp
    private val mScaleHeight = Util.dp2px(10f) //刻度长度 10dp
    private var mScaleIndex = 8 //当前刻度

    private lateinit var mPathDashPathEffect: PathDashPathEffect

    private val mOpenAngle = 120f //仪表盘开口度数

    private val mSweepAngle = 240f //仪表盘度数

    private val mPointerLength = Util.dp2px(120f) //指针长度

    private var mScaleAngle = mSweepAngle / mScaleCount//每个指针相距的度数

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        RADIUS = w / 2f - Util.dp2px(10f)
        mBound.set(w / 2f - RADIUS, h / 2f - RADIUS, w / 2f + RADIUS, h / 2f + RADIUS)
        mPath.addArc(mBound, mOpenAngle / 2f + 90f, mSweepAngle)
        val dashPath = Path().apply {
            addRect(0f, 0f, mScaleWidth, mScaleHeight, Path.Direction.CCW)
        }
        val pathMeasure = PathMeasure(mPath, false)
        mScaleSpacing = (pathMeasure.length - mScaleWidth) / mScaleCount
        mPathDashPathEffect = PathDashPathEffect(
                dashPath, mScaleSpacing, 0f, PathDashPathEffect.Style.ROTATE)
        paint.pathEffect = mPathDashPathEffect
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            //绘制刻度
            drawPath(mPath, paint)
            paint.pathEffect = null
            //绘制表盘
            drawPath(mPath, paint)
            drawLine(width / 2f, height / 2f,
                    (width / 2f + (mPointerLength * cos(Math.toRadians((mScaleIndex * mScaleAngle + mOpenAngle / 2f + 90f).toDouble())))).toFloat(),
                    (height / 2f + (mPointerLength * sin(Math.toRadians((mScaleIndex * mScaleAngle + mOpenAngle / 2f + 90f).toDouble())))).toFloat(),
                    paint)
        }
    }

}