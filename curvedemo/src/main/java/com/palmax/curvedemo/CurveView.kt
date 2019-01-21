package com.palmax.curvedemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2019-01-21 17:24
 *  description :
 */
class CurveView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mPoints = ArrayList<Point>()
    private val mPointPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
    }

    /**
     * 正在移动的标志
     */
    private var mNeedMove = false

    /**
     * 正在移动的点的下标
     */
    private var mMovingIndex = -1

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mMovingIndex = checkMovingPoint(event)
                if (mMovingIndex != -1) {
                    mNeedMove = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mNeedMove) {
                    mPoints[mMovingIndex].apply {
                        x = event.x.toInt()
                        y = event.y.toInt()
                    }
                    changeClickRegion()
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                mNeedMove = false
            }
        }

        return true
    }

    /**
     * 得到需要移动的点的下标
     */
    private fun checkMovingPoint(event: MotionEvent): Int {
        for (p in mPoints) {
            //扩大点击范围
            if (event.x <= p.x + 40
                    && event.x >= p.x - 40
                    && event.y <= p.y + 40
                    && event.y >= p.y - 40) {
                return mPoints.indexOf(p)
            }
        }
        return -1
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPoints.add(Point((w / 4f + .5f).toInt(), 100))
        mPoints.add(Point((w / 2f + .5f).toInt(), (h * 3 / 4f + .5f).toInt()))
        mPoints.add(Point((w * 2 / 3f + .5f).toInt(), (h / 2f + .5f).toInt()))
        mPoints.add(Point((w * 4f / 5 + .5f).toInt(), h - 100))
        changeClickRegion()
    }

    private fun changeClickRegion() {
        mPath.reset()
        val clickPixels = 100
        mPath.moveTo(mPoints[0].x.toFloat(), mPoints[0].y.toFloat() - clickPixels)
        mPath.lineTo(mPoints[1].x.toFloat(), mPoints[1].y.toFloat() - clickPixels)
        mPath.lineTo(mPoints[2].x.toFloat(), mPoints[2].y.toFloat() - clickPixels)
        mPath.lineTo(mPoints[3].x.toFloat(), mPoints[3].y.toFloat() - clickPixels)
        mPath.lineTo(mPoints[3].x.toFloat(), mPoints[3].y.toFloat() + clickPixels)
        mPath.lineTo(mPoints[2].x.toFloat(), mPoints[2].y.toFloat() + clickPixels)
        mPath.lineTo(mPoints[1].x.toFloat(), mPoints[1].y.toFloat() + clickPixels)
        mPath.lineTo(mPoints[0].x.toFloat(), mPoints[0].y.toFloat() + clickPixels)
        mPath.close()
    }

    private val mPath = Path()

    private val mMatrix = Matrix().apply {
        setTranslate(-40f, 0f)
    }

    private val mRegionPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        CurveUtil.drawCurvesFromPoints(canvas, mPoints, 0.6, Color.RED, 4f)
        mMatrix.setTranslate(80f, 0f)
        canvas?.drawPath(mPath, mRegionPaint)
        for (point in mPoints) {
            canvas?.drawCircle(point.x.toFloat(), point.y.toFloat(), 20f, mPointPaint)
        }
    }

}