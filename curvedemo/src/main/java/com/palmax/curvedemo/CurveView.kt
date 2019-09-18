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

    companion object {
        private const val STATE_NONE = 0
        private const val STATE_MOVE_POINT = 1
        private const val STATE_MOVE_LINE = 2
        private const val SHOW_CLICK_REGION = true
    }

    /**
     * 控制点正在移动的标志
     */
    private var mState = STATE_NONE

    /**
     * 正在移动的点的下标
     */
    private var mMovingIndex = -1

    /**
     * 线段可点击区域
     */
    private var mPathClickRegion = Region()

    private var mPathClipRegion: Region? = null

    /**
     * 曲线线段路径
     */
    private val mCurvePath = Path()
    /**
     * 曲线线段点击可视区域
     */
    private var mClickPath: Path = Path()

    private var mLastX = 0f
    private var mLastY = 0f

    private val mPointPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
    }

    private val mLinePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.BLUE
        strokeWidth = 4f
    }


    private val mRegionPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 2f
    }

    private val mClickRegionPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 100f
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mMovingIndex = checkMovingPoint(event)
                if (mMovingIndex != -1) {
                    mState = STATE_MOVE_POINT
                }
                if (mState != STATE_MOVE_POINT) {
                    val op: Boolean = mPathClickRegion.contains(event.x.toInt(), event.y.toInt())
                    if (op) {
                        mLastX = event.x
                        mLastY = event.y
                        mState = STATE_MOVE_LINE
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mState == STATE_MOVE_POINT) {
                    mPoints[mMovingIndex].apply {
                        x = event.x.toInt()
                        y = event.y.toInt()
                    }
                    changeClickRegion()
                    invalidate()
                } else if (mState == STATE_MOVE_LINE) {
                    //移动线段整体
                    val offsetX = event.x - mLastX
                    val offsetY = event.y - mLastY
                    mPoints.map {
                        it.x += offsetX.toInt()
                        it.y += offsetY.toInt()
                    }
                    changeClickRegion()
                    invalidate()
                    mLastX = event.x
                    mLastY = event.y
                }
            }
            MotionEvent.ACTION_UP -> {
                mState = STATE_NONE
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
        mPathClipRegion = Region(0, 0, w, h)
        changeClickRegion()
    }

    private fun changeClickRegion() {
        mCurvePath.reset()
        mCurvePath.addPath(CurveUtil.getCurvePathFromPoints(mPoints, 0.6))
        mClickPath.set(mCurvePath)
        mClickRegionPaint.getFillPath(mCurvePath, mClickPath)
        mPathClickRegion.setPath(mClickPath, mPathClipRegion)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (SHOW_CLICK_REGION) {
            canvas?.drawPath(mClickPath, mRegionPaint)
        }
        canvas?.drawPath(mCurvePath, mLinePaint)
        for (point in mPoints) {
            canvas?.drawCircle(point.x.toFloat(), point.y.toFloat(), 20f, mPointPaint)
        }
    }

}