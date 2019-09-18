package com.hikobe8.ovalanimation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class OvalView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mPointList by lazy { ArrayList<PointF>() }
    private lateinit var mAnimation: ValueAnimator
    private val mPosition: PointF by lazy { PointF() }
    private val mBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
    }

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPointList.clear()
        mPointList.addAll(OvalGenerator.getPointsOfOval(w, h, 2f))
        mAnimation = ValueAnimator.ofInt(0, mPointList.size).apply {
            duration = 8000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue
                val point = mPointList[value as Int]
                mPosition.set(point.x, point.y)
                invalidate()
            }
        }
        mAnimation.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimation.cancel()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (pointF in mPointList) {
            pointF.let {
                canvas?.drawPoint(it.x, it.y, mPaint)
            }
        }
        canvas?.drawBitmap(mBitmap, mPosition.x - mBitmap.width / 2f, mPosition.y - mBitmap.height / 2f, null)
    }

}

object OvalGenerator {

    /**
     * 得到椭圆上的点
     * @param step  点之间的距离，越小曲线越圆滑
     * 椭圆方程：  x*x/width/2f*width/2f + y*y/height/2f*height/2f = 1
     */
    fun getPointsOfOval(width: Int, height: Int, step: Float): ArrayList<PointF> {
        var x = 0.0
        var y: Double
        val a = width / 2.0 - 100
        val b = height / 2.0 - 100
        val points = ArrayList<PointF>()
        while (x <= a) {
            y = Math.sqrt(Math.pow(b, 2.0) * (1 - Math.pow(x, 2.0) / Math.pow(a, 2.0)))
            points.add(PointF((-x.toFloat()), (y.toFloat())))
            x += step
        }
        while (x >= 0) {
            y = Math.sqrt(Math.pow(b, 2.0) * (1 - Math.pow(x, 2.0) / Math.pow(a, 2.0)))
            points.add(PointF((-x.toFloat()), (-y.toFloat())))
            x -= step
        }
        x = 0.0
        while (x <= a) {
            y = Math.sqrt(Math.pow(b, 2.0) * (1 - Math.pow(x, 2.0) / Math.pow(a, 2.0)))
            points.add(PointF((x.toFloat()), (-y.toFloat())))
            x += step
        }
        while (x >= 0) {
            y = Math.sqrt(Math.pow(b, 2.0) * (1 - Math.pow(x, 2.0) / Math.pow(a, 2.0)))
            points.add(PointF((x.toFloat()), (y.toFloat())))
            x -= step
        }
        transferCoordinates(points, width / 2f, height / 2f)
        return points
    }

    /**
     * 翻转坐标点
     * @param x 绕x轴翻转
     * @param y 绕y轴翻转
     */
    private fun flipPoints(x: Boolean, y: Boolean, points: List<PointF>): List<PointF> {
        val ps = ArrayList<PointF>(points.size)
        return when {
            x -> {
                for (i in 0 until ps.size) {
                    ps[i] = PointF().apply {
                        this.x = points[i].x
                    }
                    ps[i].y = points[i].y * -1
                }
                ps
            }
            y -> {
                for (i in 0 until ps.size) {
                    ps[i] = PointF().apply {
                        this.y = points[i].y
                    }
                    ps[i].x = points[i].x * -1
                }
                ps
            }
            else -> throw RuntimeException("flip error")
        }
    }

    /**
     * 把笛卡尔坐标系转换为安卓坐标系
     */
    private fun transferCoordinates(points: List<PointF>, xOffset: Float, yOffset: Float) {
        points.map {
            it.x += xOffset
            it.y += yOffset
        }
    }

}